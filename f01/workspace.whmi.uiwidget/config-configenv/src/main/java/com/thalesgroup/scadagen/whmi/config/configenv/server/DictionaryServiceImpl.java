package com.thalesgroup.scadagen.whmi.config.configenv.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.thalesgroup.scadagen.whmi.config.config.shared.Dictionary;
import com.thalesgroup.scadagen.whmi.config.config.shared.Dictionary_i;
import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionaryService;
import com.thalesgroup.scadagen.whmi.config.configenv.shared.DictionaryCacheInterface;
import com.thalesgroup.scadagen.whmi.config.configenv.shared.DictionaryCacheInterface.ConfigurationType;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class DictionaryServiceImpl extends RemoteServiceServlet implements DictionaryService {
	
	private Logger logger					= LoggerFactory.getLogger(DictionaryServiceImpl.class.getSimpleName());

	public Dictionary_i dictionaryServer(String mode, String module, String folder, String xmlFile, String tag) {
		
		logger.debug("Begin");
		
		logger.debug("module[{}] [{}] tag[{}]", new Object[]{module, xmlFile, tag});
		
		Dictionary_i dictionary = new Dictionary();
		
		if ( mode.equals(ConfigurationType.XMLFile.toString()) ) {
		
			if ( null == module ) {
				module = getServletContext().getInitParameter("project.dictionary.module");
	//			logger.debug("module[{}]", module);
			}
	
			String base = getServletContext().getRealPath("/");
			String path = base + File.separator + module + File.separator + folder + File.separator + xmlFile;
			
	//		logger.debug("base[{}]", base);
	//		logger.debug("module[{}]", module);
	//		logger.debug("xmlFile[{}]", xmlFile);
	//		logger.debug("path[{}]", path);
			
			dictionary.setAttribute(DictionaryCacheInterface.XMLAttribute.FileName.toString(), xmlFile);
			dictionary.setAttribute(DictionaryCacheInterface.XMLAttribute.Tag.toString(), tag);
			dictionary.setAttribute(DictionaryCacheInterface.XMLAttribute.DateTime.toString()
					, new SimpleDateFormat(DictionaryCacheInterface.strDateTimeFormat).format(new Date()));
			
			List<Dictionary_i> cfgs = new ReadConfigXML().getDictionary(path, tag);
			for(Dictionary_i cfg: cfgs) {
				dictionary.addValue(cfg, cfg);
			}
			
	//		logger.debug("XmlFile["+dictionary.getAttribute(DictionaryCacheInterface.XmlFile)+"] XmlTag["+dictionary.getAttribute(DictionaryCacheInterface.XmlTag)+"]  CreateDateTimeLabel["+dictionary.getAttribute(DictionaryCacheInterface.CreateDateTimeLabel)+"]");
			
		} else if ( mode.equals(ConfigurationType.PropertiesFile.toString()) ) {
			if ( null == module ) {
				module = getServletContext().getInitParameter("project.dictionary.module");
	//			logger.debug("module[{}]", module);
			}
	
			String base = getServletContext().getRealPath("/");
			String path = base + File.separator + module + File.separator + folder + File.separator + xmlFile;

			dictionary.setAttribute(DictionaryCacheInterface.PropertiesAttribute.FileName.toString(), xmlFile);
			dictionary.setAttribute(DictionaryCacheInterface.PropertiesAttribute.DateTime.toString()
					, new SimpleDateFormat(DictionaryCacheInterface.strDateTimeFormat).format(new Date()));
			
			List<Dictionary_i> cfgs = new ReadConfigINI().getDictionary(path, tag);
			for(Dictionary_i cfg: cfgs) {
				dictionary.addValue(cfg, cfg);
			}
			
		}
		
		logger.debug("End");
		
		return dictionary;
	}

}
