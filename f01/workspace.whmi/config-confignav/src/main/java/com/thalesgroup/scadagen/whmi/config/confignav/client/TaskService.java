package com.thalesgroup.scadagen.whmi.config.confignav.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.thalesgroup.scadagen.whmi.config.confignav.shared.Tasks;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("taskServiceServlet")
public interface TaskService extends RemoteService {
	Tasks taskServer(String module, String strMapping, String strSetting, String profile, String location, int level, String header) throws IllegalArgumentException;
}
