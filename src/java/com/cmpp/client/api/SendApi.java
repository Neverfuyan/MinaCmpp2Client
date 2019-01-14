package com.cmpp.client.api;

import com.cmpp.client.common.Confighead;
import com.cmpp.client.common.IncmppDeliver;
import com.cmpp.smshead.*;
import com.cmpp.util.CmppConstant;
import com.cmpp.util.FormatUtil;
import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * SendApi 短信发送-接收 API
 * @author Zt
 * @date 2016-12-26
 * @address 上海
 */
public class SendApi{
	private static Logger logger = Logger.getLogger(SendApi.class);
	
	/**
	 * 心跳测试-发送
	 * @return
	 */
	public static byte[] cmpp_send_active(){
		CmppHeader cmppHeader=new CmppHeader();
		
		byte[] buffer=new byte[12];
		byte[] bytes=new byte[4];
		
		int pos=0;
		
		//组织报文头
		cmppHeader.setTotal_Length(12);
		cmppHeader.setCommand_Id(CmppConstant.CMD_ACTIVE_TEST);
		cmppHeader.setSequence_Id(FormatUtil.getSequence());
		
		bytes=FormatUtil.intToBytes4(cmppHeader.getTotal_Length());
		System.arraycopy(bytes, 0, buffer, pos, 4);
		pos+=4;
		
		bytes=new byte[4];
		bytes=FormatUtil.intToBytes4(cmppHeader.getCommand_Id());
		System.arraycopy(bytes, 0, buffer, pos, 4);
		pos+=4;
		
		bytes=new byte[4];
		bytes=FormatUtil.intToBytes4(cmppHeader.getSequence_Id());
		System.arraycopy(bytes, 0, buffer, pos, 4);
		pos+=4;
		
		return buffer;
	}
	
	/**
	 * 功能：cmpp_recv_head 报文头解析
	 * @param cmppHeader
	 * @param buffer
	 */
	public static void cmpp_recv_head(CmppHeader cmppHeader,byte[] buffer){
		byte[] bytes=new byte[4];
		
		if(buffer.length<12){
			return;
		}
		
		System.arraycopy(buffer, 0, bytes, 0, 4);
		int len=FormatUtil.bytes4ToInt(bytes);
		cmppHeader.setTotal_Length(len);
		
		bytes=new byte[4];
		System.arraycopy(buffer, 4, bytes, 0, 4);
		int command=FormatUtil.bytes4ToInt(bytes);
		cmppHeader.setCommand_Id(command);
		
		bytes=new byte[4];
		System.arraycopy(buffer, 8, bytes, 0, 4);
		int sequence=FormatUtil.bytes4ToInt(bytes);
		cmppHeader.setSequence_Id(sequence);
		
		return;
	}
	
	/**
	 * 功能：cmpp_send_connectResp
	 * @param cmppHeader
	 * @param buffer
	 * @return
	 */
	public static int cmpp_send_connectResp(CmppHeader cmppHeader,byte[] buffer){
		CmppConnectResp cmppConnectResp=new CmppConnectResp();
		
		int len=cmppHeader.getTotal_Length();
		int pos=0;
		
		if(buffer.length != len){
			logger.info("[GW->SP][connectResp][len:"+len+",buffer.length="+buffer.length+"][error:消息长度错]");
			return 1; //消息长度 和 结构 错
		}
		
		pos+=12;
		
		//解析报文结构
		//Status
		byte statusbyte=buffer[pos];
		pos+=1;
		cmppConnectResp.setStatus(statusbyte);
		
		//ISMG
		byte[] ismgbyte=new byte[16];
		System.arraycopy(buffer, pos, ismgbyte, 0, 16);
		pos+=16;
		cmppConnectResp.setAuthenticatorISMG(ismgbyte);
		
		//version
		byte versionbyte=buffer[pos];
		pos+=1;
		cmppConnectResp.setVersion(versionbyte);
		
		logger.info("[GW->SP][connectResp][Status:"+(int)cmppConnectResp.getStatus()+",ISMG:"+FormatUtil.byteArray2HexString(cmppConnectResp.getAuthenticatorISMG())+",version:"+cmppConnectResp.getVersion()+"]");

		return (int)cmppConnectResp.getStatus();
	}
	
