package com.cmpp.client.common;

import java.io.Serializable;

@SuppressWarnings("serial")
public class MonitorCount implements Serializable{
	
	private int count; //提交总数
	private int countsucc;//提交成功总数
	private int delivercount;//回执总数
	private int delivercountsucc;//回执成功总数
	private int delivercountfail;//回执失败总数
	
	public int getDelivercountfail() {
		return delivercountfail;
	}
	public void setDelivercountfail(int delivercountfail) {
		this.delivercountfail = delivercountfail;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getCountsucc() {
		return countsucc;
	}
	public void setCountsucc(int countsucc) {
		this.countsucc = countsucc;
	}
	public int getDelivercount() {
		return delivercount;
	}
	public void setDelivercount(int delivercount) {
		this.delivercount = delivercount;
	}
	public int getDelivercountsucc() {
		return delivercountsucc;
	}
	public void setDelivercountsucc(int delivercountsucc) {
		this.delivercountsucc = delivercountsucc;
	}
}
