package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.test;

import java.util.HashMap;
import java.util.Set;
import java.util.Map.Entry;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIOptionCaches;

public class UIOptionCachesTest {
	private final String className = UIWidgetUtil.getClassSimpleName(UIOptionCachesTest.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
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
		String className = UIWidgetUtil.getClassSimpleName(UIOptionCaches.class.getName());
		logger.info(className, function, "Testing [{}] Begin", className);
		
		UIOptionCaches optionCaches = new UIOptionCaches(className, dictionariesCacheName, filename, tag);
		
		optionCaches.init();
	
		String [] optionKeys = optionCaches.getKeys();
		if ( null != optionKeys ) {
			for (String optionKey : optionKeys ) {
				logger.info(className, function, "optionKey[{}]", optionKey);
			}
		} else {
			logger.warn(className, function, "optionKeys IS NULL");
		}
		
		Set<Entry<String, HashMap<String, String>>> options = optionCaches.getOptions();
		if ( null != options ) {
			for ( Entry<String, HashMap<String, String>> entry : options ) {
				if ( null != entry ) {
					String optionKey = entry.getKey();
					HashMap<String, String> option = entry.getValue();
					logger.info(className, function, "key[{}]", optionKey);
					if ( null != option ) {
						for ( String elementKey : option.keySet() ) {
							logger.info(className, function, "key[{}] elementKey[{}]", optionKey, elementKey);
							String value = option.get(elementKey);
							logger.info(className, function, "key[{}] elementKey[{}] value[{}]", new Object[]{optionKey, elementKey, value});
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
		logger.info(className, function, "Testing [{}] End", className);
	}
}
