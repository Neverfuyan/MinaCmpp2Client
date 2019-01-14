package com.cmpp.client.api;

import com.cmpp.util.Mail;

public class MonitorApi {
	
	/**
	 * 功能:日常 监控 邮件发送
	 * 开发:蔡新鹏
	 * @param content
	 * @param copytomail
	 */
	public static void sendmonitorqqmail(String content,String copytomail){
		Mail mail=new Mail();
		mail.SendEmail(content, copytomail);
	}
	
}
