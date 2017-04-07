package com.thalesgroup.scadagen.whmi.config.configenv.server.uigeneric;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.thalesgroup.scadagen.whmi.config.configenv.client.uigeneric.UIGenericService;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class UIGenericServiceImpl extends RemoteServiceServlet implements UIGenericService {

	private Logger logger					= LoggerFactory.getLogger(UIGenericServiceImpl.class.getName());
	
	final String componse = "componse";
	final String request = "request";
	final String response = "response";
	final String data = "data";
	
	@Override
	public JSONObject execute(JSONObject jsonObject) {
		logger.debug("Begin");
		
//		UIOpm_i uiOpm_i = UIOpmSCADAgen.getInstance();

//		String ip = getThreadLocalRequest().getRemoteAddr();
//		
//		JSONObject response = new JSONObject();
//		response.put(data, new JSONString(ip));
		
		String host = getThreadLocalRequest().getRemoteHost();
		
		JSONObject response = new JSONObject();
		response.put(data, new JSONString(host));
		
		logger.debug("End");
		return response;
	}

}
