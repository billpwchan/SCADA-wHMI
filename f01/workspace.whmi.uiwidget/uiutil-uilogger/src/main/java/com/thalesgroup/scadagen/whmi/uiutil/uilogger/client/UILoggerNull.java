package com.thalesgroup.scadagen.whmi.uiutil.uilogger.client;

/**
 * NULL logger
 * 
 * @author t0096643
 *
 */
public class UILoggerNull implements UILogger_i {

	public UILoggerNull(String name) {}

	@Override public String getName() { return null; }

	@Override public void clear() {}

	@Override public void setCurrentLogLevel(int level) {}

	@Override public int getCurrentLogLevel() { return 0; }

	@Override public void trace(String message) {}

	@Override public void trace(String message, Throwable throwable) {}

	@Override public void debug(String message) {}

	@Override public void debug(Object message) {}

	@Override public void debug(String message, Throwable throwable) {}

	@Override public void error(String message) {}

	@Override public void error(String message, Throwable throwable) {}

	@Override public void info(String message) {}

	@Override public void warn(String message) {}
	
	@Override public void fatal(String message) {}

	@Override public void warn(String message, Throwable throwable) {}

	@Override public boolean isLevelEnabled(int level) { return false; }
	
	@Override public boolean isFatalEnabled() { return false; }
	
	@Override public boolean isErrorEnabled() { return false; }

	@Override public boolean isWarnEnabled() { return false; }

	@Override public boolean isInfoEnabled() { return false; }

	@Override public boolean isTraceEnabled() { return false; }

	@Override public boolean isDebugEnabled() { return false; }

	@Override public void begin(String function) {}

	@Override public void end(String function) {}

	@Override public void beginEnd(String function) {}
	
	@Override public void beginEnd(String function, String message) {}

	@Override public void beginEnd(String function, String message, Object arg1) {}

	@Override public void beginEnd(String function, String message, Object arg1, Object arg2) {}

	@Override public void beginEnd(String function, String message, Object[] args) {}

	@Override public void trace(String function, String message) {}

	@Override public void trace(String function, String message, Object arg1) {}

	@Override public void trace(String function, String message, Object arg1, Object arg2) {}

	@Override public void trace(String function, String message, Object[] args) {}

	@Override public void info(String function, String message) {}

	@Override public void info(String function, String message, Object arg1) {}

	@Override public void info(String function, String message, Object arg1, Object arg2) {}

	@Override public void info(String function, String message, Object[] args) {}

	@Override public void debug(String function, String message) {}

	@Override public void debug(String function, String message, Object arg1) {}

	@Override public void debug(String function, String message, Object arg1, Object arg2) {}

	@Override public void debug(String function, String message, Object[] args) {}

	@Override public void warn(String function, String message) {}

	@Override public void warn(String function, String message, Object arg1) {}

	@Override public void warn(String function, String message, Object arg1, Object arg2) {}

	@Override public void warn(String function, String message, Object[] args) {}

	@Override public void error(String function, String message) {}

	@Override public void error(String function, String message, Object arg1) {}

	@Override public void error(String function, String message, Object arg1, Object arg2) {}

	@Override public void error(String function, String message, Object[] args) {}

	@Override public void fatal(String function, String message) {}

	@Override public void fatal(String function, String message, Object arg1) {}

	@Override public void fatal(String function, String message, Object arg1, Object arg2) {}

	@Override public void fatal(String function, String message, Object[] args) {}
}
