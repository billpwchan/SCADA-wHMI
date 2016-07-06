package com.thalesgroup.scadagen.whmi.config.configenv.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.thalesgroup.scadagen.whmi.config.config.shared.Dictionary;

/**
 * The async counterpart of <code>DictionariesService</code>.
 */
public interface DictionariesServiceAsync {
	
    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.thalesgroup.mmi.task.client.ConfigService
     */
	void dictionariesServer(String module, String path, String extension, AsyncCallback<Dictionary> callback)
			throws IllegalArgumentException;
}
