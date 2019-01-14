package com.cmpp.client.msgsend;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;

import com.cmpp.client.api.SendSubmitApi;
import com.cmpp.client.common.Confighead;
import com.cmpp.client.common.InSubmit;
import com.cmpp.client.common.IncmppSubmit;
import com.cmpp.redis.RedisUtil;
import com.cmpp.smshead.CmppHeader;
import com.cmpp.smshead.CmppSubmit;
import com.cmpp.util.FormatUtil;

/**
 * 类型描述：状态报告分发入相应平台
 * @author 邱凤丹
 * @date 日期：2017-11-8  时间：下午2:12:20
 * @version 1.0
 */
public class SendSmsThread extends Thread{

	private static final Logger logger = Logger.getLogger(SendSmsThread.class);
	private RedisUtil redis = RedisUtil.getInstance();
	
	private InSubmit bean;
	private Confighead confighead;
	private IoSession session = null;
	private String queue_flag;
	private int smsnum;
	private int flownum;

	public SendSmsThread(InSubmit bean,Confighead confighead,IoSession session,String queue_flag,int smsnum,int flownum){
		this.bean = bean;
		this.confighead = confighead;
		this.session = session;
		this.queue_flag = queue_flag;
		this.smsnum = smsnum;
		this.flownum = flownum;
	}
	@Override
	public void run(){
		try {
			CmppHeader cmppHeader=new CmppHeader();
			CmppSubmit cmppSubmit=new CmppSubmit();
			
			String content="";
			content=bean.getContent().trim();
			
			String[] mobilenumarr=bean.getMobile().split(",");
			
			for(int pp=0;pp<mobilenumarr.length;pp++){
				
				if(smsnum==1){//短短信

					byte[] sendbuffer=SendSubmitApi.cmpp_send_submitsms(cmppHeader, cmppSubmit, bean, confighead, mobilenumarr[pp], content, smsnum, smsnum, (byte)1);
					
					//获取
					String sequencekey=Integer.toString(cmppHeader.getSequence_Id());
					String mtkey = "client_cmpp_"+confighead.getQueuename()+"_mtc_"+sequencekey.trim();
					
					//存入 redis-mt 缓存
					IncmppSubmit incmppSubmit=new IncmppSubmit();
					
					incmppSubmit.setNum(1);
					incmppSubmit.setCpmid(bean.getCpmid());
					incmppSubmit.setMid(bean.getMid());
					incmppSubmit.setMobile(cmppSubmit.getDestTermIdstr());
					incmppSubmit.setPlatform(bean.getPlatform());
					incmppSubmit.setPort(bean.getPort());
					incmppSubmit.setP_num(1);
					
					redis.setex(mtkey, JSONObject.fromObject(incmppSubmit).toString(),2*60);
					
					this.session.write(sendbuffer);
					
					logger.info("[SP->GW][MsgSendThread][submit-success]["+bean.getPlatform()+":"+queue_flag+"][session:"+session.getId()+"][flow:"+flownum+"][Sequence:"+cmppHeader.getSequence_Id()+"][cpmId:"+bean.getCpmid()+"][mid:"+bean.getMid()+"][port:"+bean.getPort().trim()+"][msgId:"+FormatUtil.byteArray2HexString(cmppSubmit.getMsgId())+",pkTotal:"+cmppSubmit.getPkTotal()+",pkNumber:"+
							cmppSubmit.getPkNumber()+",needReport:"+cmppSubmit.getNeedReport()+",priority:"+cmppSubmit.getPriority()+",serviceId:"+cmppSubmit.getServiceId().trim()+",feeUserType:"+
							cmppSubmit.getFeeUserType()+",feeTermId:"+cmppSubmit.getFeeTermId().trim()+",tpPid:"+cmppSubmit.getTpPid()+",tpUdhi:"+cmppSubmit.getTpUdhi()+",msgFmt:"+cmppSubmit.getMsgFmt()+
							",msgSrc:"+cmppSubmit.getMsgSrc().trim()+",feeType:"+cmppSubmit.getFeeType()+",feeCode:"+cmppSubmit.getFeeCode().trim()+",validTime:"+cmppSubmit.getValidTime().trim()+
							",atTime:"+cmppSubmit.getAtTime().trim()+",srcId:"+cmppSubmit.getSrcId().trim()+",destTermIdCount:"+cmppSubmit.getDestTermIdCount()+",destTermId:"+cmppSubmit.getDestTermIdstr().trim()+
							",msgLen:"+FormatUtil.byteToInt(cmppSubmit.getMsgLen())+",msgContent:"+cmppSubmit.getMsgContentstr()+",Reserve:][rediskey:"+mtkey+"]");
					
				}else{
					String mcontent="";
					
					//byte pbyte = (byte)(System.currentTimeMillis()%255);
					byte pbyte = (byte)(FormatUtil.getMsymbol());
					
					for(int i=0;i<smsnum;i++){
						cmppHeader=new CmppHeader();
						cmppSubmit=new CmppSubmit();
						
						if(smsnum != (i+1)){
							mcontent=content.substring(i*67, i*67+67);
						}else{
							mcontent=content.substring((smsnum-1)*67);
						}
						
						byte[] sendbuffer=SendSubmitApi.cmpp_send_submitsms(cmppHeader, cmppSubmit, bean, confighead, mobilenumarr[pp], mcontent, smsnum, i+1, pbyte);
						
						//获取
						String sequencekey=Integer.toString(cmppHeader.getSequence_Id());
						
						String mtkey = "client_cmpp_"+confighead.getQueuename()+"_mtc_"+sequencekey.trim();
						
						//存入 redis-mt 缓存
						IncmppSubmit incmppSubmit=new IncmppSubmit();
						
						incmppSubmit.setNum(1);
						incmppSubmit.setCpmid(bean.getCpmid());
						incmppSubmit.setMid(bean.getMid());
						incmppSubmit.setMobile(cmppSubmit.getDestTermIdstr());
						incmppSubmit.setPlatform(bean.getPlatform());
						incmppSubmit.setPort(bean.getPort());
						incmppSubmit.setP_num(i+1);
						
						redis.setex(mtkey, JSONObject.fromObject(incmppSubmit).toString(),2*60);
						
						this.session.write(sendbuffer);
						
						logger.info("[SP->GW][MsgSendThread][submit-success]["+bean.getPlatform()+":"+queue_flag+"][session:"+session.getId()+"][flow:"+flownum+"][Sequence:"+cmppHeader.getSequence_Id()+"][cpmId:"+bean.getCpmid()+"][mid:"+bean.getMid()+"][port:"+bean.getPort().trim()+"][msgId:"+FormatUtil.byteArray2HexString(cmppSubmit.getMsgId())+",pkTotal:"+cmppSubmit.getPkTotal()+",pkNumber:"+
								cmppSubmit.getPkNumber()+",needReport:"+cmppSubmit.getNeedReport()+",priority:"+cmppSubmit.getPriority()+",serviceId:"+cmppSubmit.getServiceId().trim()+",feeUserType:"+
								cmppSubmit.getFeeType()+",feeTermId:"+cmppSubmit.getFeeTermId().trim()+",tpPid:"+cmppSubmit.getTpPid()+",tpUdhi:"+cmppSubmit.getTpUdhi()+",msgFmt:"+cmppSubmit.getMsgFmt()+
								",msgSrc:"+cmppSubmit.getMsgSrc().trim()+",feeType:"+cmppSubmit.getFeeType()+",feeCode:"+cmppSubmit.getFeeCode().trim()+",validTime:"+cmppSubmit.getValidTime().trim()+
								",atTime:"+cmppSubmit.getAtTime().trim()+",srcId:"+cmppSubmit.getSrcId().trim()+",destTermIdCount:"+cmppSubmit.getDestTermIdCount()+",destTermId:"+cmppSubmit.getDestTermIdstr().trim()+
								",msgLen:"+FormatUtil.byteToInt(cmppSubmit.getMsgLen())+",msgContent:("+cmppSubmit.getUdhi01().trim()+"|"+cmppSubmit.getUdhi02().trim()+"|"+cmppSubmit.getUdhi03().trim()+
								"|"+cmppSubmit.getUdhi04().trim()+"|"+cmppSubmit.getUdhi05().trim()+"|"+cmppSubmit.getUdhi06().trim()+")"+cmppSubmit.getMsgContentstr()+",Reserve:][rediskey:"+mtkey+"]");
						
					}
				}
			}
		} catch (Exception e) {
			logger.info("[SendSmsThread][Exception]["+JSONObject.fromObject(bean)+"]:"+e.getMessage());
			e.printStackTrace();
		}
		
	}
}
