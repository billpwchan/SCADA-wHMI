package com.thalesgroup.scadagen.whmi.uiutil.uilogger.client;

/**
 * SCADAgen logger factory for creation
 * @author syau
 *
 */
public class UILoggerFactory implements UILoggerFactory_i {

	private static UILoggerFactory instance = null;
	private UILoggerFactory () {}
	public static UILoggerFactory getInstance() {
		if ( null == instance ) instance = new UILoggerFactory();
		return instance;
	}
	public UILogger getLogger(String namespace) { return new UILogger(namespace); }
	
	@Override
	public UILogger_i getUILogger(String namespace) { return getLogger(namespace); }
}
