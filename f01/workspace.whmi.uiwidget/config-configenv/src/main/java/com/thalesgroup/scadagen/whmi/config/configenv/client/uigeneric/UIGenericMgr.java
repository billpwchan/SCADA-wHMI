package com.thalesgroup.scadagen.whmi.config.configenv.client.uigeneric;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;

public class UIGenericMgr implements AsyncCallback<JSONObject> {

	private final String className = UIWidgetUtil.getClassSimpleName(UIGenericMgr.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	@Override
	public void onFailure(Throwable caught) {
		final String function = "onFailure";
		logger.begin(className, function);
		logger.end(className, function);
	}

	@Override
	public void onSuccess(JSONObject result) {
		final String function = "onSuccess";
		logger.begin(className, function);
		logger.end(className, function);
	}

}
