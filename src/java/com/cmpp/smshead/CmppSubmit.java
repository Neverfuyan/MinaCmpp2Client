package com.cmpp.smshead;

public class CmppSubmit {
	private byte[] msgId = new byte[8];
	private byte pkTotal = 1;
	private byte pkNumber = 1;
	private byte needReport = 1;
	private byte priority =0;
	private String serviceId = "";
	private byte feeUserType = 0;
	private String feeTermId = "";
	private byte tpPid = 0;
	private byte tpUdhi = 0;
	private byte msgFmt=0;//信息格式 0：ASCII串;3：短信写卡操作;4：二进制信息;8：UCS2编码;15：含GB汉字  
	private String msgSrc = "";
	private String feeType = "";
	private String feeCode = "";
	private String validTime = "";
	private String atTime = "";
	private String srcId = "";
	private byte destTermIdCount = 0;
	private byte[] destTermId;
	private String destTermIdstr="";
	private byte msgLen=0;
	private byte[] msgContent;//信息内容
	private String msgContentstr;//信息内容
	private String Reserve; //保留
	
	//6字节协议
	private String udhi01;
	private String udhi02;
	private String udhi03;
	private String udhi04;
	private String udhi05;
	private String udhi06;
	
	public String getUdhi01() {
		return udhi01;
	}
	public void setUdhi01(String udhi01) {
		this.udhi01 = udhi01;
	}
	public String getUdhi02() {
		return udhi02;
	}
	public void setUdhi02(String udhi02) {
		this.udhi02 = udhi02;
	}
	public String getUdhi03() {
		return udhi03;
	}
	public void setUdhi03(String udhi03) {
		this.udhi03 = udhi03;
	}
	public String getUdhi04() {
		return udhi04;
	}
	public void setUdhi04(String udhi04) {
		this.udhi04 = udhi04;
	}
	public String getUdhi05() {
		return udhi05;
	}
	public void setUdhi05(String udhi05) {
		this.udhi05 = udhi05;
	}
	public String getUdhi06() {
		return udhi06;
	}
	public void setUdhi06(String udhi06) {
		this.udhi06 = udhi06;
	}
	public byte getMsgLen() {
		return msgLen;
	}
	public void setMsgLen(byte msgLen) {
		this.msgLen = msgLen;
	}
	public byte[] getMsgId() {
		return msgId;
	}
	public void setMsgId(byte[] msgId) {
		this.msgId = msgId;
	}
	public byte getPkTotal() {
		return pkTotal;
	}
	public void setPkTotal(byte pkTotal) {
		this.pkTotal = pkTotal;
	}
	public byte getPkNumber() {
		return pkNumber;
	}
	public void setPkNumber(byte pkNumber) {
		this.pkNumber = pkNumber;
	}
	public byte getNeedReport() {
		return needReport;
	}
	public void setNeedReport(byte needReport) {
		this.needReport = needReport;
	}
	public byte getPriority() {
		return priority;
	}
	public void setPriority(byte priority) {
		this.priority = priority;
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public byte getFeeUserType() {
		return feeUserType;
	}
	public void setFeeUserType(byte feeUserType) {
		this.feeUserType = feeUserType;
	}
	public String getFeeTermId() {
		return feeTermId;
	}
	public void setFeeTermId(String feeTermId) {
		this.feeTermId = feeTermId;
	}
	public byte getTpPid() {
		return tpPid;
	}
	public void setTpPid(byte tpPid) {
		this.tpPid = tpPid;
	}
	public byte getTpUdhi() {
		return tpUdhi;
	}
	public void setTpUdhi(byte tpUdhi) {
		this.tpUdhi = tpUdhi;
	}
	
	public byte getMsgFmt() {
		return msgFmt;
	}
	public void setMsgFmt(byte msgFmt) {
		this.msgFmt = msgFmt;
	}
	public String getMsgSrc() {
		return msgSrc;
	}
	public void setMsgSrc(String msgSrc) {
		this.msgSrc = msgSrc;
	}
	public String getFeeType() {
		return feeType;
	}
	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}
	public String getFeeCode() {
		return feeCode;
	}
	public void setFeeCode(String feeCode) {
		this.feeCode = feeCode;
	}
	public String getValidTime() {
		return validTime;
	}
	public void setValidTime(String validTime) {
		this.validTime = validTime;
	}
	public String getAtTime() {
		return atTime;
	}
	public void setAtTime(String atTime) {
		this.atTime = atTime;
	}
	public String getSrcId() {
		return srcId;
	}
	public void setSrcId(String srcId) {
		this.srcId = srcId;
	}
	public byte getDestTermIdCount() {
		return destTermIdCount;
	}
	public void setDestTermIdCount(byte destTermIdCount) {
		this.destTermIdCount = destTermIdCount;
	}
	public byte[] getDestTermId() {
		return destTermId;
	}
	public void setDestTermId(byte[] destTermId) {
		this.destTermId = destTermId;
	}
	
	public byte[] getMsgContent() {
		return msgContent;
	}
	public void setMsgContent(byte[] msgContent) {
		this.msgContent = msgContent;
	}
	public String getMsgContentstr() {
		return msgContentstr;
	}
	public void setMsgContentstr(String msgContentstr) {
		this.msgContentstr = msgContentstr;
	}
	public String getReserve() {
		return Reserve;
	}
	public void setReserve(String reserve) {
		Reserve = reserve;
	}
	public String getDestTermIdstr() {
		return destTermIdstr;
	}
	public void setDestTermIdstr(String destTermIdstr) {
		this.destTermIdstr = destTermIdstr;
	}
}
