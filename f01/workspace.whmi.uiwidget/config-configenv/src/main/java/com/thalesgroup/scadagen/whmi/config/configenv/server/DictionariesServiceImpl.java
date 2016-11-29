package com.thalesgroup.scadagen.whmi.config.configenv.server;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.thalesgroup.scadagen.whmi.config.config.shared.Dictionary;
import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionariesService;
import com.thalesgroup.scadagen.whmi.config.configenv.shared.DictionaryCacheInterface;
import com.thalesgroup.scadagen.whmi.config.configenv.shared.DictionaryCacheInterface.ConfigurationType;
import com.thalesgroup.scadagen.whmi.config.configenv.shared.DictionaryCacheInterface.ContainerType;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class DictionariesServiceImpl extends RemoteServiceServlet implements DictionariesService {
	
	private Logger logger					= LoggerFactory.getLogger(DictionariesServiceImpl.class.getName());
	
	public Dictionary dictionariesServer(String mode, String module, String folder, String extension, String tag) {
		
		logger.debug("Begin");
		
		
		logger.debug("mode[{}] module[{}] folder[{}] extension[{}] tag[{}]", new Object[]{mode, module, folder, extension, tag});
		
		if ( null == module ) {
			module = getServletContext().getInitParameter("project.dictionaries.module");
			logger.debug("module[{}]", module);
		}

		Dictionary dictionaries = new Dictionary();
		
		String base = getServletContext().getRealPath("/");
		
		logger.debug("base[{}]", base);
		
		ReadFiles configs = new ReadFiles();
		configs.setFilePathExtension(base + File.separator + module, folder, extension, false);
		List<File> files = configs.getFiles();
		
		logger.debug("files.size()[{}]", files.size());
		
		if ( mode.equals(ConfigurationType.XMLFile.toString()) ) {
		
			dictionaries.setAttribute(DictionaryCacheInterface.strContainerType, ContainerType.Dictionaries.toString());
			dictionaries.setAttribute(DictionaryCacheInterface.strConfigurationType, ConfigurationType.XMLFile.toString());
			dictionaries.setAttribute(DictionaryCacheInterface.strCreateDateTimeLabel
					, new SimpleDateFormat(DictionaryCacheInterface.strDateTimeFormat).format(new Date()));		
			
			Iterator<File> fileIterator = files.iterator();
			while ( fileIterator.hasNext() ) {
				File file = fileIterator.next();
				String path = file.getPath();
				String filename = file.getName();
	
				logger.debug("Loop path[{}] xmlFile[{}] tag[{}]", new Object[]{path, filename, tag});
	
				Dictionary dictionary = new Dictionary();
					
				dictionary.setAttribute(DictionaryCacheInterface.XMLAttribute.FileName.toString(), filename);
				dictionary.setAttribute(DictionaryCacheInterface.XMLAttribute.Tag.toString(), tag);
				dictionary.setAttribute(DictionaryCacheInterface.XMLAttribute.DateTime.toString()
						, new SimpleDateFormat(DictionaryCacheInterface.strDateTimeFormat).format(new Date()));
					
				List<Dictionary> cfgs = new ReadConfigXML().getDictionary(path, tag);
				for(Dictionary cfg: cfgs) {
					dictionary.addValue(cfg, cfg);
				}
					
				dictionaries.addValue(dictionary, dictionary);
					
				logger.debug("dictionary.getValueKeys().size()[{}]", dictionary.getValueKeys().size());
				
			}
		
		} else if ( mode.equals(ConfigurationType.PropertiesFile.toString()) ) {
			
			dictionaries.setAttribute(DictionaryCacheInterface.strContainerType, ContainerType.Dictionaries.toString());
			dictionaries.setAttribute(DictionaryCacheInterface.strConfigurationType, ConfigurationType.PropertiesFile.toString());
			dictionaries.setAttribute(DictionaryCacheInterface.strCreateDateTimeLabel
					, new SimpleDateFormat(DictionaryCacheInterface.strDateTimeFormat).format(new Date()));		
			
			Iterator<File> fileIterator = files.iterator();
			while ( fileIterator.hasNext() ) {
				File file = fileIterator.next();
				String path = file.getPath();
				String filename = file.getName();
	
				logger.debug("Loop path[{}] xmlFile[{}] tag[{}]", new Object[]{path, filename, tag});
	
				Dictionary dictionary = new Dictionary();
					
				dictionary.setAttribute(DictionaryCacheInterface.PropertiesAttribute.FileName.toString(), filename);
				dictionary.setAttribute(DictionaryCacheInterface.PropertiesAttribute.DateTime.toString()
						, new SimpleDateFormat(DictionaryCacheInterface.strDateTimeFormat).format(new Date()));
					
				List<Dictionary> cfgs = new ReadConfigINI().getDictionary(path, tag);
				for(Dictionary cfg: cfgs) {
					dictionary.addValue(cfg, cfg);
				}
					
				dictionaries.addValue(dictionary, dictionary);
					
				logger.debug("dictionary.getValueKeys().size()[{}]", dictionary.getValueKeys().size());
				
			}
		}
		
		logger.debug("dictionaries.getValueKeys().size()[{}]", dictionaries.getValueKeys().size());
		
		logger.debug("End");
		
		return dictionaries;

	}

}
