package com.cmpp.client.api;

import com.cmpp.client.common.*;
import com.cmpp.httpsqs.HttpSmsClient;
import com.cmpp.util.HttpClientUtil;
import com.cmpp.util.TimeUtil;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;

import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 功能：平台 deliver report 的处理
 * @author 蔡新鹏
 * @date 2017-02-18 14:54
 * @address 上海
 */
public class PtSubmitApi {
	
	private static final Logger logger = Logger.getLogger(PtSubmitApi.class);
	
	private static HttpSmsClient httpSmsClient = HttpSmsClient.getInstance();
	
	/**
	 * 功能：综合平台 回执deliver的推送处理
	 * 开发:蔡新鹏
	 * 时间：2017-02-18 15：04
	 * 地点：上海
	 */
	public static void pt_zh_senddeliver(IncmppSubmit incmppSubmit,IncmppDeliver incmppDeliver,Confighead confighead){
		
		PtReport bean=new PtReport();
		
		//入综合  -总队列
		String queuezhkey=confighead.getQueuezhkey().trim();
		String queuezh=confighead.getQueuezh().trim();
		String queueportzh=confighead.getQueueportzh().trim();
		
		//组织bean
		bean.setMessageid(incmppSubmit.getCpmid().trim());//平台msgid
		bean.setMobile(incmppDeliver.getDestterminalid().trim());//号码
		if(!"DELIVRD".equals(incmppDeliver.getStat().trim())){
			bean.setRemark("发送失败");
			bean.setState("0");
			bean.setSubmitstate(incmppDeliver.getStat().trim());
		}else{
			bean.setRemark("发送成功");
			bean.setState("1");
			bean.setSubmitstate("DELIVRD");
		}
		bean.setReportTime(TimeUtil.mNowTime());
		
		try {
			//入总队列
			String resvalue=httpSmsClient.putT(queuezhkey, JSONObject.fromObject(bean).toString(), queuezh, queueportzh);
			if(resvalue.equals("HTTPSQS_PUT_OK")){
				logger.info("[SP->ZH-QUEUE-OK]["+queuezh.trim()+":"+queueportzh.trim()+"]["+incmppSubmit.getP_num()+"]["+bean.getMessageid()+","+bean.getMobile()+","+bean.getState()+","+bean.getSubmitstate()+"]");
		    }else{
		    	logger.info("[SP->ZH-QUEUE-ERROR]["+queuezh.trim()+":"+queueportzh.trim()+"]["+incmppSubmit.getP_num()+"][queuerec:"+resvalue+"]["+bean.getMessageid()+","+bean.getMobile()+","+bean.getState()+","+bean.getSubmitstate()+"]");
			}
			
			//入用户队列
			String mid= incmppSubmit.getMid().trim();
			if(mid!=null && mid!=""){
				String[] userid=mid.split(",");
				String userqueuekey=userid[0].trim() + "_"+queuezhkey;
				
				boolean b = false;
				if(!incmppDeliver.getStat().startsWith("ZT")){
					b = true;
				}else if("ZT:013".equals(incmppDeliver.getStat().trim())){
					b = true;
				}
				if(b){
					String sult = httpSmsClient.putT(userqueuekey,JSONObject.fromObject(bean).toString(),queuezh, queueportzh);
					if(resvalue.equals("HTTPSQS_PUT_OK")){
						logger.info("[SP->ZH-USERQUEUE-OK]["+queuezh.trim()+":"+queueportzh.trim()+"]["+incmppSubmit.getP_num()+"][mid:"+mid+"]["+bean.getMessageid()+","+bean.getMobile()+","+bean.getState()+","+bean.getSubmitstate()+"]");
				    }else{
				    	logger.info("[SP->ZH-USERQUEUE-ERROR]["+queuezh.trim()+":"+queueportzh.trim()+"]["+incmppSubmit.getP_num()+"][queuerec:"+sult+"][mid:"+mid+"]["+bean.getMessageid()+","+bean.getMobile()+","+bean.getState()+","+bean.getSubmitstate()+"]");
					}
				}
			}
		} catch (Exception e) {
			logger.info("[PtSubmitApi-ZH-Report][Exception][value:"+JSONObject.fromObject(bean).toString()+"]["+e.getMessage()+"]");
			e.printStackTrace();
		}
	}
	
