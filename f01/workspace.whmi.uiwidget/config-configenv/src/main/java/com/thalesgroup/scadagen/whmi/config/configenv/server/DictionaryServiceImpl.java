package com.thalesgroup.scadagen.whmi.config.configenv.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.thalesgroup.scadagen.whmi.config.config.shared.Dictionary;
import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionaryService;
import com.thalesgroup.scadagen.whmi.config.configenv.shared.DictionaryCacheInterface;
import com.thalesgroup.scadagen.whmi.config.configenv.shared.DictionaryCacheInterface.ConfigurationType;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class DictionaryServiceImpl extends RemoteServiceServlet implements DictionaryService {
	
	private final String className = "DictionaryServiceImpl";
	private final String logPrefix = "["+className+"] ";

	public Dictionary dictionaryServer(String mode, String module, String folder, String xmlFile, String tag) {
		final String function = "dictionaryServer";
		
		System.out.println(logPrefix+function+" Begin");
		
		System.out.println(logPrefix+function+" module["+module+"] ["+xmlFile+"] tag["+tag+"]");
		
		Dictionary dictionary = new Dictionary();
		
		if ( mode.equals(ConfigurationType.XMLFile.toString()) ) {
		
			if ( null == module ) {
				module = getServletContext().getInitParameter("project.dictionary.module");
	//			System.out.println(logPrefix+function+" module["+module+"]");
			}
	
			String base = getServletContext().getRealPath("/");
			String path = base + File.separator + module + File.separator + folder + File.separator + xmlFile;
			
	//		System.out.println(logPrefix+function+" base["+base+"]");
	//		System.out.println(logPrefix+function+" module["+module+"]");
	//		System.out.println(logPrefix+function+" xmlFile["+xmlFile+"]");
	//		System.out.println(logPrefix+function+" path["+path+"]");
			
			dictionary.setAttribute(DictionaryCacheInterface.XMLAttribute.FileName.toString(), xmlFile);
			dictionary.setAttribute(DictionaryCacheInterface.XMLAttribute.Tag.toString(), tag);
			dictionary.setAttribute(DictionaryCacheInterface.XMLAttribute.DateTime.toString()
					, new SimpleDateFormat(DictionaryCacheInterface.strDateTimeFormat).format(new Date()));
			
			List<Dictionary> cfgs = new ReadConfigXML().getDictionary(path, tag);
			for(Dictionary cfg: cfgs) {
				dictionary.addValue(cfg, cfg);
			}
			
	//		System.out.println(logPrefix+function+" XmlFile["+dictionary.getAttribute(DictionaryCacheInterface.XmlFile)+"] XmlTag["+dictionary.getAttribute(DictionaryCacheInterface.XmlTag)+"]  CreateDateTimeLabel["+dictionary.getAttribute(DictionaryCacheInterface.CreateDateTimeLabel)+"]");
			
		} else if ( mode.equals(ConfigurationType.PropertiesFile.toString()) ) {
			if ( null == module ) {
				module = getServletContext().getInitParameter("project.dictionary.module");
	//			System.out.println(logPrefix+function+" module["+module+"]");
			}
	
			String base = getServletContext().getRealPath("/");
			String path = base + File.separator + module + File.separator + folder + File.separator + xmlFile;

			dictionary.setAttribute(DictionaryCacheInterface.PropertiesAttribute.FileName.toString(), xmlFile);
			dictionary.setAttribute(DictionaryCacheInterface.PropertiesAttribute.DateTime.toString()
					, new SimpleDateFormat(DictionaryCacheInterface.strDateTimeFormat).format(new Date()));
			
			List<Dictionary> cfgs = new ReadConfigINI().getDictionary(path, tag);
			for(Dictionary cfg: cfgs) {
				dictionary.addValue(cfg, cfg);
			}
			
	//		System.out.println(logPrefix+function+" XmlFile["+dictionary.getAttribute(DictionaryCacheInterface.XmlFile)+"] XmlTag["+dictionary.getAttribute(DictionaryCacheInterface.XmlTag)+"]  CreateDateTimeLabel["+dictionary.getAttribute(DictionaryCacheInterface.CreateDateTimeLabel)+"]");
			
		}
		
		System.out.println(logPrefix+function+" End");
		
		return dictionary;
	}

}
