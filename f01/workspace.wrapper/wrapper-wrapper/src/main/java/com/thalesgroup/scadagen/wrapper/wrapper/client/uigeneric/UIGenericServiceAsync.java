package com.thalesgroup.scadagen.wrapper.wrapper.client.uigeneric;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.thalesgroup.scadagen.wrapper.wrapper.shared.UIGenericDto_i;

/**
 * GWT-RPC service  asynchronous (client-side) interface
 * @see com.thalesgroup.scadagen.whmi.config.configenv.client.UIGenericService
 */
public interface UIGenericServiceAsync {
	
	void execute(UIGenericDto_i uiGenericDto, AsyncCallback<UIGenericDto_i> callback)
			throws IllegalArgumentException;
}
