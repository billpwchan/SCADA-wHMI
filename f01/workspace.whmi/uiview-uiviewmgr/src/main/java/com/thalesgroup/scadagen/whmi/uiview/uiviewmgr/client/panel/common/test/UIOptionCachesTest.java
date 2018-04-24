package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.test;

import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIOptionCaches;

public class UIOptionCachesTest {

//	private final String className = this.getClass().getSimpleName();
	private final UILogger logger = UILoggerFactory.getInstance().getLogger(this.getClass().getName());
	
	private String dictionariesCacheName = null;
	private String filename = null;
	private String tag = null;
	
	public String getDictionariesCacheName() {
		return dictionariesCacheName;
	}
	public void setDictionariesCacheName(String dictionariesCacheName) {
		this.dictionariesCacheName = dictionariesCacheName;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getOption() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	
	public UIOptionCachesTest() {
	}
	public void test() {
		final String function = "test";
		String className = UIOptionCachesTest.class.getSimpleName();
		logger.debug(className, function, "Testing [{}] Begin", className);
		
		final UIOptionCaches optionCaches = new UIOptionCaches(className, dictionariesCacheName, filename, tag);
		
		optionCaches.init();
	
		String [] optionKeys = optionCaches.getKeys();
		if ( null != optionKeys ) {
			for (String optionKey : optionKeys ) {
				logger.debug(className, function, "optionKey[{}]", optionKey);
			}
		} else {
			logger.warn(className, function, "optionKeys IS NULL");
		}
		
		final Set<Entry<String, Map<String, String>>> options = optionCaches.getOptions();
		if ( null != options ) {
			for ( Entry<String, Map<String, String>> entry : options ) {
				if ( null != entry ) {
					final String optionKey = entry.getKey();
					final Map<String, String> option = entry.getValue();
					logger.debug(className, function, "key[{}]", optionKey);
					if ( null != option ) {
						for ( String elementKey : option.keySet() ) {
							logger.debug(className, function, "key[{}] elementKey[{}]", optionKey, elementKey);
							String value = option.get(elementKey);
							logger.debug(className, function, "key[{}] elementKey[{}] value[{}]", new Object[]{optionKey, elementKey, value});
						}
					} else {
						logger.warn(className, function, "option IS NULL");
					}
				} else {
					logger.warn(className, function, "entry IS NULL");
				}
			}
		} else {
			logger.warn(className, function, "optionKeys IS NULL");
		}
		logger.debug(className, function, "Testing [{}] End", className);
	}
}
