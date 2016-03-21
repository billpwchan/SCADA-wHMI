package com.thalesgroup.scadagen.whmi.config.configenv.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.thalesgroup.scadagen.whmi.config.config.shared.Dictionary;

/**
 * The async counterpart of <code>DictionaryMgrEventService</code>.
 */
public interface DictionaryServiceAsync {
	
    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.thalesgroup.mmi.task.client.ConfigService
     */
	void dictionaryServer(String module, String xml, String tag, AsyncCallback<Dictionary> callback)
			throws IllegalArgumentException;
}
