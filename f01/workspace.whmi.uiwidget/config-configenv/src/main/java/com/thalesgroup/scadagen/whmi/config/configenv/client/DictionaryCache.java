package com.thalesgroup.scadagen.whmi.config.configenv.client;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.thalesgroup.scadagen.whmi.config.config.shared.Dictionary;
import com.thalesgroup.scadagen.whmi.config.configenv.shared.DictionaryCacheInterface;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;

public class DictionaryCache implements DictionaryMgrEvent {

	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());	
	
//	private static DictionaryCache instance = null;
//	private DictionaryCache () {}
//	public static DictionaryCache getInstance() {
//		if ( null == instance ) { instance = new DictionaryCache(); }
//		return instance;
//	}
	
	private static Map<String, DictionaryCache> instances = new HashMap<String, DictionaryCache>();
	private DictionaryCache () {}
	public static DictionaryCache getInstance(String key) {
		if ( ! instances.containsKey(key) ) {	instances.put(key, new DictionaryCache()); }
		DictionaryCache instance = instances.get(key);
		return instance;
	}
		
	private List<String> incoming		= new LinkedList<String>();
	private List<String> waiting		= new LinkedList<String>();
	private List<String> fails		= new LinkedList<String>();
	
	private List<DictionaryMgr> dictionaryMgrs = new LinkedList<DictionaryMgr>();
	
	private Map <String, Dictionary> dictionary = new HashMap<String, Dictionary>();
	public Set<String> getDictionaryKeys () { return this.dictionary.keySet(); }
	public Dictionary getDictionary(String xml, String tag) { return this.dictionary.get(xml+"|"+tag); }
	
	private int sent = 0;
	private int received = 0;
	private DictionaryCacheEvent dictionaryCacheEvent = null;
	
	public void add(String xml, String tag) {
		final String function = "add";
		
		logger.begin(function);
		logger.info(function, "xml[{}] tag[{}] ", xml, tag);
		
		incoming.add(xml+"|"+tag);
		
		logger.end(function);
	}
	
	public void init(String mode, String module, String folder, DictionaryCacheEvent dictionaryCacheEvent) {
		final String function = "init";
		
		logger.begin(function);
		
		this.dictionaryCacheEvent = dictionaryCacheEvent;

		logger.info(function, "module[{}]", module);
		
		if (module == null ) { 
			logger.warn(function, "module IS NULL", module);
		}
		
		for (Iterator<String> iterator = incoming.iterator(); iterator.hasNext();) {
			
			sent++;
			
			String xmlTag = iterator.next();
			
			logger.info(function, "xmlWithHeader[{}]", xmlTag);
			
			String xmlTags[] = xmlTag.split("\\|");
			
			waiting.add(xmlTag);

			DictionaryMgr dictionaryMgr = new DictionaryMgr();
			
			dictionaryMgrs.add(dictionaryMgr);
			
			dictionaryMgr.getDictionary(mode, module, folder, xmlTags[0], xmlTags[1], this);
			
		    iterator.remove();
		}
		
		logger.end(function);

	}
	
	@Override
	public void dictionaryMgrEventFailed(String xmlFile) {
		final String function = "dictionaryMgrEventFailed";
		
		logger.begin(function);
		
		logger.info(function, "xmlFile[{}]", xmlFile);
		
		received++;

		for (Iterator<String> iterator = waiting.iterator(); iterator.hasNext();) {
			String s = iterator.next();
			if ( 0 == s.compareTo(xmlFile) ) {
				iterator.remove();
			}
		}
		fails.add(xmlFile);
		
		logger.warn(function, "received[{}] >= sent[{}]", received, sent);
		
		if ( received >= sent )
			if ( null != dictionaryCacheEvent )
				dictionaryCacheEvent.dictionaryCacheEventReady(received);
		
		logger.end(function);
	}
	
	@Override
	public void dictionaryMgrEventReady(Dictionary dictionary) {
		final String function = "dictionaryMgrEventReady";
		
		logger.begin(function);
		
		received++;
		
		if ( null != dictionary ) {
			String xmlFile = (String)dictionary.getAttribute(DictionaryCacheInterface.XMLAttribute.FileName.toString());
			String XmlTag = (String)dictionary.getAttribute(DictionaryCacheInterface.XMLAttribute.Tag.toString());
			String CreateDateTimeLabel = (String)dictionary.getAttribute(DictionaryCacheInterface.XMLAttribute.DateTime.toString());
			
			logger.info(function, "dictionaryMgrEventReady dictionary XmlFile[{}] XmlTag[{}] CreateDateTimeLabel[{}]", new Object[]{xmlFile, XmlTag, CreateDateTimeLabel});		
			
			String xmlWithHeader = xmlFile + "|"+ XmlTag;
		
			this.dictionary.put(xmlFile+"|"+XmlTag, dictionary);
			
			for (Iterator<String> iterator = waiting.iterator(); iterator.hasNext();) {
				String s = iterator.next();
				if ( 0 == s.compareTo(xmlWithHeader) ) {
					iterator.remove();
				}
			}
			
		} else {
			logger.warn(function, "dictionary IS NULL");
		}
		
		logger.info(function, "received[{}] >= sent[{}]", received, sent);
		
		if ( received >= sent )
			if ( null != dictionaryCacheEvent )
				dictionaryCacheEvent.dictionaryCacheEventReady(received);
		
		logger.end(function);
	}

}
