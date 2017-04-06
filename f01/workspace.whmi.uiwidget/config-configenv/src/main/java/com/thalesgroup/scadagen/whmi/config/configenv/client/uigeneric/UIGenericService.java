package com.thalesgroup.scadagen.whmi.config.configenv.client.uigeneric;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("uiGenericServiceServlet")
public interface UIGenericService extends RemoteService {
	JSONObject execute(JSONObject jsonObject);
}