	/**
	 * 功能：阿里云平台 回执deliver的推送处理
	 * 开发:蔡新鹏
	 * 时间：2017-02-18 15：04
	 * 地点：上海
	 */
	public static void pt_aly_senddeliver(IncmppSubmit incmppSubmit,IncmppDeliver incmppDeliver,Confighead confighead){
		PtReport bean=new PtReport();
		
		//入阿里云  -总队列
		String queuealykey=confighead.getQueuealykey().trim();
		String queuealy=confighead.getQueuealy().trim();
		String queueportaly=confighead.getQueueportaly().trim();
		String queueyaly2=confighead.getQueueyaly2().trim();
		String queueAdress = "";

		int number =(int)((Math.random()*2)+1); 
		if(number==1){
			queueAdress = queuealy;
		}else if(number==2){
			queueAdress = queueyaly2;
		}
		
		//组织bean
		bean.setMessageid(incmppSubmit.getCpmid().trim());//平台msgid
		bean.setMobile(incmppDeliver.getDestterminalid().trim());//号码
		if("DELIVRD".equals(incmppDeliver.getStat().trim())){
			bean.setRemark("发送成功");
			bean.setState("1");
			bean.setSubmitstate("DELIVRD");
		}else{
			bean.setRemark("发送失败");
			bean.setState("3");
			bean.setSubmitstate(incmppDeliver.getStat().trim());
		}

		String msgid = incmppSubmit.getCpmid().trim();
		
		if(msgid != null && !"".equals(msgid) && msgid.length()>14){
			if(!msgid.startsWith("20")){
				msgid = TimeUtil.getNowTime("yyyy")+msgid;
			}
			bean.setReportTime(TimeUtil.ReportTime(msgid.substring(0,14)));
		}else{
			bean.setReportTime(TimeUtil.mNowTime());
		}
		
		//入总队列
		try{
			String resvalue=httpSmsClient.putT(queuealykey, JSONObject.fromObject(bean).toString(), queueAdress, queueportaly);
			if(resvalue.equals("HTTPSQS_PUT_OK")){
				logger.info("[SP->ALY-QUEUE-OK]["+incmppSubmit.getP_num()+"]["+bean.getMessageid()+","+bean.getMobile()+","+bean.getState()+","+bean.getSubmitstate()+"]");
		    }else{
		    	logger.info("[SP->ALY-QUEUE-ERROR][queuerec:"+resvalue+"]["+incmppSubmit.getP_num()+"]["+bean.getMessageid()+","+bean.getMobile()+","+bean.getState()+","+bean.getSubmitstate()+"]");
			}
			
			//入用户队列
			String mid= incmppSubmit.getMid().trim();
			if(mid!=null && !"".equals(mid)){
				String[] userid=mid.split(",");
				String userqueuekey=userid[0].trim() + "_"+queuealykey;
				boolean b = false;
				if(!incmppDeliver.getStat().startsWith("ZT")){
					b = true;
				}else if("ZT:013".equals(incmppDeliver.getStat().trim())){
					b = true;
				}
				if(b){
					String sult = httpSmsClient.putT(userqueuekey,JSONObject.fromObject(bean).toString(),queueAdress, queueportaly);
					
					if(resvalue.equals("HTTPSQS_PUT_OK")){
						logger.info("[SP->ALY-USERQUEUE-OK]["+incmppSubmit.getP_num()+"][mid:"+mid+"]["+bean.getMessageid()+","+bean.getMobile()+","+bean.getState()+","+bean.getSubmitstate()+"]");
				    }else{
				    	logger.info("[SP->ALY-USERQUEUE-ERROR][queuerec:"+sult+"]["+incmppSubmit.getP_num()+"][mid:"+mid+"]["+bean.getMessageid()+","+bean.getMobile()+","+bean.getState()+","+bean.getSubmitstate()+"]");
					}
				}
			}
		}catch(Exception e){
			logger.info("[PtSubmitApi-ALY-Report][Exception][value:"+JSONObject.fromObject(bean).toString()+"]["+e.getMessage()+"]");
			e.printStackTrace();
		}
	}
	
	/**
	 * 方法描述：综合平台上行http方式推送
	 * @author 邱凤丹
	 * @date 日期：2018-8-6  时间：下午3:12:36
	 * @version 1.0
	 * @param incmppDeliver
	 * @param confighead
	 * @param ptport
	 */
	public static void pt_zh_sendreport_http(IncmppDeliver incmppDeliver,Confighead confighead,Ptport ptport){
		String phone = incmppDeliver.getDestterminalid().trim();
        if (phone != null) {
            Pattern p = Pattern.compile("^(\\+86|86|0086|\\ 86)");
            Matcher m = p.matcher(phone);
            phone = m.replaceAll("");
        }
        
        String xh = ptport.getPort().trim();//存放小号
	        
	    String upurlaly="";
	    try{
	    	upurlaly = confighead.getUpurlzh().trim() + "?content="+ URLEncoder.encode(incmppDeliver.getContent(), "UTF-8")+"&mobile="+phone+"&xh="+xh+"&channel_id="+confighead.getQueuename();
	    	String ret = HttpClientUtil.sendGet(upurlaly);
		    
		    logger.info("[HTTP-ZH-REPLY][mobile:"+phone+"][xh:"+xh+"][content:"+incmppDeliver.getContent()+"]["+ret+"]");
		}catch (Exception e){
			logger.info("[PtSubmitApi-ZH-Reply][Exception][value:"+upurlaly+"]["+e.getMessage()+"]");
	    	e.printStackTrace();    	  
	    }
	}
	
