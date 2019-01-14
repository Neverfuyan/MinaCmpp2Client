package com.cmpp.smshead;

public class CmppDeilver {
	
	private byte[] msgid=new byte[8];
	private String destid;
	private String serviceid;
	private byte tppid;
	private byte tpudhi;
	private byte msgfmt;
	private String terminalid;
	private byte delivery;
	private byte msglen;
	private byte[] msgcontent;
	private byte[] reserved=new byte[8];
	
	public byte[] getMsgid() {
		return msgid;
	}
	public void setMsgid(byte[] msgid) {
		this.msgid = msgid;
	}
	public String getDestid() {
		return destid;
	}
	public void setDestid(String destid) {
		this.destid = destid;
	}
	public String getServiceid() {
		return serviceid;
	}
	public void setServiceid(String serviceid) {
		this.serviceid = serviceid;
	}
	public byte getTppid() {
		return tppid;
	}
	public void setTppid(byte tppid) {
		this.tppid = tppid;
	}
	public byte getTpudhi() {
		return tpudhi;
	}
	public void setTpudhi(byte tpudhi) {
		this.tpudhi = tpudhi;
	}
	public byte getMsgfmt() {
		return msgfmt;
	}
	public void setMsgfmt(byte msgfmt) {
		this.msgfmt = msgfmt;
	}
	public String getTerminalid() {
		return terminalid;
	}
	public void setTerminalid(String terminalid) {
		this.terminalid = terminalid;
	}
	public byte getDelivery() {
		return delivery;
	}
	public void setDelivery(byte delivery) {
		this.delivery = delivery;
	}
	public byte getMsglen() {
		return msglen;
	}
	public void setMsglen(byte msglen) {
		this.msglen = msglen;
	}
	public byte[] getMsgcontent() {
		return msgcontent;
	}
	public void setMsgcontent(byte[] msgcontent) {
		this.msgcontent = msgcontent;
	}
	public byte[] getReserved() {
		return reserved;
	}
	public void setReserved(byte[] reserved) {
		this.reserved = reserved;
	}
}
