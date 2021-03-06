package com.thalesgroup.scadagen.whmi.config.configenv.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.thalesgroup.scadagen.whmi.config.config.shared.Dictionary_i;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("dictionariesServiceServlet")
public interface DictionariesService extends RemoteService {
	Dictionary_i dictionariesServer(String configType, String configPath, String folderName, String extension, String tag) throws IllegalArgumentException;
}
