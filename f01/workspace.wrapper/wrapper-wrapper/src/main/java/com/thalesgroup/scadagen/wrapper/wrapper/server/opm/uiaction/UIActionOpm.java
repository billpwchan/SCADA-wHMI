package com.thalesgroup.scadagen.wrapper.wrapper.server.opm.uiaction;

import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.thalesgroup.scadagen.wrapper.wrapper.server.uigeneric.UIGenericServiceImpl_i;
import com.thalesgroup.scadagen.wrapper.wrapper.server.uigeneric.factory.UIAction_i;

public class UIActionOpm implements UIAction_i {
	
	/** Logger */
	private final String className = UIActionOpm.class.getSimpleName();
    private final Logger logger = LoggerFactory.getLogger(UIActionOpm.class);
	
	public static JsonNodeFactory s_json_factory = new JsonNodeFactory(false);

	public ObjectNode execute(HttpServletRequest httpServletRequest, ObjectNode request) {
		logger.debug("Begin");
		
		String ot4 = null;
		if ( null != request ) {
			JsonNode m = request.get(UIGenericServiceImpl_i.OperationAttribute4);
	        if (m != null && m.isTextual()) {
	        	ot4 = m.asText();
	        }
		}
		
		ObjectNode jsdata = null;
		if ( null != ot4 ) {
			jsdata = s_json_factory.objectNode();

	        jsdata.put(UIGenericServiceImpl_i.OperationAttribute3, className);
	        
			ObjectNode jsparam = null;
			
			if ( ot4.equalsIgnoreCase(UIActionOpm_i.GetCurrentHostName) ) {
		        jsparam = s_json_factory.objectNode();
		        
		        jsdata.put(UIGenericServiceImpl_i.OperationAttribute4, UIActionOpm_i.GetCurrentHostName);
		
				String host = httpServletRequest.getRemoteHost();
				logger.debug("host[{}]", host);
				
				if ( null == host ) { logger.warn("host IS NULL"); }
				
				// build param list
				jsparam.put(UIGenericServiceImpl_i.OperationValue1, host);
			} else if ( ot4.equalsIgnoreCase(UIActionOpm_i.GetCurrentIPAddress) ) {
				jsparam = s_json_factory.objectNode();
				
				jsdata.put(UIGenericServiceImpl_i.OperationAttribute4, UIActionOpm_i.GetCurrentIPAddress);
				
				String ip = httpServletRequest.getRemoteAddr();
				logger.debug("ip[{}]", ip);
				
				if ( null == ip ) { logger.warn("ip IS NULL"); }
				
				// build param list
				jsparam.put(UIGenericServiceImpl_i.OperationValue1, ip);		
			} else {
				logger.warn("ot4[{}] OPERATION IS UNKNOW", ot4);
			}
			
			if ( null == jsparam ) { logger.warn("jsparam IS NULL", ot4); }
			
			jsdata.set(UIGenericServiceImpl_i.OperationParameter1, jsparam);
		} else {
			logger.warn("ot4 IS NULL");
		}
		
		logger.debug("End");
		return jsdata;
	}
	
}
