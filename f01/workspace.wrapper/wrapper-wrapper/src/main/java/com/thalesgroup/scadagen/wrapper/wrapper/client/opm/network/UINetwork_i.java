package com.thalesgroup.scadagen.wrapper.wrapper.client.opm.network;

import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.common.GetCurrentIpAddressCallback_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.common.GetCurrentHostNameCallback_i;

public interface UINetwork_i {

	/**
	 * Get the current client Host Name
	 * 
	 * @return Current client Host Name
	 */
	String getCurrentHostName();

	/**
	 * Get the current client Host Name
	 * 
	 * @param cb    Callback for the Async result: Current Client Host Name
	 */
	void getCurrentHostName(final GetCurrentHostNameCallback_i cb);

	/**
	 * Get the current client IP Address
	 * 
	 * @return Current Client IP Address
	 */
	String getCurrentIPAddress();

	/**
	 * Get the current client IP Address
	 * 
	 * @param cb    Callback for the Async result: Current Client IP Address
	 */
	void getCurrentIPAddress(final GetCurrentIpAddressCallback_i cb);

	void init();
}
