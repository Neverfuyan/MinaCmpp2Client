package com.cmpp.client.common;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Ptport implements Serializable{
	
	private String port;
	private String platform;
	
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