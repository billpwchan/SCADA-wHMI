package com.thalesgroup.scadagen.whmi.uiutil.uilogger.client;

import com.allen_sauer.gwt.log.client.Logger;
import com.allen_sauer.gwt.log.shared.LogRecord;
import com.google.gwt.core.client.JavaScriptObject;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.core.factory.UILoggerCoreFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.filter.UILoggerExConfig;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.util.UILoggerUtil;

// Trace:	Begin/End
// Debug:	More Detail Information [SC Util]
// Info:	Normal Information
// Warn:	Checking Warning E.g. Application Should handle it
// Error:	Critical Error E.g Application Should be Stop

/**
 * SCADAgen default logger
 * 
 * @author syau
 *
 */
public class UILoggerEx implements UILogger_i {
	
	private final static String STR_OB		= "[";
	private final static String STR_CB		= "] ";
	private final static String STR_EMPTY	= " ";
	
	private final static String STR_OCB		= "{}";
	
	private final static String BEGIN		= "Begin";
	private final static String END			= "End";
	
	private final static String STR_NULL	= "null";

	public static int LOG_LEVEL_TRACE		= UILoggerExConfig.getInstance().LOG_LEVEL_TRACE;
	public static int LOG_LEVEL_DEBUG		= UILoggerExConfig.getInstance().LOG_LEVEL_DEBUG;
	public static int LOG_LEVEL_INFO		= UILoggerExConfig.getInstance().LOG_LEVEL_INFO;
	public static int LOG_LEVEL_WARN		= UILoggerExConfig.getInstance().LOG_LEVEL_WARN;
	public static int LOG_LEVEL_ERROR		= UILoggerExConfig.getInstance().LOG_LEVEL_ERROR;
	public static int LOG_LEVEL_FATAL		= UILoggerExConfig.getInstance().LOG_LEVEL_FATAL;
	public static int LOG_LEVEL_OFF			= UILoggerExConfig.getInstance().LOG_LEVEL_OFF;

	private Logger logger					= UILoggerCoreFactory.getInstance().getLogger();

	private UILoggerExConfig config			= UILoggerExConfig.getInstance();
	
	private String name                     = null;
	private String simpleName               = null;
	
	public UILoggerEx(final String name) { 
		this.name = name; 
		this.simpleName = UILoggerUtil.getClassSimpleName(name);
	}

	public void setFilter(UILoggerExConfig config) { this.config = config; }
	public UILoggerExConfig getConfig() { return this.config; }
	
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i#getName()
	 */
	@Override
	public String getName() { return name; }
	
	@Override
	public void clear() { logger.clear(); }

	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i#setCurrentLogLevel(int)
	 */
	@Override
	public void setCurrentLogLevel(int level) { 
		if(null!=getConfig()) getConfig().setCurrentLogLevel(level);
		logger.setCurrentLogLevel(level); 
	}
	
	@Override
	public int getCurrentLogLevel() { 
		int level = -1;
		if(null!=getConfig()) level = getConfig().getCurrentLogLevel();
		return level;
	}
	
	private String getClassName(String className) { return !getConfig().isFullClassName() ? className : name; }
	
	private String msg(final String className, final String function, final String log, final Object[] arguments) {
		String message = STR_OB+getClassName(className)+STR_CB+STR_EMPTY+function+STR_EMPTY+log;
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
		return message;
	}
	
	public void begin(final String className, final String function) {
		addLog(LOG_LEVEL_TRACE, STR_OB+getClassName(className)+STR_CB+STR_EMPTY+function+STR_EMPTY+BEGIN);
	}
	public void end(final String className, final String function) {
		addLog(LOG_LEVEL_TRACE, STR_OB+getClassName(className)+STR_CB+STR_EMPTY+function+STR_EMPTY+END);
	}

	@Override
	public void beginEnd(final String className, final String function) {
		begin(className, function);
		end(className, function);
	}
	private void beginEnd(final String className, final String function, String log) {
		begin(className, function);
		trace(className, function, log);
		end(className, function);
	}
	private void beginEnd(final String className, final String function, String log, Object argument1) {
		begin(className, function);
		trace(className, function, log, argument1);
		end(className, function);
	}
	private void beginEnd(final String className, final String function, String log, Object argument1, Object argument2) {
		begin(className, function);
		trace(className, function, log, argument1, argument2);
		end(className, function);
	}
	private void beginEnd(final String className, final String function, String log, Object [] arguments) {
		begin(className, function);
		trace(className, function, log, arguments);
		end(className, function);
	}

