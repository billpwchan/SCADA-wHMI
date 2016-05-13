package com.thalesgroup.scadagen.whmi.config.configenv.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.thalesgroup.scadagen.whmi.config.configenv.client.WebConfigService;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class WebConfigServiceImpl extends RemoteServiceServlet implements WebConfigService {
	@Override
	public String webConfigServer(String key) throws IllegalArgumentException {
		System.out.println(" **** WebConfigServiceImpl key["+key+"]");
		String value = null;
		if ( null != key ) {
			value = getServletContext().getInitParameter(key);
			System.out.println(" **** WebConfigServiceImpl key["+key+"]");
		} else {
			System.out.println(" **** WebConfigServiceImpl key IS NULL");
		}
		return value;
	}
}