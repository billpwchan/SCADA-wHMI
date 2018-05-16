package com.thalesgroup.scadagen.whmi.uipanel.uipanelnavigation.client.mgr.util;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;

public class HeaderKeyMapping {
	
	private final static UILogger_i logger = UILoggerFactory.getInstance().getUILogger(HeaderKeyMapping.class.getName());
	
	public static String replace(String currentHeader, String targetHeader, String spliter, String key) {
		final String function = "replace";
		logger.begin(function);
		String result = targetHeader;
		
		logger.debug(function, "currentHeader[{}] targetHeader[{}]", currentHeader, targetHeader);
		
		String [] targetHeaders = null;
		String [] currentHeaders = null;

		if ( null != targetHeader ) {
			logger.debug(function, "targetHeader[{}]", targetHeader);
			targetHeaders = targetHeader.split(spliter);
			if ( null != targetHeaders ) {
				if ( logger.isDebugEnabled() ) {
					for ( int i = 0 ; i < targetHeaders.length ; i++ ) {
						logger.debug(function, "targetHeaders({})[{}]", i, targetHeaders[i]);
					}
				} else {
					logger.warn(function, "targetHeaders IS NULL");
				}
			}
		} else {
			logger.warn(function, "targetHeader IS NULL");
		}

		logger.debug(function, "currentHeader[{}]", currentHeader);
		if ( null != currentHeader ) {
			currentHeaders = currentHeader.split(spliter);
			if ( null != currentHeaders ) {
				for ( int i = 0 ; i < currentHeaders.length ; i++ ) {
					logger.debug(function, "currentHeaders({})[{}]", i, currentHeaders[i]);
				}
			} else {
				logger.warn(function, "currentHeaders IS NULL");
			}
		} else {
			logger.warn(function, "currentHeader IS NULL");
		}
		
		if ( null != currentHeaders && null != targetHeaders ) {
			for ( int i = 0 ; i < targetHeaders.length ; i++ ) {
				if ( null != targetHeaders[i] ) {
					if ( 0 == key.compareTo(targetHeaders[i]) ) {
						if ( i < currentHeaders.length ) {
							logger.debug(function, "targetHeaders[i] = currentHeaders[i]", targetHeaders[i], currentHeaders[i]);
							targetHeaders[i] = currentHeaders[i];
						}
					}
				} else {
					logger.debug(function, "targetHeaders({}) IS NULL", i);
				}
			}
			
			result = "";
			for ( int i = 0 ; i < targetHeaders.length ; i++ ) {
				if ( result.length() > 0 ) {
					result += "|";
				}
				result += targetHeaders[i];
			}
			logger.debug(function, "after join result[{}]", result);
			
		} else {
			logger.debug(function, "currentHeaders IS NULL OR targetHeaders IS NULL");
		}
		
		logger.debug(function, "result[{}]", result);
		logger.end(function);
		return result;
	}
}
