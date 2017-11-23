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
	
	private final static String STR_OB		= "[";
	private final static String STR_CB		= "] ";
	private final static String STR_EMPTY	= " ";
	
	private final static String STR_OCB		= "{}";
	
	private final static String BEGIN		= "Begin";
	private final static String END			= "End";
	
	private final static String STR_NULL	= "null";

	public void setCurrentLogLevel(int level) { Log.setCurrentLogLevel(level); }
	public int getCurrentLogLevel() { return Log.getCurrentLogLevel(); }
	
	public void begin(final String className, final String function) {
		if ( Log.isTraceEnabled() ) Log.trace(STR_OB+className+STR_CB+STR_EMPTY+function+STR_EMPTY+BEGIN);
	}
	public void end(final String className, final String function) {
		if ( Log.isTraceEnabled() ) Log.trace(STR_OB+className+STR_CB+STR_EMPTY+function+STR_EMPTY+END);
	}

	public void beginEnd(final String className, final String function) {
		begin(className, function);
		end(className, function);
	}
	public void beginEnd(final String className, final String function, String log) {
		begin(className, function);
		trace(className, function, log);
		end(className, function);
	}
	public void beginEnd(final String className, final String function, String log, Object argument1) {
		begin(className, function);
		trace(className, function, log, argument1);
		end(className, function);
	}
	public void beginEnd(final String className, final String function, String log, Object argument1, Object argument2) {
		begin(className, function);
		trace(className, function, log, argument1, argument2);
		end(className, function);
	}
	public void beginEnd(final String className, final String function, String log, Object [] arguments) {
		begin(className, function);
		trace(className, function, log, arguments);
		end(className, function);
	}
	
	public boolean isTraceEnabled()	{ return Log.isTraceEnabled();	}
	public boolean isDebugEnabled()	{ return Log.isDebugEnabled();	}
	public boolean isInfoEnabled()	{ return Log.isInfoEnabled();	}
	public boolean isWarnEnabled()	{ return Log.isWarnEnabled();	}
	public boolean isErrorEnabled()	{ return Log.isErrorEnabled();	}
	public boolean isFatalEnabled()	{ return Log.isFatalEnabled();	}
	
	public void trace(final String message) {
	    Log.trace(message);
	}
	public void trace(final String className, final String function, String log) {
		trace(className, function, log, null);
	}
	public void trace(final String className, final String function, String log, Object argument) {
		if ( Log.isTraceEnabled() ) addLog(Log.LOG_LEVEL_TRACE, className, function, log, argument);
	}
	public void trace(final String className, final String function, String log, Object argument1, Object argument2) {
		if ( Log.isTraceEnabled() ) addLog(Log.LOG_LEVEL_TRACE, className, function, log, argument1, argument2);
	}
	public void trace(final String className, final String function, String log, Object [] arguments) {
		if ( Log.isTraceEnabled() ) addLog(Log.LOG_LEVEL_TRACE, className, function, log, arguments);
	}
	
	public void debug(final String message) {
	    Log.debug(message);
	}
	public void debug(final String className, final String function, String log) {
		debug(className, function, log, null);
	}
	public void debug(final String className, final String function, String log, Object argument) {
		if ( Log.isDebugEnabled() ) addLog(Log.LOG_LEVEL_DEBUG, className, function, log, argument);
	}
	public void debug(final String className, final String function, String log, Object argument1, Object argument2) {
		if ( Log.isDebugEnabled() ) addLog(Log.LOG_LEVEL_DEBUG, className, function, log, argument1, argument2);
	}
	public void debug(final String className, final String function, String log, Object [] arguments) {
		if ( Log.isDebugEnabled() ) addLog(Log.LOG_LEVEL_DEBUG, className, function, log, arguments);
	}

	public void info(final String message) {
	    Log.info(message);
	}
	public void info(final String className, final String function, String log) {
		info(className, function, log, null);
	}
	public void info(final String className, final String function, String log, Object argument) {
		if ( Log.isInfoEnabled() ) addLog(Log.LOG_LEVEL_INFO, className, function, log, argument);
	}
	public void info(final String className, final String function, String log, Object argument1, Object argument2) {
		if ( Log.isInfoEnabled() ) addLog(Log.LOG_LEVEL_INFO, className, function, log, argument1, argument2);
	}
	public void info(final String className, final String function, String log, Object [] arguments) {
		if ( Log.isInfoEnabled() ) addLog(Log.LOG_LEVEL_INFO, className, function, log, arguments);
	}
	
	public void warn(final String message) {
	    Log.warn(message);
	}
	public void warn(final String className, final String function, String log) {
		warn(className, function, log, null);
	}
	public void warn(final String className, final String function, String log, Object argument) {
		if ( Log.isWarnEnabled() ) addLog(Log.LOG_LEVEL_WARN, className, function, log, argument);
	}
	public void warn(final String className, final String function, String log, Object argument1, Object argument2) {
		if ( Log.isWarnEnabled() ) addLog(Log.LOG_LEVEL_WARN, className, function, log, argument1, argument2);
	}
	public void warn(final String className, final String function, String log, Object [] arguments) {
		if ( Log.isWarnEnabled() ) addLog(Log.LOG_LEVEL_WARN, className, function, log, arguments);
	}	
	
	public void error(final String log) {
		Log.error(log);
	}
	public void error(final String className, final String function, final String log) {
		error(className, function, log, null);
	}
	public void error(final String className, final String function, String log, Object argument) {
		if ( Log.isErrorEnabled() ) addLog(Log.LOG_LEVEL_ERROR, className, function, log, argument);
	}
	public void error(final String className, final String function, String log, Object argument1, Object argument2) {
		if ( Log.isErrorEnabled() ) addLog(Log.LOG_LEVEL_ERROR, className, function, log, argument1, argument2);
	}
	public void error(final String className, final String function, String log, Object[] arguments) {
		if ( Log.isErrorEnabled() ) addLog(Log.LOG_LEVEL_ERROR, className, function, log, arguments);
	}
	
	public void fatal(final String log) {
		Log.fatal(log);
	}
	public void fatal(final String className, final String function, final String log) {
		fatal(className, function, log, null);
	}
	public void fatal(final String className, final String function, String log, Object argument) {
		if ( Log.isFatalEnabled() ) addLog(Log.LOG_LEVEL_FATAL, className, function, log, argument);
	}
	public void fatal(final String className, final String function, String log, Object argument1, Object argument2) {
		if ( Log.isFatalEnabled() ) addLog(Log.LOG_LEVEL_FATAL, className, function, log, argument1, argument2);
	}
	public void fatal(final String className, final String function, String log, Object[] arguments) {
		if ( Log.isFatalEnabled() ) addLog(Log.LOG_LEVEL_FATAL, className, function, log, arguments);
	}
	
	private void addLog(int level, final String className, final String function, String log) {
		addLog(level, className, function, log, null);
	}
	private void addLog(int level, final String className, final String function, String log, Object argument) {
		addLog(level, className, function, log, ( null != argument ) ? new Object[]{argument} : null);
	}
	private void addLog(int level, final String className, final String function, String log, Object argument1, Object argument2) {
		addLog(level, className, function, log, ( null != argument1 || null != argument2 ) ? new Object[]{argument1, argument2} : null);
	}
	private void addLog(int level, final String className, final String function, String log, Object[] arguments) {
		String message = STR_OB+className+STR_CB+STR_EMPTY+function+STR_EMPTY+log;
		if ( null != arguments ) {
			String [] splits = message.split(STR_OCB);
			final StringBuffer buffer = new StringBuffer();
			for ( int i = 0 ; i < splits.length ; ++i) {
				buffer.append(splits[i]);
				if ( i < splits.length - 1 ) {
					buffer.append( null != arguments[i] ? arguments[i] : STR_NULL );
				}
			}
			message = buffer.toString();
		}
		addLog(level, message);
	}
	private void addLog(int level, String message) {
		switch ( level ) {
			case Log.LOG_LEVEL_TRACE:	Log.trace(message);	break;		
			case Log.LOG_LEVEL_DEBUG:	Log.debug(message);	break;
			case Log.LOG_LEVEL_INFO:	Log.info(message);	break;
			case Log.LOG_LEVEL_WARN:	Log.warn(message);	break;
			case Log.LOG_LEVEL_ERROR:	Log.error(message);	break;
			case Log.LOG_LEVEL_FATAL:	Log.fatal(message);	break;
			default:break;
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
