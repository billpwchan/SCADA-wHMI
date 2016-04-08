package com.thalesgroup.scadagen.whmi.config.configenv.server;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.thalesgroup.scadagen.whmi.config.config.shared.Dictionary;
import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionariesService;
import com.thalesgroup.scadagen.whmi.config.configenv.shared.DictionariesCacheInterface;
import com.thalesgroup.scadagen.whmi.config.configenv.shared.DictionaryCacheInterface;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class DictionariesServiceImpl extends RemoteServiceServlet implements DictionariesService {

	public Dictionary dictionariesServer(String module, String folder, String extension) {
		
		System.out.println(" **** dictionariesServer Begin");
		
		System.out.println(" **** dictionariesServer module["+module+"] folder["+folder+"] extension["+extension+"]");
		
		if ( null == module ) {
			module = getServletContext().getInitParameter("project.dictionaries.module");
			System.out.println(" **** dictionariesServer module["+module+"]");
		}

		Dictionary dictionaries = new Dictionary();
		
		String base = getServletContext().getRealPath("/");
		
		ReadConfigs configs = new ReadConfigs();
		configs.setConfigPathExtension(base + File.separator + module, folder, extension, false);
		List<File> files = configs.getConfigNames();
		
		Iterator<File> fileIterator = files.iterator();
		while ( fileIterator.hasNext() ) {
			File file = fileIterator.next();
			String path = file.getPath();
			
			ReadConfigXML readConfig = new ReadConfigXML();
			List<String> tags = readConfig.getTags(path);
			
			Iterator<String> tagIterator = tags.iterator();
			
			while ( tagIterator.hasNext() ) {
				String tag = tagIterator.next();
				
				System.out.println(" **** dictionaryServer path["+path+"] tag["+tag+"]");

//				String path = base + File.separator + module + File.separator + xmlFile;
				
				int index = path.lastIndexOf(File.pathSeparator);
				String xmlFile = path.substring(index+1);
				
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
				
				dictionaries.setAttribute(DictionariesCacheInterface.XmlType, "Dictionaries");
				dictionaries.setAttribute(DictionariesCacheInterface.XmlFile, "xmlFile");
				dictionaries.setAttribute(DictionariesCacheInterface.CreateDateTimeLabel, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
				dictionaries.setValue(DictionariesCacheInterface.Dictionary, dictionary);
			}
			
		}
		
		System.out.println(" **** dictionariesServer End");
		
		return dictionaries;

	}

}
