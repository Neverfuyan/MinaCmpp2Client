package com.cmpp.client.monitor;

import com.cmpp.client.common.Confighead;
import com.cmpp.client.common.InSubmit;
import com.cmpp.httpsqs.HttpSmsClient;
import com.cmpp.util.TimeUtil;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 类型描述：协议队列检测以及通道分流
 * @author 邱凤丹
 * @date 日期：2017-11-2  时间：下午2:22:29
 * @version 1.0
 */
public class ChannelBakThread extends Thread{
	
	public ChannelBakThread(){
		super();
		this.setDaemon(true);
	}
	
	public static void channelBak(Confighead confighead){
		ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
		scheduledExecutorService.scheduleAtFixedRate(new threadStyle(confighead), 1, 1*3, TimeUnit.SECONDS);
	}

	/**
	 * 类型描述：协议分流线程
	 * @author 邱凤丹
	 * @date 日期：2018-4-11  时间：下午3:34:42
	 * @version 1.0
	 */
	private static class threadStyle implements Runnable{

		private static Logger log = Logger.getLogger(ChannelBakThread.class);
		private HttpSmsClient sqs = HttpSmsClient.getInstance();
		
		private Confighead confighead;
		
		public threadStyle(Confighead confighead){
			this.confighead = confighead;
		}
		
		//通道号_xy   json
		@Override
		public void run() {
			String sqsIp = "10.47.24.215";
			String sqsPort = "9219";
			
			if("192.168.10.92".equals(confighead.getLocalip()) || "192.168.10.93".equals(confighead.getLocalip()) || "172.17.196.171".equals(confighead.getLocalip())){
				sqsIp = "139.196.160.207";
			}
			
//			//获取所有分流通道
			String channels=sqs.getpr(sqsIp, sqsPort, "sel_channel_bak");
			if(!"HTTPSQS_END".equals(channels)){
				//验证该通道有没有进行协议分流
				if(Arrays.asList(channels.split(",")).contains(confighead.getQueuename())){
					
					//获取该队列相关信息
					String ss=sqs.statusJson(confighead.getQueue(),confighead.getQueueport(),confighead.getQueuename());
					JSONObject json=JSONObject.fromObject(ss);
					
					//获取该协议队列还有多少数据未取
					int unread = Integer.parseInt(json.get("unread").toString());
					
					//该通道最大流速
					int max= Integer.parseInt(confighead.getConnetnum().trim()) * Integer.parseInt(confighead.getMaxnum().trim());
					
					//判断如果剩余数据大于该通道最大流速的两倍后进行分流
					if(unread > max*2){
//						JSONObject bak_json = JSONObject.fromObject(sqs.getpr("192.168.1.26", "9219", confighead.getQueuename()+"_xy"));
						String resT=sqs.getpr(sqsIp, sqsPort, confighead.getQueuename()+"_xy");
						
						if(!"HTTPSQS_END".equals(resT)){
							JSONObject bak_json = JSONObject.fromObject(resT);
							String bak_channel_id = bak_json.getString("bak_channel_id");
							String bak_channel_ip = bak_json.getString("bak_channel_ip");
							String bak_channel_port = bak_json.getString("bak_channel_port");
							log.info("[协议分流][最大流速:"+max+"][队列剩余:"+unread+"][主力通道号:"+confighead.getQueuename()+"][备用通道号:"+bak_channel_id+"]");
							
							//从该通道流速为数值取多少条数据
							for(int i=0;i<max;i++){
								
								//从该通道队列上取出要发送的数据
								String get_value=sqs.getT(confighead.getQueuename().trim(), confighead.getQueue().trim(), confighead.getQueueport().trim());
								if (get_value.startsWith("HTTPSQS_GET_END") || get_value.startsWith("HTTPSQS_ERROR")) {
									break;
				    	        }
								
								InSubmit bean=new InSubmit();
								try{
									bean=(InSubmit)JSONObject.toBean(JSONObject.fromObject(get_value),InSubmit.class);

								}catch(Exception e){
									
									log.info("[ChannelBakThread][协议分流][get_value:"+get_value+" |exception"+e.getMessage()+"]");
									
									continue;
								}
								
								//将取出的数据往备份协议队列上插入
								String result=sqs.putT(bak_channel_id.trim(), get_value, bak_channel_ip.trim(), bak_channel_port.trim());
//								String result=sqs.putT("888", get_value, "192.168.1.6", "9215");
								
								//插入发送-通道队列
						        if (!result.contains("HTTPSQS_PUT_OK")){
						        	log.info("[channel_bak_queue_error][协议分流][resT:"+result+"][bak_channel_id:"+bak_channel_id+"][bak_channel_ip:"+bak_channel_ip+"][bak_channel_port:"+bak_channel_port+"][get_value:"+get_value+"]");
						        	
						        	//入备份通道队列异常
									String content = "协议分流-通道："+confighead.getQueuename()+"，当前通道队列未取数据："+unread+"，总流速："+max+"，入分流协议通道队列失败，请检查平台配置是否正确！分流通道："+bak_channel_id+"，队列："+bak_channel_ip+"，端口："+bak_channel_port+"。时间："+TimeUtil.getNowTime("yyyy-MM-dd HH:mm:ss,SSS")+"。";
									log.info(content);
//									MonitorApi.sendmonitorqqmail(content, "qiufengdan@ztinfo.cn;wujian@ztinfo.cn");
									sqs.putT(confighead.getQueuename().trim(), get_value, confighead.getQueue().trim(), confighead.getQueueport().trim());
						        	break;
						        }else{
						        	log.info("[channel_bak_queue_ok][协议分流][resT:"+result+"][bak_channel_id:"+bak_channel_id+"][bak_channel_ip:"+bak_channel_ip+"][bak_channel_port:"+bak_channel_port+"][get_value:"+get_value+"]");
						        	sqs.putT("batch_abnormal", bak_channel_id+","+bean.getMobile()+","+bean.getCpmid(), sqsIp, "9217");
						        }
							}
						}else{
							//平台没有配置备份通道
							String content = "协议分流-通道："+confighead.getQueuename()+"，当前通道队列未取数据："+unread+"，总流速："+max+"，未配置分流通道。时间："+TimeUtil.getNowTime("yyyy-MM-dd HH:mm:ss,SSS")+"。";
							log.info(content);
//							MonitorApi.sendmonitorqqmail(content, "qiufengdan@ztinfo.cn;xiangjinjing@ztinfo.cn");
						}
					}
				}
			}
		}
	}
	
	public static void main(String[] args) {
		
//		System.out.println("=====================================");
		HttpSmsClient sqs = HttpSmsClient.getInstance();
		
//		String val = sqs.put("batch_abnormal", "87,13524696324,201804111444389099178shztxthy");
		
//		System.out.println(val);
//		System.out.println("=====================================");
//		HttpSmsClient sqs = HttpSmsClient.getInstance();
//		String resT=sqs.getpr("47.100.64.16", "9219", "111_xy");
//		System.out.println(resT);
//		System.out.println("=====================================");
//		while(true){
//			HttpSmsClient sqs = HttpSmsClient.getInstance();
			String json= sqs.getpr("139.196.160.207","9219","88_xy");
//			String json= sqs.getpr("139.196.160.207","9219","sel_channel_bak");
			System.out.println(json);
			
//			sqs.putT("batch_abnormal", "87,13524696324,05231037018271429", "139.196.160.207", "9217");
			
			
//			try {
//				TimeUnit.SECONDS.sleep(1);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
	}
}
