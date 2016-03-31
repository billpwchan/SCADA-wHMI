package com.thalesgroup.scadagen.whmi.config.configenv.client;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.thalesgroup.scadagen.whmi.config.config.shared.Dictionary;

public class DictionariesCache implements DictionariesMgrEvent {
	
	private static Logger logger = Logger.getLogger(DictionariesCache.class.getName());
	
	private static DictionariesCache instance = null;
	private DictionariesCache () {}
	public static DictionariesCache getInstance() {
		if ( null == instance ) { instance = new DictionariesCache(); }
		return instance;
	}
	
	public static final String strXmlType				= "XmlType";
	public static final String strXmlFile				= "XmlFile";
	public static final String strCreateDateTimeLabel	= "CreateDateTimeLabel";
		
	private LinkedList<String> incoming		= new LinkedList<String>();
	private LinkedList<String> waiting		= new LinkedList<String>();
	private LinkedList<String> fails		= new LinkedList<String>();
	
	private LinkedList<DictionariesMgr> dictionariesMgrs = new LinkedList<DictionariesMgr>();
	
	private HashMap <String, Dictionary> dictionaries = new HashMap<String, Dictionary>();
	public Set<String> getDictionaryKeys () { return this.dictionaries.keySet(); }
	public Dictionary getDictionary(String path, String extention) { return this.dictionaries.get(path+"|"+extention); }
	
	private int sent = 0;
	private int received = 0;
	private DictionariesCacheEvent dictionariesCacheEvent = null;
	
	public void add(String path, String extention) {
		
		logger.log(Level.SEVERE, "add Begin");
		
		logger.log(Level.SEVERE, "add path["+path+"] extention["+extention+"] ");
		
		incoming.add(path+"|"+extention);
		
		logger.log(Level.SEVERE, "add End");
	}
	
	public void init(String module, DictionariesCacheEvent dictionariesCacheEvent) {
		
		logger.log(Level.SEVERE, "init");
		
		this.dictionariesCacheEvent = dictionariesCacheEvent;

		logger.log(Level.SEVERE, "init module["+module+"]");
		
		for (Iterator<String> iterator = incoming.iterator(); iterator.hasNext();) {
			
			sent++;
			
			String xmlTag = iterator.next();
			
			logger.log(Level.SEVERE, "init xmlWithHeader["+xmlTag+"]");
			
			String xmlTags[] = xmlTag.split("\\|");
			
			waiting.add(xmlTag);

			DictionariesMgr dictionariesMgr = new DictionariesMgr();
			
			dictionariesMgrs.add(dictionariesMgr);
			
			dictionariesMgr.getDictionaries(module, xmlTags[0], xmlTags[1], this);
			
		    iterator.remove();
		}
		
		logger.log(Level.SEVERE, "init End");

	}
	
	@Override
	public void dictionariesMgrEventFailed(String xmlFile) {
		
		logger.log(Level.SEVERE, "dictionariesMgrEventFailed xmlFile["+xmlFile+"]");
		
		received++;

		for (Iterator<String> iterator = waiting.iterator(); iterator.hasNext();) {
			String s = iterator.next();
			if ( 0 == s.compareTo(xmlFile) ) {
				iterator.remove();
			}
		}
		fails.add(xmlFile);
		
		logger.log(Level.SEVERE, "dictionariesMgrEventFailed received["+received+"] >= sent["+sent+"]");
		
		if ( received >= sent )
			if ( null != dictionariesCacheEvent )
				dictionariesCacheEvent.dictionariesCacheEventReady(received);
		
		logger.log(Level.SEVERE, "dictionariesMgrEventFailed End");
	}
	
	@Override
	public void dictionariesMgrEventReady(Dictionary dictionary) {
		
		logger.log(Level.SEVERE, "dictionariesMgrEventReady Begin");
		
		received++;
		
		if ( null != dictionary ) {
			String xmlType = (String)dictionary.getAttribute(strXmlType);
			String XmlFile = (String)dictionary.getAttribute(strXmlFile);
			String CreateDateTimeLabel = (String)dictionary.getAttribute(strCreateDateTimeLabel);
			
			logger.log(Level.SEVERE, "dictionariesMgrEventReady dictionary xmlType["+xmlType+"]");
			logger.log(Level.SEVERE, "dictionariesMgrEventReady dictionary XmlFile["+XmlFile+"]");
			logger.log(Level.SEVERE, "dictionariesMgrEventReady dictionary CreateDateTimeLabel["+CreateDateTimeLabel+"]");		
			
			String xmlWithHeader = xmlType + "|"+ XmlFile;
		
			this.dictionaries.put(xmlType+"|"+XmlFile, dictionary);
			
			for (Iterator<String> iterator = waiting.iterator(); iterator.hasNext();) {
				String s = iterator.next();
				if ( 0 == s.compareTo(xmlWithHeader) ) {
					iterator.remove();
				}
			}
			
		} else {
			logger.log(Level.SEVERE, "dictionariesMgrEventReady dictionary IS NULL");
		}
		
		logger.log(Level.SEVERE, "dictionariesMgrEventReady received["+received+"] >= sent["+sent+"]");
		
		if ( received >= sent )
			if ( null != dictionariesCacheEvent )
				dictionariesCacheEvent.dictionariesCacheEventReady(received);
		
		logger.log(Level.SEVERE, "dictionariesMgrEventReady End");
	}

}
