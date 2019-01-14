package com.cmpp.client.api;

import com.cmpp.client.common.Confighead;
import com.cmpp.client.common.InSubmit;
import com.cmpp.smshead.CmppHeader;
import com.cmpp.smshead.CmppSubmit;
import com.cmpp.util.CmppConstant;
import com.cmpp.util.FormatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Random;

public class SubmitApi {
	private static final Logger logger = LoggerFactory.getLogger(SubmitApi.class);
	
	/**
	 * 功能：短信组织
	 * @param inSubmit
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static CmppSubmit[] cmpp_send_zzsubmit(InSubmit inSubmit,Confighead confighead) throws Exception{
		ArrayList submitList = new ArrayList();
		String mobileString = inSubmit.getMobile();
		String[] userMobiles = mobileString.split(",");
		
		byte[] contentBytes = FormatUtil.encodeUCS2(inSubmit.getContent());
		
		int messageSize = 0;
	    int messageLeft = 0;
	    boolean flag = false;
	    
	    if(contentBytes.length <= 140){
	    	messageSize = 1;
	        messageLeft = 70;
	    }else{
	    	messageSize = contentBytes.length / 134;
	        messageLeft = contentBytes.length % 134;
	        
	        if(messageLeft == 0){
	        	//messageSize++;
	            flag = true;
	        }else if (messageLeft > 0){
	        	messageSize++;
	        }
	    }
	    
	    int nowMessageSize = messageSize;
	    int cur = 0;
	    int messageLen = 0;
	    int messageIndex = 1;
	    int contentSequence = new Random().nextInt(252);
	    
	    //手机号码分条-2
	    int ccnum=Integer.parseInt(confighead.getMobilenum());
	    int m_tt=0;
	    int m_m01=userMobiles.length / ccnum;
	    int m_m02=userMobiles.length % ccnum;
	    
	    if(m_m02==0){
	    	m_tt = m_m01;
	    }else if(m_m02 > 0){
	    	m_tt = m_m01 + 1;
	    }
	    
	    while(nowMessageSize > 0){
	    	if (messageSize > 1) {
	            if (nowMessageSize > 1)
	              messageLen = 134;
	            else if (nowMessageSize == 1)
	              messageLen = messageLeft;
	          }else 
	        	  messageLen = contentBytes.length;
	    	
	    	
	    	
	    	byte[] bMessageContent = new byte[messageLen];
	    	System.arraycopy(contentBytes, cur, bMessageContent, 0, messageLen);
//	    	String mContent=new String(bMessageContent,"UTF-16BE");
	    	//logger.info("nowMessageSize:"+nowMessageSize+" ,messageLen:"+messageLen+",mContent="+mContent);
	    	
	    	int i=0; //记录跟踪 数
	        int j=0;
	        String mMobilestr="";
	        
	        for (String mobile : userMobiles){
	        	if(!mobile.equals("")){
	        		i=i+1;
	        		if(userMobiles.length<=1){
	        			mMobilestr = mobile;
	        		}else{
	        			if(userMobiles.length<=ccnum && userMobiles.length>1){
	        				mMobilestr=mMobilestr+mobile+",";
	        				if(i != userMobiles.length){
	        					continue;
	        				}
	        			}else{
	        				if(j < m_tt-1){
	        					if(i%ccnum != 0){
	    		    				mMobilestr=mMobilestr+mobile+",";
	    		    				continue;
	    		    			}else{
	    		    				mMobilestr=mMobilestr+mobile+",";
	    		    				j=j+1;
	    		    			}
	        				}else{
	        					mMobilestr=mMobilestr+mobile+",";
	    	    				if(i != userMobiles.length){
	    	    					continue;
	    	    				}else{
	    	    					j=j+1;
	    	    				}
	        				}
	        			}
	        		}
	        		
	        		CmppSubmit cmppSubmit=new CmppSubmit();
	        		
	        		//msgid
	        		byte[] msgidbyte=new byte[8];
	        		cmppSubmit.setMsgId(msgidbyte);

	        		//needReport
	        		cmppSubmit.setNeedReport((byte)Integer.parseInt(confighead.getIsreport()));
	        		
	        		//priority
	        		cmppSubmit.setPriority((byte)0);
	        		
	        		//serviceId
	        		cmppSubmit.setServiceId(confighead.getServiceid().trim());
	        		
	        		//feeUserType
	        		cmppSubmit.setFeeUserType((byte)2);
	        		
	        		//feeTermId
	        		cmppSubmit.setFeeTermId("0");
	        		
	        		//tpPid
	        		cmppSubmit.setTpPid((byte)0);

	        		//msgSrc
	        		cmppSubmit.setMsgSrc(confighead.getCorpid().trim());
	        		
	        		//feeType
	        		cmppSubmit.setFeeType("01");
	        		
	        		//feeCode
	        		cmppSubmit.setFeeCode("");
	        		
	        		//validTime 存活有效期
	        		cmppSubmit.setValidTime("");
	        		
	        		//atTime 定时发送时间
	        		cmppSubmit.setAtTime("");
	        		
	        		//srcId
	        		cmppSubmit.setSrcId(confighead.getSpport().trim()+inSubmit.getPort());
	        		
	        		//destTermIdCount
	        		if(",".equals(mMobilestr.substring(mMobilestr.length()-1))){
	                	mMobilestr=mMobilestr.substring(0,mMobilestr.length()-1);
	                }
	        		String[] tTmMobilestr = mMobilestr.split(",");
	        		cmppSubmit.setDestTermIdCount((byte)tTmMobilestr.length);
	        		
	        		//destTermId
	        		byte[] destTermIdbyte=new byte[21*tTmMobilestr.length];
	        		int pos=0;
	        		for(int u=0;u<tTmMobilestr.length;u++){
	        			System.arraycopy(tTmMobilestr[u].getBytes(), 0, destTermIdbyte, pos, tTmMobilestr[u].getBytes().length);
	        			pos+=21;
	        		}
	        		cmppSubmit.setDestTermId(destTermIdbyte);

	        		//Reserve
	        		cmppSubmit.setReserve("");
	        		
	        		if (messageSize > 1){
	        			//长短信拆分
	        			//tpUdhi
	        			cmppSubmit.setTpUdhi((byte)1);
	        			
	        			byte[] contentHead = new byte[6];
	        			
	        			contentHead[0] = 5;
	      	          	contentHead[1] = 0;
	      	          	contentHead[2] = 3;
	      	          	
	      	          	contentHead[3] = FormatUtil.intToByte(contentSequence);
	      	          	contentHead[4] = FormatUtil.intToByte(flag ? messageSize - 1 : messageSize);
	      	          	contentHead[5] = FormatUtil.intToByte(messageIndex);
	      	          	
	      	          	byte[] perBytes = new byte[6 + messageLen];
	      	          	System.arraycopy(contentHead, 0, perBytes, 0, 6);
	      	          	System.arraycopy(bMessageContent, 0, perBytes, 6, messageLen);
	      	          	
	      	          	//msgFmt
	      	          	cmppSubmit.setMsgFmt((byte)Integer.parseInt(confighead.getUcs().trim()));
	      	          	//msgLen
	      	          	cmppSubmit.setMsgLen(FormatUtil.intToByte(perBytes.length));
	      	          	//msgContent
	      	          	cmppSubmit.setMsgContent(perBytes);
	      	          	//pkTotal
	      	          	cmppSubmit.setPkTotal((byte)(flag ? messageSize - 1 : messageSize));
	      	          	//pkNumber
	      	          	cmppSubmit.setPkNumber((byte)messageIndex);
	        		}else{
	        			//短-短信拆分
	        			//tpUdhi
	        			cmppSubmit.setTpUdhi((byte)0);
	        			byte[] bContent = inSubmit.getContent().getBytes("UnicodeBigUnmarked");
	        			//msgFmt
	      	          	cmppSubmit.setMsgFmt((byte)Integer.parseInt(confighead.getUcs().trim()));
	      	          	//msgLen
	      	          	cmppSubmit.setMsgLen((byte)bContent.length);
	      	          	//msgContent
	      	          	cmppSubmit.setMsgContent(bContent);
	      	          	//pkTotal
	      	          	cmppSubmit.setPkTotal((byte)1);
	      	          	//pkNumber
	      	          	cmppSubmit.setPkNumber((byte)1);
	        		}
	        		
	        		mMobilestr=""; //清空
	        		submitList.add(cmppSubmit);
	        		logger.info("");
	        	}
	        }
	        
	        cur += messageLen;
	        nowMessageSize--;
	        messageIndex++;
	    }
	    
	    return (CmppSubmit[])submitList.toArray(new CmppSubmit[0]);
	}
	
	public static byte[] cmpp_send_submit(CmppHeader cmppHeader,CmppSubmit cmppSubmit) throws UnsupportedEncodingException{
		byte[] buffer=new byte[1024];
		byte[] bytes=new byte[4];
		
		int pos=0;
		//组织报文结构
		//Total_Length
		//Command_Id
		cmppHeader.setCommand_Id(CmppConstant.CMD_SUBMIT);
		//Sequence_Id
		cmppHeader.setSequence_Id(FormatUtil.getSequence());
		
		pos+=4;
		bytes=new byte[4];
		bytes=FormatUtil.intToBytes4(cmppHeader.getCommand_Id());
		System.arraycopy(bytes, 0, buffer, pos, 4);
		pos+=4;
		
		bytes=new byte[4];
		bytes=FormatUtil.intToBytes4(cmppHeader.getSequence_Id());
		System.arraycopy(bytes, 0, buffer, pos, 4);
		pos+=4;
		
		//报文体 组织
		//msgId
		pos+=8;
		
		//pkTotal
		buffer[pos]=cmppSubmit.getPkTotal();
		pos+=1;
		//logger.info("cmppSubmit.getPkTotal()="+cmppSubmit.getPkTotal());
		
		//pkNumber
		buffer[pos]=cmppSubmit.getPkNumber();
		pos+=1;
		//logger.info("cmppSubmit.getPkNumber()="+cmppSubmit.getPkNumber());
		
		//needReport
		buffer[pos]=cmppSubmit.getNeedReport();
		pos+=1;
		
		//priority
		buffer[pos]=cmppSubmit.getPriority();
		pos+=1;
		
		//serviceId
		System.arraycopy(cmppSubmit.getServiceId().getBytes(), 0, buffer, pos, cmppSubmit.getServiceId().length());
		pos+=10;
		//logger.info("cmppSubmit.getServiceId()="+cmppSubmit.getServiceId());
		
		//feeUserType
		buffer[pos]=cmppSubmit.getFeeUserType();
		pos+=1;
		
		//feeTermId
		System.arraycopy(cmppSubmit.getFeeTermId().getBytes(), 0, buffer, pos, cmppSubmit.getFeeTermId().length());
		pos+=21;
		
		//tpPid
		buffer[pos]=cmppSubmit.getTpPid();
		pos+=1;
		
		//tpUdhi
		buffer[pos]=cmppSubmit.getTpUdhi();
		pos+=1;
		
		//msgFmt
		buffer[pos]=cmppSubmit.getMsgFmt();
		pos+=1;
		logger.info("cmppSubmit.getMsgFmt()="+cmppSubmit.getMsgFmt());
		
		//msgSrc
		System.arraycopy(cmppSubmit.getMsgSrc().getBytes(), 0, buffer, pos, cmppSubmit.getMsgSrc().length());
		pos+=6;
		//logger.info("cmppSubmit.getMsgSrc()="+cmppSubmit.getMsgSrc());
		
		//feeType
		System.arraycopy(cmppSubmit.getFeeType().getBytes(), 0, buffer, pos, cmppSubmit.getFeeType().length());
		pos+=2;
		
		//feeCode
		System.arraycopy(cmppSubmit.getFeeCode().getBytes(), 0, buffer, pos, cmppSubmit.getFeeCode().length());
		pos+=6;
		
		//validTime
		System.arraycopy(cmppSubmit.getValidTime().getBytes(), 0, buffer, pos, cmppSubmit.getValidTime().length());
		pos+=17;
		
		//atTime
		System.arraycopy(cmppSubmit.getAtTime().getBytes(), 0, buffer, pos, cmppSubmit.getAtTime().length());
		pos+=17;
		
		//srcId
		System.arraycopy(cmppSubmit.getSrcId().getBytes(), 0, buffer, pos, cmppSubmit.getSrcId().length());
		pos+=21;
		
		//destTermIdCount
		buffer[pos]=cmppSubmit.getDestTermIdCount();
		pos+=1;
		//logger.info("cmppSubmit.getDestTermIdCount()="+cmppSubmit.getDestTermIdCount()+" ,pos="+pos);
		
		//destTermId
		System.arraycopy(cmppSubmit.getDestTermId(), 0, buffer, pos, cmppSubmit.getDestTermId().length);
		pos+= 21*(int)cmppSubmit.getDestTermIdCount();
		
		//msgLen
		buffer[pos]=cmppSubmit.getMsgLen();
		pos+=1;
		//logger.info("cmppSubmit.getMsgLen()="+FormatUtil.byteToInt(cmppSubmit.getMsgLen())+",pos="+pos);
		
		//msgContent
		System.arraycopy(cmppSubmit.getMsgContent(), 0, buffer, pos, cmppSubmit.getMsgContent().length);
		pos+= FormatUtil.byteToInt(cmppSubmit.getMsgLen());
		
		
//		String mContent=new String(cmppSubmit.getMsgContent(),"UTF-16BE");
		//logger.info("cmppSubmit.getMsgContent()="+mContent);
		
		//Reserve
		System.arraycopy(cmppSubmit.getReserve().getBytes(), 0, buffer, pos, cmppSubmit.getReserve().length());
		pos+=8;
		
		//Total_Length
		//logger.info("cmppHeader.setTotal_Length="+pos);
		cmppHeader.setTotal_Length(pos);
		bytes=new byte[4];
		bytes=FormatUtil.intToBytes4(cmppHeader.getTotal_Length());
		System.arraycopy(bytes, 0, buffer, 0, 4);
		
		//logger.info("pos:"+pos);
		byte[] buffersend=new byte[pos];
		System.arraycopy(buffer, 0, buffersend, 0, pos);
		
		return buffersend;
	}
	
}
