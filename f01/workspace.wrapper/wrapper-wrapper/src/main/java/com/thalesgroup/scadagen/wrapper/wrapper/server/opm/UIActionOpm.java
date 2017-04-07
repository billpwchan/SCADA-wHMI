package com.thalesgroup.scadagen.wrapper.wrapper.server.opm;

import javax.servlet.http.HttpServletRequest;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.thalesgroup.scadagen.whmi.config.configenv.server.uigeneric.UIGenericServiceImpl_i;
import com.thalesgroup.scadagen.whmi.config.configenv.server.uigeneric.factory.UIAction_i;
import com.thalesgroup.scadagen.whmi.config.configenv.server.uigeneric.util.JSONUtil;

public class UIActionOpm implements UIAction_i {
	
	public JSONObject execute(HttpServletRequest httpServletRequest, JSONObject request) {
		
		JSONObject response = null;
		
		JSONValue value1 = JSONUtil.getJSONValue(request, UIGenericServiceImpl_i.OperationString1);
		String os1 = JSONUtil.getString(value1);
		
		JSONValue value2 = JSONUtil.getJSONValue(request, UIGenericServiceImpl_i.OperationString2);
		String os2 = JSONUtil.getString(value2);
		
		JSONValue value3 = JSONUtil.getJSONValue(request, UIGenericServiceImpl_i.OperationString3);
		String os3 = JSONUtil.getString(value3);
		
		if ( os1.equalsIgnoreCase(UIGenericServiceImpl_i.REQUEST) ) {
			if ( os2.equalsIgnoreCase("opm") ) {
				if ( os3.equalsIgnoreCase("getCurrentHostName") ) {
					
					String ip = null;
					
					ip = httpServletRequest.getRemoteAddr();
					
					response = new JSONObject();
					
					response.put(UIGenericServiceImpl_i.OperationString1, new JSONString(UIGenericServiceImpl_i.RESPONSE));
					response.put(UIGenericServiceImpl_i.OperationString2, new JSONString("opm"));
					response.put(UIGenericServiceImpl_i.OperationString3, new JSONString("getCurrentHostName"));
					response.put(UIGenericServiceImpl_i.OperationString4, new JSONString(ip));
					
				} else if ( os3.equalsIgnoreCase("getCurrentIPAddress") ) {
					
					String host = null;
					
					host = httpServletRequest.getRemoteHost();
					
					response = new JSONObject();
					
					response.put(UIGenericServiceImpl_i.OperationString1, new JSONString(UIGenericServiceImpl_i.RESPONSE));
					response.put(UIGenericServiceImpl_i.OperationString2, new JSONString("opm"));
					response.put(UIGenericServiceImpl_i.OperationString3, new JSONString("getCurrentHostName"));
					response.put(UIGenericServiceImpl_i.OperationString4, new JSONString(host));
					
				}
			}
		}
		
		return response;
	}
}
