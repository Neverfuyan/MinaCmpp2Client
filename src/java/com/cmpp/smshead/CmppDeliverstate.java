package com.cmpp.smshead;

public class CmppDeliverstate {
	private byte[] msgid=new byte[8];
	private String stat;
	private String submittime;
	private String donetime;
	private String destterminalid;
	private int smscsequence;
	
	public byte[] getMsgid() {
		return msgid;
	}
	public void setMsgid(byte[] msgid) {
		this.msgid = msgid;
	}
	public String getStat() {
		return stat;
	}
	public void setStat(String stat) {
		this.stat = stat;
	}
	public String getSubmittime() {
		return submittime;
	}
	public void setSubmittime(String submittime) {
		this.submittime = submittime;
	}
	public String getDonetime() {
		return donetime;
	}
	public void setDonetime(String donetime) {
		this.donetime = donetime;
	}
	public String getDestterminalid() {
		return destterminalid;
	}
	public void setDestterminalid(String destterminalid) {
		this.destterminalid = destterminalid;
	}
	public int getSmscsequence() {
		return smscsequence;
	}
	public void setSmscsequence(int smscsequence) {
		this.smscsequence = smscsequence;
	}
}
