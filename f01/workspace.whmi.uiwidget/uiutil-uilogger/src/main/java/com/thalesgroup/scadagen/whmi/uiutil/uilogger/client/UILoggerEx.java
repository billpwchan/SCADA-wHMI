package com.thalesgroup.scadagen.whmi.uiutil.uilogger.client;

import com.allen_sauer.gwt.log.client.Logger;
import com.allen_sauer.gwt.log.shared.LogRecord;
import com.google.gwt.core.client.JavaScriptObject;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.core.factory.UILoggerCoreFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.filter.UILoggerExConfig;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.filter.UILoggerExConfig_i;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.util.UILoggerUtil;

// Trace:	Begin/End
// Debug:	More Detail Information [SC Util]
// Info:	Normal Information
// Warn:	Checking Warning E.g. Application Should handle it
// Error:	Critical Error E.g Application Should be Stop

/**
 * SCADAgen default logger
 * 
 * @author t0096643
 *
 */
public class UILoggerEx implements UILogger_i {
	
	private static String STR_OB         = "[";
	private static String STR_CB         = "] ";
	private static String STR_EMPTY      = " ";
	
	private static String STR_PREFIX    = UILoggerExConfig.getInstance().getMsg(UILoggerExConfig_i.AttributeMsg.PREFIX.toString());
	
	private static String STR_MSG       = UILoggerExConfig.getInstance().getMsg(UILoggerExConfig_i.AttributeMsg.MSG.toString());
	
	private static String STR_OCB       = UILoggerExConfig.getInstance().getMsg(UILoggerExConfig_i.AttributeMsg.OCB.toString());
	
	private static String STR_BEGIN     = UILoggerExConfig.getInstance().getMsg(UILoggerExConfig_i.AttributeMsg.BEGIN.toString());
	private static String STR_END       = UILoggerExConfig.getInstance().getMsg(UILoggerExConfig_i.AttributeMsg.END.toString());
	private static String STR_BEGINEND  = UILoggerExConfig.getInstance().getMsg(UILoggerExConfig_i.AttributeMsg.BEGINEND.toString());
	
	private static String STR_NULL      = UILoggerExConfig.getInstance().getMsg(UILoggerExConfig_i.AttributeMsg.NULL.toString());
	
	private static Formatter formatter  = UILoggerExConfig.getInstance().getFormatter();
	
	private static int LOG_LEVEL_TRACE   = UILoggerExConfig.getInstance().getLevel(UILoggerExConfig_i.AttributeLevel.TRACE.toString());
	private static int LOG_LEVEL_DEBUG   = UILoggerExConfig.getInstance().getLevel(UILoggerExConfig_i.AttributeLevel.DEBUG.toString());
	private static int LOG_LEVEL_INFO    = UILoggerExConfig.getInstance().getLevel(UILoggerExConfig_i.AttributeLevel.INFO.toString());
	private static int LOG_LEVEL_WARN    = UILoggerExConfig.getInstance().getLevel(UILoggerExConfig_i.AttributeLevel.WARN.toString());
	private static int LOG_LEVEL_ERROR   = UILoggerExConfig.getInstance().getLevel(UILoggerExConfig_i.AttributeLevel.ERROR.toString());
	private static int LOG_LEVEL_FATAL   = UILoggerExConfig.getInstance().getLevel(UILoggerExConfig_i.AttributeLevel.FATAL.toString());
	private static int LOG_LEVEL_OFF     = UILoggerExConfig.getInstance().getLevel(UILoggerExConfig_i.AttributeLevel.OFF.toString());

	private Logger logger               = UILoggerCoreFactory.getInstance().getLogger();

	private UILoggerExConfig config     = UILoggerExConfig.getInstance();
	
	private String name                 = null;
	private String simpleName           = null;
	
	public UILoggerEx(final String name) { 
		this.name = name; 
		this.simpleName = UILoggerUtil.getClassSimpleName(name);
	}

	public void setFilter(final UILoggerExConfig cfg) { this.config = cfg; }
	public UILoggerExConfig getConfig() { return this.config; }
	
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i#getName()
	 */
	@Override public String getName() { return name; }
	
