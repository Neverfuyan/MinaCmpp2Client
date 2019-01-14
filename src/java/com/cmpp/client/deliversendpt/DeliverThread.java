package com.cmpp.client.deliversendpt;

import com.cmpp.client.api.PtSubmitApi;
import com.cmpp.client.common.*;
import com.cmpp.httpsqs.HttpSmsClient;
import com.cmpp.redis.RedisUtil;
import com.google.gson.Gson;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 功能：上行 - 回执 的队列检索发送
 * @author 蔡新鹏
 * @date 2017-02-17 14:17
 * @address 上海
 */
public class DeliverThread extends Thread{
	private static final Logger logger = LoggerFactory.getLogger(DeliverThread.class);
	private RedisUtil redis = RedisUtil.getInstance();
	private Confighead confighead=new Confighead();
	private Cache monitorCache;
	
	private static HttpSmsClient httpSmsClient = HttpSmsClient.getInstance();
	
	public DeliverThread(Confighead confighead,Cache monitorCache){
		this.confighead=confighead;
		this.monitorCache=monitorCache;
	}
	
	public void run(){
		try{
			while (true){
				//获取队列数据
				String get_value=httpSmsClient.getT(confighead.getQueuename().trim()+"#deliver", confighead.getQueue().trim(), confighead.getQueueport().trim());
				if (get_value.startsWith("HTTPSQS_GET_END") || get_value.startsWith("HTTPSQS_ERROR")){
					Thread.sleep(500L);
    	            continue;
    	        }
				
				//解析 -IncmppDeliver
				IncmppDeliver bean=new IncmppDeliver();
				try{
					bean=(IncmppDeliver)JSONObject.toBean(JSONObject.fromObject(get_value),IncmppDeliver.class);
					
					if(bean.getDelivery() == 1){ //状态报告
						logger.info("[DeliverSendThread-Report-2][get-queue][delivery:"+bean.getDelivery()+",msgid:"+bean.getMsgid()+",stat:"+bean.getStat()+",submittime:"+bean.getSubmittime()+",donetime:"+bean.getDonetime()+",destterminalid:"+
								bean.getDestterminalid()+",smscsequence:"+bean.getSmscsequence()+",content:"+bean.getContent()+"]");
						
						//根据msgid获取reids-report数据
						String deliverkey= "client_cmpp_"+confighead.getQueuename()+ "_rc_" + bean.getMsgid().trim();
						
						String delivermtElement = redis.get(deliverkey);
						
						IncmppSubmit incmppSubmit=new IncmppSubmit();
						
						if(delivermtElement==null || "".equals(delivermtElement)){
							logger.info("[DeliverSendThread][reportRedis][not found][deliverkey:"+deliverkey+"][delivery:"+bean.getDelivery()+",msgid:"+bean.getMsgid()+",stat:"+bean.getStat()+",submittime:"+bean.getSubmittime()+",donetime:"+bean.getDonetime()+",destterminalid:"+
									bean.getDestterminalid()+",smscsequence:"+bean.getSmscsequence()+",content:"+bean.getContent()+"]");
							continue;
						}else{
							incmppSubmit = new Gson().fromJson(delivermtElement, IncmppSubmit.class);
							redis.del(deliverkey);
						}
						
						//判断平台
						String pt=incmppSubmit.getPlatform();
						if("ZH".equals(pt.trim())){
							//deliver-综合平台
							PtSubmitApi.pt_zh_senddeliver(incmppSubmit, bean, confighead);
						}else if("ALY".equals(pt.trim())){
							//deliver-阿里云平台
							PtSubmitApi.pt_aly_senddeliver(incmppSubmit, bean, confighead);
						}else if("VX".equals(pt.trim())){
							//deliver-融合平台
							PtSubmitApi.pt_vx_report(incmppSubmit, bean, confighead);
						}/*else if("NT".equals(pt.trim())){
							String msg= TimeUtil.getNowTime("yyyyMMddHHmmssSSS888");
							String mobile=bean.getDestterminalid().trim();
							String url ="http://139.224.218.50:8848/bjkjcxreport.do?report=" + msg+","+mobile+","+bean.getStat().trim()+","+TimeUtil.getNowTime("yyyyMMdd")+"&channel_id:"+confighead.getQueuename();
							String result= HttpClientUtil.sendGet(url);
							logger.info("[HTTP-ALY-OK][queuerec:"+result+"]["+msg+","+mobile+","+bean.getStat().trim()+"]");
						}*/
						
						//回执条数插入监控 缓存块
						MonitorCount monitorCount=new MonitorCount();
						
						String monitorkey="monitor";
						
						Element monitorElement=null;
						synchronized(this.monitorCache){
							monitorElement = this.monitorCache.get(monitorkey);
				    	}
						
						if(monitorElement==null){
							monitorCount.setCount(0);
							monitorCount.setCountsucc(0);
							monitorCount.setDelivercount(1);
							
							if("DELIVRD".equals(bean.getStat().trim())){
								monitorCount.setDelivercountsucc(1);
							}else{
								monitorCount.setDelivercountsucc(0);
							}

							synchronized(this.monitorCache){
								Element monitorElementput=new Element(monitorkey,monitorCount);
								this.monitorCache.put(monitorElementput);
							}
						}else{
							monitorCount=(MonitorCount)monitorElement.getValue();
							
							int delivercount=monitorCount.getDelivercount()+1;
							monitorCount.setDelivercount(delivercount);
							
							int delivercountsucc=0;
							if("DELIVRD".equals(bean.getStat().trim())){
								delivercountsucc=monitorCount.getDelivercountsucc()+1;
							}else{
								delivercountsucc=monitorCount.getDelivercountsucc();
							}
							monitorCount.setDelivercountsucc(delivercountsucc);
							
							synchronized(this.monitorCache){
								Element monitorElementput=new Element(monitorkey,monitorCount);
								this.monitorCache.put(monitorElementput);
							}
						}
						
					}else{//上行 -回复
						logger.info("[DeliverSendThread-Reply-2][get-queue][port:"+bean.getStat().trim()+"][delivery:"+bean.getDelivery()+",msgid:"+bean.getMsgid()+",stat:"+bean.getStat()+",submittime:"+bean.getSubmittime()+",donetime:"+bean.getDonetime()+",destterminalid:"+
								bean.getDestterminalid()+",smscsequence:"+bean.getSmscsequence()+",content:"+bean.getContent()+"]");
						
						int len=confighead.getSpport().length();
						String port=bean.getStat().trim();
						port=port.substring(len);
						
						Ptport ptport=new Ptport();
						
						if("".equals(port)){
							ptport.setPlatform("ALY");
							ptport.setPort("616698");
						}else{
							String portkey = "client_cmpp_"+confighead.getQueuename()+"_pc_"+bean.getDestterminalid().trim();
							
							String portElement = redis.get(portkey);
							
							if(portElement==null || "".equals(portElement)){
								logger.info("[上行-小号在缓存块portRedis中找不到][phone:"+bean.getDestterminalid().trim()+"][port:"+port+"]["+portkey+"]");
								
								if(confighead.getQueuename().length()==4){
									if(confighead.getQueuename().startsWith("9") || confighead.getQueuename().startsWith("8") || confighead.getQueuename().startsWith("7") || confighead.getQueuename().startsWith("6")){
										ptport.setPlatform("VX");
									}else{
										ptport.setPlatform("ALY");
									}
								}else{
									ptport.setPlatform("ZH");
								}
								ptport.setPort(port);
							}else{
								ptport = new Gson().fromJson(portElement, Ptport.class);
							}
						}
						
						if("ZH".equals(ptport.getPlatform())){
							//report-综合平台
//							if("162".equals(confighead.getQueuename())){
								PtSubmitApi.pt_zh_sendreport_http(bean, confighead, ptport);
//							}else{
//								PtSubmitApi.pt_zh_sendreport(bean, confighead, ptport);
//							}
						}else if("ALY".equals(ptport.getPlatform())){
							//report-行业平台
//							if("178".equals(confighead.getQueuename())){
								PtSubmitApi.pt_aly_sendreport_http(bean, confighead, ptport);
//							}else{
//								PtSubmitApi.pt_aly_sendreport(bean, confighead, ptport);
//							}
						}else if("VX".equals(ptport.getPlatform())){
							PtSubmitApi.pt_vx_reply(bean, confighead, ptport);
						}
					}
				}catch(Exception e){
					logger.info("[DeliverSendThread][get_value:"+get_value+" |exception"+e.getMessage()+"]");
					Thread.sleep(3000L);
					continue;
				}
			}
		}catch (Exception e){
			logger.info("[DeliverSendThread][exception]:"+e.getMessage());
			try {
				Thread.sleep(3000L);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}
}
