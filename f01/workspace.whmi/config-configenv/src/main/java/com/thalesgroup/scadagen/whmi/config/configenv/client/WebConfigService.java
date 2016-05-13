package com.thalesgroup.scadagen.whmi.config.configenv.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("webConfigServiceServlet")
public interface WebConfigService extends RemoteService {
	String webConfigServer(String key) throws IllegalArgumentException;
}
