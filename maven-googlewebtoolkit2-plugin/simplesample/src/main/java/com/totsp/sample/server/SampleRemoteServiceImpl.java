package com.totsp.sample.server;

import org.apache.log4j.Logger;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.totsp.sample.client.SampleRemoteService;

import org.apache.log4j.Logger;

public class SampleRemoteServiceImpl extends RemoteServiceServlet implements
		SampleRemoteService {

   private static final Logger LOG = Logger.getLogger(SampleRemoteServiceImpl.class);
   
	public String doComplimentMe() {
	   LOG.debug("Some logging stuff - where does it end up! (see src/main/resources/log4j.xml)");
		return "this is a compliment from the server side - you don't look TOO bad today";
	}	
}
