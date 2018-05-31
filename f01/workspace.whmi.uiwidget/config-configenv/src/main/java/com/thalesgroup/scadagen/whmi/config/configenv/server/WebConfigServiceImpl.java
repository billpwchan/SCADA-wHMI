package com.thalesgroup.scadagen.whmi.config.configenv.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.thalesgroup.scadagen.whmi.config.configenv.client.WebConfigService;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.server.UILogger_i;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.server.factory.UILoggerFactory;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class WebConfigServiceImpl extends RemoteServiceServlet implements WebConfigService {
	
	private UILogger_i logger = UILoggerFactory.getInstance().get(this.getClass().getName());
	
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