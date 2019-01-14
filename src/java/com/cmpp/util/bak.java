package com.cmpp.util;

public class bak {

	public static void main(String[] args) {
		
		//TODO 发送条数插入监控 缓存块
		/*MonitorCount monitorCount=new MonitorCount();
		
		String monitorkey="monitor";
		
		Element monitorElement=null;
		synchronized(this.monitorCache){
			monitorElement = this.monitorCache.get(monitorkey);
    	}
		
		if(monitorElement==null){
			monitorCount.setCount(0);
			monitorCount.setCountsucc(1);
			monitorCount.setDelivercount(0);
			monitorCount.setDelivercountsucc(0);
			
			synchronized(this.monitorCache){
				Element monitorElementput=new Element(monitorkey,monitorCount);
				this.monitorCache.put(monitorElementput);
			}
		}else{
			monitorCount=(MonitorCount)monitorElement.getValue();
			
			int countsucc=monitorCount.getCountsucc()+1;
			monitorCount.setCountsucc(countsucc);
			
			synchronized(this.monitorCache){
				Element monitorElementput=new Element(monitorkey,monitorCount);
				this.monitorCache.put(monitorElementput);
			}
		}*/
		
		/**
		 * 功能：TODO 阿里云平台 回复report的推送处理
		 * 开发:蔡新鹏
		 * 时间：2017-02-18 15：04
		 * 地点：上海
		 */
		/*public static void pt_aly_sendreport(IncmppDeliver incmppDeliver,Confighead confighead,Ptport ptport){
			try{
				Document deliverDocument = DocumentHelper.createDocument();
		        deliverDocument.setXMLEncoding("utf-8");
		        Element rootElement = deliverDocument.addElement("SYNCPacket");
		        Element midElement = rootElement.addElement("mid");
		        midElement.setText(String.valueOf(incmppDeliver.getSmscsequence()));
		        Element mobileElement = rootElement.addElement("mobile");
		        String phone = incmppDeliver.getDestterminalid().trim();
		        if (phone != null) {
		            Pattern p = Pattern.compile("^(\\+86|86|0086|\\ 86)");
		            Matcher m = p.matcher(phone);
		            phone = m.replaceAll("");
		        }
		        mobileElement.setText(phone);
		        Element portElement = rootElement.addElement("port");//大号
		        portElement.setText(incmppDeliver.getStat().trim());
		        
		        Element msgElement = rootElement.addElement("msg");
		        msgElement.setText(incmppDeliver.getContent());
		        
		        Element typeElement = rootElement.addElement("type");
		        typeElement.setText("1");
		        
		        Element channelElement = rootElement.addElement("channel");
		        channelElement.setText("1");
		        
		        Element reservedElement = rootElement.addElement("reserved");
		        
		        if(confighead.getQueuename().length()!=4 && !"98".equals(confighead.getQueuename()) && !"96".equals(confighead.getQueuename()) ){
		        	if(ptport.getPort().trim().startsWith("6") || ptport.getPort().trim().startsWith("8")){
			        	reservedElement.setText(ptport.getPort().trim().substring(1));//存放小号
			        }
		        }else{
		        	reservedElement.setText(ptport.getPort().trim());//存放小号
		        }
		        
		        String upurlaly=confighead.getUpurlaly().trim();

		        HttpClientUtil.fetchURLviaPost(upurlaly, deliverDocument);
		        
		        logger.info("[POST-ALY-REPORT][platform:"+ptport.getPlatform()+"]["+phone+"][port:"+ptport.getPort()+"][content:"+incmppDeliver.getContent()+"]["+"[sequence:"+incmppDeliver.getSmscsequence()+"]");
		        
			}catch (Exception e){
		    	  System.out.println("aly-UtilDeliverRes:"+e.getMessage());
		    	  e.printStackTrace();    	  
		    }
		}*/
		
		/**
		 * 功能：TODO 综合平台 回复report的推送处理
		 * 开发:蔡新鹏
		 * 时间：2017-02-18 15：04
		 * 地点：上海
		 */
		/*public static void pt_zh_sendreport(IncmppDeliver incmppDeliver,Confighead confighead,Ptport ptport){
			try {
				Document deliverDocument = DocumentHelper.createDocument();
		        deliverDocument.setXMLEncoding("utf-8");
		        Element rootElement = deliverDocument.addElement("SYNCPacket");
		        Element midElement = rootElement.addElement("mid");
		        midElement.setText(String.valueOf(incmppDeliver.getSmscsequence()));
		        Element mobileElement = rootElement.addElement("mobile");
		        String phone = incmppDeliver.getDestterminalid().trim();
		        if (phone != null) {
		            Pattern p = Pattern.compile("^(\\+86|86|0086|\\ 86)");
		            Matcher m = p.matcher(phone);
		            phone = m.replaceAll("");
		        }
		        mobileElement.setText(phone);
		        Element portElement = rootElement.addElement("port");
		        portElement.setText(incmppDeliver.getStat().trim());
		        
		        Element msgElement = rootElement.addElement("msg");
		        msgElement.setText(incmppDeliver.getContent());
		        Element typeElement = rootElement.addElement("type");
		        typeElement.setText("1");
		        Element channelElement = rootElement.addElement("channel");
		        channelElement.setText("1");
		        Element reservedElement = rootElement.addElement("reserved");
		        if(confighead.getQueuename().equals("98")){
		        	reservedElement.setText(ptport.getPort().trim());//存放小号
		        }else{
		        	reservedElement.setText(ptport.getPort().trim().substring(1));//存放小号
		        }
		        
		        String upurlzh=confighead.getUpurlzh().trim();
		        
		        HttpClientUtil.fetchURLviaPost(upurlzh, deliverDocument);
		        
		        logger.info("[POST-ZH-REPORT][platform:"+ptport.getPlatform()+"]["+phone+"][port:"+ptport.getPort()+"][content:"+incmppDeliver.getContent()+"]["+"[sequence:"+incmppDeliver.getSmscsequence()+"]");
		        
			}catch (Exception e){
		    	  System.out.println("zh-UtilDeliverRes:"+e.getMessage());
		    	  e.printStackTrace();    	  
		    }
		}*/
		
		/**
		 * 功能：TODO 验证码平台 回执deliver的推送处理
		 * 开发:蔡新鹏
		 * 时间：2017-02-18 15：04
		 * 地点：上海
		 */
		/*public static void pt_yzm_senddeliver(IncmppSubmit incmppSubmit,IncmppDeliver incmppDeliver,Confighead confighead){
			PtReport bean=new PtReport();
			
			//入验证码  -总队列
			String queueyzmkey=confighead.getQueueyzmkey().trim();
			String queueyzm=confighead.getQueueyzm().trim();
			String queueportyzm=confighead.getQueueportyzm().trim();
			
			//组织bean
			bean.setMessageid(incmppSubmit.getCpmid().trim());//平台msgid
			bean.setMobile(incmppDeliver.getDestterminalid().trim());//号码
			if("DELIVRD".equals(incmppDeliver.getStat().trim())){
				bean.setRemark("发送失败");
				bean.setState("0");
				bean.setSubmitstate(incmppDeliver.getStat().trim());
			}else{
				bean.setRemark("发送成功");
				bean.setState("1");
				bean.setSubmitstate("DELIVRD");
			}
			bean.setReportTime(TimeUtil.mNowTime());
			
			//入总队列
			String resvalue=httpSmsClient.putT(queueyzmkey, JSONObject.fromObject(bean).toString(), queueyzm, queueportyzm);
			if(resvalue.equals("HTTPSQS_PUT_OK")){
				logger.info("[SP->YZM-QUEUE-OK][queuerec:"+resvalue+"]["+incmppSubmit.getP_num()+"]["+bean.getMessageid()+","+bean.getMobile()+","+bean.getState()+","+bean.getSubmitstate()+"]");
		    }else{
		    	logger.info("[SP->YZM-QUEUE-ERROR][queuerec:"+resvalue+"]["+incmppSubmit.getP_num()+"]["+bean.getMessageid()+","+bean.getMobile()+","+bean.getState()+","+bean.getSubmitstate()+"]");
			}
			
			//入用户队列
			String mid= incmppSubmit.getMid().trim();
			if(mid!=null && !"".equals(mid)){
				String[] userid=mid.split(",");
				String userqueuekey=userid[0].trim() + "_"+queueyzmkey;
				
				boolean b = false;
				if(!incmppDeliver.getStat().startsWith("ZT")){
					b = true;
				}else if("ZT:013".equals(incmppDeliver.getStat().trim())){
					b = true;
				}
				if(b){
					String sult = httpSmsClient.putT(userqueuekey,JSONObject.fromObject(bean).toString(),queueyzm, queueportyzm);
					if(resvalue.equals("HTTPSQS_PUT_OK")){
						logger.info("[SP->YZM-USERQUEUE-OK][queuerec:"+sult+"]["+incmppSubmit.getP_num()+"][mid:"+mid+"]["+bean.getMessageid()+","+bean.getMobile()+","+bean.getState()+","+bean.getSubmitstate()+"]");
				    }else{
				    	logger.info("[SP->YZM-USERQUEUE-ERROR][queuerec:"+sult+"]["+incmppSubmit.getP_num()+"][mid:"+mid+"]["+bean.getMessageid()+","+bean.getMobile()+","+bean.getState()+","+bean.getSubmitstate()+"]");
					}
				}
			}
		}*/
		
		/**
		 * 功能：TODO 行业平台 回执deliver的推送处理
		 * 开发:蔡新鹏
		 * 时间：2017-02-18 15：04
		 * 地点：上海
		 */
		/*public static void pt_hy_senddeliver(IncmppSubmit incmppSubmit,IncmppDeliver incmppDeliver,Confighead confighead){
			PtReport bean=new PtReport();
			
			//入行业  -总队列
			String queuehykey=confighead.getQueuehykey().trim();
			String queuehy=confighead.getQueuehy().trim();
			String queueporthy=confighead.getQueueporthy().trim();
			
			//组织bean
			bean.setMessageid(incmppSubmit.getCpmid().trim());//平台msgid
			bean.setMobile(incmppDeliver.getDestterminalid().trim());//号码
			if(!"DELIVRD".equals(incmppDeliver.getStat().trim())){
				bean.setRemark("发送失败");
				bean.setState("0");
				bean.setSubmitstate(incmppDeliver.getStat().trim());
			}else{
				bean.setRemark("发送成功");
				bean.setState("1");
				bean.setSubmitstate("DELIVRD");
			}
			bean.setReportTime(TimeUtil.mNowTime());
			
			//入总队列
			String resvalue=httpSmsClient.putT(queuehykey, JSONObject.fromObject(bean).toString(), queuehy, queueporthy);
			if(resvalue.equals("HTTPSQS_PUT_OK")){
				logger.info("[SP->HY-QUEUE-OK][queuerec:"+resvalue+"]["+incmppSubmit.getP_num()+"]["+bean.getMessageid()+","+bean.getMobile()+","+bean.getState()+","+bean.getSubmitstate()+"]");
		    }else{
		    	logger.info("[SP->HY-QUEUE-ERROR][queuerec:"+resvalue+"]["+incmppSubmit.getP_num()+"]["+bean.getMessageid()+","+bean.getMobile()+","+bean.getState()+","+bean.getSubmitstate()+"]");
			}

			boolean b = false;
			if(!incmppDeliver.getStat().startsWith("ZT")){
				b = true;
			}else if("ZT:013".equals(incmppDeliver.getStat().trim())){
				b = true;
			}
			if(b){
				//入用户队列
				String mid= incmppSubmit.getMid().trim();
				String[] userid=mid.split(",");
				String userqueuekey=userid[0].trim() + "_"+queuehykey;
				
				String sult = httpSmsClient.putT(userqueuekey,JSONObject.fromObject(bean).toString(),queuehy, queueporthy);
				if(resvalue.equals("HTTPSQS_PUT_OK")){
					logger.info("[SP->HY-USERQUEUE-OK][queuerec:"+sult+"]["+incmppSubmit.getP_num()+"][mid:"+mid+"]["+bean.getMessageid()+","+bean.getMobile()+","+bean.getState()+","+bean.getSubmitstate()+"]");
			    }else{
			    	logger.info("[SP->HY-USERQUEUE-ERROR][queuerec:"+sult+"]["+incmppSubmit.getP_num()+"][mid:"+mid+"]["+bean.getMessageid()+","+bean.getMobile()+","+bean.getState()+","+bean.getSubmitstate()+"]");
				}
			}
		}*/
		
		// TODO 下发加入监控模块 长短信
		//发送条数插入监控 缓存块
		/*MonitorCount monitorCount=new MonitorCount();
		
		Element monitorElement=null;
		synchronized(this.monitorCache){
			monitorElement = this.monitorCache.get(monitorkey);
    	}
		
		if(monitorElement==null){
			monitorCount.setCount(1);
			monitorCount.setCountsucc(0);
			monitorCount.setDelivercount(0);
			monitorCount.setDelivercountsucc(0);
			
			synchronized(this.monitorCache){
				Element monitorElementput=new Element(monitorkey,monitorCount);
				this.monitorCache.put(monitorElementput);
			}
		}else{
			monitorCount=(MonitorCount)monitorElement.getValue();
			
			int count=monitorCount.getCount()+1;
			monitorCount.setCount(count);
			
			synchronized(this.monitorCache){
				Element monitorElementput=new Element(monitorkey,monitorCount);
				this.monitorCache.put(monitorElementput);
			}
		}*/
		
		// TODO 下发加入监控模块 短短信
		//发送条数插入监控 缓存块
		/*MonitorCount monitorCount=new MonitorCount();
		
		Element monitorElement=null;
		synchronized(this.monitorCache){
			monitorElement = this.monitorCache.get(monitorkey);
    	}
		
		if(monitorElement==null){
			monitorCount.setCount(1);
			monitorCount.setCountsucc(0);
			monitorCount.setDelivercount(0);
			monitorCount.setDelivercountsucc(0);
			
			synchronized(this.monitorCache){
				Element monitorElementput=new Element(monitorkey,monitorCount);
				this.monitorCache.put(monitorElementput);
			}
		}else{
			monitorCount=(MonitorCount)monitorElement.getValue();
			
			int count=monitorCount.getCount()+1;
			monitorCount.setCount(count);
			
			synchronized(this.monitorCache){
				Element monitorElementput=new Element(monitorkey,monitorCount);
				this.monitorCache.put(monitorElementput);
			}
		}*/
		
		// TODO 状态报告回执监控
		//回执条数插入监控 缓存块
		/*MonitorCount monitorCount=new MonitorCount();
		
		String monitorkey="monitor";
		
		Element monitorElement=null;
		synchronized(this.monitorCache){
			monitorElement = this.monitorCache.get(monitorkey);
    	}
		
		if(monitorElement==null){
			monitorCount.setCount(0);
			monitorCount.setCountsucc(0);
			monitorCount.setDelivercount(1);
			
			if("DELIVRD".equals(bean.getStat().trim())){
				monitorCount.setDelivercountsucc(1);
			}else{
				monitorCount.setDelivercountsucc(0);
			}

			synchronized(this.monitorCache){
				Element monitorElementput=new Element(monitorkey,monitorCount);
				this.monitorCache.put(monitorElementput);
			}
		}else{
			monitorCount=(MonitorCount)monitorElement.getValue();
			
			int delivercount=monitorCount.getDelivercount()+1;
			monitorCount.setDelivercount(delivercount);
			
			int delivercountsucc=0;
			if("DELIVRD".equals(bean.getStat().trim())){
				delivercountsucc=monitorCount.getDelivercountsucc()+1;
			}else{
				delivercountsucc=monitorCount.getDelivercountsucc();
			}
			monitorCount.setDelivercountsucc(delivercountsucc);
			
			synchronized(this.monitorCache){
				Element monitorElementput=new Element(monitorkey,monitorCount);
				this.monitorCache.put(monitorElementput);
			}
		}*/
		
		
		//TODO 状态报告-缓存中找不到时异常推送
		/*else if("NT".equals(pt.trim())){
		String msg=TimeUtil.getNowTime("yyyyMMddHHmmssSSS888");
		String mobile=bean.getDestterminalid().trim();
		String url ="http://139.224.218.50:8848/bjkjcxreport.do?report=" + msg+","+mobile+","+bean.getStat().trim()+","+TimeUtil.getNowTime("yyyyMMdd")+"&channel_id:"+confighead.getQueuename();
		String result=HttpClientUtil.sendGet(url);
		logger.info("[HTTP-ALY-OK][queuerec:"+result+"]["+msg+","+mobile+","+bean.getStat().trim()+"]");
		}*/
		
		//TODO 天润预警入缓存-发送-短短信
		/*if("ALY".equals(bean.getPlatform())){
			String[] userids = {"2875","1775","4226","1772"};
			
			for(int i = 0 ; i < userids.length ; i ++ ){
				
				if(bean.getMid().startsWith(userids[i])){
					
					String key=userids[i]+"_TRMonitor";
					//发送条数插入监控 缓存块
					MonitorCount TRmonitor=new MonitorCount();
					
					Element TRElement=null;
					synchronized(this.monitorCache){
						TRElement = this.monitorCache.get(key);
		        	}
					
					if(TRElement==null){
						TRmonitor.setCount(1);
						TRmonitor.setDelivercount(0);
						TRmonitor.setDelivercountsucc(0);
						TRmonitor.setDelivercountfail(0);
						
						synchronized(this.monitorCache){
							Element monitorElementput=new Element(key,TRmonitor);
							this.monitorCache.put(monitorElementput);
						}
					}else{
						TRmonitor=(MonitorCount)TRElement.getValue();
						
						int count=TRmonitor.getCount()+1;
						TRmonitor.setCount(count);
						
						synchronized(this.monitorCache){
							Element monitorElementput=new Element(key,TRmonitor);
							this.monitorCache.put(monitorElementput);
						}
					}
				}
			}
		}*/
		
		//TODO 天润预警入缓存-发送-长短信
		/*if("ALY".equals(bean.getPlatform())){
			String[] userids = {"2875","1775","4226","1772"};
			
			for(int j = 0 ; j < userids.length ; j ++ ){
				
				if(bean.getMid().startsWith(userids[j])){
					
					String key=userids[j]+"_TRMonitor";
					//发送条数插入监控 缓存块
					MonitorCount TRmonitor=new MonitorCount();
					
					Element TRElement=null;
					synchronized(this.monitorCache){
						TRElement = this.monitorCache.get(key);
		        	}
					
					if(TRElement==null){
						TRmonitor.setCount(1);
						TRmonitor.setDelivercount(0);
						TRmonitor.setDelivercountsucc(0);
						TRmonitor.setDelivercountfail(0);
						
						synchronized(this.monitorCache){
							Element monitorElementput=new Element(key,TRmonitor);
							this.monitorCache.put(monitorElementput);
						}
					}else{
						TRmonitor=(MonitorCount)TRElement.getValue();
						
						int count=TRmonitor.getCount()+1;
						TRmonitor.setCount(count);
						
						synchronized(this.monitorCache){
							Element monitorElementput=new Element(key,TRmonitor);
							this.monitorCache.put(monitorElementput);
						}
					}
				}
			}	
		}*/
		
		//TODO 天润预警入缓存-状态
		/*if("ALY".equals(incmppSubmit.getPlatform().trim())){
		
			String[] userids = {"2875","1775","4226","1772"};
			
			for(int j = 0 ; j < userids.length ; j ++ ){
				
				if(incmppSubmit.getMid().startsWith(userids[j])){
			
					MonitorCount TRCount=new MonitorCount();
					
					String key=userids[j] + "_TRMonitor";
					
					Element TRElement=null;
					synchronized(this.monitorCache){
						TRElement = this.monitorCache.get(key);
		        	}
					
					if(TRElement==null){
						TRCount.setCount(0);
						TRCount.setDelivercount(1);
						
						if("DELIVRD".equals(bean.getStat().trim())){
							TRCount.setDelivercountsucc(1);
							TRCount.setDelivercountfail(0);
						}else{
							TRCount.setDelivercountfail(1);
							TRCount.setDelivercountsucc(0);
						}

						synchronized(this.monitorCache){
							Element monitorElementput=new Element(key,TRCount);
							this.monitorCache.put(monitorElementput);
						}
					}else{
						TRCount=(MonitorCount)TRElement.getValue();
						
						int delivercount=TRCount.getDelivercount()+1;
						TRCount.setDelivercount(delivercount);
						
						int delivercountsucc=0;
						int delivercountfail=0;
						if("DELIVRD".equals(bean.getStat().trim())){
							delivercountsucc=TRCount.getDelivercountsucc()+1;
							delivercountfail=TRCount.getDelivercountfail();
						}else{
							delivercountfail=TRCount.getDelivercountfail()+1;
							delivercountsucc=TRCount.getDelivercountsucc();
						}
						TRCount.setDelivercountsucc(delivercountsucc);
						TRCount.setDelivercountfail(delivercountfail);
						
						synchronized(this.monitorCache){
							Element monitorElementput=new Element(key,TRCount);
							this.monitorCache.put(monitorElementput);
						}
					}
				}
			}
		}*/
	}
}
