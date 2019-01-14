package com.cmpp.smshead;

public class CmppQuery {
	private String qtime;
	private byte querytype;
	private String querycode;
	private String reserve;
	
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
	public String getReserve() {
		return reserve;
	}
	public void setReserve(String reserve) {
		this.reserve = reserve;
	}
}
