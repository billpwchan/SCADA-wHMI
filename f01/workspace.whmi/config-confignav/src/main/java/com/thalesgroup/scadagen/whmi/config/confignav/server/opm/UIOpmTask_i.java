package com.thalesgroup.scadagen.whmi.config.confignav.server.opm;

import com.thalesgroup.scadagen.whmi.config.confignav.shared.Task;
import com.thalesgroup.scadagen.wrapper.wrapper.server.opm.UIOpm_i;

public interface UIOpmTask_i {
	
	void setUIOpm_i(UIOpm_i uiOpm_i);
	
	/**
	 * @param task
	 * @return integer result:
	 * 							-1: Opm not valid
	 * 							 0: Opm configuration not available
	 * 							 1: Opm Valid
	 */
	int isValid(Task task);

}
