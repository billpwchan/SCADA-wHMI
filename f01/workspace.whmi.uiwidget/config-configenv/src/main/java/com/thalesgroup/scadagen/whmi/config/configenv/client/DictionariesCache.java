package com.thalesgroup.scadagen.whmi.config.configenv.client;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.thalesgroup.scadagen.whmi.config.config.shared.Dictionary;
import com.thalesgroup.scadagen.whmi.config.config.shared.Dictionary_i;
import com.thalesgroup.scadagen.whmi.config.configenv.shared.DictionaryCacheInterface;
import com.thalesgroup.scadagen.whmi.config.configenv.shared.DictionaryCacheInterface.ConfigurationType;
import com.thalesgroup.scadagen.whmi.config.configenv.shared.DictionaryCacheInterface.ContainerType;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;

public class DictionariesCache implements DictionariesMgrEvent {
	
	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());

	private static Map<String, DictionariesCache> instances = new HashMap<String, DictionariesCache>();
	private DictionariesCache () {}
	public static DictionariesCache getInstance(String key) {
		if ( ! instances.containsKey(key) ) {	instances.put(key, new DictionariesCache()); }
		DictionariesCache instance = instances.get(key);
		return instance;
	}
		
	private List<String> incoming		= new LinkedList<String>();
//	private List<String> waiting		= new LinkedList<String>();
	private List<String> fails		= new LinkedList<String>();
	
	private List<DictionariesMgr> dictionariesMgrs = new LinkedList<DictionariesMgr>();
	
	private Map <String, Dictionary> dictionaries = new HashMap<String, Dictionary>();
	public Set<String> getDictionaryKeys () { return this.dictionaries.keySet(); }
	public Dictionary getDictionary(String path, String extention) { return this.dictionaries.get(path+"|"+extention); }
	public Dictionary getDictionary(String path) { return this.dictionaries.get(path); }
	
	// For Json Option
	public String getData(final String fileName) {
		final String function = "getData";
		logger.begin(function);
		logger.trace(function, "fileName[{}]", fileName);
		String value = null;
		Dictionary dictionary = getDictionary(fileName);
    	if ( null != dictionary ) {
    		logger.trace(function, "begin of for dictionary");
			for ( Object o : dictionary.getValueKeys() ) {
				if ( null != o ) {
					Dictionary d2 = (Dictionary) dictionary.getValue(o);
					if ( null != d2 ) {
						value = d2.getData();
						break;
					} else {
						logger.warn(function, "d2 IS NULL");
					}
				} else {
					logger.warn(function, "o IS NULL");
				}
			}
    	} else {
			logger.warn(function, "dictionary IS NULL");
		}
//		if ( null != dictionary ) {
//			value = dictionary.getData();
//		}
		return value;
	}
	
	// For XML Option
	public String getStringValue(final String fileName, final String keyValue, final String tag, final String tagKey) {
		final String function = "getStringValue";
		logger.begin(function);
		logger.trace(function, "fileName[{}] keyValue[{}] tag[{}] tagKey[{}]", new Object[]{fileName, keyValue, tag, tagKey});
	
		String value = null;
		Dictionary dictionary = getDictionary(fileName+(null!=tag?"|"+tag:""));
			
    	if ( null != dictionary ) {
    		logger.trace(function, "begin of for dictionary");
			for ( Object o : dictionary.getValueKeys() ) {
				if ( null != o ) {
					Dictionary d2 = (Dictionary) dictionary.getValue(o);
					logger.trace(function, "d2[{}]", d2);
					if ( null != d2 ) {
						value = (String) d2.getValue(keyValue);
						logger.trace(function, "keyValue[{}] value[{}]", keyValue, value);
						if ( null != tagKey ) {
							String d2av = (String) d2.getAttribute("key");
							logger.trace(function, "tagKey[{}] d2av[{}]", tagKey, d2av);
							if ( null != d2av ) {
								if ( d2av.equals(tagKey) ) {
									logger.trace(function, "tagKey[{}] equals d2av[{}]", tagKey, d2av);
									break;
								}								
							}
						}
					} else {
						logger.warn(function, "d2 IS NULL");
					}
				} else {
					logger.warn(function, "o IS NULL");
				}
			}
			logger.trace(function, "end of for dictionary");
    	} else {
			logger.warn(function, "dictionary IS NULL");
		}
    	logger.trace(function, "fileName[{}] keyValue[{}] tag[{}] tagKey[{}] => value[{}]", new Object[]{fileName, keyValue, tag, tagKey, value});
    	logger.end(function);
    	return value;
	}
	
	// For XML Header
	public String getStringValue(final String fileName, final String keyValue, final String tag) {
    	return getStringValue(fileName, keyValue, tag, null);
	}
	// For Properties
	public String getStringValue(final String fileName, final String keyValue) {
		return getStringValue(fileName, keyValue, null);
	}
	
	private int sent = 0;
	private int received = 0;
	private DictionariesCacheEvent dictionariesCacheEvent = null;
	
	public void add(String folder, String extention, String tag) {
		final String function = "add";
		logger.begin(function);
		logger.trace(function, "path[{}] extention[{}] tag[{}]", new Object[]{folder, extention, tag});
		
		incoming.add(folder+"|"+extention+"|"+tag);
		
		logger.end(function);
	}
	
	public void init(String mode, String module, DictionariesCacheEvent dictionariesCacheEvent) {
		final String function = "init";
		logger.begin(function);
		
		this.dictionariesCacheEvent = dictionariesCacheEvent;

		logger.trace(function, "module[{}]", module);
		
		for (Iterator<String> iterator = incoming.iterator(); iterator.hasNext();) {
			
			sent++;
			
			String xmlTag = iterator.next();
			
			logger.trace(function, "xmlWithHeader[{}]", xmlTag);
			
			String xmlTags[] = xmlTag.split("\\|");
			
//			waiting.add(xmlTag);

			DictionariesMgr dictionariesMgr = new DictionariesMgr();
			
			dictionariesMgrs.add(dictionariesMgr);
			
			logger.trace(function, "xmlTags[0][{}] xmlTags[1][{}] xmlTags[2][{}]", new Object[]{xmlTags[0], xmlTags[1], xmlTags[2]});
			
			dictionariesMgr.getDictionaries(mode, module, xmlTags[0], xmlTags[1], xmlTags[2], this);
			
		    iterator.remove();
		}
		
		logger.end(function);
	}
	
	@Override
	public void dictionariesMgrEventFailed(String xmlFile) {
		final String function = "dictionariesMgrEventFailed";
		logger.begin(function);
		logger.trace(function, "xmlFile[{}]", xmlFile);
		
		received++;

		fails.add(xmlFile);
		
		logger.trace(function, "received[{}] >= sent[{}]", received, sent);
		
		if ( received >= sent )
			if ( null != dictionariesCacheEvent )
				dictionariesCacheEvent.dictionariesCacheEventReady(received);
		
		logger.end(function);
	}
	
	@Override
	public void dictionariesMgrEventReady(Dictionary_i dictionaries) {
		final String function = "dictionariesMgrEventReady";
		logger.begin(function);
		
		received++;
		
		if ( null != dictionaries ) {

			logger.trace(function, "dictionaryCur.getValueKeys().size()[{}]", dictionaries.getValueKeys().size());
			
			String containerType = (String) dictionaries.getAttribute(DictionaryCacheInterface.strContainerType);
			
			if ( ContainerType.Dictionaries.toString().equals(containerType) ) {
				
				String configurationType = (String) dictionaries.getAttribute(DictionaryCacheInterface.strConfigurationType);
				
				if ( ConfigurationType.XMLFile.toString().equals(configurationType) ) {
					
					for ( Object key : dictionaries.getValueKeys() ) {
						
						logger.trace(function, "key[{}]", key);
						
						Object value = dictionaries.getValue(key);
						
						if ( null != value ) {
							
							if ( value instanceof Dictionary ) {
								Dictionary dictionary = (Dictionary) value;
								
//								String fileSeparator = (String)dictionary.getAttribute(DictionaryCacheInterface.XMLAttribute.FileSeparator.toString());
								String relativePath = (String)dictionary.getAttribute(DictionaryCacheInterface.XMLAttribute.RelativePath.toString());
								String fileName = (String)dictionary.getAttribute(DictionaryCacheInterface.XMLAttribute.FileName.toString());
								String tag = (String)dictionary.getAttribute(DictionaryCacheInterface.XMLAttribute.Tag.toString());
								String createDateTimeLabel = (String)dictionary.getAttribute(DictionaryCacheInterface.XMLAttribute.DateTime.toString());
								
								logger.trace(function, "fileName[{}] tag[{}] createDateTimeLabel[{}]", new Object[]{fileName, tag, createDateTimeLabel});
								logger.trace(function, "relativePath[{}]", relativePath);
								
								String dictionariesKey = relativePath+"|"+tag;

								this.dictionaries.put(dictionariesKey, dictionary);

							} else {
								logger.warn(function, "key[{}] value IS NOT Dictionary", key);
							}
						} else {
							logger.warn(function, "value[{}] IS NULL", value);
						}
					}
					
				} else if ( ConfigurationType.PropertiesFile.toString().equals(configurationType) ) {
					
					for ( Object key : dictionaries.getValueKeys() ) {
						
						logger.trace(function, "key[{}]", key);
						
						Object value = dictionaries.getValue(key);
						
						if ( null != value ) {
							
							if ( value instanceof Dictionary ) {
								
								Dictionary dictionary = (Dictionary) value;
								
								String fileName = (String)dictionary.getAttribute(DictionaryCacheInterface.PropertiesAttribute.FileName.toString());
								String CreateDateTimeLabel = (String)dictionary.getAttribute(DictionaryCacheInterface.PropertiesAttribute.DateTime.toString());
								String dictionariesKey = fileName;
								
								logger.trace(function, "dictionary XmlFile[{}] CreateDateTimeLabel[{}]", fileName, CreateDateTimeLabel);
								logger.trace(function, "dictionariesKey[{}]", dictionariesKey);
								
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
//											logger.trace(function, "s[{}] v[{}]",s, v);
//										}
//									}
//								}
//								// End of Debug
							} else {
								logger.warn(function, "key[{}] value IS NOT Dictionary", key);
							}
						} else {
							logger.warn(function, "value[{}] IS NULL", value);
						}
					}
				} else if ( ConfigurationType.JsonFile.toString().equals(configurationType) ) {
					
					for ( Object key : dictionaries.getValueKeys() ) {
						
						logger.trace(function, "key[{}]", key);
						
						Object value = dictionaries.getValue(key);
						
						if ( null != value ) {
							
							if ( value instanceof Dictionary ) {
								
								Dictionary dictionary = (Dictionary) value;
								
								String fileName = (String)dictionary.getAttribute(DictionaryCacheInterface.PropertiesAttribute.FileName.toString());
								String CreateDateTimeLabel = (String)dictionary.getAttribute(DictionaryCacheInterface.PropertiesAttribute.DateTime.toString());
								String dictionariesKey = fileName;
								
								logger.trace(function, "dictionary XmlFile[{}] CreateDateTimeLabel[{}]", fileName, CreateDateTimeLabel);
								logger.trace(function, "dictionariesKey[{}]", dictionariesKey);
								
								this.dictionaries.put(dictionariesKey, dictionary);

							} else {
								logger.warn(function, "key[{}] value IS NOT Dictionary", key);
							}
						} else {
							logger.warn(function, "value[{}] IS NULL", value);
						}
					}
				}
			}
		} else {
			logger.warn(function, "dictionary IS NULL");
		}
		
		logger.trace(function, "received[{}] >= sent[{}]", received, sent);
		
		if ( received >= sent )
			if ( null != dictionariesCacheEvent )
				dictionariesCacheEvent.dictionariesCacheEventReady(received);
		
		logger.end(function);
	}

}
