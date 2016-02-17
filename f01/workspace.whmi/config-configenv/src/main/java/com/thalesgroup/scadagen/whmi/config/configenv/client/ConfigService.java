package com.thalesgroup.scadagen.whmi.config.configenv.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.thalesgroup.scadagen.whmi.config.config.shared.Configs;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("configServiceServlet")
public interface ConfigService extends RemoteService {
	Configs configServer(String module, String xmlFile, String tag) throws IllegalArgumentException;
}