	@Override public void clear() { logger.clear(); }

	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i#setCurrentLogLevel(int)
	 */
	@Override public 
	void setCurrentLogLevel(final int l) { 
		if(null!=getConfig()) getConfig().setCurrentLogLevel(l);
		logger.setCurrentLogLevel(l); 
	}
	
	@Override public 
	int getCurrentLogLevel() { 
		int l = -1;
		if(null!=getConfig()) l = getConfig().getCurrentLogLevel();
		return l;
	}
	
	private String getClassName(final String c) { return !getConfig().isFullClassName() ? c : this.name; }
	
	public interface Formatter {
		String formatt(String m, Object [] args);
	}
	
	private String format(final String m, final Object[] args) {
		if(null!=formatter) return formatter.formatt(m, args);
		String msg = m;
		if ( null != args ) {
			String [] splits = msg.split(STR_OCB);
			final StringBuffer buffer = new StringBuffer();
			for ( int i = 0 ; i < splits.length ; ++i) {
				buffer.append(splits[i]);
				if ( i < splits.length - 1 ) {
					buffer.append( null != args[i] ? args[i] : STR_NULL );
				}
			}
			msg = buffer.toString();
		}
		return msg;
	}
	
	private String msgPrefix(final String c, final String f) { 
		return (null==STR_PREFIX)
				? STR_OB+getClassName(c)+STR_CB+STR_EMPTY+f+STR_EMPTY
				: format(STR_PREFIX, new Object[]{getClassName(c), f});
	}
	private String msgContent(final String log, final Object[] args) { return format(log, args); }

	private String msg(final String c, final String f, final String log, final Object[] args) {
		return (null==STR_MSG)
				? msgPrefix(c, f) + format(log, args)
				: format(STR_MSG, new Object[]{msgPrefix(c, f), msgContent(log, args)});
	}
	
	@Override public boolean isLevelEnabled(int level)	{ return isEnabled(level); }
	@Override public boolean isTraceEnabled()	{ return isEnabled(LOG_LEVEL_TRACE); }
	@Override public boolean isDebugEnabled()	{ return isEnabled(LOG_LEVEL_DEBUG); }
	@Override public boolean isInfoEnabled()	{ return isEnabled(LOG_LEVEL_INFO);  }
	@Override public boolean isWarnEnabled()	{ return isEnabled(LOG_LEVEL_WARN);  }
	@Override public boolean isErrorEnabled()	{ return isEnabled(LOG_LEVEL_ERROR); }
	@Override public boolean isFatalEnabled()	{ return isEnabled(LOG_LEVEL_FATAL); }
	
	private boolean isEnabled(int l) { return this.getConfig().isEnabled(l, this.name); }

	public void begin(final String c, final String f)	{ addLog(LOG_LEVEL_TRACE, msgPrefix(c, f)+STR_BEGIN); }
	public void end(final String c, final String f)		{ addLog(LOG_LEVEL_TRACE, msgPrefix(c, f)+STR_END); }

	@Override public
	void beginEnd(final String c, final String f) { addLog(LOG_LEVEL_TRACE, msgPrefix(c, f) + STR_BEGINEND); }
	private void beginEnd(final String c, final String f, final String m) { addLog(LOG_LEVEL_TRACE, msgPrefix(c, f) + STR_BEGINEND + STR_EMPTY + m); }
	private void beginEnd(final String c, final String f, final String m, final Object arg1) { addLog(LOG_LEVEL_TRACE, msgPrefix(c, f) + STR_BEGINEND + STR_EMPTY + msgContent(m , new Object[]{arg1})); }
	private void beginEnd(final String c, final String f, final String m, final Object arg1, final Object arg2) { addLog(LOG_LEVEL_TRACE, msgPrefix(c, f) + STR_BEGINEND + STR_EMPTY + msgContent(m , new Object[]{arg1, arg2})); }
	private void beginEnd(final String c, final String f, final String m, final Object [] args) { addLog(LOG_LEVEL_TRACE, msgPrefix(c, f) + STR_BEGINEND + STR_EMPTY + msgContent(m , args)); }
	
