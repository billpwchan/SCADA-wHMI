package com.thalesgroup.scadagen.whmi.config.confignav.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.thalesgroup.scadagen.whmi.config.confignav.shared.Tasks;

/**
 * The async counterpart of <code>TaskMgrService</code>.
 */
public interface TaskServiceAsync {
	void taskServer(String module, String mappingFile, String settingFile, String profile, String location, int level, String header, AsyncCallback<Tasks> callback)
			throws IllegalArgumentException;
}
