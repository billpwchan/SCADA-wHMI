package com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.core;

import com.allen_sauer.gwt.log.client.Log;
import com.allen_sauer.gwt.log.client.Logger;
import com.allen_sauer.gwt.log.shared.LogRecord;

public class LogWrapper implements Logger {
	
	/* (non-Javadoc)
	 * @see com.allen_sauer.gwt.log.client.Logger#clear()
	 */
	@Override
	public void clear() {
		Log.clear();
	}

	/* (non-Javadoc)
	 * @see com.allen_sauer.gwt.log.client.Logger#isSupported()
	 */
	@Override
	public boolean isSupported() {
		return true;
	}

	/* (non-Javadoc)
	 * @see com.allen_sauer.gwt.log.client.Logger#log(com.allen_sauer.gwt.log.shared.LogRecord)
	 */
	@Override
	public void log(LogRecord record) {
		Log.log(record);
	}

	/* (non-Javadoc)
	 * @see com.allen_sauer.gwt.log.client.Logger#setCurrentLogLevel(int)
	 */
	@Override
	public void setCurrentLogLevel(int level) {
		Log.setCurrentLogLevel(level);
	}

}
