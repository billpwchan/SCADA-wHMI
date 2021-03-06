package com.thalesgroup.scadagen.wrapper.wrapper.server.opm.uiaction;

import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.opm.client.dto.OperatorOpmInfo;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.opm.client.dto.RoleDto;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.server.UILogger_i;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.server.factory.UILoggerFactory;
import com.thalesgroup.scadagen.wrapper.wrapper.server.opm.OpmMgr;
import com.thalesgroup.scadagen.wrapper.wrapper.server.opm.UIOpmRoleSelect;
import com.thalesgroup.scadagen.wrapper.wrapper.server.uigeneric.UIGenericServiceImpl_i;
import com.thalesgroup.scadagen.wrapper.wrapper.server.uigeneric.factory.UIAction_i;

public class UIActionOpm implements UIAction_i {
	
	/** Logger */
	private final String className = UIActionOpm.class.getSimpleName();
	private UILogger_i logger = UILoggerFactory.getInstance().get(this.getClass().getName());
	
	public static JsonNodeFactory s_json_factory = new JsonNodeFactory(false);
	
	private static final String STR_UIOPMSCADAGEN = "UIOpmSCADAgen";

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
		        
		        String host = OpmMgr.getInstance(STR_UIOPMSCADAGEN).getRemoteHostName(httpServletRequest);
				logger.debug("host[{}]", host);
				
				if ( null == host ) { logger.warn("host IS NULL"); }
				
				// build param list
				jsparam.put(UIGenericServiceImpl_i.OperationValue1, host);
			}
			else if ( ot4.equalsIgnoreCase(UIActionOpm_i.GetCurrentIPAddress) ) {
				jsparam = s_json_factory.objectNode();
				
				jsdata.put(UIGenericServiceImpl_i.OperationAttribute4, UIActionOpm_i.GetCurrentIPAddress);
				
				String ip = OpmMgr.getInstance(STR_UIOPMSCADAGEN).getRemoteIPAddress(httpServletRequest);
				logger.debug("ip[{}]", ip);
				
				if ( null == ip ) { logger.warn("ip IS NULL"); }
				
				// build param list
				jsparam.put(UIGenericServiceImpl_i.OperationValue1, ip);		
			}
			else if ( ot4.equalsIgnoreCase(UIActionOpm_i.SelectRole) ) {
				jsparam = s_json_factory.objectNode();
				
				jsdata.put(UIGenericServiceImpl_i.OperationAttribute4, UIActionOpm_i.SelectRole);
				
				String ot5 = null;
				if ( null != request ) {
					JsonNode m = request.get(UIGenericServiceImpl_i.OperationAttribute5);
			        if (m != null && m.isTextual()) {
			        	ot5 = m.asText();
			        }
				}
				
				logger.debug("ot5[{}]", ot5);
				
				OperatorOpmInfo operatorOpmInfo = new UIOpmRoleSelect().update(ot5);
			
				String role = null;
				Map<String, RoleDto> map = operatorOpmInfo.getOperator().getRoleId();
				for(Entry<String, RoleDto> keyValue: map.entrySet()) {
					logger.debug("key[{}] value[{}]", keyValue.getKey(), keyValue.getValue());
					role = keyValue.getKey();
				}
				
				// build param list
				jsparam.put(UIGenericServiceImpl_i.OperationValue1, role);		
			}
			else {
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
