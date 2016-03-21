package com.thalesgroup.scadagen.whmi.config.configenv.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.thalesgroup.scadagen.whmi.config.config.shared.Dictionary;
import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionaryService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class DictionaryServiceImpl extends RemoteServiceServlet implements DictionaryService {

	public Dictionary dictionaryServer(String module, String xmlFile, String tag) {
		
		System.out.println(" **** dictionaryServer ["+xmlFile+"] tag["+tag+"]");

		Dictionary dictionary = new Dictionary();
		
		dictionary.setAttribute("XmlFile", xmlFile);
		dictionary.setAttribute("XmlTag", tag);
		dictionary.setAttribute("CreateDateTimeLabel", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
		System.out.println(" **** dictionaryServer configs.getXmlFile()["+dictionary.getAttribute("XmlFile")+"] configs.getCreateDateTimeLabel()["+dictionary.getAttribute("CreateDateTimeLabel")+"]");
		
		String path = getServletContext().getRealPath("/") + "/" + module + "/config/"+xmlFile;
		
		System.out.println(" **** dictionaryServer path to read["+path+"]");
		
		ReadConfigXML readConfig = new ReadConfigXML();
		
		ArrayList<Dictionary> cfgs = readConfig.getDictionary(path, tag);
		
		for(Dictionary cfg: cfgs) {
			dictionary.setValue(cfg, cfg);
		}
		
		return dictionary;
	}

}
