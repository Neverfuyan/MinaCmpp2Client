package com.cmpp.client.main;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

public class CmppEncoder implements ProtocolEncoder{
	@Override
	public void dispose(IoSession session) throws Exception{
		// --
	}
	@Override
	public void encode(IoSession session, Object message, ProtocolEncoderOutput output) throws Exception{
		// --编码器
		//System.out.println("encode............");
		
		byte[] buffer=(byte[])message;
		//System.out.println("in to encode....编码器--buffer.length="+buffer.length);
		IoBuffer buf = IoBuffer.allocate(buffer.length, false);
		
		buf.setAutoExpand(true);
		buf.put(buffer);
		buf.flip();
		
		output.write(buf);
	}
}
