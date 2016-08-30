package com.thalesgroup.scadagen.whmi.config.configenv.server;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

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
	
	private final String className = "DictionariesServiceImpl";
	private final String logPrefix = "["+className+"] ";
	
	public Dictionary dictionariesServer(String mode, String module, String folder, String extension, String tag) {
		final String function = "dictionariesServer";
		
		System.out.println(logPrefix+function+" Begin");
		
		System.out.println(logPrefix+function+" mode["+mode+"] module["+module+"] folder["+folder+"] extension["+extension+"] tag["+tag+"]");
		
		if ( null == module ) {
			module = getServletContext().getInitParameter("project.dictionaries.module");
			System.out.println(logPrefix+function+" module["+module+"]");
		}

		Dictionary dictionaries = new Dictionary();
		
		String base = getServletContext().getRealPath("/");
		
		System.out.println(logPrefix+function+" base["+base+"]");
		
		ReadFiles configs = new ReadFiles();
		configs.setFilePathExtension(base + File.separator + module, folder, extension, false);
		List<File> files = configs.getFiles();
		
		System.out.println(logPrefix+function+" files.size()["+files.size()+"]");
		
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
	
				System.out.println(logPrefix+function+" Loop path["+path+"] xmlFile["+filename+"] tag["+tag+"]");
	
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
					
				System.out.println(logPrefix+function+" dictionary.getValueKeys().size()["+dictionary.getValueKeys().size()+"]");
				
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
	
				System.out.println(logPrefix+function+" Loop path["+path+"] xmlFile["+filename+"] tag["+tag+"]");
	
				Dictionary dictionary = new Dictionary();
					
				dictionary.setAttribute(DictionaryCacheInterface.PropertiesAttribute.FileName.toString(), filename);
				dictionary.setAttribute(DictionaryCacheInterface.PropertiesAttribute.DateTime.toString()
						, new SimpleDateFormat(DictionaryCacheInterface.strDateTimeFormat).format(new Date()));
					
				List<Dictionary> cfgs = new ReadConfigINI().getDictionary(path, tag);
				for(Dictionary cfg: cfgs) {
					dictionary.addValue(cfg, cfg);
				}
					
				dictionaries.addValue(dictionary, dictionary);
					
				System.out.println(logPrefix+function+" dictionary.getValueKeys().size()["+dictionary.getValueKeys().size()+"]");
				
			}
		}
		
		System.out.println(logPrefix+function+" dictionaries.getValueKeys().size()["+dictionaries.getValueKeys().size()+"]");
		
		System.out.println(logPrefix+function+" End");
		
		return dictionaries;

	}

}
