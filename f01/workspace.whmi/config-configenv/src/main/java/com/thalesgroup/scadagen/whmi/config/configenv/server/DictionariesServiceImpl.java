package com.thalesgroup.scadagen.whmi.config.configenv.server;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.thalesgroup.scadagen.whmi.config.config.shared.Dictionary;
import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionariesService;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class DictionariesServiceImpl extends RemoteServiceServlet implements DictionariesService {

	public Dictionary dictionariesServer(String module, String path, String extension) {
		
		System.out.println(" **** dictionariesServer Begin");
		
		System.out.println(" **** dictionariesServer module["+module+"] path["+path+"] extension["+extension+"]");

		Dictionary dictionaries = new Dictionary();
		
		ReadConfigs configs = new ReadConfigs();
		configs.setConfigPathExtension(module, path, extension, false);
		List<File> files = configs.getConfigNames();
		
		Iterator<File> fileIterator = files.iterator();
		while ( fileIterator.hasNext() ) {
			File file = fileIterator.next();
			String xmlFile = file.getPath();
			
			ReadConfigXML readConfig = new ReadConfigXML();
			List<String> tags = readConfig.getTags(xmlFile);
			
			Iterator<String> tagIterator = tags.iterator();
			
			while ( tagIterator.hasNext() ) {
				String tag = tagIterator.next();
				
				Dictionary dictionary = new Dictionary();
				
				dictionary.setAttribute("XmlFile", xmlFile);
				dictionary.setAttribute("XmlTag", tag);
				dictionary.setAttribute("CreateDateTimeLabel", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
				System.out.println(" **** dictionariesServer configs.getXmlFile()["+dictionary.getAttribute("XmlFile")+"] configs.getCreateDateTimeLabel()["+dictionary.getAttribute("CreateDateTimeLabel")+"]");
				
				String folder = getServletContext().getRealPath("/") + "/" + module + "/"+ path + "/" + xmlFile;
				
				System.out.println(" **** dictionariesServer path to read["+folder+"]");
				
				List<Dictionary> cfgs = readConfig.getDictionary(folder, tag);
				
				for(Dictionary cfg: cfgs) {
					dictionary.setValue(cfg, cfg);
				}
				
				dictionaries.setAttribute("XmlType", "Dictionaries");
				dictionaries.setAttribute("XmlFile", "xmlFile");
				dictionaries.setAttribute("CreateDateTimeLabel", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
				dictionaries.setValue("Dictionary", dictionary);
			}
			
		}
		
		System.out.println(" **** dictionariesServer End");
		
		return dictionaries;

	}

}
