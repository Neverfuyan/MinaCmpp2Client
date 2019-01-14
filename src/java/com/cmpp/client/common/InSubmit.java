package com.cmpp.client.common;

import java.io.Serializable;

@SuppressWarnings("serial")
public class InSubmit implements Serializable{
	private String mid;
	private String cpmid;
	private String mobile;
	private String port;
	private String content;
	private String platform;
	
	public InSubmit(){
		//
	}
	
	public InSubmit(String mid, String cpmid, String mobile, String port, String content,String platform)
	{
		this.mid = mid;
		this.cpmid = cpmid;
		this.mobile = mobile;
		this.port = port;
		this.content = content;
		this.platform = platform;
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
}
