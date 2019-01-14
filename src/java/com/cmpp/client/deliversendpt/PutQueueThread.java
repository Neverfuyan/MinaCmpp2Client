package com.cmpp.client.deliversendpt;

import org.apache.log4j.Logger;

import com.cmpp.client.api.PtSubmitApi;
import com.cmpp.client.common.Confighead;
import com.cmpp.client.common.IncmppDeliver;
import com.cmpp.client.common.IncmppSubmit;
import com.cmpp.client.common.Ptport;
import com.cmpp.redis.RedisUtil;
import com.google.gson.Gson;

/**
 * 类型描述：状态报告分发入相应平台
 * @author 邱凤丹
 * @date 日期：2017-11-8  时间：下午2:12:20
 * @version 1.0
 */
public class PutQueueThread extends Thread{

	private static final Logger logger = Logger.getLogger(DeliverSendThread.class);
	private RedisUtil redis = RedisUtil.getInstance();
	private IncmppDeliver bean;
	private Confighead confighead=new Confighead();
	
	public PutQueueThread(IncmppDeliver bean,Confighead confighead){
		this.bean = bean;
		this.confighead = confighead;
	}
	@Override
	public void run(){
		if(bean.getDelivery() == 1){ //状态报告
			
			logger.info("[PutQueueThread-Report][get-queue][delivery:"+bean.getDelivery()+",msgid:"+bean.getMsgid()+",stat:"+bean.getStat()+",submittime:"+bean.getSubmittime()+",donetime:"+bean.getDonetime()+",destterminalid:"+
					bean.getDestterminalid()+",smscsequence:"+bean.getSmscsequence()+"]");
			
			//根据msgid获取 redis-report缓存块
			String deliverkey= "client_cmpp_"+confighead.getQueuename()+ "_rc_" + bean.getMsgid().trim();
			
			String delivermtElement = redis.get(deliverkey);
			
			IncmppSubmit incmppSubmit=new IncmppSubmit();
			
			if(delivermtElement == null || "".equals(delivermtElement)){
				logger.info("[PutQueueThread][reportRedis][not found][deliverkey:"+deliverkey+"][msgid:"+bean.getMsgid()+",stat:"+bean.getStat()+",destterminalid:"+
						bean.getDestterminalid()+",smscsequence:"+bean.getSmscsequence()+"]");
			}else{
				incmppSubmit = new Gson().fromJson(delivermtElement, IncmppSubmit.class);
				redis.del(deliverkey);
				
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
				}
			}
		}else{//上行 -回复
			logger.info("[PutQueueThread-Reply][get-queue][port:"+bean.getStat().trim()+"][msgid:"+bean.getMsgid()+",destterminalid:"+
					bean.getDestterminalid()+",smscsequence:"+bean.getSmscsequence()+",content:"+bean.getContent()+"]");
			
			int len=confighead.getSpport().length();
			String port=bean.getStat().trim();
			port=port.substring(len);
			
			Ptport ptport=new Ptport();
			
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
			
			if("ZH".equals(ptport.getPlatform())){
				PtSubmitApi.pt_zh_sendreport_http(bean, confighead, ptport);
			}else if("ALY".equals(ptport.getPlatform())){
				PtSubmitApi.pt_aly_sendreport_http(bean, confighead, ptport);
			}else if("VX".equals(ptport.getPlatform())){
				PtSubmitApi.pt_vx_reply(bean, confighead, ptport);
			}
		}
	}
}
