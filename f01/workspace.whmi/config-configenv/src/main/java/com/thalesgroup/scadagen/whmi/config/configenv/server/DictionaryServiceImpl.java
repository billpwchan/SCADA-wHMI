package com.thalesgroup.scadagen.whmi.config.configenv.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.thalesgroup.scadagen.whmi.config.config.shared.Dictionary;
import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionaryService;
import com.thalesgroup.scadagen.whmi.config.configenv.shared.DictionaryCacheInterface;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class DictionaryServiceImpl extends RemoteServiceServlet implements DictionaryService {

	public Dictionary dictionaryServer(String module, String xmlFile, String tag) {
		
		System.out.println(" **** dictionaryServer module["+module+"] ["+xmlFile+"] tag["+tag+"]");
		
		if ( null == module ) {
			module = getServletContext().getInitParameter("project.dictionary.module");
			System.out.println(" **** dictionaryServer module["+module+"]");
		}

		String base = getServletContext().getRealPath("/");
		String path = base + File.separator + module + File.separator + xmlFile;
		
		System.out.println(" **** dictionaryServer base["+base+"]");
		System.out.println(" **** dictionaryServer module["+module+"]");
		System.out.println(" **** dictionaryServer xmlFile["+xmlFile+"]");
		System.out.println(" **** dictionaryServer path["+path+"]");
		
		Dictionary dictionary = new Dictionary();
		
		dictionary.setAttribute(DictionaryCacheInterface.XmlFile, xmlFile);
		dictionary.setAttribute(DictionaryCacheInterface.XmlTag, tag);
		dictionary.setAttribute(DictionaryCacheInterface.CreateDateTimeLabel, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
		
		List<Dictionary> cfgs = new ReadConfigXML().getDictionary(path, tag);
		for(Dictionary cfg: cfgs) {
			dictionary.setValue(cfg, cfg);
		}
		
		System.out.println(" **** dictionaryServer XmlFile["+dictionary.getAttribute(DictionaryCacheInterface.XmlFile)+"] XmlTag["+dictionary.getAttribute(DictionaryCacheInterface.XmlTag)+"]  CreateDateTimeLabel["+dictionary.getAttribute(DictionaryCacheInterface.CreateDateTimeLabel)+"]");
		
		return dictionary;
	}

}
