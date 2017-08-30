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

	void getCurrentHOMValue(final String scsEnvId, final String alias, final GetCurrentHOMValueEvent_i getCurrentHOMValueEvent_i);
	
	boolean isHOMAction(String action);
	
	boolean isBypassValue(int value);

	/**
	 * Init the UIControlPriority instance
	 */
	void init();

}
