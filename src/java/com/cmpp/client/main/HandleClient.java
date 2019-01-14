package com.cmpp.client.main;

import com.cmpp.client.active.ActiveThread;
import com.cmpp.client.api.ConnectApi;
import com.cmpp.client.api.QueryApi;
import com.cmpp.client.api.SendApi;
import com.cmpp.client.common.Confighead;
import com.cmpp.client.common.IncmppDeliver;
import com.cmpp.client.common.IncmppSubmit;
import com.cmpp.client.deliversendpt.DeliverSendThread;
import com.cmpp.client.msgsend.MsgSendThread;
import com.cmpp.httpsqs.HttpSmsClient;
import com.cmpp.redis.RedisUtil;
import com.cmpp.smshead.*;
import com.cmpp.util.CmppConstant;
import com.cmpp.util.FormatUtil;
import com.cmpp.util.TimeUtil;
import com.google.gson.Gson;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import java.io.IOException;

public class HandleClient extends IoHandlerAdapter{
	private static Logger logger = Logger.getLogger(HandleClient.class);
	private RedisUtil redis = RedisUtil.getInstance();
	private static HttpSmsClient httpSmsClient = HttpSmsClient.getInstance();
	
	public boolean Connect = false;
	public boolean Firstmsg = true;
	
	private Confighead confighead=new Confighead();
	
	private Cache monitorCache;
	
	public HandleClient(Confighead confighead,Cache monitorCache){
		
		this.confighead=confighead;
		this.monitorCache=monitorCache;
	}
	
	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception{
		if(!(cause instanceof IOException)){
			logger.error("ExceptionCaught-客户端异常-Exception: ", cause);
		}else{
			logger.info("I/O error: " + cause.getMessage());
		}
		session.close(true);
	}
	@Override
	public void sessionOpened(IoSession session) throws Exception{ 
		logger.info("客户端会话打开:session="+session.getId());  
        //连接
		Connect(session);
	}
	
	//连接
	public void Connect(IoSession session){
		byte[] connectbytes=new byte[39];
		
		connectbytes=ConnectApi.cmpp_send_connect(confighead);
		session.write(connectbytes);
		
		logger.info("[SP->GW][connect][session="+session.getId()+"]");
	}
	@Override
	public void sessionCreated(IoSession session) throws Exception{
		logger.info("客户端会话创建");  
	     super.sessionCreated(session);  
	}
	@Override
	public void sessionClosed(IoSession session) throws Exception{
		logger.info("客户端会话关闭");  
        super.sessionClosed(session);
	}
	@Override
	public void sessionIdle(IoSession iosession, IdleStatus idlestatus)  
            throws Exception {
		logger.info("客户端会话休眠");
        super.sessionIdle(iosession,idlestatus);  
    }
	
