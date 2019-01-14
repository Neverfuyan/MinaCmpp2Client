package com.cmpp.client.api;

import org.apache.log4j.Logger;

import com.cmpp.smshead.CmppHeader;
import com.cmpp.smshead.CmppQuery;
import com.cmpp.smshead.CmppQueryResp;
import com.cmpp.util.CmppConstant;
import com.cmpp.util.FormatUtil;
import com.cmpp.util.TimeUtil;

/**
 * QueryApi 短信发送-情况 API
 * @author Zt
 * @date 2016-03-01
 * @address 上海
 */
public class QueryApi {
	
	private static Logger logger = Logger.getLogger(QueryApi.class);
	
	/**
	 * 功能: 短信发送-情况 API
	 * @author 蔡新鹏
	 * @date 2016-03-01
	 * @address 上海
	 */
	public static byte[] cmpp_send_query(CmppHeader cmppHeader,CmppQuery cmppQuery){

		byte[] buffer=new byte[39];
		byte[] bytes=new byte[4];
		
		int pos=0;
		
		//组织报文头
		cmppHeader.setTotal_Length(39);
		cmppHeader.setCommand_Id(CmppConstant.CMD_QUERY);
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
		
		//组织报文体
		//qtime
		String qtime=TimeUtil.mNowDate();
		System.arraycopy(qtime.getBytes(), 0, buffer, pos, qtime.length());
		pos+=8;
		cmppQuery.setQtime(qtime.trim());
		
		//querytype
		buffer[pos]=(byte)0;
		pos+=1;
		cmppQuery.setQuerytype((byte)0);
		
		//querycode
		pos+=10;
		cmppQuery.setQuerycode("");
		
		//reserve
		pos+=8;
		cmppQuery.setReserve("");
		
		return buffer;
	}
	
	/**
	 * 功能: 短信发送-情况 API
	 * @author 蔡新鹏
	 * @date 2016-03-01
	 * @address 上海
	 */
	public static int cmpp_send_queryResp(CmppHeader cmppHeader,CmppQueryResp cmppQueryResp,byte[] buffer){
		
		int len=cmppHeader.getTotal_Length();
		
		int pos=0;
		
		if(buffer.length != len){
			logger.info("[GW->SP][queryResp][len:"+len+",buffer.length="+buffer.length+"][error:消息长度错]");
			return 4; //消息长度错
		}
		pos+=12;
		
		//解析报文结构体
		//qtime  时间(精确至日)
		byte[] qtimebytes=new byte[8];
		System.arraycopy(buffer, pos, qtimebytes, 0, 8);
		String qtime=new String(qtimebytes,0,qtimebytes.length);
		pos+=8;
		cmppQueryResp.setQtime(qtime.trim());
		
		//querytype  总数查询
		byte querytype=buffer[pos];
		pos+=1;
		cmppQueryResp.setQuerytype(querytype);
		
		//querycode 查询码
		byte[] querycodebytes=new byte[10];
		System.arraycopy(buffer, pos, querycodebytes, 0, 10);
		String querycode=new String(qtimebytes,0,querycodebytes.length);
		pos+=10;
		cmppQueryResp.setQuerycode(querycode);
		
		byte[] bytes=new byte[4];
		
		//mtTlmsg 从SP接收信息总数
		System.arraycopy(buffer, pos, bytes, 0, 4);
		int mtTlmsg=FormatUtil.bytes4ToInt(bytes);
		pos+=4;
		cmppQueryResp.setMtTlmsg(mtTlmsg);
		
		//mtTluser 从SP接收用户总数
		bytes=new byte[4];
		System.arraycopy(buffer, pos, bytes, 0, 4);
		int mtTluser=FormatUtil.bytes4ToInt(bytes);
		pos+=4;
		cmppQueryResp.setMtTluser(mtTluser);
		
		//mtScs 成功转发数量
		bytes=new byte[4];
		System.arraycopy(buffer, pos, bytes, 0, 4);
		int mtScs=FormatUtil.bytes4ToInt(bytes);
		pos+=4;
		cmppQueryResp.setMtScs(mtScs);
		
		//mtWt 待转发数量
		bytes=new byte[4];
		System.arraycopy(buffer, pos, bytes, 0, 4);
		int mtWt=FormatUtil.bytes4ToInt(bytes);
		pos+=4;
		cmppQueryResp.setMtWt(mtWt);
		
		// mtFl 转发失败数量
		bytes=new byte[4];
		System.arraycopy(buffer, pos, bytes, 0, 4);
		int mtFl=FormatUtil.bytes4ToInt(bytes);
		pos+=4;
		cmppQueryResp.setMtFl(mtFl);
		
		//moScs 向SP成功送达数量
		bytes=new byte[4];
		System.arraycopy(buffer, pos, bytes, 0, 4);
		int moScs=FormatUtil.bytes4ToInt(bytes);
		pos+=4;
		cmppQueryResp.setMoScs(moScs);
		
		//moWt 向SP待送达数量
		bytes=new byte[4];
		System.arraycopy(buffer, pos, bytes, 0, 4);
		int moWt=FormatUtil.bytes4ToInt(bytes);
		pos+=4;
		cmppQueryResp.setMoWt(moWt);
		
		//moFl 向SP送达失败数量
		bytes=new byte[4];
		System.arraycopy(buffer, pos, bytes, 0, 4);
		int moFl=FormatUtil.bytes4ToInt(bytes);
		pos+=4;
		cmppQueryResp.setMoFl(moFl);
		
		return 0;
	}
}
