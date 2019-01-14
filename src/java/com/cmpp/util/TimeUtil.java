package com.cmpp.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TimeUtil {
	public static int TIMEOUT=300;
	public static int REPEAT_TIME=5;
	public static String getNowTime(){
		SimpleDateFormat sdf=new SimpleDateFormat("MMddHHmmss");
		String time=sdf.format(new Date());
		return time;
	}
	
	public static String getNowTime(String format){
		SimpleDateFormat sdf=new SimpleDateFormat(format);
		String time=sdf.format(new Date());
		return time;
	}
	
	public static String stringNowTime(){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time=sdf.format(new Date());
		return time;
	}
	
	public static String mNowTime(){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
		String time=sdf.format(new Date());
		return time;
	}
	
	public static String mNowDate(){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
		String time=sdf.format(new Date());
		return time;
	}
	
	/**
	 * 方法描述：回执相差8s~15s之间的时间修改成相差4s~8s内随机时间
	 * @author 邱凤丹
	 * @date 日期：2018-4-25  时间：下午5:04:08
	 * @version 1.0
	 * @param msgid（yyyyMMddHHmmss）
	 * @return
	 */
	public static String ReportTime(String msgid){
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			String reportTime= sdf.format(new Date());
			Date one = sdf.parse(msgid);
			Date two = sdf.parse(reportTime);
			long interval = (two.getTime() - one.getTime())/1000;
			
			int difference = 0;
			if(interval <= 15 && interval >= 8){
				difference = (int)interval - (int)(Math.random()*(4)+4);
		        Date beforeDate = new Date(two.getTime() - difference*1000);//30分钟前的时间
		        reportTime = sdf.format(beforeDate);
			}
			return reportTime;
		}catch (Exception e) {
			return mNowTime();
		}
	}
	
	/**
	 * 方法描述：统计当天还剩余多少秒
	 * @author 邱凤丹
	 * @date 日期：2018-9-7  时间：下午5:05:51
	 * @version 1.0
	 * @return
	 */
	public static int time(){
		Calendar curDate = Calendar.getInstance();  
	    Calendar tommorowDate = new GregorianCalendar(curDate  
	            .get(Calendar.YEAR), curDate.get(Calendar.MONTH), curDate  
	            .get(Calendar.DATE) + 1, 0, 0, 0);  
	    return (int)(tommorowDate.getTimeInMillis() - curDate .getTimeInMillis()) / 1000;  
	}
	
	/**
	 * 方法描述：预警短信下发
	 * @author 邱凤丹
	 * @date 日期：2018-9-7  时间：下午3:33:11
	 * @version 1.0
	 * @param mobile	
	 * @param content
	 * @return
	 */
	public static String sms(String mobile,String content){
		String tkey = TimeUtil.getNowTime("yyyyMMddHHmmss");
		String ret;
		try {
			if(mobile.split(",").length > 1){
				for (String str : mobile.split(",")) {
					ret = HttpClientUtil.sendPost("http://api.zthysms.com/sendSms.do", "username=shztxthy"+"&password="+MD5Gen.getMD5(MD5Gen.getMD5("5ncDsT")+tkey)+"&tkey="+tkey+"&mobile="+str+"&content="+URLEncoder.encode(content, "utf-8"));
				}
			}else{
				ret = HttpClientUtil.sendPost("http://api.zthysms.com/sendSms.do", "username=shztxthy"+"&password="+MD5Gen.getMD5(MD5Gen.getMD5("5ncDsT")+tkey)+"&tkey="+tkey+"&mobile="+mobile+"&content="+URLEncoder.encode(content, "utf-8"));
				return ret;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}
}
