package com.cmpp.client.api;

import com.cmpp.client.common.Confighead;
import com.cmpp.smshead.CmppConnect;

import java.security.MessageDigest;

public class AuthentApi {
	public static byte[] getAuthenticator(Confighead confighead,CmppConnect cmppConnect){
		
		byte[] sp = confighead.getClientid().trim().getBytes();
	    byte[] bzero = new byte[9];
	    byte[] bytePassword = confighead.getClientpwd().trim().getBytes();
	    
	    String mTimestamp=Integer.toString(cmppConnect.getTimestamp());
	    if(mTimestamp.length()==9){
	    	mTimestamp="0"+mTimestamp.trim();
	    }
	    
	    byte[] btimestamp = mTimestamp.getBytes();
	    
	    byte[] bmd5 = new byte[sp.length + 9 + bytePassword.length + btimestamp.length];
	    
	    int cur = 0;
	    System.arraycopy(sp, 0, bmd5, cur, sp.length);
	    cur += sp.length;
	    
	    System.arraycopy(bzero, 0, bmd5, cur, 9);
	    cur += bzero.length;
	    
	    System.arraycopy(bytePassword, 0, bmd5, cur, bytePassword.length);
	    cur += bytePassword.length;
	    
	    System.arraycopy(btimestamp, 0, bmd5, cur, btimestamp.length);
	    byte[] result = new byte[16];
	    try {
	      MessageDigest md = MessageDigest.getInstance("md5");
	      md.update(bmd5);
	      result = md.digest();
	    }
	    catch (Exception e) {
	    }
	    
	    return result;
	}
}
