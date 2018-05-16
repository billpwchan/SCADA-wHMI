package com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.core.factory;

import com.allen_sauer.gwt.log.client.Logger;

/**
 * @author syau
 *
 */
public interface UILoggerCoreFactory_i {
	/**
	 * Get the logger. Should be implemented by Factory owner
	 * 
	 * @param className
	 * @return return logger retrieved
	 */
	Logger getLogger(final String className);
}
