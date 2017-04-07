package com.thalesgroup.scadagen.whmi.config.configenv.client.uigeneric;

import com.google.gwt.json.client.JSONObject;

/**
 * GWT-RPC service  asynchronous (client-side) interface
 * @see com.thalesgroup.scadagen.whmi.config.configenv.client.UIGenericService
 */
public interface UIGenericServiceAsync {
	void execute(JSONObject jsonObject);
}
