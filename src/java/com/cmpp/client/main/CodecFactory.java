package com.cmpp.client.main;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class CodecFactory implements ProtocolCodecFactory{
	
	private CmppDecoder decoder=new CmppDecoder();
	private CmppEncoder encoder=new CmppEncoder();
	@Override
	public ProtocolDecoder getDecoder(IoSession arg0) throws Exception{
		return decoder;
	}
	@Override
	public ProtocolEncoder getEncoder(IoSession arg0) throws Exception{
		return encoder;
	}
}
