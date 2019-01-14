package com.cmpp.client.common;

@SuppressWarnings("serial")
public class PtReport implements java.io.Serializable{
	
	private String state;
	private String  submitstate;
	private String remark;
	private String  mobile;
	private String messageid;
	private String reportTime;//yyyy-MM-dd HH:mm:ss
	private int stateWait = 1;
	
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getSubmitstate() {
		return submitstate;
	}
	public void setSubmitstate(String submitstate) {
		this.submitstate = submitstate;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getMessageid() {
		return messageid;
	}
	public void setMessageid(String messageid) {
		this.messageid = messageid;
	}
	public String getReportTime() {
		return reportTime;
	}
	public void setReportTime(String reportTime) {
		this.reportTime = reportTime;
	}
	public int getStateWait() {
		return stateWait;
	}
	public void setStateWait(int stateWait) {
		this.stateWait = stateWait;
	}
}
