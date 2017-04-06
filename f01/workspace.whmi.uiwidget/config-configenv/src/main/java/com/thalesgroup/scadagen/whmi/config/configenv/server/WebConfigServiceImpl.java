package com.thalesgroup.scadagen.whmi.config.configenv.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.thalesgroup.scadagen.whmi.config.configenv.client.WebConfigService;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class WebConfigServiceImpl extends RemoteServiceServlet implements WebConfigService {
	
	private Logger logger					= LoggerFactory.getLogger(WebConfigServiceImpl.class.getSimpleName());
	
	@Override
	public String webConfigServer(String key) throws IllegalArgumentException {
		logger.debug("WebConfigServiceImpl key[{}]", key);
		String value = null;
		if ( null != key ) {
			value = getServletContext().getInitParameter(key);
			logger.debug("WebConfigServiceImpl key[{}]", key);
		} else {
			logger.warn("WebConfigServiceImpl key IS NULL");
		}
		return value;
	}
}