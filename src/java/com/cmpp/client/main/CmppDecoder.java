package com.cmpp.client.main;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import com.cmpp.util.FormatUtil;

public class CmppDecoder extends CumulativeProtocolDecoder{
//	private static Logger logger = Logger.getLogger(CmppDecoder.class);
	@Override
	protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput output) throws Exception{
		
		//logger.info("in.remaining : "+in.remaining());
		
		if(in.remaining() > 4){
			byte [] sizeBytes = new byte[4];
			
			in.mark();
			
			in.get(sizeBytes);//读取4字节 //如果当前读取的缓冲区中的数据，小于响应数据的总长度，则让缓冲区继续累积数据，一直累积到数据接收完成，即缓冲区中数据的长度等于响应数据的总长度时，则开始解析.
			
			int length=FormatUtil.bytes4ToInt(sizeBytes);
			
			in.reset();
			
			//logger.info("length=" + length + ",in.limit=" + in.limit()+",in.remaining="+in.remaining());
			
			if (length > in.remaining()){ //如果消息内容不够，则重置，相当于不读取size 
				//logger.info("length="+length+" >in.remaining: "+ in.remaining());
				
				return false; //父类接收新数据，以拼凑成完整数据
			}else{
				byte[] bytesize = new byte[length];
				in.get(bytesize);
				output.write(bytesize);
				
				if(in.remaining()>0){
					//logger.info("length="+length+" tt---in.remaining: "+ in.remaining());
					
					return true;
				}
			}
		}
		
        return false; //处理成功，让父类进行接收下个包
	}
}
