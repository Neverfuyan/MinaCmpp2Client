package com.cmpp.smshead;
/**
 * 
 * @author 蔡新鹏
 * @date 2016-12-21
 * @address 上海
 */
public class CmppDeilverResp {
	
	private byte[] msgId = new byte[8];
	private byte result;
	
	public byte[] getMsgId() {
		return msgId;
	}
	public void setMsgId(byte[] msgId) {
		this.msgId = msgId;
	}
	public byte getResult() {
		return result;
	}
	public void setResult(byte result) {
		this.result = result;
	}
}
