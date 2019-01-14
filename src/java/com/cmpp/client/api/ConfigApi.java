package com.cmpp.client.api;

import org.dom4j.Document;

import com.cmpp.client.common.Confighead;
import com.cmpp.util.Dom4jXmlUtils;


public class ConfigApi {
	public static Confighead ConfigGet(String configuration) throws Exception{
		Confighead confighead=new Confighead();
		
		Document rootDocument = Dom4jXmlUtils.load(configuration);
		org.dom4j.Element rootElement = rootDocument.getRootElement();
		
		String bindip=Dom4jXmlUtils.getChildText(rootElement, "bindip");
		confighead.setBindip(bindip);
		
		String bindport=Dom4jXmlUtils.getChildText(rootElement, "bindport");
		confighead.setBindport(bindport);
		
		String clientid=Dom4jXmlUtils.getChildText(rootElement, "clientid");
		confighead.setClientid(clientid);
		
		String clientpwd=Dom4jXmlUtils.getChildText(rootElement, "clientpwd");
		confighead.setClientpwd(clientpwd);
		
		String corpid=Dom4jXmlUtils.getChildText(rootElement, "corpid");
		confighead.setCorpid(corpid);
		
		String spport=Dom4jXmlUtils.getChildText(rootElement, "spport");
		confighead.setSpport(spport);
		
		String monitormail=Dom4jXmlUtils.getChildText(rootElement, "monitormail");
		confighead.setMonitormail(monitormail);
		
		String warningmail=Dom4jXmlUtils.getChildText(rootElement, "warningmail");
		confighead.setWarningmail(warningmail);
		
		String isreport=Dom4jXmlUtils.getChildText(rootElement, "isreport");
		confighead.setIsreport(isreport);
		
		String serviceid=Dom4jXmlUtils.getChildText(rootElement, "serviceid");
		confighead.setServiceid(serviceid);
		
		String queue=Dom4jXmlUtils.getChildText(rootElement, "queue");
		confighead.setQueue(queue);
		
		String queueport=Dom4jXmlUtils.getChildText(rootElement, "queueport");
		confighead.setQueueport(queueport);
		
		String queuename=Dom4jXmlUtils.getChildText(rootElement, "queuename");
		confighead.setQueuename(queuename);
		
		String mobilenum=Dom4jXmlUtils.getChildText(rootElement, "mobilenum");
		confighead.setMobilenum(mobilenum);
		
		String ucs=Dom4jXmlUtils.getChildText(rootElement, "ucs");
		confighead.setUcs(ucs);
		
		String maxnum=Dom4jXmlUtils.getChildText(rootElement, "maxnum");
		confighead.setMaxnum(maxnum);
		
		String upurlzh=Dom4jXmlUtils.getChildText(rootElement, "upurlzh");
		confighead.setUpurlzh(upurlzh);
		
		String deliverurlzh=Dom4jXmlUtils.getChildText(rootElement, "deliverurlzh");
		confighead.setDeliverurlzh(deliverurlzh);
		
		String queuezh=Dom4jXmlUtils.getChildText(rootElement, "queuezh");
		confighead.setQueuezh(queuezh);
		
		String queueportzh=Dom4jXmlUtils.getChildText(rootElement, "queueportzh");
		confighead.setQueueportzh(queueportzh);
		
		String upurlhy=Dom4jXmlUtils.getChildText(rootElement, "upurlhy");
		confighead.setUpurlhy(upurlhy);
		
		String deliverurlhy=Dom4jXmlUtils.getChildText(rootElement, "deliverurlhy");
		confighead.setDeliverurlhy(deliverurlhy);
		
		String queuehy=Dom4jXmlUtils.getChildText(rootElement, "queuehy");
		confighead.setQueuehy(queuehy);
		
		String queueporthy=Dom4jXmlUtils.getChildText(rootElement, "queueporthy");
		confighead.setQueueporthy(queueporthy);
		
		String queueyzm=Dom4jXmlUtils.getChildText(rootElement, "queueyzm");
		confighead.setQueueyzm(queueyzm);
		
		String queueportyzm=Dom4jXmlUtils.getChildText(rootElement, "queueportyzm");
		confighead.setQueueportyzm(queueportyzm);
		
		String upurlaly=Dom4jXmlUtils.getChildText(rootElement, "upurlaly");
		confighead.setUpurlaly(upurlaly);
		
		String deliverurlaly=Dom4jXmlUtils.getChildText(rootElement, "deliverurlaly");
		confighead.setDeliverurlaly(deliverurlaly);
		
		String queuealy=Dom4jXmlUtils.getChildText(rootElement, "queueyaly");
		confighead.setQueuealy(queuealy);
		
		String queueportaly=Dom4jXmlUtils.getChildText(rootElement, "queueportaly");
		confighead.setQueueportaly(queueportaly);
		
		String queuezhkey=Dom4jXmlUtils.getChildText(rootElement, "queuezhkey");
		confighead.setQueuezhkey(queuezhkey);
		
		String queuehykey=Dom4jXmlUtils.getChildText(rootElement, "queuehykey");
		confighead.setQueuehykey(queuehykey);
		
		String queueyzmkey=Dom4jXmlUtils.getChildText(rootElement, "queueyzmkey");
		confighead.setQueueyzmkey(queueyzmkey);
		
		String queuealykey=Dom4jXmlUtils.getChildText(rootElement, "queuealykey");
		confighead.setQueuealykey(queuealykey);
		
		String islocal=Dom4jXmlUtils.getChildText(rootElement, "islocal");
		confighead.setIslocal(islocal);
		
		String ismonitor=Dom4jXmlUtils.getChildText(rootElement, "ismonitor");
		confighead.setIsmonitor(ismonitor);
		
		String monitortime=Dom4jXmlUtils.getChildText(rootElement, "monitortime");
		confighead.setMonitortime(monitortime);
		
		String monitorfailure=Dom4jXmlUtils.getChildText(rootElement, "monitorfailure");
		confighead.setMonitorfailure(monitorfailure);
		
		String localip=Dom4jXmlUtils.getChildText(rootElement, "localip");
		confighead.setLocalip(localip);
		
		String queueyaly2=Dom4jXmlUtils.getChildText(rootElement, "queueyaly2");
		confighead.setQueueyaly2(queueyaly2);
		
		String flow=Dom4jXmlUtils.getChildText(rootElement, "flow");
		confighead.setFlow(flow);
		
		String connetnum=Dom4jXmlUtils.getChildText(rootElement, "connetnum");
		confighead.setConnetnum(connetnum);
		
		String channelbak=Dom4jXmlUtils.getChildText(rootElement, "channelbak");
		confighead.setChannelbak(channelbak);
		
		String vxreport=Dom4jXmlUtils.getChildText(rootElement, "vxreport");
		confighead.setVxreport(vxreport);
		
		String vxreply=Dom4jXmlUtils.getChildText(rootElement, "vxreply");
		confighead.setVxreply(vxreply);
		
		//redis参数
		String redis_ip=Dom4jXmlUtils.getChildText(rootElement, "redis_ip");
		System.setProperty("redis.ip", redis_ip);
		String redis_port=Dom4jXmlUtils.getChildText(rootElement, "redis_port");
		System.setProperty("redis.port", redis_port);
		String redis_password=Dom4jXmlUtils.getChildText(rootElement, "redis_password");
		System.setProperty("redis.password", redis_password);
		String redis_jedispooltimeout=Dom4jXmlUtils.getChildText(rootElement, "redis_jedispooltimeout");
		System.setProperty("redis.jedispooltimeout", redis_jedispooltimeout);
		String redis_maxtotal=Dom4jXmlUtils.getChildText(rootElement, "redis_maxtotal");
		System.setProperty("redis.maxtotal", redis_maxtotal);
		String redis_maxwaitmillis=Dom4jXmlUtils.getChildText(rootElement, "redis_maxwaitmillis");
		System.setProperty("redis.maxwaitmillis", redis_maxwaitmillis);
		String redis_minevictableidletimemillis=Dom4jXmlUtils.getChildText(rootElement, "redis_minevictableidletimemillis");
		System.setProperty("redis.minevictableidletimemillis", redis_minevictableidletimemillis);
		String redis_maxidle=Dom4jXmlUtils.getChildText(rootElement, "redis_maxidle");
		System.setProperty("redis.maxidle", redis_maxidle);
		String redis_numtestsperevictionrun=Dom4jXmlUtils.getChildText(rootElement, "redis_numtestsperevictionrun");
		System.setProperty("redis.numtestsperevictionrun", redis_numtestsperevictionrun);
		String redis_softminevictableidletimemillis=Dom4jXmlUtils.getChildText(rootElement, "redis_softminevictableidletimemillis");
		System.setProperty("redis.softminevictableidletimemillis", redis_softminevictableidletimemillis);
		String redis_timebetweenevictionrunsmillis=Dom4jXmlUtils.getChildText(rootElement, "redis_timebetweenevictionrunsmillis");
		System.setProperty("redis.timebetweenevictionrunsmillis", redis_timebetweenevictionrunsmillis);
		
		return confighead;
	}
}
