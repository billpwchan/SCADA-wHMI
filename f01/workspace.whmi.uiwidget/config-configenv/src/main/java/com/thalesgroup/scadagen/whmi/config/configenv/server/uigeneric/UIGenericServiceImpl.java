package com.thalesgroup.scadagen.whmi.config.configenv.server.uigeneric;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.thalesgroup.scadagen.whmi.config.configenv.client.uigeneric.UIGenericService;
import com.thalesgroup.scadagen.whmi.config.configenv.server.uigeneric.factory.UIActionMgr;
import com.thalesgroup.scadagen.whmi.config.configenv.server.uigeneric.factory.UIAction_i;
import com.thalesgroup.scadagen.whmi.config.configenv.server.uigeneric.util.JSONUtil;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class UIGenericServiceImpl extends RemoteServiceServlet implements UIGenericService {

	private Logger logger					= LoggerFactory.getLogger(UIGenericServiceImpl.class.getName());
	
	@Override
	public JSONObject execute(JSONObject request) {
		logger.debug("Begin");
		
		JSONObject response = null;
		
		if ( null != request ) {
			
			JSONValue value1 = JSONUtil.getJSONValue(request, UIGenericServiceImpl_i.OperationString1);
			String os1 = JSONUtil.getString(value1);
			
			logger.debug("execute os1[{}]", os1);

			if ( os1.equalsIgnoreCase(UIGenericServiceImpl_i.REQUEST) ) {
				
				JSONValue value2 = JSONUtil.getJSONValue(request, UIGenericServiceImpl_i.OperationString2);
				String os2 = JSONUtil.getString(value2);
				
				logger.debug("execute os2[{}]", os2);
				
				UIActionMgr uiActionMgr = UIActionMgr.getInstance();
				UIAction_i uiAction_i = uiActionMgr.getUIAction(os2);
				if ( null != uiAction_i ) {
					response = uiAction_i.execute(getThreadLocalRequest(), request);
				} else {
					logger.warn("execute os2[{}] uiAction_i IS NULL", os2);
				}
			}
		}
		
		if ( null != response ) {
			response.put("source", request);
		}
		
		logger.debug("End");
		return response;
	}

}