	/**
	 * 方法描述：V5平台上行http方式推送
	 * @author 邱凤丹
	 * @date 日期：2018-1-16  时间：下午4:04:22
	 * @version 1.0
	 * @param incmppDeliver
	 * @param confighead
	 * @param ptport
	 */
	public static void pt_aly_sendreport_http(IncmppDeliver incmppDeliver,Confighead confighead,Ptport ptport){
		String phone = incmppDeliver.getDestterminalid().trim();
        if (phone != null) {
            Pattern p = Pattern.compile("^(\\+86|86|0086|\\ 86)");
            Matcher m = p.matcher(phone);
            phone = m.replaceAll("");
        }
        
        String xh = ptport.getPort().trim();//存放小号
        
		String upurlaly = "";
		
		try{		
			upurlaly = confighead.getUpurlaly() + "?content="+ URLEncoder.encode(incmppDeliver.getContent(), "UTF-8")+"&mobile="+phone+"&xh="+xh+"&channel_id="+confighead.getQueuename();
			String ret = HttpClientUtil.sendGet(upurlaly);
		    
		    logger.info("[HTTP-ALY-REPLY][mobile:"+phone+"][xh:"+xh+"][content:"+incmppDeliver.getContent()+"]["+ret+"]");
		}catch (Exception e){
			logger.info("[PtSubmitApi-ALY-Reply][Exception][value:"+upurlaly+"]["+e.getMessage()+"]");
	    	e.printStackTrace();    	  
	    }
	}
	
	/**
	 * 方法描述：融合平台状态报告推送
	 * @author 邱凤丹
	 * @date 日期：2018-10-25  时间：下午3:42:18
	 * @version 1.0
	 * @param incmppSubmit
	 * @param incmppDeliver
	 * @param confighead
	 */
	public static void pt_vx_report(IncmppSubmit incmppSubmit,IncmppDeliver incmppDeliver,Confighead confighead){
		
			String messageid = incmppSubmit.getCpmid().trim();//消息id
			String submitstate = incmppDeliver.getStat().trim();//原始状态报告
			String reportTime = TimeUtil.stringNowTime();//状态报告时间
			String channel_id = confighead.getQueuename();//通道号
			String mobile = incmppDeliver.getDestterminalid().trim();
			
			JSONObject json = new JSONObject();
			json.put("messageid", messageid);
			json.put("submitstate", submitstate);
			json.put("reportTime", reportTime);
			json.put("channel_id", channel_id);
			json.put("mobile", mobile);
			
			String url = confighead.getVxreport().trim();
			
		try{
			String resvalue = HttpClientUtil.sendPostJson(url, json.toString());
			
			logger.info("[HTTP-VX-REPORT][resvalue:"+resvalue+"][p_num:"+incmppSubmit.getP_num()+"]["+messageid+","+mobile+","+submitstate+"]");
		}catch (Exception e){
			logger.info("[PtSubmitApi-VX-Report][Exception][value:"+json.toString()+"]["+e.getMessage()+"]");
	    	e.printStackTrace();    	  
	    }
	}

	/**
	 * 方法描述：融合平台上行回复推送
	 * @author 邱凤丹
	 * @date 日期：2018-10-25  时间：下午3:43:36
	 * @version 1.0
	 * @param incmppDeliver
	 * @param confighead
	 * @param ptport
	 */
	public static void pt_vx_reply(IncmppDeliver incmppDeliver,Confighead confighead,Ptport ptport){
		String phone = incmppDeliver.getDestterminalid().trim();
        if (phone != null) {
            Pattern p = Pattern.compile("^(\\+86|86|0086|\\ 86)");
            Matcher m = p.matcher(phone);
            phone = m.replaceAll("");
        }
        
        String xh = ptport.getPort().trim();//存放小号
        
        JSONObject json = new JSONObject();
        json.put("content", incmppDeliver.getContent());
        json.put("mobile", phone);
        json.put("xh", xh);
        json.put("channel_id", confighead.getQueuename());
        
		String url=confighead.getVxreply().trim();
		
		try{
			String resvalue = HttpClientUtil.sendPostJson(url, json.toString());
		    
			logger.info("[HTTP-VX-REPLY][mobile:"+phone+"][xh:"+xh+"][content:"+incmppDeliver.getContent()+"]["+resvalue+"]");
		}catch (Exception e){
			logger.info("[PtSubmitApi-VX-Reply][Exception][value:"+json.toString()+"]["+e.getMessage()+"]");
	    	e.printStackTrace();    	  
	    }
	}
}