	@SuppressWarnings("deprecation")
	public void messageReceived(IoSession session, Object message) throws Exception{
		super.messageReceived(session, message);
		
		byte[] req = (byte[]) message;
		CmppHeader cmppHeader=new CmppHeader();
		
		SendApi.cmpp_recv_head(cmppHeader, req);
		
		if (Firstmsg == true || Connect == true){
			Firstmsg=false;
			
			switch(cmppHeader.getCommand_Id()){
				case CmppConstant.CMD_CONNECT_RESP:
					//
					int resnum=SendApi.cmpp_send_connectResp(cmppHeader, req);
					if(resnum == 0){
						Connect=true;
						logger.info("[GW->SP][connectResp][result:0,(认证成功!),session:"+session.getId()+"]");
						
						//心跳测试
						Thread t = new Thread(new ActiveThread(session));
						t.setDaemon(true);
						t.start();
						session.resumeRead();
						
						//获取内容发送
						Thread sendThread=new Thread(new MsgSendThread(session,confighead,Connect,monitorCache));
						sendThread.setDaemon(true);
						sendThread.start();
						
						int islocal=Integer.parseInt(confighead.getIslocal());
						if(islocal!=1){
							//启动 上行 与 回执 的检索队列
							Thread deliversendThread=new Thread(new DeliverSendThread(session,Connect,confighead));
							deliversendThread.setDaemon(true);
							deliversendThread.start();
						}
						
					}else{
						String resmessage="";
						if(resnum == 1){
							resmessage="消息长度 或 结构 错!";
						}else if(resnum == 2){
							resmessage="非法 源地址(企业代码错)!";
						}else if(resnum == 3){
							resmessage="认证错!";
						}else if(resnum == 4){
							resmessage="版本太高!";
						}else{
							resmessage="其他错误!";
						}
						
						logger.info("[GW->SP][connectResp][result:"+resnum+",("+resmessage+"),session:"+session.getId()+"]");
						
						//发送-异常 断开链接信息
						byte[] bufTermnate=new byte[12];
						bufTermnate=SendApi.cmpp_send_terminate();
						
						session.write(bufTermnate);
						
						Thread.sleep(1000L);
					}
					
					break;
					
				case CmppConstant.CMD_ACTIVE_TEST:
					
					byte[] buffer=SendApi.cmpp_send_activeResp(cmppHeader);
					session.write(buffer);
					
					break;
					
				case CmppConstant.CMD_ACTIVE_TEST_RESP:
					SendApi.cmpp_recv_activeResp(cmppHeader, req);
					
					ActiveThread.lastActiveTime = System.currentTimeMillis();
					
					break;
					
				case CmppConstant.CMD_SUBMIT_RESP:
					
					CmppSubmitResp cmppSubmitResp=new CmppSubmitResp();
					
					byte resultbyte=SendApi.cmpp_send_submitResp(cmppHeader, cmppSubmitResp, req);

					//根据sequence 获取 reids-mt的key值
					String sequencekey=Integer.toString(cmppHeader.getSequence_Id());
					
					String mtkey = "client_cmpp_"+confighead.getQueuename()+"_mtc_"+sequencekey.trim();
					String mtElement = redis.get(mtkey);
					
					IncmppSubmit incmppSubmit=new IncmppSubmit();
					
					if(mtElement==null || "".equals(mtElement)){
						logger.info("[submitResp][根据sequence 获取 mtReids的key值][缓存块中没有找到 key="+mtkey+"]");
						break;
					}else{
						incmppSubmit = new Gson().fromJson(mtElement, IncmppSubmit.class);
						int num=incmppSubmit.getNum();
						
						if(num>1){
							num=num-1;
							incmppSubmit.setNum(num);
							
							//重新存入redis-mt块
							redis.setex(mtkey, JSONObject.fromObject(incmppSubmit).toString(),2*60);
						}else{
							redis.del(mtkey);
						}
						
						//根据msgid存入 redis-report缓存块
						String deliverkey=FormatUtil.byteArray2HexString(cmppSubmitResp.getMsgId());
						
						String dckey = "client_cmpp_"+confighead.getQueuename()+ "_rc_" + deliverkey.trim();
						
						redis.setex(dckey, JSONObject.fromObject(incmppSubmit).toString(), 3*24*60*60);
					}
					
					int num=0;
					if(Integer.parseInt(confighead.getFlow())<100){
						//响应滑动窗口监控
						String flowkey="session-"+session.getId();
						
						Element flowElement=null;
						synchronized(this.monitorCache){
							flowElement = this.monitorCache.get(flowkey);
			        	}
						
						if(flowElement!=null){
							int flow=Integer.parseInt(flowElement.getValue().toString());
							if(flow>0){
								num=flow-1;
							}
						}
						
						synchronized(this.monitorCache){
							Element flowElementput=new Element(flowkey,num);
							this.monitorCache.put(flowElementput);
						}
					}
					
					if((int)resultbyte == 0){
						logger.info("[GW->SP][submitResp][mobile:"+incmppSubmit.getMobile()+"][session:"+session.getId()+"][flow:"+num+"][sequence:"+cmppHeader.getSequence_Id()+",msgid:"+FormatUtil.byteArray2HexString(cmppSubmitResp.getMsgId())+",result:"+cmppSubmitResp.getResult()+" -成功]");
					}else{
						if((int)resultbyte == 1){ //-消息结构错
							logger.info("[GW->SP][submitResp][mobile:"+incmppSubmit.getMobile()+"][session:"+session.getId()+"][flow:"+num+"][sequence:"+cmppHeader.getSequence_Id()+",msgid:"+FormatUtil.byteArray2HexString(cmppSubmitResp.getMsgId())+",result:"+cmppSubmitResp.getResult()+" -error-submitResp-消息结构错]");
						}else if((int)resultbyte == 2){ //-命令字错
							logger.info("[GW->SP][submitResp][mobile:"+incmppSubmit.getMobile()+"][session:"+session.getId()+"][flow:"+num+"][sequence:"+cmppHeader.getSequence_Id()+",msgid:"+FormatUtil.byteArray2HexString(cmppSubmitResp.getMsgId())+",result:"+cmppSubmitResp.getResult()+" -error-submitResp-命令字错]");
						}else if((int)resultbyte == 3){ //-消息序号重复
							logger.info("[GW->SP][submitResp][mobile:"+incmppSubmit.getMobile()+"][session:"+session.getId()+"][flow:"+num+"][sequence:"+cmppHeader.getSequence_Id()+",msgid:"+FormatUtil.byteArray2HexString(cmppSubmitResp.getMsgId())+",result:"+cmppSubmitResp.getResult()+" -error-submitResp-消息序号重复]");
						}else if((int)resultbyte == 4){ //-消息长度错
							logger.info("[GW->SP][submitResp][mobile:"+incmppSubmit.getMobile()+"][session:"+session.getId()+"][flow:"+num+"][sequence:"+cmppHeader.getSequence_Id()+",msgid:"+FormatUtil.byteArray2HexString(cmppSubmitResp.getMsgId())+",result:"+cmppSubmitResp.getResult()+" -error-submitResp-消息长度错]");
						}else if((int)resultbyte == 5){ //-资费代码错
							logger.info("[GW->SP][submitResp][mobile:"+incmppSubmit.getMobile()+"][session:"+session.getId()+"][flow:"+num+"][sequence:"+cmppHeader.getSequence_Id()+",msgid:"+FormatUtil.byteArray2HexString(cmppSubmitResp.getMsgId())+",result:"+cmppSubmitResp.getResult()+" -error-submitResp-资费代码错]");
						}else if((int)resultbyte == 6){ //-超过最大信息长
							logger.info("[GW->SP][submitResp][mobile:"+incmppSubmit.getMobile()+"][session:"+session.getId()+"][flow:"+num+"][sequence:"+cmppHeader.getSequence_Id()+",msgid:"+FormatUtil.byteArray2HexString(cmppSubmitResp.getMsgId())+",result:"+cmppSubmitResp.getResult()+" -error-submitResp-超过最大信息长]");
						}else if((int)resultbyte == 7){ //-业务代码错
							logger.info("[GW->SP][submitResp][mobile:"+incmppSubmit.getMobile()+"][session:"+session.getId()+"][flow:"+num+"][sequence:"+cmppHeader.getSequence_Id()+",msgid:"+FormatUtil.byteArray2HexString(cmppSubmitResp.getMsgId())+",result:"+cmppSubmitResp.getResult()+" -error-submitResp-业务代码错]");
						}else if((int)resultbyte == 8){ //-流量控制错
							logger.info("[GW->SP][submitResp][mobile:"+incmppSubmit.getMobile()+"][session:"+session.getId()+"][flow:"+num+"][sequence:"+cmppHeader.getSequence_Id()+",msgid:"+FormatUtil.byteArray2HexString(cmppSubmitResp.getMsgId())+",result:"+cmppSubmitResp.getResult()+" -error-submitResp-流量控制错]");
						}else{
							logger.info("[GW->SP][submitResp][mobile:"+incmppSubmit.getMobile()+"][session:"+session.getId()+"][flow:"+num+"][sequence:"+cmppHeader.getSequence_Id()+",msgid:"+FormatUtil.byteArray2HexString(cmppSubmitResp.getMsgId())+",result:"+cmppSubmitResp.getResult()+" -error-submitResp-其它错误]");
						}
						
						IncmppDeliver incmppDeliverMm=new IncmppDeliver();
						
						//插入状态队列
						String stat="";
						if((int)resultbyte==8){
							stat="ZT:008";
						}else{
							stat="ZT:"+resultbyte;
						}
						
						
						String mtime=TimeUtil.mNowTime();
						
						incmppDeliverMm.setDelivery((byte)1);
						incmppDeliverMm.setMsgid(FormatUtil.byteArray2HexString(cmppSubmitResp.getMsgId()));
						incmppDeliverMm.setStat(stat);
						incmppDeliverMm.setSubmittime(mtime);
						incmppDeliverMm.setDonetime(mtime);
						incmppDeliverMm.setDestterminalid(incmppSubmit.getMobile());
						incmppDeliverMm.setSmscsequence(cmppHeader.getSequence_Id());
						
						//插入发送-通道队列
						String deliverkeyMm=confighead.getQueuename().trim()+"#deliver";
						String  resT=httpSmsClient.putT(deliverkeyMm, new Gson().toJson(incmppDeliverMm), confighead.getQueue().trim(), confighead.getQueueport().trim());
				        if (!resT.contains("HTTPSQS_PUT_OK")){
				        	logger.info("[Deliver][Post-queue-error]["+deliverkeyMm+"][delivery:"+incmppDeliverMm.getDelivery()+"][msgid:"+incmppDeliverMm.getMsgid()+"][stat:"+incmppDeliverMm.getStat()+"][submittime:"+incmppDeliverMm.getSubmittime()+"][donetime:"+incmppDeliverMm.getDonetime()+
				        			"][destterminalid:"+incmppDeliverMm.getDestterminalid()+"][smscsequence:"+incmppDeliverMm.getSmscsequence()+"][content:"+incmppDeliverMm.getContent()+"]");
				        }
					}
					
					break;
					
				case CmppConstant.CMD_DELIVER:
					// 状态报告 - 上行
					CmppDeilver cmppDeilver=new CmppDeilver();
					CmppDeliverstate cmppDeliverstate=new CmppDeliverstate();
					CmppDeilverResp cmppDeilverResp=new CmppDeilverResp();
					IncmppDeliver incmppDeliver=new IncmppDeliver();
					
					int resdeliver=SendApi.cmpp_send_deliver(cmppHeader, cmppDeilver, cmppDeliverstate, req,confighead,incmppDeliver);
					
					byte[] bufferdeliver=SendApi.cmpp_send_deliverResp(cmppHeader, cmppDeilver, cmppDeilverResp,resdeliver);
					
					session.write(bufferdeliver);
					
//					logger.info("[SP->GW][deliverResp][msgid:"+FormatUtil.byteArray2HexString(cmppDeilverResp.getMsgId())+",result:"+(int)cmppDeilverResp.getResult()+"]");
					
					int islocal=Integer.parseInt(confighead.getIslocal());
					if(islocal!=1){
						//插入发送-通道队列
						String deliverkey=confighead.getQueuename().trim()+"#deliver";
						String  resT=httpSmsClient.putT(deliverkey, new Gson().toJson(incmppDeliver), confighead.getQueue().trim(), confighead.getQueueport().trim());
				        if (!resT.contains("HTTPSQS_PUT_OK")){
				        	logger.info("[Deliver][Post-queue-error]["+deliverkey+"][delivery:"+incmppDeliver.getDelivery()+"][msgid:"+incmppDeliver.getMsgid()+"][stat:"+incmppDeliver.getStat()+"][submittime:"+incmppDeliver.getSubmittime()+"][donetime:"+incmppDeliver.getDonetime()+
				        			"][destterminalid:"+incmppDeliver.getDestterminalid()+"][smscsequence:"+incmppDeliver.getSmscsequence()+"][content:"+incmppDeliver.getContent()+"]");
				        }
					}
					
					break;
					
				case CmppConstant.CMD_TERMINATE: //GW->SP 终止链接
					logger.info("[GW->SP][Termnate][Total:"+cmppHeader.getTotal_Length()+",command:"+Integer.toHexString(cmppHeader.getCommand_Id())+",sequence:"+cmppHeader.getSequence_Id()+"]");
					
					byte[] termnatebytes=new byte[12];
					termnatebytes=SendApi.cmpp_send_terminateResp(cmppHeader);
					session.write(termnatebytes);
					
					Thread.sleep(1000L);
					
					session.close(true);
					
					break;
					
				case CmppConstant.CMD_TERMINATE_RESP: //GW->SP 终止链接 应答
					logger.info("[SP->GW][TermnateResp][Total:"+cmppHeader.getTotal_Length()+",command:"+Integer.toHexString(cmppHeader.getCommand_Id())+",sequence:"+cmppHeader.getSequence_Id()+"]");
					
					Thread.sleep(1000L);
					
					session.close();
					
					break;
				
				case CmppConstant.CMD_QUERY_RESP: //GW->SP 查询应答 
					
					CmppQueryResp cmppQueryResp=new CmppQueryResp();
					
					int res=QueryApi.cmpp_send_queryResp(cmppHeader, cmppQueryResp, req);
					if(res==0){
						logger.info("[GW->SP][queryResp][时间:"+cmppQueryResp.getQtime().trim()+",查询类别:总数查询,从SP接收信息总数:"+cmppQueryResp.getMtTlmsg()+",从SP接收用户总数:"+cmppQueryResp.getMtTluser()+
								",成功转发数量:"+cmppQueryResp.getMtScs()+",待转发数量:"+cmppQueryResp.getMtWt()+",转发失败数量:"+cmppQueryResp.getMtFl()+",向SP成功送达数量:"+cmppQueryResp.getMoScs()+
								",向SP待送达数量:"+cmppQueryResp.getMoWt()+",向SP送达失败数量:"+cmppQueryResp.getMoFl()+"]");
					}
					
					break;
					
				default:
					//..
					break;
			}
		}
	}
}