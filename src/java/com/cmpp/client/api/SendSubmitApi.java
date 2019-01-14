package com.cmpp.client.api;

import com.cmpp.client.common.Confighead;
import com.cmpp.client.common.InSubmit;
import com.cmpp.smshead.CmppHeader;
import com.cmpp.smshead.CmppSubmit;
import com.cmpp.util.CmppConstant;
import com.cmpp.util.FormatUtil;

import java.io.UnsupportedEncodingException;

public class SendSubmitApi {
	
	/**
	 * 功能：短信组织
	 * @param inSubmit
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static byte[] cmpp_send_submitsms(CmppHeader cmppHeader,CmppSubmit cmppSubmit,InSubmit inSubmit,Confighead confighead,String mobiles,String content,int totlenum,int m_num,byte pbyte) throws UnsupportedEncodingException{
		byte[] buffer=new byte[2048+1];
		byte[] bytes=new byte[4];
		
		int pos=0;
		
		//组织报文结构
		//Total_Length
		//Command_Id
		cmppHeader.setCommand_Id(CmppConstant.CMD_SUBMIT);
		//Sequence_Id
		cmppHeader.setSequence_Id(FormatUtil.getSequence());
		
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
		//msgId
		pos+=8;
		
		//pkTotal
		buffer[pos]=(byte)totlenum;
		pos+=1;
		cmppSubmit.setPkTotal((byte)totlenum);
		
		//pkNumber
		buffer[pos]=(byte)m_num;
		pos+=1;
		cmppSubmit.setPkNumber((byte)m_num);
		
		//needReport
		buffer[pos]=(byte)Integer.parseInt(confighead.getIsreport());
		pos+=1;
		cmppSubmit.setNeedReport((byte)Integer.parseInt(confighead.getIsreport()));
		
		//priority
		buffer[pos]=(byte)0;
		pos+=1;
		cmppSubmit.setPriority((byte)0);
		
		//serviceId
		String serviceId=confighead.getServiceid().trim();
		System.arraycopy(serviceId.getBytes(), 0, buffer, pos, serviceId.getBytes().length);
		pos+=10;
		cmppSubmit.setServiceId(serviceId.trim());
		
		//feeUserType
		buffer[pos]=(byte)2;
		pos+=1;
		cmppSubmit.setFeeUserType((byte)2);
		
		//feeTermId
		String feeTermId="0";
		System.arraycopy(feeTermId.getBytes(), 0, buffer, pos, feeTermId.getBytes().length);
		pos+=21;
		cmppSubmit.setFeeTermId(feeTermId);
		
		//tpPid
		buffer[pos]=(byte)0;
		pos+=1;
		cmppSubmit.setTpPid((byte)0);
		
		//tpUdhi
		if(totlenum>1){
			buffer[pos]=(byte)1;
			cmppSubmit.setTpUdhi((byte)1);
		}else{
			buffer[pos]=(byte)0;
			cmppSubmit.setTpUdhi((byte)0);
		}
		
		pos+=1;
		
		//msgFmt
		String ucs=confighead.getUcs().trim();
		buffer[pos]=(byte)Integer.parseInt(ucs);
		pos+=1;
		cmppSubmit.setMsgFmt((byte)Integer.parseInt(ucs));
		
		//msgSrc
		String msgSrc=confighead.getCorpid().trim();
		System.arraycopy(msgSrc.getBytes(), 0, buffer, pos, msgSrc.getBytes().length);
		pos+=6;
		cmppSubmit.setMsgSrc(msgSrc.trim());
		
		//feeType
		String feeType="01";
		System.arraycopy(feeType.getBytes(), 0, buffer, pos, feeType.getBytes().length);
		pos+=2;
		cmppSubmit.setFeeType(feeType);
		
		//feeCode
		String feeCode="";
		pos+=6;
		cmppSubmit.setFeeCode(feeCode);
		
		//validTime
		String validTime="";
		pos+=17;
		cmppSubmit.setValidTime(validTime);
		
		//atTime
		String atTime="";
		pos+=17;
		cmppSubmit.setAtTime(atTime);
		
		//srcId
		String srcId=confighead.getSpport().trim()+inSubmit.getPort();
		int srcIdLen=srcId.length();
		if(srcIdLen>21){
			srcIdLen=21;
		}
		System.arraycopy(srcId.getBytes(), 0, buffer, pos, srcIdLen);
		cmppSubmit.setSrcId(srcId.trim());
		pos+=21;
		
		//destTermIdCount
		//int destTermIdCount=Integer.parseInt(confighead.getMobilenum());
		int destTermIdCount=1;
		buffer[pos]=(byte)destTermIdCount;
		pos+=1;
		cmppSubmit.setDestTermIdCount((byte)destTermIdCount);
		
		//destTermId
		String[] tTmMobilestr = mobiles.split(",");
		for(int u=0;u<destTermIdCount;u++){
			System.arraycopy(tTmMobilestr[u].getBytes(), 0, buffer, pos, tTmMobilestr[u].getBytes().length);
			pos+=21;
		}
		cmppSubmit.setDestTermIdstr(mobiles);
		
		//msgLen
		int msgLen=0;
		if(totlenum==1){ //短短信
			msgLen = 2*content.length();
		}else{
			msgLen = 6+2*content.length();
		}
		cmppSubmit.setMsgContentstr(content);
		
		buffer[pos]=FormatUtil.intToByte(msgLen);
		pos+=1;
		cmppSubmit.setMsgLen(FormatUtil.intToByte(msgLen));
		
		//msgContent
		byte[] msgContentbyte=new byte[msgLen];
		byte[] msgContentbytemm=new byte[msgLen];
		if(totlenum==1){ //短短信
			if("8".equals(ucs)){
				msgContentbyte=content.getBytes("UTF-16BE");
			}else if("15".equals(ucs)){
				msgContentbyte=content.getBytes("GBK");
			}else{
				msgContentbyte=content.getBytes("UTF-16BE");
			}
			
			System.arraycopy(msgContentbyte, 0, buffer, pos, msgContentbyte.length);
			pos+=msgLen;
			cmppSubmit.setMsgContent(msgContentbyte);
			
		}else{ //长短信
			byte[] contentHead = new byte[6];
			
			contentHead[0] = 5;
          	contentHead[1] = 0;
          	contentHead[2] = 3;
          	contentHead[3] = pbyte;
          	contentHead[4] = (byte)totlenum;
          	contentHead[5] = (byte)m_num;
			
          	System.arraycopy(contentHead, 0, buffer, pos, 6);
          	System.arraycopy(contentHead, 0, msgContentbytemm, 0, 6);
          	pos+=6;
          	
          	cmppSubmit.setUdhi01(Integer.toHexString(FormatUtil.byteToInt(contentHead[0])));
          	cmppSubmit.setUdhi02(Integer.toHexString(FormatUtil.byteToInt(contentHead[1])));
          	cmppSubmit.setUdhi03(Integer.toHexString(FormatUtil.byteToInt(contentHead[2])));
          	cmppSubmit.setUdhi04(Integer.toHexString(FormatUtil.byteToInt(contentHead[3])));
          	cmppSubmit.setUdhi05(Integer.toHexString(FormatUtil.byteToInt(contentHead[4])));
          	cmppSubmit.setUdhi06(Integer.toHexString(FormatUtil.byteToInt(contentHead[5])));	
          	
          	if("8".equals(ucs)){
				msgContentbyte=content.getBytes("UTF-16BE");
			}else if("15".equals(ucs)){
				msgContentbyte=content.getBytes("GBK");
			}else{
				msgContentbyte=content.getBytes("UTF-16BE");
			}
          	
          	System.arraycopy(msgContentbyte, 0, buffer, pos, msgContentbyte.length);
          	System.arraycopy(msgContentbyte, 0, msgContentbytemm, 6, msgContentbyte.length);
          	
			pos+=(msgLen-6);	
			
			cmppSubmit.setMsgContent(msgContentbytemm);
		}
		
		//Reserve
		pos+=8;
		
		//Total_Length
		cmppHeader.setTotal_Length(pos);
		
		bytes=new byte[4];
		bytes=FormatUtil.intToBytes4(cmppHeader.getTotal_Length());
		System.arraycopy(bytes, 0, buffer, 0, 4);
		
		byte[] buffersend=new byte[pos];
		System.arraycopy(buffer, 0, buffersend, 0, pos);
		
		return buffersend;
	}
	
}
