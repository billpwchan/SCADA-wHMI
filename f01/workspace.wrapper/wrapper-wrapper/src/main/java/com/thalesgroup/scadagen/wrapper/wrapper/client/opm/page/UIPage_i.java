package com.thalesgroup.scadagen.wrapper.wrapper.client.opm.page;

public interface UIPage_i {

	/**
	 * Log-in into spring framework, Authentication bean setting in the applicationContext-security.xml configuration file. 
	 * 
	 * @param operator Operator name for authentication.
	 * @param password Password for authentication 
	 */
	void login(String operator, String password);

	/**
	 * Log-out the current spring framework session, bean setting in the applicationContext-security.xml configuration file.
	 */
	void logout();

	/**
	 * Reload the page.
	 */	
	void reloadPage();
	
	void init();
}
