package com.thalesgroup.scadagen.whmi.uiutil.uilogger.client;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.JavaScriptObject;

// Trace:	Begin/End
// Debug:	More Detail Information [SC Util]
// Info:	Normal Information
// Warn:	Checking Warning E.g. Application Should handle it
// Error:	Critical Error E.g Application Should be Stop

/**
 * @author syau
 *
 */
public class UILogger {
	
	private final String BEGIN = "Begin";
	private final String END = "End";

	public void setCurrentLogLevel(int level) {
		Log.setCurrentLogLevel(level);
	}
	public int getCurrentLogLevel(int level) {
		return Log.getCurrentLogLevel();
	}
	
	public void begin(final String className, final String function) {
		String message = "["+className+"]" + " "+ function + " " + BEGIN;
		Log.debug(message);
	}
	
	public void end(final String className, final String function) {
		String message = "["+className+"]" + " "+ function + " " + END;
		Log.debug(message);
	}

	public void beginEnd(final String className, final String function) {
		begin(className, function);
		end(className, function);
	}
	
	public boolean isTraceEnabled() { return Log.isTraceEnabled(); }
	public boolean isDebugEnabled() { return Log.isDebugEnabled(); }
	public boolean isInfoEnabled() { return Log.isInfoEnabled(); }
	public boolean isWarnEnabled() { return Log.isWarnEnabled(); }
	public boolean isErrorEnabled() { return Log.isErrorEnabled(); }
	
	public void trace(final String message) {
	    Log.trace(message);
	}
	public void trace(final String className, final String function, String log) {
		trace(className, function, log, null);
	}
	public void trace(final String className, final String function, String log, Object argument) {
		if ( Log.isTraceEnabled() ) {
			addLog(Log.LOG_LEVEL_TRACE, className, function, log, argument);
		}
	}
	public void trace(final String className, final String function, String log, Object argument1, Object argument2) {
		if ( Log.isTraceEnabled() ) {
			addLog(Log.LOG_LEVEL_TRACE, className, function, log, argument1, argument2);
		}
	}
	public void trace(final String className, final String function, String log, Object [] arguments) {
		if ( Log.isTraceEnabled() ) {
			addLog(Log.LOG_LEVEL_TRACE, className, function, log, arguments);
		}
	}
	
	public void debug(final String message) {
	    Log.debug(message);
	}
	public void debug(final String className, final String function, String log) {
		debug(className, function, log, null);
	}
	public void debug(final String className, final String function, String log, Object argument) {
		if ( Log.isDebugEnabled() ) {
			addLog(Log.LOG_LEVEL_DEBUG, className, function, log, argument);
		}
	}
	public void debug(final String className, final String function, String log, Object argument1, Object argument2) {
		if ( Log.isDebugEnabled() ) {
			addLog(Log.LOG_LEVEL_DEBUG, className, function, log, argument1, argument2);
		}
	}
	public void debug(final String className, final String function, String log, Object [] arguments) {
		if ( Log.isDebugEnabled() ) {
			addLog(Log.LOG_LEVEL_DEBUG, className, function, log, arguments);
		}
	}

	public void info(final String message) {
	    Log.info(message);
	}
	public void info(final String className, final String function, String log) {
		info(className, function, log, null);
	}
	public void info(final String className, final String function, String log, Object argument) {
		if ( Log.isInfoEnabled() ) {
			addLog(Log.LOG_LEVEL_INFO, className, function, log, argument);
		}
	}
	public void info(final String className, final String function, String log, Object argument1, Object argument2) {
		if ( Log.isInfoEnabled() ) {
			addLog(Log.LOG_LEVEL_INFO, className, function, log, argument1, argument2);
		}
	}
	public void info(final String className, final String function, String log, Object [] arguments) {
		if ( Log.isInfoEnabled() ) {
			addLog(Log.LOG_LEVEL_INFO, className, function, log, arguments);
		}
	}
	
	public void warn(final String message) {
	    Log.warn(message);
	}
	public void warn(final String className, final String function, String log) {
		warn(className, function, log, null);
	}
	public void warn(final String className, final String function, String log, Object argument) {
		if ( Log.isWarnEnabled() ) {
			addLog(Log.LOG_LEVEL_WARN, className, function, log, argument);
		}
	}
	public void warn(final String className, final String function, String log, Object argument1, Object argument2) {
		if ( Log.isWarnEnabled() ) {
			addLog(Log.LOG_LEVEL_WARN, className, function, log, argument1, argument2);
		}
	}
	public void warn(final String className, final String function, String log, Object [] arguments) {
		if ( Log.isWarnEnabled() ) {
			addLog(Log.LOG_LEVEL_WARN, className, function, log, arguments);
		}
	}	
	
	public void error(final String log) {
		Log.error(log);
	}
	public void error(final String className, final String function, final String log) {
		error(className, function, log, null);
	}
	public void error(final String className, final String function, String log, Object argument) {
		if ( Log.isErrorEnabled() ) {
			addLog(Log.LOG_LEVEL_ERROR, className, function, log, argument);
		}
	}
	public void error(final String className, final String function, String log, Object argument1, Object argument2) {
		if ( Log.isErrorEnabled() ) {
			addLog(Log.LOG_LEVEL_ERROR, className, function, log, argument1, argument2);
		}
	}
	public void error(final String className, final String function, String log, Object[] arguments) {
		if ( Log.isErrorEnabled() ) {
			addLog(Log.LOG_LEVEL_ERROR, className, function, log, arguments);
		}
	}
	
	private void addLog(int level, final String className, final String function, String log) {
		addLog(level, className, function, log, null);
	}
	private void addLog(int level, final String className, final String function, String log, Object argument) {
		Object[] objs = null;
		if ( null != argument ) {
			objs = new Object[]{argument};
		}
		addLog(level, className, function, log, objs);
	}
	private void addLog(int level, final String className, final String function, String log, Object argument1, Object argument2) {
		Object[] objs = null;
		objs = new Object[]{argument1, argument2};
		addLog(level, className, function, log, objs);
	}
	private void addLog(int level, final String className, final String function, String log, Object[] arguments) {
		String message = "["+className+"]" + " "+ function + " " + log;
		if ( null != arguments ) {
			String [] splits = message.split("{}");
			final StringBuffer buffer = new StringBuffer();
			for ( int i = 0 ; i < splits.length ; ++i) {
				buffer.append(splits[i]);
				if ( i < splits.length - 1 ) {
					if ( null != arguments[i] ) {
						buffer.append(arguments[i]);
					} else {
						buffer.append("null");
					}
				}
				message = buffer.toString();
			}
		}
		addLog(level, message);
	}
	private void addLog(int level, String message) {
		switch ( level ) {
		case Log.LOG_LEVEL_INFO:
			Log.info(message);
			break;
		case Log.LOG_LEVEL_DEBUG: 
			Log.debug(message);
			break;
		case Log.LOG_LEVEL_WARN:
			Log.warn(message);
			break;
		case Log.LOG_LEVEL_ERROR:
			Log.error(message);
			break;
		}
	}
	public void logJSObject(final JavaScriptObject o) {
	    nativeLogJSObject(o);
	}
	private static native void nativeLogJSObject(JavaScriptObject o)
	/*-{
	    console.log(o);
	}-*/;
}
