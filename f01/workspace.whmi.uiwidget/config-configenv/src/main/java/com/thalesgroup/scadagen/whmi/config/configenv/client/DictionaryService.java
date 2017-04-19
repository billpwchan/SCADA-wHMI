package com.thalesgroup.scadagen.whmi.config.configenv.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.thalesgroup.scadagen.whmi.config.config.shared.Dictionary_i;

@RemoteServiceRelativePath("dictionaryServiceServlet")
public interface DictionaryService extends RemoteService {
	Dictionary_i dictionaryServer(String mode, String module, String folder, String xmlFile, String tag) throws IllegalArgumentException;
}
