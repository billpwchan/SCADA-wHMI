package com.thalesgroup.scadagen.whmi.config.confignav.client;

import com.thalesgroup.scadagen.whmi.config.confignav.shared.Tasks;

public interface TaskMgrEvent {
	
	public void ready(Tasks tasks);
	public void failed();
	
}
