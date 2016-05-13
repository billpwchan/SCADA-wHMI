package com.thalesgroup.scadagen.whmi.config.configenv.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>WebConfigService</code>.
 */
public interface WebConfigServiceAsync {
	
    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see  com.thalesgroup.scadagen.whmi.config.configenv.client.WebConfigService
     */
	void webConfigServer(String key, AsyncCallback<String> callback)
			throws IllegalArgumentException;
}
