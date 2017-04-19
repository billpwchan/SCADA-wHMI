package com.thalesgroup.scadagen.wrapper.wrapper.client.uigeneric;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.thalesgroup.scadagen.wrapper.wrapper.shared.UIGenericDto_i;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("uiGenericServiceServlet")
public interface UIGenericService extends RemoteService {

	UIGenericDto_i execute(UIGenericDto_i uiGenericDto) throws IllegalArgumentException;
}
