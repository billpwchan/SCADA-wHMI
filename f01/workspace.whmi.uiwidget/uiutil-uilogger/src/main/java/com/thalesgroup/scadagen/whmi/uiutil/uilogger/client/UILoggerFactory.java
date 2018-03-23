package com.thalesgroup.scadagen.whmi.uiutil.uilogger.client;

public class UILoggerFactory implements UILoggerFactory_i {

	private static UILoggerFactory instance = null;
	private UILoggerFactory () {}
	public static UILoggerFactory getInstance() {
		if ( null == instance ) instance = new UILoggerFactory();
		return instance;
	}
	public static String DEF_CLASSNAME            = "com.thalesgroup.scadagen.whmi";
	
	@Override
	public UILogger getLogger(String namespace) { return getLogger(namespace); }
}
