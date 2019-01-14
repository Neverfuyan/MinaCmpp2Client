package com.cmpp.util;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FormatUtil {
	
	private static int msgi = 1000;
	private static Object obj =new Object();
    
	public static int sequence = 1000000000;
	
	public static int msymbol = 1;
	
	public static synchronized int getSequence()
	{
	    sequence += 1;
	    if (sequence > 2000000000)
	      sequence = 1000000000;
	    return sequence;
	}
	
	public static synchronized int getMsymbol()
	{
		msymbol += 1;
	    if (msymbol > 127)
	    	msymbol = 1;
	    return msymbol;
	}
	
	public static int bytes4ToInt(byte[] mybytes)
	{
		int tmp = (0xFF & mybytes[0]) << 24 | (0xFF & mybytes[1]) << 16 | (0xFF & mybytes[2]) << 8 | 0xFF & mybytes[3];
		return tmp;
	}
	
	public static byte[] intToBytes4(int i)
	{
	    byte[] mybytes = new byte[4];
	    mybytes[3] = ((byte)(0xFF & i));
	    mybytes[2] = ((byte)((0xFF00 & i) >> 8));
	    mybytes[1] = ((byte)((0xFF0000 & i) >> 16));
	    mybytes[0] = ((byte)((0xFF000000 & i) >> 24));
	    return mybytes;
	}
	
	/** 
     * @Title:bytes2HexString 
     * @Description:字节数组转16进制字符串 
     * @param b 
     *            字节数组 
     * @return 16进制字符串 
     * @throws 
     */
	public static String bytes2HexString(byte[] b) {  
        StringBuffer result = new StringBuffer();  
        String hex;  
        for (int i = 0; i < b.length; i++) {  
            hex = Integer.toHexString(b[i] & 0xFF);  
            if (hex.length() == 1) {  
                hex = '0' + hex;  
            }  
            result.append(hex.toUpperCase());  
        }  
        return result.toString();  
    }

	public static byte[] hexString2ByteArray(String hex) {
		 
		if (hex.length() % 2 != 0)
			return null;
		byte[] buf = new byte[hex.length() / 2];
		for (int i = 0; i < hex.length(); i += 2) {
			char c0 = hex.charAt(i);
			char c1 = hex.charAt(i + 1);
			byte b0 = hexChar2byte(c0);
			byte b1 = hexChar2byte(c1);
			if (b0 < 0 || b0 > 15)
			return null;
			if (b1 < 0 || b1 > 15)
			return null;
			buf[i / 2] = (byte) ((b0 << 4) | b1);
		}
		
		return buf;
	}
	
	public static byte hexChar2byte(char c) { 
		if (c >= '0' && c <= '9')
		return (byte) (c - '0');
		if (c >= 'a' && c <= 'f')
		return (byte) (c - 'a' + 10);
		if (c >= 'A' && c <= 'F')
		return (byte) (c - 'A' + 10);
		return -1;
	}
	
	public static String byteArray2HexString(byte[] buf) {
		 
		char[] hexTable = {
		 '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
		'A', 'B', 'C', 'D', 'E', 'F'
		};
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
		 
		byte b = buf[i];
		byte b0 = (byte) (b & 0x0F);
		b = (byte) (b >>> 4);
		byte b1 = (byte) (b & 0x0F);
		sb.append(hexTable[b1]).append(hexTable[b0]);
		}
		
		return sb.toString();
	}
	
	public static byte[] GetMsgid() {
		String s_msg = "";
		Date nowTime = new Date();
		SimpleDateFormat time = new SimpleDateFormat("yyMMddHHmmss");
		byte[] msgid = null;
		
		try {
			synchronized(obj){
				msgi = msgi + 1;
			}
			if (msgi >= 9999)
				msgi = 1000;
			
			s_msg = time.format(nowTime) + msgi;
			// logger.info(s_msg);
			msgid = hexString2ByteArray(s_msg);
			
		}catch (Exception e) {
			e.printStackTrace(); 
		}
		
		return msgid;
	}
	
	public static byte[] encodeUCS2(String src) throws Exception
	{
		byte[] bytes;
	    try{
	      bytes = src.getBytes("UTF-16BE");
	    } catch (UnsupportedEncodingException e) {
	    	throw new Exception(e);
	    }
	    
	    return bytes;
    }
	
	public static byte intToByte(int n) {
	    byte bInt = (byte)(n & 0xFF);
	    return bInt;
	}
	
	public static int byteToInt(byte bInt) {
	    int intValue = 0xFF & bInt;
	    return intValue;
	}
	
	/**
	 * 方法描述：字符串转二进制 
	 * @author 邱凤丹
	 * @date 日期：2017-12-13  时间：下午3:37:14
	 * @version 1.0
	 * @param str
	 * @return
	 */
	public static byte[] hex2byte(String str) { 
		if (str == null){  
			return null;  
		}
       	str = str.trim();  
       	
       	String [] temp = str.split(",");
		byte [] b = new byte[temp.length];
		for(int i = 0;i<b.length;i++)
		{
			b[i] = Long.valueOf(temp[i], 2).byteValue();
		}
		return b;
	}
	
	/**
     * 8-BIT编码
     */
    public static String encode8bit(String src) {
        String result = null;

        if (src != null && src.length() == src.getBytes().length) {
            result=str_to_Hex(src);
        }

        return result;
    }
    
    /**
     * 字符串 转 16进制
     * @param str
     * @return
     */
    public static String str_to_Hex(String str) {
        return new String(encodeHex(str.getBytes()));
    }
    
    /**
     * 16进制 打印
     * @param data
     * @return
     */
    public static char[] encodeHex(byte[] data) {
        char[] DIGITS_HEX = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

        int l = data.length;
        char[] out = new char[l << 1];
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = DIGITS_HEX[(0xF0 & data[i]) >>> 4];
            out[j++] = DIGITS_HEX[0x0F & data[i]];
        }

        return out;
    }
}