	private void trace(final String c, final String f, final String m) { addLog(LOG_LEVEL_TRACE, c, f, m); }
	private void trace(final String c, final String f, final String m, final Object arg) { addLog(LOG_LEVEL_TRACE, c, f, m, arg); }
	private void trace(final String c, final String f, final String m, final Object arg1, final Object arg2) { addLog(LOG_LEVEL_TRACE, c, f, m, arg1, arg2); }
	private void trace(final String c, final String f, final String m, final Object [] args) { addLog(LOG_LEVEL_TRACE, c, f, m, args); }
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i#trace(java.lang.String)
	 */
	@Override public void trace(final String m) { addLog(LOG_LEVEL_TRACE, m); }
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i#trace(java.lang.String, java.lang.Throwable)
	 */
	@Override public void trace(final String m, final Throwable r) { addLog(LOG_LEVEL_TRACE, m, r); }
	
	private void debug(final String c, final String f, final String m) { addLog(LOG_LEVEL_DEBUG, c, f, m); }
	private void debug(final String c, final String f, final String m, final Object arg) { addLog(LOG_LEVEL_DEBUG, c, f, m, arg); }
	private void debug(final String c, final String f, final String m, final Object arg1, final Object arg2) { addLog(LOG_LEVEL_DEBUG, c, f, m, arg1, arg2); }
	private void debug(final String c, final String f, final String m, final Object [] args) { addLog(LOG_LEVEL_DEBUG, c, f, m, args); }
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i#debug(java.lang.String)
	 */
	@Override public void debug(final String m) { addLog(LOG_LEVEL_DEBUG, m); }
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i#debug(java.lang.String, java.lang.Throwable)
	 */
	@Override public void debug(final String m, final Throwable t) { addLog(LOG_LEVEL_DEBUG, m, t); }
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i#debug(java.lang.Object)
	 */
	@Override public void debug(final Object o) { addLog(LOG_LEVEL_DEBUG, String.valueOf(o)); }

	private void info(final String c, final String f, final String m) { addLog(LOG_LEVEL_INFO, c, f, m); }
	private void info(final String c, final String f, final String m, final Object arg) { addLog(LOG_LEVEL_INFO, c, f, m, arg); }
	private void info(final String c, final String f, final String m, final Object arg1, final Object arg2) { addLog(LOG_LEVEL_INFO, c, f, m, arg1, arg2); }
	private void info(final String c, final String f, final String m, final Object [] args) { addLog(LOG_LEVEL_INFO, c, f, m, args); }
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i#info(java.lang.String)
	 */
	@Override public void info(final String m) { addLog(LOG_LEVEL_INFO, m); }
	
	private void warn(final String c, final String f, final String m) { addLog(LOG_LEVEL_WARN, c, f, m);  }
	private void warn(final String c, final String f, final String m, final Object arg) { addLog(LOG_LEVEL_WARN, c, f, m, arg); }
	private void warn(final String c, final String f, final String m, final Object arg1, final Object arg2) { addLog(LOG_LEVEL_WARN, c, f, m, arg1, arg2); }
	private void warn(final String c, final String f, final String m, final Object [] args) { addLog(LOG_LEVEL_WARN, c, f, m, args); }
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i#warn(java.lang.String)
	 */
	@Override public void warn(final String log) { addLog(LOG_LEVEL_WARN, log); }
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i#warn(java.lang.String, java.lang.Throwable)
	 */
	@Override public void warn(final String m, final Throwable t) { addLog(LOG_LEVEL_WARN, m, t); }
	
