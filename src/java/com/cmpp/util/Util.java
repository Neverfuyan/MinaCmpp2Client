package com.cmpp.util;

import org.apache.log4j.Logger;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 工具类
 * @author zhutong
 *
 */
public class Util {
	
	static Logger log = Logger.getLogger( Util.class);
	
  private static Calendar calendar = Calendar.getInstance();
  private static DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  public static void main(String[] args) {
	  System.out.println( Util.getMsgId());
  }
  public static String getMsgId(){
	  return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+ Util.generateRandomStr(4);
  }
  public static String getFormatDate(Date date)
  {
    if (date == null) {
      return "";
    }
    return dateTimeFormat.format(date);
  }

  public static Date convertStrToDate(String strDate)
  {
    Date date = null;
    try {
      date = dateTimeFormat.parse(strDate);
    } catch (ParseException e) {
      log.info("convert string to date error!");
      e.printStackTrace();
    }
    return date;
  }

  public static String getChinaDateString()
  {
    int year = calendar.get(1);
    int month = calendar.get(2) + 1;
    int date = calendar.get(5);
    String strYear = String.valueOf(year);
    String strMonth = month < 10 ? "0" + month : String.valueOf(month);
    String strDate = date < 10 ? "0" + date : String.valueOf(date);
    return strYear + strMonth + strDate;
  }

  public static String getRandomString(int size)
  {
    String seed = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    byte[] chs = seed.getBytes();
    byte[] bs = new byte[size];
    Random random = new Random();
    int length = chs.length;
    for (int i = 0; i < size; i++) {
      bs[i] = chs[random.nextInt(length)];
    }
    return new String(bs);
  }

  public static long generateLongId()
  {
    StringBuffer longId = new StringBuffer(20);

    long random = Math.round(Math.random() * 100000000.0D);

    if (random < 10L)
      longId.append("00").append(random);
    else if (random < 100L)
      longId.append("0").append(random);
    else {
      longId.append(random);
    }
    return Long.valueOf(longId.toString()).longValue();
  }

  public static String generateSN(String userId)
  {
    StringBuffer longId = new StringBuffer(20).append(userId.substring(0, 9));
    long random = Math.round(Math.random() * 1000000000.0D);
    String str = String.valueOf(random);
    int l = str.length();
    if (l < 9) {
      for (int i = 0; i < 9 - l; i++) {
        longId.append("0");
      }
    }
    longId.append(random);

    return longId.toString();
  }

  public static String generateRandomStr() {
    String[] vec = { "a", "b", "c", "d", "e", "f", "g", 
      "h", "i", "j", "k", "l", "m", "n", 
      "o", "p", "q", "r", "t", 
      "u", "v", "x", "y", "z" };
    int r1 = Long.valueOf(Math.round(Math.random() * (vec.length - 1))).intValue();
    int r2 = Long.valueOf(Math.round(Math.random() * (vec.length - 1))).intValue();
    return vec[r1] + vec[r2];
  }

  public static String generateRandomStr(int num) {
    String[] vec = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };
    num = num <= 0 ? 1 : num;
    StringBuffer str = new StringBuffer(10);
    for (int i = 0; i < num; i++) {
      int r1 = Long.valueOf(Math.round(Math.random() * (vec.length - 1))).intValue();
      str.append(vec[r1]);
    }
    return str.toString();
  }
  
  public static String getStrongPwd(int pswdLen){
		String[] pswdStr = { "abcdefghijkmnopqrstuvwxyz",
				"ABCDEFGHIJKMNOPQRSTUVWXYZ", "123456789",
				"~!@#$^(];,.|:<>-_+)", };

		String pswd = ""; // 密码

		// 根据传入的参数，设置密码长度
		if (pswdLen < 5) {
			log.info("密码长度必须大于 5 !");
			System.exit(0);
		}

		// chs 用于存放密码的字符 .
		char[] chs = new char[pswdLen];

		// 这个循环用于保证密码包含四种字符.
		for (int i = 0; i < pswdStr.length; i++) {

			int idx = (int) (Math.random() * pswdStr[i].length());
			chs[i] = pswdStr[i].charAt(idx);

		}
		// 这个循环用于保证密码的长度.
		for (int i = pswdStr.length; i < pswdLen; i++) {

			int arrIdx = (int) (Math.random() * pswdStr.length);
			int strIdx = (int) (Math.random() * pswdStr[arrIdx].length());

			chs[i] = pswdStr[arrIdx].charAt(strIdx);
		}

		// 打乱 chs 的顺序
		for (int i = 0; i < 1000; i++) {
			int idx1 = (int) (Math.random() * chs.length);
			int idx2 = (int) (Math.random() * chs.length);

			if (idx1 == idx2) {
				continue;
			}

			char tempChar = chs[idx1];
			chs[idx1] = chs[idx2];
			chs[idx2] = tempChar;
		}

		pswd = new String(chs);
		return pswd;
  }
  /**
   * 对手机号分隔  每group_num手机号一组，并以'#'分开
   * @param old
   * @param type
   * @param group_num
   * @return
   */
  public static String groupManage(String old,String type,int group_num){
  	String[] arr=old.split(type);
  	StringBuffer sb=new StringBuffer();
  	for(int i=0;i<arr.length;i++){
  		sb.append(arr[i]).append((i+1)%group_num==0?"#":i==arr.length-1?"":",");
  	}
  	return sb.toString();
  }
  /**
	 * string的字符返回数组list
	 * @param str
	 * @param rex
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List tranStrToList(String str,String rex){
		List list = new ArrayList();
		String[] arr=str.split(rex);
		for(String s:arr){
			list.add(s);
		}
		return list;
	}
}