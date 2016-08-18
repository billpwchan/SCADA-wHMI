package com.thalesgroup.scadagen.whmi.uiutil.uilogger.client;

public class UILoggerFactory {
	private static UILoggerFactory instance = null;
	private UILoggerFactory () {}
	public static UILoggerFactory getInstance() {
		if ( null == instance ) instance = new UILoggerFactory();
		return instance;
	}
	public static String LOGGER_DEFAULT = "UILogger";
	
	public UILogger getLogger() {
		return getLogger(LOGGER_DEFAULT);
	}
	public UILogger getLogger(String logger) {
		return new UILogger();
	}
}
