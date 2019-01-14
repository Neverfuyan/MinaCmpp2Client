package com.cmpp.client.monitor;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cmpp.client.common.Confighead;
import com.cmpp.client.common.MonitorCount;

/**
 * 功能：cmpp2.0 实时监控
 * @author 蔡新鹏
 * @date 2017-03-01 14:17
 * @address 上海
 */

public class MonitorThread extends Thread{
	private static final Logger logger = LoggerFactory.getLogger(MonitorThread.class);

	private Confighead confighead=new Confighead();
	private Cache monitorCache;
	
	public MonitorThread(Confighead confighead,Cache monitorCache){
		
		this.confighead=confighead;
		this.monitorCache=monitorCache;
	}
	
	public void run(){
		try{
			MonitorCount monitorCount=new MonitorCount();
			
			String monitortime=confighead.getMonitortime().trim();
			String monitorfailure=confighead.getMonitorfailure().trim();
			
//			String monitormail=confighead.getMonitormail().trim();
//			String warningmail=confighead.getWarningmail().trim();
			
			String sp=confighead.getSpport().trim();
			String channel=confighead.getQueuename().trim();
			String localip=confighead.getLocalip().trim();
			
			Element monitorElement=null;
			String monitorkey="monitor";
			
			while (!Thread.currentThread().isInterrupted()){
				
				Thread.sleep(Integer.parseInt(monitortime)*1000);
				
				synchronized(this.monitorCache){
					monitorElement = this.monitorCache.get(monitorkey);
	        	}
				
				if(monitorElement==null){
					monitorCount=new MonitorCount();
					
					monitorCount.setCount(0);
					monitorCount.setCountsucc(0);
					monitorCount.setDelivercount(0);
					monitorCount.setDelivercountsucc(0);
					
					synchronized(this.monitorCache){
						Element monitorElementput=new Element(monitorkey,monitorCount);
						this.monitorCache.put(monitorElementput);
					}
					
					continue;
				}
				
				monitorCount=(MonitorCount)monitorElement.getValue();
				
				//预警算法
				if(monitorCount.getCount()>0){
					String content="通道:"+channel+
							",提交网关总数:"+monitorCount.getCount()+",提交成功:"+monitorCount.getCountsucc()+",提交失败:"+(+monitorCount.getCount()-monitorCount.getCountsucc())+
							",回执总数:"+monitorCount.getDelivercount()+",Deliver:"+monitorCount.getDelivercountsucc()+",非Deliver:"+(monitorCount.getDelivercount()-monitorCount.getDelivercountsucc()+",通道长号:"+sp+",服务器IP:"+localip);
					
					//监控邮件发送
					//MonitorApi.sendmonitorqqmail(content, monitormail);
					logger.info("监控邮件->"+content);
					
					//预警邮件发送
					if(monitorCount.getCountsucc()>=0){
						
						if(monitorCount.getCountsucc()>0){
							double b= Double.valueOf(Integer.toString(monitorCount.getCount()-monitorCount.getCountsucc()))/Double.valueOf(Integer.toString(monitorCount.getCount()));
							if(b >= Double.valueOf(monitorfailure)){
								content="预警:(通道:"+channel+
										",提交网关总数:"+monitorCount.getCount()+",提交成功:"+monitorCount.getCountsucc()+",提交失败:"+(monitorCount.getCount()-monitorCount.getCountsucc()+
										",回执总数:"+monitorCount.getDelivercount()+",Deliver:"+monitorCount.getDelivercountsucc()+",非Deliver:"+(monitorCount.getDelivercount()-monitorCount.getDelivercountsucc())+",通道长号:"+sp+",服务器IP:"+localip);
							
								monitorCount=new MonitorCount();
								
								monitorCount.setCount(0);
								monitorCount.setCountsucc(0);
								monitorCount.setDelivercount(0);
								monitorCount.setDelivercountsucc(0);
								
								synchronized(this.monitorCache){
									Element monitorElementput=new Element(monitorkey,monitorCount);
									this.monitorCache.put(monitorElementput);
								}
								
								//预警邮件发送
//								MonitorApi.sendmonitorqqmail(content, warningmail);
								logger.info(content);
								
								continue;
							}
							
							//回执预警
							if(monitorCount.getDelivercount()>0){

								if(monitorCount.getDelivercountsucc()>0){
									double a= Double.valueOf(Integer.toString(monitorCount.getDelivercount()-monitorCount.getDelivercountsucc()))/Double.valueOf(Integer.toString(monitorCount.getDelivercount()));
									if(a >= Double.valueOf(monitorfailure)){
										content="预警(通道:"+channel+
												",提交网关总数:"+monitorCount.getCount()+",提交成功:"+monitorCount.getCountsucc()+",提交失败:"+(monitorCount.getCount()-monitorCount.getCountsucc()+
												",回执总数:"+monitorCount.getDelivercount()+",Deliver:"+monitorCount.getDelivercountsucc()+",非Deliver:"+(monitorCount.getDelivercount()-monitorCount.getDelivercountsucc())+",通道长号:"+sp+",服务器IP:"+localip);
										
										//预警邮件发送
//										MonitorApi.sendmonitorqqmail(content, warningmail);
										logger.info(content);
										
										monitorCount=new MonitorCount();
										
										monitorCount.setCount(0);
										monitorCount.setCountsucc(0);
										monitorCount.setDelivercount(0);
										monitorCount.setDelivercountsucc(0);
										
										synchronized(this.monitorCache){
											Element monitorElementput=new Element(monitorkey,monitorCount);
											this.monitorCache.put(monitorElementput);
										}
										
										continue;
									}
								}
							}
						}
					}
					
					monitorCount=new MonitorCount();
					
					monitorCount.setCount(0);
					monitorCount.setCountsucc(0);
					monitorCount.setDelivercount(0);
					monitorCount.setDelivercountsucc(0);
					
					synchronized(this.monitorCache){
						Element monitorElementput=new Element(monitorkey,monitorCount);
						this.monitorCache.put(monitorElementput);
					}
				}
			}
		}catch (Exception e){
			logger.info("[MonitorThread][exception]:"+e.getMessage());
			try {
				Thread.sleep(3000L);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}
}