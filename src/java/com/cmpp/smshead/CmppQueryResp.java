package com.cmpp.smshead;

public class CmppQueryResp {
	private String qtime;
	private byte querytype;
	private String querycode;
	private int mtTlmsg;
	private int mtTluser;
	private int mtScs;
	private int mtWt;
	private int mtFl;
	private int moScs;
	private int moWt;
	private int moFl;
	
	public String getQtime() {
		return qtime;
	}
	public void setQtime(String qtime) {
		this.qtime = qtime;
	}
	public byte getQuerytype() {
		return querytype;
	}
	public void setQuerytype(byte querytype) {
		this.querytype = querytype;
	}
	public String getQuerycode() {
		return querycode;
	}
	public void setQuerycode(String querycode) {
		this.querycode = querycode;
	}
	public int getMtTlmsg() {
		return mtTlmsg;
	}
	public void setMtTlmsg(int mtTlmsg) {
		this.mtTlmsg = mtTlmsg;
	}
	public int getMtTluser() {
		return mtTluser;
	}
	public void setMtTluser(int mtTluser) {
		this.mtTluser = mtTluser;
	}
	public int getMtScs() {
		return mtScs;
	}
	public void setMtScs(int mtScs) {
		this.mtScs = mtScs;
	}
	public int getMtWt() {
		return mtWt;
	}
	public void setMtWt(int mtWt) {
		this.mtWt = mtWt;
	}
	public int getMtFl() {
		return mtFl;
	}
	public void setMtFl(int mtFl) {
		this.mtFl = mtFl;
	}
	public int getMoScs() {
		return moScs;
	}
	public void setMoScs(int moScs) {
		this.moScs = moScs;
	}
	public int getMoWt() {
		return moWt;
	}
	public void setMoWt(int moWt) {
		this.moWt = moWt;
	}
	public int getMoFl() {
		return moFl;
	}
	public void setMoFl(int moFl) {
		this.moFl = moFl;
	}
}
