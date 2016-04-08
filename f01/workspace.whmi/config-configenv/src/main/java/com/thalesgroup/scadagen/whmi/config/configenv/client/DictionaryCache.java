package com.thalesgroup.scadagen.whmi.config.configenv.client;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.thalesgroup.scadagen.whmi.config.config.shared.Dictionary;
import com.thalesgroup.scadagen.whmi.config.configenv.shared.DictionaryCacheInterface;

public class DictionaryCache implements DictionaryMgrEvent {
	
	private static Logger logger = Logger.getLogger(DictionaryCache.class.getName());
	
	private static DictionaryCache instance = null;
	private DictionaryCache () {}
	public static DictionaryCache getInstance() {
		if ( null == instance ) { instance = new DictionaryCache(); }
		return instance;
	}
		
	private LinkedList<String> incoming		= new LinkedList<String>();
	private LinkedList<String> waiting		= new LinkedList<String>();
	private LinkedList<String> fails		= new LinkedList<String>();
	
	private LinkedList<DictionaryMgr> dictionaryMgrs = new LinkedList<DictionaryMgr>();
	
	private HashMap <String, Dictionary> dictionary = new HashMap<String, Dictionary>();
	public Set<String> getDictionaryKeys () { return this.dictionary.keySet(); }
	public Dictionary getDictionary(String xml, String tag) { return this.dictionary.get(xml+"|"+tag); }
	
	private int sent = 0;
	private int received = 0;
	private DictionaryCacheEvent dictionaryCacheEvent = null;
	
	public void add(String xml, String tag) {
		
		logger.log(Level.SEVERE, "add Begin");
		
		logger.log(Level.SEVERE, "add xml["+xml+"] tag["+tag+"] ");
		
		incoming.add(xml+"|"+tag);
		
		logger.log(Level.SEVERE, "add End");
	}
	
	public void init(String module, DictionaryCacheEvent dictionaryCacheEvent) {
		
		logger.log(Level.SEVERE, "init");
		
		this.dictionaryCacheEvent = dictionaryCacheEvent;

		logger.log(Level.SEVERE, "init module["+module+"]");
		
		for (Iterator<String> iterator = incoming.iterator(); iterator.hasNext();) {
			
			sent++;
			
			String xmlTag = iterator.next();
			
			logger.log(Level.SEVERE, "init xmlWithHeader["+xmlTag+"]");
			
			String xmlTags[] = xmlTag.split("\\|");
			
			waiting.add(xmlTag);

			DictionaryMgr dictionaryMgr = new DictionaryMgr();
			
			dictionaryMgrs.add(dictionaryMgr);
			
			dictionaryMgr.getDictionary(module, xmlTags[0], xmlTags[1], this);
			
		    iterator.remove();
		}
		
		logger.log(Level.SEVERE, "init End");

	}
	
	@Override
	public void dictionaryMgrEventFailed(String xmlFile) {
		
		logger.log(Level.SEVERE, "dictionaryMgrEventFailed xmlFile["+xmlFile+"]");
		
		received++;

		for (Iterator<String> iterator = waiting.iterator(); iterator.hasNext();) {
			String s = iterator.next();
			if ( 0 == s.compareTo(xmlFile) ) {
				iterator.remove();
			}
		}
		fails.add(xmlFile);
		
		logger.log(Level.SEVERE, "dictionaryMgrEventFailed received["+received+"] >= sent["+sent+"]");
		
		if ( received >= sent )
			if ( null != dictionaryCacheEvent )
				dictionaryCacheEvent.dictionaryCacheEventReady(received);
		
		logger.log(Level.SEVERE, "dictionaryMgrEventFailed End");
	}
	
	@Override
	public void dictionaryMgrEventReady(Dictionary dictionary) {
		
		logger.log(Level.SEVERE, "dictionaryMgrEventReady Begin");
		
		received++;
		
		if ( null != dictionary ) {
			String xmlFile = (String)dictionary.getAttribute(DictionaryCacheInterface.XmlFile);
			String XmlTag = (String)dictionary.getAttribute(DictionaryCacheInterface.XmlTag);
			String CreateDateTimeLabel = (String)dictionary.getAttribute(DictionaryCacheInterface.CreateDateTimeLabel);
			
			logger.log(Level.SEVERE, "dictionaryMgrEventReady dictionary XmlFile["+xmlFile+"]");
			logger.log(Level.SEVERE, "dictionaryMgrEventReady dictionary XmlTag["+XmlTag+"]");
			logger.log(Level.SEVERE, "dictionaryMgrEventReady dictionary CreateDateTimeLabel["+CreateDateTimeLabel+"]");		
			
			String xmlWithHeader = xmlFile + "|"+ XmlTag;
		
			this.dictionary.put(xmlFile+"|"+XmlTag, dictionary);
			
			for (Iterator<String> iterator = waiting.iterator(); iterator.hasNext();) {
				String s = iterator.next();
				if ( 0 == s.compareTo(xmlWithHeader) ) {
					iterator.remove();
				}
			}
			
		} else {
			logger.log(Level.SEVERE, "dictionaryMgrEventReady dictionary IS NULL");
		}
		
		logger.log(Level.SEVERE, "dictionaryMgrEventReady received["+received+"] >= sent["+sent+"]");
		
		if ( received >= sent )
			if ( null != dictionaryCacheEvent )
				dictionaryCacheEvent.dictionaryCacheEventReady(received);
		
		logger.log(Level.SEVERE, "dictionaryMgrEventReady End");
	}

}
