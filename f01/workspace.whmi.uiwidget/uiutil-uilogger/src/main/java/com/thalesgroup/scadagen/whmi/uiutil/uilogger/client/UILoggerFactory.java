package com.thalesgroup.scadagen.whmi.uiutil.uilogger.client;

/**
 * SCADAgen logger factory for creation
 * @author t0096643
 *
 */
public class UILoggerFactory implements UILoggerFactory_i {

	private static UILoggerFactory instance = null;
	private UILoggerFactory () {}
	public static UILoggerFactory getInstance() {
		if ( null == instance ) instance = new UILoggerFactory();
		return instance;
	}
	
	/**
	 * Return this UILogger instance
	 * 
	 * @Deprecated: Should be using getUILogger instead of getLogger
	 * 
	 * @param namespace
	 * @return new UILogger instance
	 */
	@Deprecated
	public UILogger getLogger(String namespace) { return new UILogger(namespace); }
	
	private String loggerName = UILoggerNull.class.getSimpleName();
	public void setDefaultLogger(String loggerName) { this.loggerName = loggerName; }
	public String getDefaultLoggerName() { return this.loggerName; }
	
	@Override
	public UILogger_i getUILogger(String namespace) { return getUILogger(this.loggerName, namespace); }
	
	@Override
	public UILogger_i getUILogger(String logger, String namespace) {
		UILogger_i ret = null;
		if(null!=logger) {
			if(0 == UILoggerNull.class.getSimpleName().compareTo(logger)) {
				ret = new UILoggerNull(namespace);
			} else if(0 == UILoggerEx.class.getSimpleName().compareTo(logger)) {
				ret = new UILoggerEx(namespace);
			}
		}
		return ((null!=ret)? ret : new UILogger(namespace)); 
	}
}