	public boolean isTraceEnabled()	{ return isEnabled(LOG_LEVEL_TRACE);	}
	public boolean isDebugEnabled()	{ return isEnabled(LOG_LEVEL_DEBUG);	}
	public boolean isInfoEnabled()	{ return isEnabled(LOG_LEVEL_INFO);		}
	public boolean isWarnEnabled()	{ return isEnabled(LOG_LEVEL_WARN);		}
	public boolean isErrorEnabled()	{ return isEnabled(LOG_LEVEL_ERROR);	}
	public boolean isFatalEnabled()	{ return isEnabled(LOG_LEVEL_FATAL);	}
	
	private boolean isEnabled(int level) { return getCurrentLogLevel() >= level; }
	
	private void trace(final String className, final String function, String log) {
		trace(className, function, log, null);
	}
	private void trace(final String className, final String function, String log, Object argument) {
		addLog(LOG_LEVEL_TRACE, className, function, log, argument);
	}
	private void trace(final String className, final String function, String log, Object argument1, Object argument2) {
		addLog(LOG_LEVEL_TRACE, className, function, log, argument1, argument2);
	}
	private void trace(final String className, final String function, String log, Object [] arguments) {
		addLog(LOG_LEVEL_TRACE, className, function, log, arguments);
	}
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i#trace(java.lang.String)
	 */
	@Override
	public void trace(final String message) { addLog(LOG_LEVEL_TRACE, message); }
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i#trace(java.lang.String, java.lang.Throwable)
	 */
	@Override
    public void trace(final String message, final Throwable throwable) { addLog(LOG_LEVEL_TRACE, message, throwable); }
	
	private void debug(final String className, final String function, String log) {
		debug(className, function, log, null);
	}
	private void debug(final String className, final String function, String log, Object argument) {
		addLog(LOG_LEVEL_DEBUG, className, function, log, argument);
	}
	private void debug(final String className, final String function, String log, Object argument1, Object argument2) {
		addLog(LOG_LEVEL_DEBUG, className, function, log, argument1, argument2);
	}
	private void debug(final String className, final String function, String log, Object [] arguments) {
		addLog(LOG_LEVEL_DEBUG, className, function, log, arguments);
	}
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i#debug(java.lang.String)
	 */
	@Override
	public void debug(final String message) { addLog(LOG_LEVEL_DEBUG, message); }
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i#debug(java.lang.String, java.lang.Throwable)
	 */
	@Override
	public void debug(final String message, final Throwable throwable) { addLog(LOG_LEVEL_DEBUG, message, throwable); }
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i#debug(java.lang.Object)
	 */
	@Override
    public void debug(final Object message) { addLog(LOG_LEVEL_DEBUG, String.valueOf(message)); }

	private void info(final String className, final String function, String log) {
		info(className, function, log, null);
	}
	private void info(final String className, final String function, String log, Object argument) {
		addLog(LOG_LEVEL_INFO, className, function, log, argument);
	}
	private void info(final String className, final String function, String log, Object argument1, Object argument2) {
		addLog(LOG_LEVEL_INFO, className, function, log, argument1, argument2);
	}
	private void info(final String className, final String function, String log, Object [] arguments) {
		addLog(LOG_LEVEL_INFO, className, function, log, arguments);
	}
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i#info(java.lang.String)
	 */
	@Override
	public void info(final String log) { addLog(LOG_LEVEL_INFO, log); }
	
	private void warn(final String className, final String function, String log) {
		warn(className, function, log, null);
	}
	private void warn(final String className, final String function, String log, Object argument) {
		addLog(LOG_LEVEL_WARN, className, function, log, argument);
	}
	private void warn(final String className, final String function, String log, Object argument1, Object argument2) {
		addLog(LOG_LEVEL_WARN, className, function, log, argument1, argument2);
	}
	private void warn(final String className, final String function, String log, Object [] arguments) {
		addLog(LOG_LEVEL_WARN, className, function, log, arguments);
	}
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i#warn(java.lang.String)
	 */
	@Override
	public void warn(final String log) { addLog(LOG_LEVEL_WARN, log); }
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i#warn(java.lang.String, java.lang.Throwable)
	 */
	@Override
    public void warn(final String message, final Throwable throwable) { addLog(LOG_LEVEL_WARN, message, throwable); }
	
	private void error(final String className, final String function, final String log) {
		error(className, function, log, null);
	}
	private void error(final String className, final String function, String log, Object argument) {
		addLog(LOG_LEVEL_ERROR, className, function, log, argument);
	}
	private void error(final String className, final String function, String log, Object argument1, Object argument2) {
		addLog(LOG_LEVEL_ERROR, className, function, log, argument1, argument2);
	}
	private void error(final String className, final String function, String log, Object[] arguments) {
		addLog(LOG_LEVEL_ERROR, className, function, log, arguments);
	}
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i#error(java.lang.String)
	 */
	@Override
	public void error(final String log) { addLog(LOG_LEVEL_ERROR, log); }
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i#error(java.lang.String, java.lang.Throwable)
	 */
	@Override
    public void error(final String message, final Throwable throwable) { addLog(LOG_LEVEL_ERROR, message, throwable); }
	
