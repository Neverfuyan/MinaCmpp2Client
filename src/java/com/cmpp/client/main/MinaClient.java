package com.cmpp.client.main;

import net.sf.ehcache.Cache;

import com.cmpp.client.common.Confighead;

public class MinaClient {
	
	private Confighead confighead;
	private Cache monitorCache;
	
	public MinaClient(Confighead confighead,Cache monitorCache){
		this.confighead=confighead;
		this.monitorCache=monitorCache;
	}
	
	public void onCreate(){
		//开启单独的线程，因为Service是位于主线程的，为了避免主线程被阻塞
		HeartBeatThread thread = new HeartBeatThread(confighead,monitorCache);
		
		thread.setDaemon(true);
		thread.start();
	}
}