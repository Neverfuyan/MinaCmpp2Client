package com.cmpp.smshead;

public class CmppConnectResp {
	private byte Status; //状态
	private byte[] AuthenticatorISMG=new byte[16];//ISMG认证码，用于鉴别ISMG
	private byte Version; //服务器支持的最高版本号
	
	public byte getStatus() {
		return Status;
	}
	public void setStatus(byte status) {
		Status = status;
	}
	
	public byte[] getAuthenticatorISMG() {
		return AuthenticatorISMG;
	}
	public void setAuthenticatorISMG(byte[] authenticatorISMG) {
		AuthenticatorISMG = authenticatorISMG;
	}
	public byte getVersion() {
		return Version;
	}
	public void setVersion(byte version) {
		Version = version;
	}
}
