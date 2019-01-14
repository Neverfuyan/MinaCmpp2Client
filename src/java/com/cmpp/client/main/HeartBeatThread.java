package com.cmpp.client.main;

import com.cmpp.client.common.Confighead;
import com.cmpp.redis.RedisUtil;
import com.cmpp.util.TimeUtil;
import net.sf.ehcache.Cache;
import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

public class HeartBeatThread extends Thread{
	private static final Logger logger = LoggerFactory.getLogger(HeartBeatThread.class);
	private RedisUtil redis = RedisUtil.getInstance();
	
	public Confighead confighead;
	private IoConnector connector = null;
	private IoSession session;
	private Cache monitorCache;
//	private String warnemail = "sysmer2@ztinfo.cn;qiufengdan@ztinfo.cn";
	
	public HeartBeatThread(Confighead confighead,Cache monitorCache){
		this.confighead=confighead;
		this.monitorCache=monitorCache;
	}

	public void run(){
		initClientMina();
	} 
	
	public void initClientMina(){
		
		try {
			connector = new NioSocketConnector();
			//创建接受数据的过滤器
			connector.getFilterChain().addLast("codec",new ProtocolCodecFilter(new CodecFactory()));
			//设置IO处理器
			connector.setHandler(new HandleClient(confighead,monitorCache));
			connector.setConnectTimeoutMillis(30000);
			
			connector.getFilterChain().addLast("exceutor", new ExecutorFilter());
			connector.getFilterChain().addFirst("reconnection", new IoFilterAdapter() {  
	            @Override  
	            public void sessionClosed(NextFilter nextFilter, IoSession ioSession) throws Exception {  
	                for(;;){
	                	long num = 0;
	                    try{
	                    	//当天断链10/50/100次是触发短信预警
	                    	String key = "cmpp_client_error_"+confighead.getQueuename();
	                    	num = redis.incr(key);
	                    	if(num == 1){
	                    		redis.expire(key, TimeUtil.time());
	                    	}
	                    	if(num == 10 || num == 50 || num == 100){
	                    		String content = "CMPP协议"+confighead.getQueuename()+"通道链接异常，当天已断开"+num+"次，流数："+confighead.getConnetnum()+"*"+confighead.getMaxnum()+"。协议地址："+confighead.getLocalip();
	                    		for(int i=0;i<3;i++){
	                    			TimeUtil.sms("18356265133,13611924596", content);
	                    			Thread.sleep(1000);
	                    		}
	                    	}else{
	                    		Thread.sleep(3000);
	                    	}
	                    	
	                        ConnectFuture future = connector.connect(new InetSocketAddress(confighead.getBindip(), Integer.parseInt(confighead.getBindport()))); 
	                        
	                        // 等待连接创建成功  
	    	                future.awaitUninterruptibly();  
	    	                // 获取会话  
	    	                session = future.getSession();
	    	                
	    	                if(session.isConnected()){
	    	                	logger.info("[SP->GW][HeartBeatThread][connect]["+num+"]["+confighead.getLocalip()+"][session="+session.getId()+"]重连成功 ....11");
	    	                	
		                		break;
	                        }
	                    }catch(Exception ex){  
	                    	logger.info("[SP->GW][connect]["+num+"]["+confighead.getLocalip()+",重连服务器登录失败,5秒再连接一次:" + ex.getMessage());  
	                    	
//	                    	String channel=confighead.getQueuename().trim();
//	        				String localip=confighead.getLocalip().trim();
	        				
//	        				String content="严重预警:通道:"+channel+",服务器IP:"+localip+" 链接出现异常.[HeartBeatThread-2][exception]"+ex.getMessage();
//	        				MonitorApi.sendmonitorqqmail(content, warnemail);
	                    }  
	                }  
	            }  
	        });
			
			//连接到服务器
			for (;;) {  
	            try {  
	            	ConnectFuture future = connector.connect(new InetSocketAddress(confighead.getBindip(), Integer.parseInt(confighead.getBindport()))); 
	                
	            	// 等待连接创建成功  
	                future.awaitUninterruptibly();  
	                
	                // 获取会话  
	                session = future.getSession();
	                
	                logger.info("[SP->GW][connect][HeartBeatThread]["+confighead.getLocalip()+",连接服务端 IP:"+confighead.getBindip()+" port:"+confighead.getBindport()+" 成功!");
	                
	                break;
	            } catch (RuntimeIoException e) {  
	            	logger.info("[SP->GW][connect][HeartBeatThread]["+confighead.getLocalip()+",连接服务端 IP:"+confighead.getBindip()+" port:"+confighead.getBindport()+" 失败!");
	            	
	            	Thread.sleep(5000);// 连接失败后,重连间隔5s
	            	
//	            	String channel=confighead.getQueuename().trim();
//    				String localip=confighead.getLocalip().trim();
    				
//    				String content="严重预警:通道:"+channel+",服务器IP:"+localip+" 链接出现异常.[HeartBeatThread-2][exception]"+e.getMessage();
    				
//    				MonitorApi.sendmonitorqqmail(content, warnemail);
    				
	            }
			}
			
		}catch (Exception e2) {
            e2.printStackTrace();  
            logger.info(e2.toString());  
        }
	}
}
