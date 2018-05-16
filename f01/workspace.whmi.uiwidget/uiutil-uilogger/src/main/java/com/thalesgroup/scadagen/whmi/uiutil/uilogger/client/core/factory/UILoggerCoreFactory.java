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
 * @author syau
 *
 */
public class UILoggerCoreFactory implements UILoggerCoreFactory_i {

	private static UILoggerCoreFactory instance = null;
	private UILoggerCoreFactory () {}
	public static UILoggerCoreFactory getInstance() {
		if ( null == instance ) instance = new UILoggerCoreFactory();
		return instance;
	}
	
	private String defaultLoggerName = "LogWrapper";
	public void setDefaultLogger(String defaultLoggerName) { this.defaultLoggerName = defaultLoggerName; }
	public String getDefaultLoggerName() { return this.defaultLoggerName; }
	
	public Map<String, Logger> loggers = new HashMap<String, Logger>();
	public void addLogger(String name, Logger logger) { this.loggers.put(name, logger); }
	public void remoteLogger(String name) { this.loggers.remove(name); }

	public Logger getLogger() { return getLogger(getDefaultLoggerName()); }
	public Logger getLogger(String name) {
		Logger logger = loggers.get(name);
		if(null==logger && null!=name) {
			switch(name) {
			case "ConsoleLogger":	logger = new ConsoleLogger();	break;
			case "DivLogger":		logger = new DivLogger();		break;
			case "GWTLogger":		logger = new GWTLogger();		break;
			case "LogWrapper":		logger = new LogWrapper();		break;
			case "NullLogger":		logger = new NullLogger();		break;
			case "RemoteLogger":	logger = new RemoteLogger();	break;
			case "SystemLogger":	logger = new SystemLogger();	break;
			case "WindowLogger":	logger = new WindowLogger();	break;
			}
		}
		return ((null!=logger)?logger:new LogWrapper());
	}
}