	private void error(final String c, final String f, final String m) { addLog(LOG_LEVEL_ERROR, c, f, m); }
	private void error(final String c, final String f, final String m, final Object arg) { addLog(LOG_LEVEL_ERROR, c, f, m, arg); }
	private void error(final String c, final String f, final String m, final Object arg1, Object arg2) { addLog(LOG_LEVEL_ERROR, c, f, m, arg1, arg2); }
	private void error(final String c, final String f, final String m, final Object[] args) { addLog(LOG_LEVEL_ERROR, c, f, m, args); }
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i#error(java.lang.String)
	 */
	@Override public void error(final String log) { addLog(LOG_LEVEL_ERROR, log); }
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i#error(java.lang.String, java.lang.Throwable)
	 */
	@Override public void error(final String m, final Throwable throwable) { addLog(LOG_LEVEL_ERROR, m, throwable); }
	
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i#fatal(java.lang.String)
	 */
	@Override public void fatal(final String m) { addLog(LOG_LEVEL_FATAL, m); }
	private void fatal(final String c, final String f, final String m) { addLog(LOG_LEVEL_FATAL, c, f, m);  }
	private void fatal(final String c, final String f, final String m, final Object arg) { addLog(LOG_LEVEL_FATAL, c, f, m, arg); }
	private void fatal(final String c, final String f, final String m, final Object arg1, final Object arg2) { addLog(LOG_LEVEL_FATAL, c, f, m, arg1, arg2); }
	private void fatal(final String c, final String f, final String m, final Object[] args) { addLog(LOG_LEVEL_FATAL, c, f, m, args); }
	
	private void addLog(final int l, final String c, final String f, String m) { addLog(l, c, f, m, null); }
	private void addLog(final int l, final String c, final String f, String m, final Object arg) { addLog(l, c, f, m, ( null != arg ) ? new Object[]{arg} : null); }
	private void addLog(final int l, final String c, final String f, String m, final Object arg1, Object arg2) { addLog(l, c, f, m, ( null != arg1 || null != arg2 ) ? new Object[]{arg1, arg2} : null); }
	private void addLog(final int l, final String c, final String f, String m, final Object[] args) { addLog(l, msg(c, f, m, args)); }
	private void addLog(final int l, final String m) { addLog(l, m, (Throwable) null); }
	private void addLog(final int l, final String m, final Throwable e) { if(isEnabled(l)) addLog(new LogRecord(this.getConfig().getCategory(), l, m, e)); }
	private void addLog(final LogRecord logRecord) { logger.log(logRecord); }