	/**
	 * 功能：terminate 断开链接 信息
	 * @return
	 */
	public static byte[] cmpp_send_terminate(){
		CmppHeader cmppHeader=new CmppHeader();
		
		byte[] buffer=new byte[12];
		byte[] bytes=new byte[4];
		
		int pos=0;
		
		//组织报文头
		cmppHeader.setTotal_Length(12);
		cmppHeader.setCommand_Id(CmppConstant.CMD_TERMINATE);
		cmppHeader.setSequence_Id(FormatUtil.getSequence());
		
		bytes=FormatUtil.intToBytes4(cmppHeader.getTotal_Length());
		System.arraycopy(bytes, 0, buffer, pos, 4);
		pos+=4;
		
		bytes=new byte[4];
		bytes=FormatUtil.intToBytes4(cmppHeader.getCommand_Id());
		System.arraycopy(bytes, 0, buffer, pos, 4);
		pos+=4;
		
		bytes=new byte[4];
		bytes=FormatUtil.intToBytes4(cmppHeader.getSequence_Id());
		System.arraycopy(bytes, 0, buffer, pos, 4);
		pos+=4;
		
		logger.info("[SP->GW][terminate][Len:"+cmppHeader.getTotal_Length()+",Command_Id:"+Integer.toHexString(cmppHeader.getCommand_Id())+",Sequence_Id:"+cmppHeader.getSequence_Id()+"]");
		
		return buffer;
	}
	
	/**
	 * 功能：心跳-回复
	 * @param cmppHeader
	 * @return
	 */
	public static byte[] cmpp_send_activeResp(CmppHeader cmppHeader){
		byte[] buffer=new byte[13];
		byte[] bytes=new byte[4];
		
		int pos=0;
		
		//组织报文头
		cmppHeader.setTotal_Length(13);
		cmppHeader.setCommand_Id(CmppConstant.CMD_ACTIVE_TEST_RESP);
		cmppHeader.setSequence_Id(cmppHeader.getSequence_Id());
		
		bytes=FormatUtil.intToBytes4(cmppHeader.getTotal_Length());
		System.arraycopy(bytes, 0, buffer, pos, 4);
		pos+=4;
		
		bytes=new byte[4];
		bytes=FormatUtil.intToBytes4(cmppHeader.getCommand_Id());
		System.arraycopy(bytes, 0, buffer, pos, 4);
		pos+=4;
		
		bytes=new byte[4];
		bytes=FormatUtil.intToBytes4(cmppHeader.getSequence_Id());
		System.arraycopy(bytes, 0, buffer, pos, 4);
		pos+=4;
		
		buffer[pos]=(byte)0;
		pos+=1;
		
		return buffer;
	}
	
	/**
	 * 心跳 测试 解析
	 * @param buffer
	 * @return
	 */
	public static byte cmpp_recv_activeResp(CmppHeader cmppHeader,byte[] buffer){
		byte bytes;
		
		if(cmppHeader.getTotal_Length()<=12){
			return (byte)0;
		}else{
			bytes=buffer[12];
		}
		
		return bytes;
	}
	
	/**
	 * 功能：Submit Resp
	 * @param cmppSubmitResp
	 * @param buffer
	 * @return
	 */
	public static byte cmpp_send_submitResp(CmppHeader cmppHeader,CmppSubmitResp cmppSubmitResp,byte[] buffer){
		int len=cmppHeader.getTotal_Length();
		int pos=0;
		
		if(buffer.length != len){
			logger.info("[GW->SP][submitResp][len:"+len+",buffer.length="+buffer.length+"][error:消息长度错]");
			return 4; //消息长度错
		}
		pos+=12;
		
		//解析报文结构体
		//msgId
		byte[] bytes=new byte[8];
		System.arraycopy(buffer, pos, bytes, 0, 8);
		pos+=8;
		
		//Result
		byte byteresult=buffer[pos];
		pos+=1;
		cmppSubmitResp.setResult(byteresult);
		
		if((int)byteresult!=0){
			SimpleDateFormat sdf=new SimpleDateFormat("mmssSSS");
			String time=sdf.format(new Date());
			String msgid = time+ (int)(Math.random()*(9999-1000+1)+1000);
			bytes=msgid.getBytes();
		}
		
		cmppSubmitResp.setMsgId(bytes);
		
		return byteresult;
	}
	
