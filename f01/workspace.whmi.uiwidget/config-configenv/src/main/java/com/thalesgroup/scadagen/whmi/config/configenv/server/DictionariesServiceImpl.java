package com.thalesgroup.scadagen.whmi.config.configenv.server;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.thalesgroup.scadagen.whmi.config.config.shared.Dictionary;
import com.thalesgroup.scadagen.whmi.config.config.shared.Dictionary_i;
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
	
	public Dictionary_i dictionariesServer(String configType, String configPath, String folderName, String extension, String tag) {
		
		logger.debug("Begin");
		
		logger.debug("configType[{}] configPath[{}] folderName[{}] extension[{}] tag[{}]", new Object[]{configType, configPath, folderName, extension, tag});
		
		if ( null == configPath ) {
			configPath = getServletContext().getInitParameter("project.dictionaries.module");
			logger.debug("configPath[{}]", configPath);
		}

		Dictionary_i dictionaries = new Dictionary();
		
		String realBase = getServletContext().getRealPath("/");
		
		logger.debug("realBase[{}]", realBase);
		
		final String basePath = realBase + File.separator + configPath;
		
		logger.debug("basePath[{}]", basePath);
		
		ReadFiles configs = new ReadFiles();
		
		configs.setFilePathExtension(basePath, folderName, extension);
//		configs.setFilePathExtension(basePath, folderName, extension, false);
		
		List<File> files = configs.getFiles();
		
		logger.debug("files.size()[{}]", files.size());
		
		if ( configType.equals(ConfigurationType.XMLFile.toString()) ) {
		
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
				
				Path fullPath = Paths.get(path);
				Path dir = Paths.get(basePath, folderName);
				Path subPath = dir.relativize(fullPath);
				
				logger.debug("Loop subPath[{}] dir[{}] fullPath[{}]", new Object[]{subPath, dir, fullPath});
				
				String relativePath = subPath.toString();
				
				logger.debug("Loop relativePath[{}] BF", relativePath);
				
				relativePath = relativePath.replaceAll(Matcher.quoteReplacement("\\"), "/");
				
				logger.debug("Loop relativePath[{}] AF", relativePath);
	
				Dictionary dictionary = new Dictionary();
				
				dictionary.setAttribute(DictionaryCacheInterface.XMLAttribute.FileSeparator.toString(), File.separator);
				dictionary.setAttribute(DictionaryCacheInterface.XMLAttribute.RelativePath.toString(), relativePath);
				dictionary.setAttribute(DictionaryCacheInterface.XMLAttribute.FileName.toString(), filename);
				dictionary.setAttribute(DictionaryCacheInterface.XMLAttribute.Tag.toString(), tag);
				dictionary.setAttribute(DictionaryCacheInterface.XMLAttribute.DateTime.toString()
						, new SimpleDateFormat(DictionaryCacheInterface.strDateTimeFormat).format(new Date()));
					
				List<Dictionary_i> cfgs = new ReadConfigXML().getDictionary(path, tag);
				for(Dictionary_i cfg: cfgs) {
					dictionary.addValue(cfg, cfg);
				}
					
				dictionaries.addValue(dictionary, dictionary);
					
				logger.debug("dictionary.getValueKeys().size()[{}]", dictionary.getValueKeys().size());
				
			}
		
		} else if ( configType.equals(ConfigurationType.PropertiesFile.toString()) ) {
			
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
				
				Path fullPath = Paths.get(path);
				Path dir = Paths.get(basePath, folderName);
				Path subPath = dir.relativize(fullPath);
				
				logger.debug("Loop subPath[{}] dir[{}] fullPath[{}]", new Object[]{subPath, dir, fullPath});
				
				String relativePath = subPath.toString();
				
				logger.debug("Loop relativePath[{}] BF", relativePath);
				
				relativePath = relativePath.replaceAll(Matcher.quoteReplacement("\\"), "/");
				
				logger.debug("Loop relativePath[{}] AF", relativePath);
	
				Dictionary_i dictionary = new Dictionary();
					
				dictionary.setAttribute(DictionaryCacheInterface.PropertiesAttribute.FileSeparator.toString(), File.separator);
				dictionary.setAttribute(DictionaryCacheInterface.PropertiesAttribute.RelativePath.toString(), relativePath);
				dictionary.setAttribute(DictionaryCacheInterface.PropertiesAttribute.FileName.toString(), filename);
				dictionary.setAttribute(DictionaryCacheInterface.PropertiesAttribute.DateTime.toString()
						, new SimpleDateFormat(DictionaryCacheInterface.strDateTimeFormat).format(new Date()));
					
				List<Dictionary_i> cfgs = new ReadConfigINI().getDictionary(path, tag);
				for(Dictionary_i cfg: cfgs) {
					dictionary.addValue(cfg, cfg);
				}
					
				dictionaries.addValue(dictionary, dictionary);
					
				logger.debug("dictionary.getValueKeys().size()[{}]", dictionary.getValueKeys().size());
				
			}
		} else if ( configType.equals(ConfigurationType.JsonFile.toString()) ) {
			
			dictionaries.setAttribute(DictionaryCacheInterface.strContainerType, ContainerType.Dictionaries.toString());
			dictionaries.setAttribute(DictionaryCacheInterface.strConfigurationType, ConfigurationType.JsonFile.toString());
			dictionaries.setAttribute(DictionaryCacheInterface.strCreateDateTimeLabel
					, new SimpleDateFormat(DictionaryCacheInterface.strDateTimeFormat).format(new Date()));		
			
			Iterator<File> fileIterator = files.iterator();
			while ( fileIterator.hasNext() ) {
				File file = fileIterator.next();
				String path = file.getPath();
				String filename = file.getName();
	
				logger.debug("Loop path[{}] xmlFile[{}] tag[{}]", new Object[]{path, filename, tag});
				
				Path fullPath = Paths.get(path);
				Path dir = Paths.get(basePath, folderName);
				Path subPath = dir.relativize(fullPath);
				
				logger.debug("Loop subPath[{}] dir[{}] fullPath[{}]", new Object[]{subPath, dir, fullPath});
				
				String relativePath = subPath.toString();
				
				logger.debug("Loop relativePath[{}] BF", relativePath);
				
				relativePath = relativePath.replaceAll(Matcher.quoteReplacement("\\"), "/");
				
				logger.debug("Loop relativePath[{}] AF", relativePath);
	
				Dictionary_i dictionary = new Dictionary();
					
				dictionary.setAttribute(DictionaryCacheInterface.PropertiesAttribute.FileSeparator.toString(), File.separator);
				dictionary.setAttribute(DictionaryCacheInterface.PropertiesAttribute.RelativePath.toString(), relativePath);
				dictionary.setAttribute(DictionaryCacheInterface.PropertiesAttribute.FileName.toString(), filename);
				dictionary.setAttribute(DictionaryCacheInterface.PropertiesAttribute.DateTime.toString()
						, new SimpleDateFormat(DictionaryCacheInterface.strDateTimeFormat).format(new Date()));
					
				List<Dictionary_i> cfgs = new ReadConfigJson().getDictionary(path, tag);
				for(Dictionary_i cfg: cfgs) {
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