	public void logJSObject(final JavaScriptObject o) { nativeLogJSObject(o); }
	private static native void nativeLogJSObject(JavaScriptObject o)
	/*-{
	    console.log(o);
	}-*/;

	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i#begin(java.lang.String)
	 */
	@Override public void begin    (final String f) { begin(this.simpleName, f); }
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i#end(java.lang.String)
	 */
	@Override public void end      (final String f) { end(this.simpleName, f); }
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i#beginEnd(java.lang.String)
	 */
	@Override public void beginEnd (final String f) { beginEnd(this.simpleName, f); }
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i#beginEnd(java.lang.String, java.lang.String, java.lang.Object)
	 */
	@Override public void beginEnd (final String f, final String m, final Object arg1) { beginEnd(this.simpleName, f, m, arg1); }
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i#beginEnd(java.lang.String, java.lang.String, java.lang.Object, java.lang.Object)
	 */
	@Override public void beginEnd (final String f, final String m, final Object arg1, final Object arg2) { beginEnd(this.simpleName, f, m, arg1, arg2); }
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i#beginEnd(java.lang.String, java.lang.String, java.lang.Object[])
	 */
	@Override public void beginEnd (final String f, final String m, final Object[] args) { beginEnd(this.simpleName, f, m, args); }
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i#trace(java.lang.String, java.lang.String)
	 */
	@Override public void trace    (final String f, final String m) { trace(this.simpleName, f, m); }
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i#trace(java.lang.String, java.lang.String, java.lang.Object)
	 */
	@Override public void trace    (final String f, final String m, final Object arg1) { trace(this.simpleName, f, m, arg1); }
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i#trace(java.lang.String, java.lang.String, java.lang.Object, java.lang.Object)
	 */
	@Override public void trace    (final String f, final String m, final Object arg1, final Object arg2) { trace(this.simpleName, f, m, arg1, arg2); }
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i#trace(java.lang.String, java.lang.String, java.lang.Object[])
	 */
	@Override public void trace    (final String f, final String m, final Object[] args) { trace(this.simpleName, f, m, args); }
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i#info(java.lang.String, java.lang.String)
	 */
	@Override public void info     (final String f, final String m) { info(this.simpleName, f, m); }
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i#info(java.lang.String, java.lang.String, java.lang.Object)
	 */
	@Override public void info     (final String f, final String m, final Object arg1) { info(this.simpleName, f, m, arg1); }
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i#info(java.lang.String, java.lang.String, java.lang.Object, java.lang.Object)
	 */
	@Override public void info     (final String f, final String m, final Object arg1, final Object arg2) { info(this.simpleName, f, m, arg1, arg2); }
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i#info(java.lang.String, java.lang.String, java.lang.Object[])
	 */
	@Override public void info     (final String f, final String m, final Object[] args) { info(this.simpleName, f, m, args); }
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i#debug(java.lang.String, java.lang.String)
	 */
	@Override public void debug    (final String f, final String m) { debug(this.simpleName, f, m); }
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i#debug(java.lang.String, java.lang.String, java.lang.Object)
	 */
	@Override public void debug    (final String f, final String m, final Object arg1) { debug(this.simpleName, f, m, arg1); }
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i#debug(java.lang.String, java.lang.String, java.lang.Object)
	 */
	@Override public void debug    (final String f, final String m, final Object arg1, final Object arg2) { debug(this.simpleName, f, m, arg1, arg2); }
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i#debug(java.lang.String, java.lang.String, java.lang.Object[])
	 */
	@Override public void debug    (final String f, final String m, final Object[] args) { debug(this.simpleName, f, m, args); }
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i#warn(java.lang.String, java.lang.String)
	 */
	@Override public void warn     (final String f, final String m) { warn(this.simpleName, f, m); }
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i#warn(java.lang.String, java.lang.String, java.lang.Object)
	 */
	@Override public void warn     (final String f, final String m, final Object arg1) { warn(this.simpleName, f, m, arg1); }
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i#warn(java.lang.String, java.lang.String, java.lang.Object, java.lang.Object)
	 */
	@Override public void warn     (final String f, final String m, final Object arg1, final Object arg2) { warn(this.simpleName, f, m, arg1, arg2); }
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i#warn(java.lang.String, java.lang.String, java.lang.Object[])
	 */
	@Override public void warn     (final String f, final String m, final Object[] args) { warn(this.simpleName, f, m, args); }
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i#error(java.lang.String, java.lang.String)
	 */
	@Override public void error    (final String f, final String m) { error(this.simpleName, f, m); }
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i#error(java.lang.String, java.lang.String, java.lang.Object)
	 */
	@Override public void error    (final String f, final String m, final Object arg1) { error(this.simpleName, f, m, arg1); }
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i#error(java.lang.String, java.lang.String, java.lang.Object, java.lang.Object)
	 */
	@Override public void error    (final String f, final String m, final Object arg1, final Object arg2) { error(this.simpleName, f, m, arg2); }
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i#error(java.lang.String, java.lang.String, java.lang.Object[])
	 */
	@Override public void error    (final String f, final String m, final Object[] args) { error(this.simpleName, f, m, args); }
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i#fatal(java.lang.String, java.lang.String)
	 */
	@Override public void fatal    (final String f, final String m) { fatal(this.simpleName, f, m); }
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i#fatal(java.lang.String, java.lang.String, java.lang.Object)
	 */
	@Override public void fatal    (final String f, final String m, final Object arg1) { fatal(this.simpleName, f, m, arg1); }
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i#fatal(java.lang.String, java.lang.String, java.lang.Object, java.lang.Object)
	 */
	@Override public void fatal    (final String f, final String m, final Object arg1, final Object arg2) { fatal(this.simpleName, f, m, arg1, arg2); }
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i#fatal(java.lang.String, java.lang.String, java.lang.Object[])
	 */
	@Override public void fatal    (final String f, final String m, final Object[] args) { fatal(this.simpleName, f, m, args); }
}
