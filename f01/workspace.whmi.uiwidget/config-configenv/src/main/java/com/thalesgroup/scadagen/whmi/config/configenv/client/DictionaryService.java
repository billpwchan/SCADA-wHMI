package com.thalesgroup.scadagen.whmi.config.configenv.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.thalesgroup.scadagen.whmi.config.config.shared.Dictionary;

@RemoteServiceRelativePath("dictionaryServiceServlet")
public interface DictionaryService extends RemoteService {
	Dictionary dictionaryServer(String mode, String module, String folder, String xmlFile, String tag) throws IllegalArgumentException;
}
