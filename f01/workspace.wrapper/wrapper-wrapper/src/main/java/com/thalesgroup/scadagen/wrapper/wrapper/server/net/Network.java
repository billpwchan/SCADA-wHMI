package com.thalesgroup.scadagen.wrapper.wrapper.server.net;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Network Class for the UIOpmSCADAgen
 * 
 * @author syau
 *
 */
public class Network {
	
	private final static Logger logger = LoggerFactory.getLogger(Network.class.getName());
	
	private static final String STR_UNKNOWN = "unknown";
	
	private static final String[] STR_HEADERS_LIST = {
			"X-Forwarded-For",
			"Proxy-Client-IP",
			"WL-Proxy-Client-IP",
			"HTTP_X_FORWARDED_FOR",
			"HTTP_X_FORWARDED",
			"HTTP_X_CLUSTER_CLIENT_IP",
			"HTTP_CLIENT_IP",
			"HTTP_FORWARDED_FOR",
			"HTTP_FORWARDED",
			"HTTP_VIA",
			"REMOTE_ADDR" 
	};

	private void dumpGetRemoteHeader(HttpServletRequest httpServletRequest) {
		logger.debug("dumpGetRemoteHeader begin");
		Enumeration<String> headerNames = httpServletRequest.getHeaderNames();
		if (headerNames != null) {
			logger.debug("dumpGetRemoteHeader Start to list out headerNames");
			while (headerNames.hasMoreElements()) {
				String element = headerNames.nextElement();
				logger.debug("dumpGetRemoteHeader element[{}]", element);
				if ( null != element ) {
					String elementValues =  httpServletRequest.getHeader(element);
					logger.debug("dumpGetRemoteHeader {}=[{}]", element, elementValues);
				} else {
					logger.debug("dumpGetRemoteHeader element IS NULL");
				}
			}
		} else {
			logger.debug("headerNames IS NULL");
		}
		logger.debug("dumpGetRemoteHeader end");
	}
	
	public String getRemoteHostName(HttpServletRequest httpServletRequest) {
		logger.debug("getRemoteHostName begin");
		String ret = getRemoteIPAddress(httpServletRequest);
		InetAddress inetAddress;
		try {
			inetAddress = InetAddress.getByName(ret);
			if ( null != ret ) {
				String hostname = inetAddress.getHostName();
				if ( null != hostname) {
					ret = hostname;
				}
			}
		} catch (UnknownHostException e) {
			logger.debug("getRemoteHostName UnknownHostException e.tostring[{}]", e.toString());
		}
		logger.debug("getRemoteHostName ret[{}]", ret);
		logger.debug("getRemoteHostName end");
		return ret;
	}

	public String getRemoteIPAddress(HttpServletRequest httpServletRequest) {
		logger.debug("getRemoteIPAddress begin");
		String ret = null;
		
		if ( logger.isDebugEnabled() ) {
			dumpGetRemoteHeader(httpServletRequest);
		}
		
		for (String header : STR_HEADERS_LIST) {
			logger.debug("getRemoteIPAddress header[{}]", header);
			ret = httpServletRequest.getHeader(header);
			logger.debug("getRemoteIPAddress {}=[{}]", header, ret);
			if (ret != null && ret.length() != 0 && !STR_UNKNOWN.equalsIgnoreCase(ret)) {
				logger.debug("getRemoteIPAddress FOUNT {}=[{}]", header, ret);
				break;
			}
		}
		
		// Return the Ip address if ip address not found in Header (X-Forwarded-For)
		if ( null == ret ) {
			ret = httpServletRequest.getRemoteAddr();
		}
		
		logger.debug("getRemoteIPAddress ret[{}]", ret);
		logger.debug("getRemoteIPAddress end");
		return ret;
	}

	public String getHostName() {
		logger.debug("getHostName begin");
		String ret = null;
		try {
			ret = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			logger.debug("getHostName UnknownHostException e.tostring[{}]", e.toString());
		}
		logger.debug("getHostName ret[{}]", ret);
		logger.debug("getHostName end");
		return ret;
	}

	public String getIPAddress() {
		logger.debug("getIPAddress begin");
		String ret = null;
		try {
			ret = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			logger.debug("getIPAddress UnknownHostException e.tostring[{}]", e.toString());
		}
		logger.debug("getIPAddress ret[{}]", ret);
		logger.debug("getIPAddress end");
		return ret;
	}
}
