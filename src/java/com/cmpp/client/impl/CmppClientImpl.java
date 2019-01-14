package com.cmpp.client.impl;

import net.sf.ehcache.Cache;

import org.apache.log4j.Logger;

import com.cmpp.client.api.ConfigApi;
import com.cmpp.client.common.Confighead;
import com.cmpp.client.component.AbstractComponent;
import com.cmpp.client.main.MinaClient;
import com.cmpp.client.monitor.ChannelBakThread;
import com.cmpp.client.monitor.FlowCloseThread;
import com.cmpp.client.monitor.MonitorThread;


public class CmppClientImpl extends AbstractComponent{
	private static Logger log = Logger.getLogger(CmppClientImpl.class);

	private String cliConfiguration;
	private Confighead confighead;
	private Cache monitorCache;
	
	public void setCliConfiguration(String cliConfiguration) {
		this.cliConfiguration = cliConfiguration;
	}
	
	public CmppClientImpl(){
		this.monitorCache=null;
	}
	
	public void initialize(){
		
		this.monitorCache = this.cacheManager.getCache("monitorCache");
		
		log.info("server--initialize.......start....");
		
		try{
			confighead=ConfigApi.ConfigGet(cliConfiguration);
			
			//滑动窗口数值清理
			FlowCloseThread.flowClose(this.monitorCache,confighead);

			//协议分流
			if(!"1".equals(confighead.getChannelbak())){
				ChannelBakThread.channelBak(confighead);
			}

			int connetnum = Integer.parseInt(confighead.getConnetnum().trim());
			for(int i=0;i<connetnum;i++){
				MinaClient minaClient=new MinaClient(confighead,this.monitorCache);
				minaClient.onCreate();
			}
			
			int islocal=Integer.parseInt(confighead.getIslocal());

			int ismonitor=Integer.parseInt(confighead.getIsmonitor());
			if(ismonitor==1){ //启动实时 监控-预警 机制
				Thread monitorThread=new Thread(new MonitorThread(confighead,monitorCache));
				monitorThread.setDaemon(true);
				monitorThread.start();
			}
			
		}catch (Exception e) {
			log.error("服务端启动异常....:", e);
		    e.printStackTrace();
	    }
	}
	@Override
	public void destroy() throws Exception{
		this.monitorCache.flush();
	}
}