	/**
	 * 功能：cmpp_send_deliver
	 * @param cmppHeader
	 * @param cmppDeilver
	 * @param buffer
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static int cmpp_send_deliver(CmppHeader cmppHeader,CmppDeilver cmppDeilver,CmppDeliverstate cmppDeliverstate,byte[] buffer,Confighead confighead,IncmppDeliver incmppDeliver) throws UnsupportedEncodingException{
		int len=cmppHeader.getTotal_Length();
		int pos=0;
		
		if(buffer.length != len){
			logger.info("[GW->SP][deliver][len:"+len+",buffer.length="+buffer.length+"][error:消息长度错]");
			return 4; //消息长度错
		}
		pos+=12;
		
		//解析报文结构体
		//msgid
		byte[] msgidbytes=new byte[8];
		System.arraycopy(buffer, pos, msgidbytes, 0, 8);
		pos+=8;
		cmppDeilver.setMsgid(msgidbytes);
		
		//destid --目的号码-sp服务代码
		byte[] destidbytes=new byte[21];
		System.arraycopy(buffer, pos, destidbytes, 0, 21);
		pos+=21;
		String destid=new String(destidbytes,0,destidbytes.length);
		cmppDeilver.setDestid(destid);
		
		//-serviceid -业务代码
		byte[] serviceidbytes=new byte[10];
		System.arraycopy(buffer, pos, serviceidbytes, 0, 10);
		pos+=10;
		String serviceid=new String(serviceidbytes,0,serviceidbytes.length);
		cmppDeilver.setServiceid(serviceid);
		/**
		if(!serviceid.equals(confighead.getServiceid().trim())){
			logger.info("[GW->SP][deliver][serviceid:"+serviceid+"][error:业务代码错]");
			
			return 7;  //业务代码错
		}**/
		
		//tppid
		byte tppid=buffer[pos];
		pos+=1;
		cmppDeilver.setTppid(tppid);
		
		//tpudhi
		byte tpudhi=buffer[pos];
		pos+=1;
		cmppDeilver.setTpudhi(tpudhi);
		
		//msgfmt
		byte msgfmt=buffer[pos];
		pos+=1;
		cmppDeilver.setMsgfmt(msgfmt);
		
		//terminalid 源终端MSISDN号码（状态报告时填为CMPP_SUBMIT消息的目的终端号码）
		byte[] terminalidbytes=new byte[21];
		System.arraycopy(buffer, pos, terminalidbytes, 0, 21);
		pos+=21;
		String terminalid=new String(terminalidbytes,0,terminalidbytes.length);
		if("86".equals(terminalid.substring(0, 2))){
			terminalid=terminalid.substring(2).trim();
		}
		cmppDeilver.setTerminalid(terminalid);
		
		//delivery 是否为状态报告 1-非状态报告 0-状态报告
		byte deliverybyte=buffer[pos];
		pos+=1;
		cmppDeilver.setDelivery(deliverybyte);
		
		//msglen
		byte msglenbyte=buffer[pos];
		pos+=1;
		cmppDeilver.setMsglen(msglenbyte);
		
		incmppDeliver.setDelivery(cmppDeilver.getDelivery());
		