	@Override
	public void fatal(final String log) {
		addLog(LOG_LEVEL_FATAL, log);
	}
	private void fatal(final String className, final String function, final String log) {
		fatal(className, function, log, null);
	}
	private void fatal(final String className, final String function, String log, Object argument) {
		addLog(LOG_LEVEL_FATAL, className, function, log, argument);
	}
	private void fatal(final String className, final String function, String log, Object argument1, Object argument2) {
		addLog(LOG_LEVEL_FATAL, className, function, log, argument1, argument2);
	}
	private void fatal(final String className, final String function, String log, Object[] arguments) {
		addLog(LOG_LEVEL_FATAL, className, function, log, arguments);
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
		addLog(level, msg(className, function, log, arguments));
	}
	private void addLog(int level, String message) {
		addLog(level, message, (Throwable) null);
	}
	private void addLog(int level, String message, Throwable e) {
		if( null==this.getConfig() ) {
			logger.log(new LogRecord(this.getConfig().getCategory(), level, message, e));
		} else if (this.getConfig().isEnabled(level, this.name) ) {
			logger.log(new LogRecord(this.getConfig().getCategory(), level, message, e));
		}
	}
	public void logJSObject(final JavaScriptObject o) {
	    nativeLogJSObject(o);
	}
	private static native void nativeLogJSObject(JavaScriptObject o)
	/*-{
	    console.log(o);
	}-*/;

	@Override
	public void begin    (String function) { begin(simpleName, function); }
	@Override
	public void end      (String function) { end(simpleName, function); }
	@Override
	public void beginEnd (String function) { beginEnd(simpleName, function); }
	@Override
	public void beginEnd (String function, String message, Object arg1) { beginEnd(simpleName, function, message, arg1); }
	@Override
	public void beginEnd (String function, String message, Object arg1, Object arg2) { beginEnd(simpleName, function, message, arg1, arg2); }
	@Override
	public void beginEnd (String function, String message, Object[] args) { beginEnd(simpleName, function, message, args); }
	@Override
	public void trace    (String function, String message) { trace(simpleName, function, message); }
	@Override
	public void trace    (String function, String message, Object arg1) { trace(simpleName, function, message, arg1); }
	@Override
	public void trace    (String function, String message, Object arg1, Object arg2) { trace(simpleName, function, message, arg1, arg2); }
	@Override
	public void trace    (String function, String message, Object[] args) { trace(simpleName, function, message, args); }
	@Override
	public void info     (String function, String message) { info(simpleName, function, message); }
	@Override
	public void info     (String function, String message, Object arg1) { info(simpleName, function, message, arg1); }
	@Override
	public void info     (String function, String message, Object arg1, Object arg2) { info(simpleName, function, message, arg1, arg2); }
	@Override
	public void info     (String function, String message, Object[] args) { info(simpleName, function, message, args); }
	@Override
	public void debug    (String function, String message) { debug(simpleName, function, message); }
	@Override
	public void debug    (String function, String message, Object arg1) { debug(simpleName, function, message, arg1); }
	@Override
	public void debug    (String function, String message, Object arg1, Object arg2) { debug(simpleName, function, message, arg1, arg2); }
	@Override
	public void debug    (String function, String message, Object[] args) { debug(simpleName, function, message, args); }
	@Override
	public void warn     (String function, String message) { warn(simpleName, function, message); }
	@Override
	public void warn     (String function, String message, Object arg1) { warn(simpleName, function, message, arg1); }
	@Override
	public void warn     (String function, String message, Object arg1, Object arg2) { warn(simpleName, function, message, arg1, arg2); }
	@Override
	public void warn     (String function, String message, Object[] args) { warn(simpleName, function, message, args); }
	@Override
	public void error    (String function, String message) { error(simpleName, function, message); }
	@Override
	public void error    (String function, String message, Object arg1) { error(simpleName, function, message, arg1); }
	@Override
	public void error    (String function, String message, Object arg1, Object arg2) { error(simpleName, function, message, arg2); }
	@Override
	public void error    (String function, String message, Object[] args) { error(simpleName, function, message, args); }
	@Override
	public void fatal    (String function, String message) { fatal(simpleName, function, message); }
	@Override
	public void fatal    (String function, String message, Object arg1) { fatal(simpleName, function, message, arg1); }
	@Override
	public void fatal    (String function, String message, Object arg1, Object arg2) { fatal(simpleName, function, message, arg1, arg2); }
	@Override
	public void fatal    (String function, String message, Object[] args) { fatal(simpleName, function, message, args); }
}
