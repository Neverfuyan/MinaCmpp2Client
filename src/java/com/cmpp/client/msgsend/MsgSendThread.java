package com.cmpp.client.msgsend;

import com.cmpp.client.common.Confighead;
import com.cmpp.client.common.InSubmit;
import com.cmpp.client.common.Ptport;
import com.cmpp.httpsqs.HttpSmsClient;
import com.cmpp.redis.RedisUtil;
import com.cmpp.util.TimeUtil;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 功能：短信组织发送
 * @author 蔡新鹏
 * @date 2017-01-10 15:16
 * @address 上海
 */
public class MsgSendThread extends Thread{
	private IoSession session = null;
	private boolean Connect=false;

	private long lastActiveTime = 0;
	
	private int maxnum=0;
	
	private static final Logger logger = Logger.getLogger(MsgSendThread.class);
	private RedisUtil redis = RedisUtil.getInstance();

	private static HttpSmsClient httpSmsClient = HttpSmsClient.getInstance();
	
	private Confighead confighead=new Confighead();
	
	private Cache monitorCache;

	public MsgSendThread(IoSession s,Confighead confighead,boolean Connect,Cache monitorCache){
		setDaemon(true);
		
		this.session = s;
		this.confighead=confighead;
		this.Connect=Connect;
		this.monitorCache=monitorCache;
	}

	public void run(){
		
		lastActiveTime=System.currentTimeMillis();//每秒发送条数统计
		int numsms=0;
		String queue_flag = "yzm";
		maxnum=Integer.parseInt(confighead.getMaxnum());
		if(maxnum==0){
			maxnum=60;
		}
		ExecutorService pool = null;
		try{
			pool = Executors.newFixedThreadPool(2);
			while (session.isConnected() & Connect == true){
				//获取队列数据
				String get_value=httpSmsClient.getT(queue_flag+confighead.getQueuename().trim(), confighead.getQueue().trim(), confighead.getQueueport().trim());
				if (get_value.startsWith("HTTPSQS_GET_END") || get_value.startsWith("HTTPSQS_ERROR")) {
					if("yzm".equals(queue_flag)){
						queue_flag = "";
					}else if("".equals(queue_flag)){
						queue_flag = "yx";
					}else{
						Thread.sleep(10L);
						queue_flag = "yzm";
					}
    	            continue;
    	        }
				
				InSubmit bean=new InSubmit();
				try{
					bean=(InSubmit)JSONObject.toBean(JSONObject.fromObject(get_value),InSubmit.class);

				}catch(Exception e){
					
					logger.info("[MsgSendThread][get_value:"+get_value+" |Exception"+e.getMessage()+"]");
					Thread.sleep(10L);
					
					continue;
				}
				
				String xh1 = bean.getPort();
				if(bean.getMid().startsWith("7082,")){
					if(bean.getPort().length()>=6){
						bean.setPort(bean.getPort().substring(6));
					}
				}
				
				if("4004".equals(confighead.getQueuename()) && bean.getMid().startsWith("7897,")){//zytxhy独享通道4004（抹除我们平台6位扩展）
					if(bean.getPort().length()>=6){
						bean.setPort(bean.getPort().substring(6));
					}
				}
				
				String xh = bean.getPort();
				int portnum=20-confighead.getSpport().length();
		    	if(xh.length()>portnum){
		    		bean.setPort(xh.substring(0, portnum));
		    		logger.info("[MsgSendThread][总码号超长20位截取："+bean.getPort()+"][port:"+xh+"][mobile:"+bean.getMobile()+"]");
		    	}
		    	
		    	xh = bean.getPort();
		    	
		    	//抹签名
		    	if("96".equals(confighead.getQueuename())){
		    		bean.setContent(bean.getContent().substring(bean.getContent().lastIndexOf("】")+1));
		    	}

		    	//小号 平台名称 存 缓存 -redis-port
				Ptport ptport=new Ptport();
				
				String platform=bean.getPlatform().trim();
				
				if(!"YZM".equals(platform)){
					ptport.setPort(xh1);
					ptport.setPlatform(platform);

					String portkey = "client_cmpp_"+confighead.getQueuename()+"_pc_"+bean.getMobile();
					
					redis.set(portkey, JSONObject.fromObject(ptport).toString());
					redis.expire(portkey, 2*24*60*60);
				}
		    	
				//组织短信报文
				//判断短信条数
				int smsnum=0;
				int msgLen = 0;
				
				msgLen=bean.getContent().trim().length();

				if(msgLen<=70){
					smsnum=1;  //短信条数
				}else{
					int smsnumLeft = msgLen/67;
					int smsnumRight = msgLen%67;
					
					if(smsnumRight == 0){
						smsnum = smsnumLeft;
					}else{
						smsnum = smsnumLeft+1;
					}
				}
				
				//提交短信滑动窗口监控
				int flownum=0;
				if(Integer.parseInt(confighead.getFlow())<100){
				
					String flowkey="session-" + session.getId();
					Element flowElement=null;
					while(true){
						synchronized(this.monitorCache){
							flowElement = this.monitorCache.get(flowkey);
			        	}
						if(flowElement==null){
							flownum=smsnum;
						}else{
							int flow=Integer.parseInt(flowElement.getValue().toString());
							flownum=flow+smsnum;
							if(flownum>Integer.parseInt(confighead.getFlow().trim())){
								Thread.sleep(10L);
								continue;
							}
						}
						//重新存入缓存
						synchronized(this.monitorCache){
							Element flowElementput=new Element(flowkey,flownum);
							this.monitorCache.put(flowElementput);
						}
						break;
					}
				}
				
				Thread thread = new Thread(new SendSmsThread(bean, confighead, session, queue_flag, smsnum, flownum));
				pool.execute(thread);
				
				if(Integer.parseInt(confighead.getFlow())<200){
					Thread.sleep(5L);
				}
				
				numsms += smsnum;
		    	
				if(numsms>=maxnum){
					long tt = System.currentTimeMillis() - lastActiveTime;
					if(tt < 1000){
						logger.info("[超流速预警][总流速："+maxnum+"][当前流速:"+numsms+"][剩余时间:"+tt+"]");
						Thread.sleep(1000-tt);
					}
				}

				//每秒统计
				long tt = System.currentTimeMillis() - lastActiveTime;
				if(tt>=1000){
					if(numsms != 0 ){
						logger.info("[当前链接："+session.getId()+"][总流速："+maxnum+"][每秒发送条数："+numsms+"][所用时间:"+tt+"]");
					}
					queue_flag = "yzm";
					numsms=0;
					lastActiveTime=System.currentTimeMillis();
				}
			}
		}catch (Exception e){
			String channel=confighead.getQueuename().trim();
			String localip=confighead.getLocalip().trim();
			
			String content="严重预警:服务器IP:"+localip+",通道:"+channel+",session:"+session.getId()+",流速:"+confighead.getConnetnum()+"/"+confighead.getMaxnum()+",发送[MsgSendThread]链接出现异常:"+e.getMessage();
			
			logger.info("Exception|"+content);
			
			TimeUtil.sms("13524696324", content);
			
			e.printStackTrace();
		}finally{
			if(pool != null){
				pool.shutdown();
			}
		}
	}
}
