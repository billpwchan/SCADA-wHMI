package com.thalesgroup.scadagen.whmi.config.configenv.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.thalesgroup.scadagen.whmi.config.config.shared.Dictionary;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("dictionaryServiceServlet")
public interface DictionaryService extends RemoteService {
	Dictionary dictionaryServer(String module, String xmlFile, String tag) throws IllegalArgumentException;
}
