package com.thalesgroup.scadagen.whmi.config.configenv.client;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import com.thalesgroup.scadagen.whmi.config.config.shared.Dictionary;
import com.thalesgroup.scadagen.whmi.config.configenv.shared.DictionaryCacheInterface;
import com.thalesgroup.scadagen.whmi.config.configenv.shared.DictionaryCacheInterface.ConfigurationType;
import com.thalesgroup.scadagen.whmi.config.configenv.shared.DictionaryCacheInterface.ContainerType;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;

public class DictionariesCache implements DictionariesMgrEvent {
	
	private final String className = UIWidgetUtil.getClassSimpleName(DictionariesCache.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);

	private static HashMap<String, DictionariesCache> instances = new HashMap<String, DictionariesCache>();
	private DictionariesCache () {}
	public static DictionariesCache getInstance(String key) {
		if ( ! instances.containsKey(key) ) {	instances.put(key, new DictionariesCache()); }
		DictionariesCache instance = instances.get(key);
		return instance;
	}
		
	private LinkedList<String> incoming		= new LinkedList<String>();
//	private LinkedList<String> waiting		= new LinkedList<String>();
	private LinkedList<String> fails		= new LinkedList<String>();
	
	private LinkedList<DictionariesMgr> dictionariesMgrs = new LinkedList<DictionariesMgr>();
	
	private HashMap <String, Dictionary> dictionaries = new HashMap<String, Dictionary>();
	public Set<String> getDictionaryKeys () { return this.dictionaries.keySet(); }
	public Dictionary getDictionary(String path, String extention) { return this.dictionaries.get(path+"|"+extention); }
	public Dictionary getDictionary(String path) { return this.dictionaries.get(path); }
	
	public String getStringValue(final String fileName, final String key, final String tag) {
		final String function = "getStringValue";
		
		logger.begin(className, function);
		
		logger.info(className, function, "fileName[{}] key[{}] tag[{}]", new Object[]{fileName, key, tag});
		
		String value = null;
		Dictionary dictionary = getDictionary(fileName+(null!=tag?"|"+tag:""));
			
    	if ( null != dictionary ) {
    		logger.info(className, function, "begin of for dictionary");
			for ( Object o : dictionary.getValueKeys() ) {
				if ( null != o ) {
					Dictionary d2 = (Dictionary) dictionary.getValue(o);
					logger.info(className, function, "d2[{}]", d2);
					if ( null != d2 ) {
						value = (String) d2.getValue(key);
						logger.info(className, function, "value[{}]", value);
					} else {
						logger.warn(className, function, "d2 IS NULL");
					}
				} else {
					logger.warn(className, function, "o IS NULL");
				}
			}
			logger.info(className, function, "end of for dictionary");
    	} else {
			logger.warn(className, function, "dictionary IS NULL");
		}
    	logger.end(className, function);
    	return value;
	}
	public String getStringValue(final String fileName, final String key) {
		return getStringValue(fileName, key, null);
	}
	
	private int sent = 0;
	private int received = 0;
	private DictionariesCacheEvent dictionariesCacheEvent = null;
	
	public void add(String folder, String extention, String tag) {
		final String function = "add";
		
		logger.begin(className, function);
		
		logger.info(className, function, "path[{}] extention[{}] tag[{}]", new Object[]{folder, extention, tag});
		
		incoming.add(folder+"|"+extention+"|"+tag);
		
		logger.end(className, function);
	}
	
	public void init(String mode, String module, DictionariesCacheEvent dictionariesCacheEvent) {
		final String function = "init";
		
		logger.begin(className, function);
		
		this.dictionariesCacheEvent = dictionariesCacheEvent;

		logger.info(className, function, "module[{}]", module);
		
		for (Iterator<String> iterator = incoming.iterator(); iterator.hasNext();) {
			
			sent++;
			
			String xmlTag = iterator.next();
			
			logger.info(className, function, "xmlWithHeader[{}]", xmlTag);
			
			String xmlTags[] = xmlTag.split("\\|");
			
//			waiting.add(xmlTag);

			DictionariesMgr dictionariesMgr = new DictionariesMgr();
			
			dictionariesMgrs.add(dictionariesMgr);
			
			logger.info(className, function, "xmlTags[0][{}] xmlTags[1][{}] xmlTags[2][{}]", new Object[]{xmlTags[0], xmlTags[1], xmlTags[2]});
			
			dictionariesMgr.getDictionaries(mode, module, xmlTags[0], xmlTags[1], xmlTags[2], this);
			
		    iterator.remove();
		}
		
		logger.end(className, function);

	}
	
