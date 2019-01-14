package com.cmpp.smshead;

public class CmppSubmitResp {
	
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