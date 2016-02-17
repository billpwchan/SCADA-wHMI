package com.thalesgroup.scadagen.whmi.config.configenv.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.thalesgroup.scadagen.whmi.config.config.shared.Configs;

/**
 * The async counterpart of <code>ConfigService</code>.
 */
public interface ConfigServiceAsync {
	
    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.thalesgroup.mmi.task.client.ConfigService
     */
	void configServer(String module, String xml, String tag, AsyncCallback<Configs> callback)
			throws IllegalArgumentException;
}
