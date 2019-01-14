package com.cmpp.client.api;

import org.apache.log4j.Logger;

import com.cmpp.client.common.Confighead;
import com.cmpp.smshead.CmppConnect;
import com.cmpp.smshead.CmppHeader;
import com.cmpp.util.CmppConstant;
import com.cmpp.util.FormatUtil;
import com.cmpp.util.TimeUtil;
/**
 * 功能：Connect 连接实现
 * @author 蔡新鹏
 * @date 2016-12-25
 * @address 上海
 */
public class ConnectApi {
	private static Logger logger = Logger.getLogger(ConnectApi.class);
	
	/**
	 * 功能：cmpp_send_connect
	 * @return
	 */
	public static byte[] cmpp_send_connect(Confighead confighead){
		CmppHeader cmppHeader=new CmppHeader();
		CmppConnect cmppConnect=new CmppConnect();
		
		int pos=0;
		byte[] buffer=new byte[39];
		byte[] bytes=new byte[4];
		
		//组织报文头
		cmppHeader.setTotal_Length(39);
		cmppHeader.setCommand_Id(CmppConstant.CMD_CONNECT);
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
		//Source_Addr
//		byte[] sourcebytes=new byte[6];
//		sourcebytes=confighead.getClientid().trim().getBytes();
//		System.arraycopy(sourcebytes, 0, buffer, pos, 6);
//		pos+=6;
//		cmppConnect.setSource_Addr(confighead.getClientid());
		
		byte[] sourcebytes=new byte[6];
		byte[] data3=new byte[6];
		sourcebytes=confighead.getClientid().trim().getBytes();
		int num = 6-sourcebytes.length;
		if(num>0){
			byte[] data2 = new byte[num];
			for(int i=0;i<num;i++){
				data2[i] = (byte)0x00;
			}
		    System.arraycopy(sourcebytes, 0, data3, 0, sourcebytes.length);  
		    System.arraycopy(data2, 0, data3, sourcebytes.length, data2.length);
		}else{
			data3=confighead.getClientid().trim().getBytes();
		}
		
		System.arraycopy(data3, 0, buffer, pos, 6);
		pos+=6;
		cmppConnect.setSource_Addr(confighead.getClientid());

		
		//version
		cmppConnect.setVersion((byte)0x20);
		//Timestamp
		String timestamp=TimeUtil.getNowTime();
		cmppConnect.setTimestamp(Integer.parseInt(timestamp.trim()));
		
		//Auth
		byte[] authbytes=new byte[16];
		authbytes=AuthentApi.getAuthenticator(confighead, cmppConnect);
		System.arraycopy(authbytes, 0, buffer, pos, 16);
		pos+=16;
		cmppConnect.setAuthenticatorSource(authbytes);
		String AuthenticatorSource = FormatUtil.bytes2HexString(authbytes);
		
		//version
		buffer[pos]=cmppConnect.getVersion();
		pos+=1;
		
		//timestamp
		bytes=new byte[4];
		bytes=FormatUtil.intToBytes4(cmppConnect.getTimestamp());
		System.arraycopy(bytes, 0, buffer, pos, 4);
		pos+=4;
		
		logger.info("[SP->GW][connect][len:"+cmppHeader.getTotal_Length()+",commandId:"+Integer.toHexString(cmppHeader.getCommand_Id())+",sequence:"+cmppHeader.getSequence_Id()+
				"source:"+cmppConnect.getSource_Addr()+",Auth:"+AuthenticatorSource+",version:"+cmppConnect.getVersion()+",timestamp:"+cmppConnect.getTimestamp()+"]");
		
		return buffer;
	}
}