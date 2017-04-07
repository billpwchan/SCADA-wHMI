package com.thalesgroup.scadagen.whmi.config.configenv.client.uigeneric;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;

public class UIGenericMgr implements AsyncCallback<JSONObject> {

	private final String className = UIWidgetUtil.getClassSimpleName(UIGenericMgr.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private final UIGenericServiceAsync uiGenericService = GWT.create(UIGenericService.class);
	
	Map<JSONObject, UIGenericMgrEvent> requests = new HashMap<JSONObject, UIGenericMgrEvent>();
	
	public void executeUIGeneric(JSONObject request, UIGenericMgrEvent event) {
		final String function = "onFailure";
		logger.begin(className, function);
		
		requests.put(request, event);
		
		uiGenericService.execute(request);
		
		logger.end(className, function);
	}

	@Override
	public void onSuccess(JSONObject result) {
		final String function = "onSuccess";
		logger.begin(className, function);
		if ( null != result) {
			JSONValue jsonValueRequest = result.get("source");
			JSONObject request = jsonValueRequest.isObject();
			if ( null != request ) {
				UIGenericMgrEvent event = requests.get(request);
				if ( null != event ) {
					event.uiGenericMgrEventReady(result);
				}
				requests.remove(request);
			}
		}
		logger.end(className, function);
	}
	
	@Override
	public void onFailure(Throwable caught) {
		final String function = "onFailure";
		logger.begin(className, function);
		logger.end(className, function);
	}

}
