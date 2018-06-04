package com.thalesgroup.scadagen.whmi.uiutil.uilogger.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UILogger implements UILogger_i {
	
	private String name                     = null;
//	private String simpleName               = null;
	
	private Logger logger					= null;
	
	public UILogger(final String name) { 
		this.name = name; 
//		this.simpleName = UILoggerUtil.getClassSimpleName(name);
		logger					= LoggerFactory.getLogger(name);
	}

	@Override public
	String getName() { return this.name; }
	
	@Override public
	boolean isTraceEnabled() { return logger.isTraceEnabled(); }
	
	@Override public
	boolean isInfoEnabled() { return logger.isInfoEnabled(); }
	
	@Override public
	boolean isDebugEnabled() { return logger.isDebugEnabled(); }

	@Override public
	void trace(String message) { logger.trace(message); }
	
	@Override public
	void trace(String message, Object arg) { logger.trace(message, arg); }
	
	@Override public
	void trace(String message, Object arg1, Object arg2) { logger.trace(message, arg1, arg2); }
	
	@Override public
	void trace(String message, Object[] args) { logger.trace(message, args); }

	@Override public
	void info(String message) { logger.info(message); }
	
	@Override public
	void info(String message, Object arg) { logger.info(message, arg); }
	
	@Override public
	void info(String message, Object arg1, Object arg2) { logger.info(message, arg1, arg2); }
	
	@Override public
	void info(String message, Object[] args) { logger.info(message, args); }

	@Override public
	void debug(String message) { logger.debug(message); }
	
	@Override public
	void debug(String message, Object arg) { logger.debug(message, arg); }
	
	@Override public
	void debug(String message, Object arg1, Object arg2) { logger.debug(message, arg1, arg2); }
	
	@Override public
	void debug(String message, Object[] args) { logger.debug(message, args); }

	@Override public
	void debug(String message, Throwable throwable) { logger.debug(message, throwable); }

	@Override public
	void warn(String message) { logger.warn(message); }
	
	@Override public
	void warn(String message, Object arg) { logger.warn(message, arg); }
	
	@Override public
	void warn(String message, Object arg1, Object arg2) { logger.warn(message, arg1, arg2); }

	@Override public
	void warn(String message, Throwable throwable) { logger.warn(message, throwable); }

	@Override public
	void error(String message) { logger.error(message); }
	
	@Override public
	void error(String message, Object arg) { logger.error(message, arg); }
	
	@Override public
	void error(String message, Object arg1, Object arg2) { logger.error(message, arg1, arg2); }

	@Override public
	void error(String message, Throwable throwable) { logger.error(message, throwable); }
}
