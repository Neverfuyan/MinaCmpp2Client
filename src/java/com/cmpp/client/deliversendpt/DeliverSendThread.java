package com.cmpp.client.deliversendpt;

import net.sf.json.JSONObject;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cmpp.client.common.Confighead;
import com.cmpp.client.common.IncmppDeliver;
import com.cmpp.httpsqs.HttpSmsClient;

/**
 * 功能：上行 - 回执 的队列检索发送
 * @author 蔡新鹏
 * @date 2017-02-17 14:17
 * @address 上海
 */
public class DeliverSendThread extends Thread{
	private static final Logger logger = LoggerFactory.getLogger(DeliverSendThread.class);
	private IoSession session = null;
	private boolean Connect=false;
	private Confighead confighead=new Confighead();
	
	private static HttpSmsClient httpSmsClient = HttpSmsClient.getInstance();
	
	public DeliverSendThread(IoSession session,boolean Connect,Confighead confighead){
		this.session=session;
		this.Connect=Connect;
		this.confighead=confighead;
	}
	
	public void run(){
		while (session.isConnected() & Connect == true){
			String get_value = "";
			try{
				//获取队列数据
				get_value=httpSmsClient.getT(confighead.getQueuename().trim()+"#deliver", confighead.getQueue().trim(), confighead.getQueueport().trim());
				if (get_value.startsWith("HTTPSQS_GET_END") || get_value.startsWith("HTTPSQS_ERROR")){
					Thread.sleep(500L);
		            continue;
		        }
				
				//解析 -IncmppDeliver
				IncmppDeliver bean=new IncmppDeliver();
				bean=(IncmppDeliver)JSONObject.toBean(JSONObject.fromObject(get_value),IncmppDeliver.class);
					
				Thread thread=new Thread(new PutQueueThread(bean, confighead));
				thread.setDaemon(true);
				thread.start();
						
			} catch (Exception e) {
				logger.info("[DeliverSendThread][get_value:"+get_value+" |exception:"+e.getMessage()+"]");
				e.printStackTrace();
			}
		}
	}
}
