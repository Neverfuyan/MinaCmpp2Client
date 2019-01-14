package com.cmpp.client.common;

import java.io.Serializable;

@SuppressWarnings("serial")
public class IncmppDeliver implements Serializable{
	private byte delivery;
	//private byte[] msgid=new byte[8];
	private String msgid;
	private String stat;   //上行  做port 小号用
	private String submittime;
	private String donetime;
	private String destterminalid; //上行 共用-手机号码
	private int smscsequence;
	private String content; //上行 内容
	
	public byte getDelivery() {
		return delivery;
	}
	public void setDelivery(byte delivery) {
		this.delivery = delivery;
	}
	
	public String getMsgid() {
		return msgid;
	}
	public void setMsgid(String msgid) {
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
}
