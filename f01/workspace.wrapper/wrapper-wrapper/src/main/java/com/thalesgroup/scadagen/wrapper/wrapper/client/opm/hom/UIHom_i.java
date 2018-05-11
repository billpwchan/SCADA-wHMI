package com.thalesgroup.scadagen.wrapper.wrapper.client.opm.hom;

import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i.CheckAccessWithHOMEvent_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i.GetCurrentHOMValueEvent_i;

public interface UIHom_i {
	
	boolean checkHom(final int hdvValue, final String key);
	
	void checkAccessWithHom(
			  final String function, final String location, final String action, final String mode
			, final String scsEnvId, final String dbAddress
			, final UIOpm_i uiOpm_i
			, final CheckAccessWithHOMEvent_i resultEvent);
	
	void checkAccessWithHom(
			final String function, final String location, final String action, final String mode
			, final int hdvValue, final String key
			, final UIOpm_i uiOpm_i
			, final CheckAccessWithHOMEvent_i resultEvent);
	
	boolean checkAccessWithHom(
			final String function, final String location, final String action, final String mode
			, final int hdvValue, final UIOpm_i uiOpm_i);

	/**
	 * Get Current HOM value from the RTDB
	 * 
	 * @param scsEnvId  RTDB ScsEnvId to read the hdv.
	 * @param dbAddress RTDB Alias to read the hdv.
	 * @param event     Return the Integer value
	 */
	void getCurrentHOMValue(final String scsEnvId, final String alias, final GetCurrentHOMValueEvent_i getCurrentHOMValueEvent_i);
	
	/**
	 * Check the action value is HOM action
	 * 
	 * @param action Action key to check
	 * @return Return ture if the action is HOM Action, otherwise return false
	 */
	boolean isHOMAction(String action);
	
	/**
	 * Check the action value is By Pass Value
	 * @param value
	 * @return Return true if the action is by pass value, otherwise return false
	 */
	boolean isBypassValue(int value);
	
	/**
	 * @return Current HOM Identity
	 */
	String getHOMIdentityType();
	
	/**
	 * @return Current HOM Identity
	 */
	String getHOMIdentity(final UIOpm_i uiOpm_i);

	/**
	 * Init the UIControlPriority instance
	 */
	void init();

}
