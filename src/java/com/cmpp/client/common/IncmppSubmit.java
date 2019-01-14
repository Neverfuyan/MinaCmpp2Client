package com.cmpp.client.common;

import java.io.Serializable;

@SuppressWarnings("serial")
public class IncmppSubmit implements Serializable{
	private int num;
	private String mid;
	private String cpmid;
	private String mobile;
	private String port;
	private String platform;
	private int p_num;
	
	public int getP_num() {
		return p_num;
	}
	public void setP_num(int p_num) {
		this.p_num = p_num;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getMid() {
		return mid;
	}
	public void setMid(String mid) {
		this.mid = mid;
	}
	public String getCpmid() {
		return cpmid;
	}
	public void setCpmid(String cpmid) {
		this.cpmid = cpmid;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
}
