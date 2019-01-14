package com.cmpp.client.monitor;

import com.cmpp.client.common.Confighead;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import org.apache.log4j.Logger;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 类型描述：提交未响应计数清理
 * @author 邱凤丹
 * @date 日期：2017-9-7  时间：下午7:05:18
 * @version 1.0
 */
public class FlowCloseThread extends Thread{
	
	public FlowCloseThread(){
		super();
		this.setDaemon(true);
	}
	
	public static void flowClose(Cache monitorCache,Confighead confighead){
		ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
		scheduledExecutorService.scheduleAtFixedRate(new threadStyle(monitorCache,confighead), 1, 10*60, TimeUnit.SECONDS);
	}

	private static class threadStyle implements Runnable{

		private static Logger log = Logger.getLogger(FlowCloseThread.class);
		
		private Cache monitorCache;
		private Confighead confighead;
	
		
		public threadStyle(Cache monitorCache,Confighead confighead){
			this.monitorCache = monitorCache;
			this.confighead = confighead;
		}
		
		@Override
		public void run() {
//			Mail mail=new Mail();
			String content=confighead.getQueuename()+"通道提交未响应计数清理：";
			String flowkey= null;
			Element flowElement=null;
			int flow=0;
			
			for(int i=1;i<100;i++){
				
				flowkey = "session-" + i;
				
				synchronized(this.monitorCache){
					flowElement = this.monitorCache.get(flowkey);
				}
				
				if(flowElement!=null){
					flow=Integer.parseInt(flowElement.getValue().toString());
					if(flow>0){
						//重新存入缓存
						synchronized(this.monitorCache){
							Element flowElementput=new Element(flowkey,0);
							this.monitorCache.put(flowElementput);
						}
						log.info("flow-close-key:"+flowkey+",count:"+flow);
						
					}
					content+="[key:"+flowkey+",count:"+flow+"]";
				}
				
			}
			
			content+="，清理完毕！";
			log.info(content);
//			mail.SendEmail(content, "qiufengdan@ztinfo.cn;xiangjinjing@ztinfo.cn");
			
//			log.info("flow====提交未响应计数清理缓存完毕。");
			
		}
	}
}
