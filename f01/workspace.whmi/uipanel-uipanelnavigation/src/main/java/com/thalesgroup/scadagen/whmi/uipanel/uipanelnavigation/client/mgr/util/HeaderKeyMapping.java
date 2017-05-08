package com.thalesgroup.scadagen.whmi.uipanel.uipanelnavigation.client.mgr.util;

import com.thalesgroup.scadagen.whmi.uipanel.uipanelnavigation.client.mgr.NavigationMgr;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;

public class HeaderKeyMapping {
	
	private final static String className = UIWidgetUtil.getClassSimpleName(NavigationMgr.class.getName());
	private final static UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	public static String replace(String currentHeader, String targetHeader, String spliter, String key) {
		final String function = "replace";
		logger.begin(className, function);
		String result = targetHeader;
		
		logger.debug(className, function, "currentHeader[{}] targetHeader[{}]", currentHeader, targetHeader);
		
		String [] targetHeaders = null;
		String [] currentHeaders = null;

		if ( null != targetHeader ) {
			logger.debug(className, function, "targetHeader[{}]", targetHeader);
			targetHeaders = targetHeader.split(spliter);
			if ( null != targetHeaders ) {
				if ( logger.isDebugEnabled() ) {
					for ( int i = 0 ; i < targetHeaders.length ; i++ ) {
						logger.debug(className, function, "targetHeaders({})[{}]", i, targetHeaders[i]);
					}
				} else {
					logger.warn(className, function, "targetHeaders IS NULL");
				}
			}
		} else {
			logger.warn(className, function, "targetHeader IS NULL");
		}

		logger.debug(className, function, "currentHeader[{}]", currentHeader);
		if ( null != currentHeader ) {
			currentHeaders = currentHeader.split(spliter);
			if ( null != currentHeaders ) {
				for ( int i = 0 ; i < currentHeaders.length ; i++ ) {
					logger.debug(className, function, "currentHeaders({})[{}]", i, currentHeaders[i]);
				}
			} else {
				logger.warn(className, function, "currentHeaders IS NULL");
			}
		} else {
			logger.warn(className, function, "currentHeader IS NULL");
		}
		
		if ( null != currentHeaders && null != targetHeaders ) {
			for ( int i = 0 ; i < targetHeaders.length ; i++ ) {
				if ( null != targetHeaders[i] ) {
					if ( 0 == key.compareTo(targetHeaders[i]) ) {
						if ( i < currentHeaders.length ) {
							logger.debug(className, function, "targetHeaders[i] = currentHeaders[i]", targetHeaders[i], currentHeaders[i]);
							targetHeaders[i] = currentHeaders[i];
						}
					}
				} else {
					logger.debug(className, function, "targetHeaders({}) IS NULL", i);
				}
			}
			
			result = "";
			for ( int i = 0 ; i < targetHeaders.length ; i++ ) {
				if ( result.length() > 0 ) {
					result += "|";
				}
				result += targetHeaders[i];
			}
			logger.debug(className, function, "after join result[{}]", result);
			
		} else {
			logger.debug(className, function, "currentHeaders IS NULL OR targetHeaders IS NULL");
		}
		
		logger.debug(className, function, "result[{}]", result);
		logger.end(className, function);
		return result;
	}
}