	@Override
	public void dictionariesMgrEventFailed(String xmlFile) {
		final String function = "dictionariesMgrEventFailed";
		
		logger.begin(className, function);
		
		logger.info(className, function, "xmlFile[{}]", xmlFile);
		
		received++;

//		for (Iterator<String> iterator = waiting.iterator(); iterator.hasNext();) {
//			String s = iterator.next();
//			if ( 0 == s.compareTo(xmlFile) ) {
//				iterator.remove();
//			}
//		}
		fails.add(xmlFile);
		
		logger.info(className, function, "received[{}] >= sent[{}]", received, sent);
		
		if ( received >= sent )
			if ( null != dictionariesCacheEvent )
				dictionariesCacheEvent.dictionariesCacheEventReady(received);
		
		logger.end(className, function);
	}
	
	@Override
	public void dictionariesMgrEventReady(Dictionary dictionaries) {
		final String function = "dictionariesMgrEventReady";
		
		logger.begin(className, function);
		
		received++;
		
		if ( null != dictionaries ) {

			logger.info(className, function, "dictionaryCur.getValueKeys().size()[{}]", dictionaries.getValueKeys().size());
			
			String containerType = (String) dictionaries.getAttribute(DictionaryCacheInterface.strContainerType);
			
			if ( ContainerType.Dictionaries.toString().equals(containerType) ) {
				
				String configurationType = (String) dictionaries.getAttribute(DictionaryCacheInterface.strConfigurationType);
				
				if ( ConfigurationType.XMLFile.toString().equals(configurationType) ) {
					
					for ( Object key : dictionaries.getValueKeys() ) {
						
						logger.info(className, function, "key[{}]", key);
						
						Object value = dictionaries.getValue(key);
						
						if ( null != value ) {
							
							if ( value instanceof Dictionary ) {
								Dictionary dictionary = (Dictionary) value;
								
								String fileName = (String)dictionary.getAttribute(DictionaryCacheInterface.XMLAttribute.FileName.toString());
								String tag = (String)dictionary.getAttribute(DictionaryCacheInterface.XMLAttribute.Tag.toString());
								String CreateDateTimeLabel = (String)dictionary.getAttribute(DictionaryCacheInterface.XMLAttribute.DateTime.toString());
								
								logger.info(className, function, "dictionary XmlFile[{}] XmlTag[{}] CreateDateTimeLabel[{}]", new Object[]{fileName, tag, CreateDateTimeLabel});		

								this.dictionaries.put(fileName+"|"+tag, dictionary);

							} else {
								logger.warn(className, function, "key[{}] value IS NOT Dictionary", key);
							}
						} else {
							logger.warn(className, function, "value[{}] IS NULL", value);
						}
					}
					
				} else if ( ConfigurationType.PropertiesFile.toString().equals(configurationType) ) {
					
					for ( Object key : dictionaries.getValueKeys() ) {
						
						logger.info(className, function, "key[{}]", key);
						
						Object value = dictionaries.getValue(key);
						
						if ( null != value ) {
							
							if ( value instanceof Dictionary ) {
								
								Dictionary dictionary = (Dictionary) value;
								
								String fileName = (String)dictionary.getAttribute(DictionaryCacheInterface.PropertiesAttribute.FileName.toString());
								String CreateDateTimeLabel = (String)dictionary.getAttribute(DictionaryCacheInterface.PropertiesAttribute.DateTime.toString());
								String dictionariesKey = fileName;
								
								logger.info(className, function, "dictionary XmlFile[{}] CreateDateTimeLabel[{}]", fileName, CreateDateTimeLabel);
								logger.info(className, function, "dictionariesKey[{}]", dictionariesKey);
								
								this.dictionaries.put(dictionariesKey, dictionary);
								
//								// Debug
//								for ( Object o : dictionary.getValueKeys() ) {
//									if ( null != o ) {
//										Dictionary d2 = (Dictionary) dictionary.getValue(o);
//										for ( Object o2 : d2.getAttributeKeys() ) {
//											if ( null != o2 ) {
//											}
//										}
//										for ( Object o2 : d2.getValueKeys() ) {
//											String s = (String) o2;
//											String v = (String) d2.getValue(o2);
//											
//											logger.info(className, function, "s[{}] v[{}]",s, v);
//										}
//									}
//								}
//								// End of Debug
							} else {
								logger.warn(className, function, "key[{}] value IS NOT Dictionary", key);
							}
						} else {
							logger.warn(className, function, "value[{}] IS NULL", value);
						}
					}
				} else {
					logger.warn(className, function, "configurationType[{}] IS UNKNOW!", configurationType);
				}
			} else {
				logger.warn(className, function, "containerType[{}] IS UNKNOW!", containerType);
			}
			
		} else {
			logger.warn(className, function, "dictionary IS NULL");
		}
		
		logger.info(className, function, "received[{}] >= sent[{}]", received, sent);
		
		if ( received >= sent )
			if ( null != dictionariesCacheEvent )
				dictionariesCacheEvent.dictionariesCacheEventReady(received);
		
		logger.end(className, function);
	}

}
