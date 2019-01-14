package com.cmpp.client.active;

import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;

import com.cmpp.client.api.SendApi;

public class ActiveThread extends Thread{
	private IoSession session = null;
	private static final Logger logger = Logger.getLogger(ActiveThread.class);
	
	private long heartbeatInterval = 30000;
	private long heartbeatRetry = 3;
	private long reconnectInterval = 3000;
	public static long lastActiveTime = 0;
	private long lastCheckTime = 0;
	
	public ActiveThread(IoSession s){
		setDaemon(true);
		this.session = s;
		lastCheckTime = System.currentTimeMillis();
		lastActiveTime = System.currentTimeMillis();
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void run(){
		try{
			while (session.isConnected()){
				long currentTime = System.currentTimeMillis();
				//logger.info("[心跳][启动检测.............][(currentTime - lastCheckTime):"+(currentTime - lastCheckTime)+"][currentTime:"+currentTime+",lastCheckTime:"+lastCheckTime+",heartbeatInterval:"+heartbeatInterval+"]");
				if ((currentTime - lastCheckTime) > heartbeatInterval){
					//logger.info("CmppSession.checkConnection");
					if ((currentTime - lastActiveTime) < (heartbeatInterval * heartbeatRetry)){
						//logger.info("send ActiveTest----begin");
						lastCheckTime = currentTime;
						byte[] buffer=SendApi.cmpp_send_active();
						session.write(buffer);
						//logger.info("send ActiveTest----end");
					}else{
						logger.info("connection lost!");
						session.close(true);
						break;
					}
				}
				try{
					Thread.sleep(reconnectInterval);
				}catch (InterruptedException e){
					//
					logger.info("CmppSession.checkConnection:session is not Connected");
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
}