		//msgcontent
		if((int)cmppDeilver.getDelivery() == 1){ //状态报告
			//解析状态报告
			//msgid
			byte[] statmsgidbytes=new byte[8];
			System.arraycopy(buffer, pos, statmsgidbytes, 0, 8);
			pos+=8;
			cmppDeliverstate.setMsgid(statmsgidbytes);
			incmppDeliver.setMsgid(FormatUtil.byteArray2HexString(statmsgidbytes));
			
			//stat
			byte[] statbytes=new byte[7];
			System.arraycopy(buffer, pos, statbytes, 0, 7);
			pos+=7;
			String stat=new String(statbytes,0,statbytes.length);
			cmppDeliverstate.setStat(stat);
			incmppDeliver.setStat(stat);
			
			//submittime
			byte[] submittimebyte=new byte[10];
			System.arraycopy(buffer, pos, submittimebyte, 0, 10);
			pos+=10;
			String submittime=new String(submittimebyte,0,submittimebyte.length);
			cmppDeliverstate.setSubmittime(submittime);
			incmppDeliver.setSubmittime(submittime);
			
			//donetime
			byte[] donetimebyte=new byte[10];
			System.arraycopy(buffer, pos, donetimebyte, 0, 10);
			pos+=10;
			String donetime=new String(donetimebyte,0,donetimebyte.length);
			cmppDeliverstate.setDonetime(donetime);
			incmppDeliver.setDonetime(donetime);
			
			//destterminalid
			byte[] destterminalidbytes=new byte[21];
			System.arraycopy(buffer, pos, destterminalidbytes, 0, 21);
			pos+=21;
			String destterminalid=new String(destterminalidbytes,0,destterminalidbytes.length);
			if("86".equals(destterminalid.substring(0, 2))){
				destterminalid=destterminalid.substring(2).trim();
			}
			cmppDeliverstate.setDestterminalid(destterminalid);
			incmppDeliver.setDestterminalid(destterminalid);
			
			//smscsequence
			byte[] smscsequencebytes=new byte[4];
			System.arraycopy(buffer, pos, smscsequencebytes, 0, 4);
			pos+=4;
			int smscsequence=FormatUtil.bytes4ToInt(smscsequencebytes);
			cmppDeliverstate.setSmscsequence(smscsequence);
			incmppDeliver.setSmscsequence(smscsequence);
			
			//日志打印
			logger.info("[GW->SP][report][msgid:"+FormatUtil.byteArray2HexString(cmppDeilver.getMsgid())+",destid:"+cmppDeilver.getDestid().trim()+
					",terminalid:"+cmppDeilver.getTerminalid().trim()+
					",delivery:"+(int)cmppDeilver.getDelivery()+",msglen:"+(int)cmppDeilver.getMsglen()+",msgcontent:(msgid:"+FormatUtil.byteArray2HexString(cmppDeliverstate.getMsgid())+
					",stat:"+cmppDeliverstate.getStat()+",submittime:"+cmppDeliverstate.getSubmittime()+",donetime:"+cmppDeliverstate.getDonetime()+",destterminalid:"+cmppDeliverstate.getDestterminalid()+
					",smscsequence:"+cmppDeliverstate.getSmscsequence()+")]");
			
		}else if((int)cmppDeilver.getDelivery() == 0){ //上行短信
			if(!destid.contains(confighead.getSpport().trim())){
				logger.info("[GW->SP][reply]["+cmppDeilver.getTerminalid()+"][destid:"+destid+"]["+confighead.getSpport()+"][error:上行sp号码错 -其它错误]");
				
				return 9; //sp号码错 -其它错误
			}
			
			//int msglen=(int)cmppDeilver.getMsglen();
			int msglen=FormatUtil.byteToInt(cmppDeilver.getMsglen());
			
			String content="";
			byte[] msgcontentbytes=new byte[msglen];
			
			System.arraycopy(buffer, pos, msgcontentbytes, 0, msglen);
			pos+=msglen;
			
			cmppDeilver.setMsgcontent(msgcontentbytes);
			
		    if((int)cmppDeilver.getTpudhi() > 0){ //上行长 短信
		    	byte[] msgcontentbytesH=new byte[6];
		    	byte[] msgcontentbytesT=new byte[msglen-6];
		    	
		    	System.arraycopy(msgcontentbytes, 0, msgcontentbytesH, 0, 6);
		    	System.arraycopy(msgcontentbytes, 6, msgcontentbytesT, 0, msglen-6);
		    	
		    	//String mHeadstr=msgcontentbytesH[0]+"|"+msgcontentbytesH[1]+"|"+msgcontentbytesH[2]+"|"+msgcontentbytesH[3]+"|"+
		    		//	msgcontentbytesH[4]+"|"+msgcontentbytesH[5];
		    	
		    	String mHeadstr=msgcontentbytesH[4]+"->"+msgcontentbytesH[5];
		    	
		    	if((int)cmppDeilver.getMsgfmt() == 8){
					content=new String(msgcontentbytesT,"UTF-16BE");
				}else if((int)cmppDeilver.getMsgfmt() == 15){
					content=new String(msgcontentbytesT,"GBK");
				}else{
					content=new String(msgcontentbytesT);
				}
		    	
		    	content = "["+mHeadstr+"]"+content;
		    	
		    }else{ //上行短 短信

				if((int)cmppDeilver.getMsgfmt() == 8){
					content=new String(msgcontentbytes,"UTF-16BE");
				}else if((int)cmppDeilver.getMsgfmt() == 15){
					content=new String(msgcontentbytes,"GBK");
				}else{
					content=new String(msgcontentbytes);
				}
		    }

			incmppDeliver.setContent(content); //上行短信
			
			incmppDeliver.setDestterminalid(cmppDeilver.getTerminalid()); //手机号码
			incmppDeliver.setStat(cmppDeilver.getDestid()); //sp号码
			incmppDeliver.setSmscsequence(cmppHeader.getSequence_Id()); //暂时作为上行的标识
			
			//日志打印
			logger.info("[GW->SP][reply][msgid:"+FormatUtil.byteArray2HexString(cmppDeilver.getMsgid())+",destid:"+cmppDeilver.getDestid()+",serviceid:"+cmppDeilver.getServiceid()+
					",tppid:"+(int)cmppDeilver.getTppid()+",tpudhi:"+(int)cmppDeilver.getTpudhi()+",msgfmt:"+(int)cmppDeilver.getMsgfmt()+",terminalid:"+cmppDeilver.getTerminalid()+
					",delivery:"+(int)cmppDeilver.getDelivery()+",msglen:"+msglen+",msgcontent:"+content+"]");
		}
		
