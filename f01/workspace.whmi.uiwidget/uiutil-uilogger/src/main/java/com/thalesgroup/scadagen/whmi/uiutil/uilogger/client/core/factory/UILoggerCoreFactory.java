package com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.core.factory;

import java.util.HashMap;
import java.util.Map;

import com.allen_sauer.gwt.log.client.ConsoleLogger;
import com.allen_sauer.gwt.log.client.DivLogger;
import com.allen_sauer.gwt.log.client.GWTLogger;
import com.allen_sauer.gwt.log.client.Logger;
import com.allen_sauer.gwt.log.client.NullLogger;
import com.allen_sauer.gwt.log.client.RemoteLogger;
import com.allen_sauer.gwt.log.client.SystemLogger;
import com.allen_sauer.gwt.log.client.WindowLogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.core.LogWrapper;

/**
 * SCADAgen logger factory for creation
 * @author t0096643
 *
 */
public class UILoggerCoreFactory implements UILoggerCoreFactory_i {

	private static UILoggerCoreFactory instance = null;
	private UILoggerCoreFactory () {}
	public static UILoggerCoreFactory getInstance() {
		if ( null == instance ) { instance = new UILoggerCoreFactory(); }
		return instance;
	}
	
	private String coreLoggerName = "LogWrapper";
	public void setCoreLogger(String coreLoggerName) { this.coreLoggerName = coreLoggerName; }
	public String getCoreLoggerName() { return this.coreLoggerName; }
	
	private Map<String, Logger> loggers = new HashMap<String, Logger>();
	public void addLogger(String name, Logger logger) { this.loggers.put(name, logger); }
	public void remoteLogger(String name) { this.loggers.remove(name); }

	public Logger getLogger() { return getLogger(getCoreLoggerName()); }
	
	public Logger getLogger(String name) {
		Logger logger = null;
		if(null!=loggers) { logger = loggers.get(name); }
		if(null==logger) { logger = getDefaultSupportedLogger(name); }
		return ((null!=logger)?logger:new LogWrapper());
	}
	
	private Logger getDefaultSupportedLogger(String name) {
		Logger logger = null;
		if(null!=name) {
			if( 0 == DefaultSupportedLoggerName.ConsoleLogger.toString().compareTo(name)) {
				logger = new ConsoleLogger();
			} else if( 0 == DefaultSupportedLoggerName.DivLogger.toString().compareTo(name)) {
				logger = new DivLogger();
			} else if( 0 == DefaultSupportedLoggerName.GWTLogger.toString().compareTo(name)) {
				logger = new GWTLogger();
			} else if( 0 == DefaultSupportedLoggerName.LogWrapper.toString().compareTo(name)) {
				logger = new LogWrapper();
			} else if( 0 == DefaultSupportedLoggerName.NullLogger.toString().compareTo(name)) {
				logger = new NullLogger();
			} else if( 0 == DefaultSupportedLoggerName.RemoteLogger.toString().compareTo(name)) {
				logger = new RemoteLogger();
			} else if( 0 == DefaultSupportedLoggerName.SystemLogger.toString().compareTo(name)) {
				logger = new SystemLogger();
			} else if( 0 == DefaultSupportedLoggerName.WindowLogger.toString().compareTo(name)) {
				logger = new WindowLogger();
			}
		}
		return logger;
	}
}
