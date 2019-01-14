package com.cmpp.smshead;

public class CmppConnect {
	
	private String Source_Addr;  //源地址，此处为SP_Id，即SP的企业代码。
	private byte[] AuthenticatorSource=new byte[16]; //用于鉴别源地址
	private byte Version;  //双方协商的版本号
	private int Timestamp; //时间戳的明文,由客户端产生,格式为MMDDHHMMSS，即月日时分秒，10位数字的整型，右对齐
	
	public String getSource_Addr() {
		return Source_Addr;
	}
	public void setSource_Addr(String source_Addr) {
		Source_Addr = source_Addr;
	}
	
	public byte[] getAuthenticatorSource() {
		return AuthenticatorSource;
	}
	public void setAuthenticatorSource(byte[] authenticatorSource) {
		AuthenticatorSource = authenticatorSource;
	}
	public byte getVersion() {
		return Version;
	}
	public void setVersion(byte version) {
		Version = version;
	}
	public int getTimestamp() {
		return Timestamp;
	}
	public void setTimestamp(int timestamp) {
		Timestamp = timestamp;
	}
}