		//reserved
		byte[] reservedbytes=new byte[8];
		System.arraycopy(buffer, pos, reservedbytes, 0, 8);
		pos+=8;

		return 0;
	}
	
	/**
	 * 功能: deliverResp
	 * @param cmppHeader
	 * @param cmppDeilver
	 * @return
	 */
	public static byte[] cmpp_send_deliverResp(CmppHeader cmppHeader,CmppDeilver cmppDeilver,CmppDeilverResp cmppDeilverResp,int resdeliver){
		byte[] buffer=new byte[21];
		byte[] bytes=new byte[4];
		
		int pos=0;
		//组织报文结构
		cmppHeader.setTotal_Length(21);
		cmppHeader.setCommand_Id(CmppConstant.CMD_DELIVER_RESP);
		cmppHeader.setSequence_Id(cmppHeader.getSequence_Id());
		
		bytes=FormatUtil.intToBytes4(cmppHeader.getTotal_Length());
		System.arraycopy(bytes, 0, buffer, pos, 4);
		pos+=4;
		
		bytes=new byte[4];
		bytes=FormatUtil.intToBytes4(cmppHeader.getCommand_Id());
		System.arraycopy(bytes, 0, buffer, pos, 4);
		pos+=4;
		
		bytes=new byte[4];
		bytes=FormatUtil.intToBytes4(cmppHeader.getSequence_Id());
		System.arraycopy(bytes, 0, buffer, pos, 4);
		pos+=4;
		
		//组织报文体
		//msgid
		cmppDeilverResp.setMsgId(cmppDeilver.getMsgid());
		System.arraycopy(cmppDeilverResp.getMsgId(), 0, buffer, pos, 8);
		pos+=8;
		
		//result
		cmppDeilverResp.setResult((byte)resdeliver);
		buffer[pos]=cmppDeilverResp.getResult();
		pos+=1;
		
		return buffer;
	}
	
	/**
	 * 功能: cmpp_send_terminateResp 终止链接
	 * @param cmppHeader
	 * @return
	 */
	public static byte[] cmpp_send_terminateResp(CmppHeader cmppHeader){
		byte[] buffer=new byte[12];
		byte[] bytes=new byte[4];
		
		int pos=0;
		
		//组织报文头
		cmppHeader.setTotal_Length(12);
		cmppHeader.setCommand_Id(CmppConstant.CMD_TERMINATE_RESP);
		cmppHeader.setSequence_Id(cmppHeader.getSequence_Id());
		
		bytes=FormatUtil.intToBytes4(cmppHeader.getTotal_Length());
		System.arraycopy(bytes, 0, buffer, pos, 4);
		pos+=4;
		
		bytes=new byte[4];
		bytes=FormatUtil.intToBytes4(cmppHeader.getCommand_Id());
		System.arraycopy(bytes, 0, buffer, pos, 4);
		pos+=4;
		
		bytes=new byte[4];
		bytes=FormatUtil.intToBytes4(cmppHeader.getSequence_Id());
		System.arraycopy(bytes, 0, buffer, pos, 4);
		pos+=4;
		
		logger.info("[SP->GW][terminateResp][Len:"+cmppHeader.getTotal_Length()+",Command_Id:"+Integer.toHexString(cmppHeader.getCommand_Id())+",Sequence_Id:"+cmppHeader.getSequence_Id()+"]");
		
		return buffer;
	}
	
}
