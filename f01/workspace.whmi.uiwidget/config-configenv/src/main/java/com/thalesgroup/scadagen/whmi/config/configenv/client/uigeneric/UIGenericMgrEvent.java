package com.thalesgroup.scadagen.whmi.config.configenv.client.uigeneric;

import com.google.gwt.json.client.JSONObject;

public interface UIGenericMgrEvent {
	public void uiGenericMgrEventReady(JSONObject response);
	public void uiGenericMgrEventFailed(JSONObject response);
}
