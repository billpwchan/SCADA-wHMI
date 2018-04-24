package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.test;

import java.util.Map;
import java.util.Map.Entry;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIOptionMgr;

public class UIOptionMgrTest {
	
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
	
	public void test() {
		final String function = "test";
		String className = UIOptionMgr.class.getSimpleName();
		logger.debug(className, function, "Testing [{}] Begin", className);
		
		final UIOptionMgr uiOptionMgr = new UIOptionMgr(className);
		
		final Map<String, Map<String, String>> options = uiOptionMgr.getOptions(dictionariesCacheName, filename, tag);
		if ( null != options ) {
			for ( Entry<String, Map<String, String>> option : options.entrySet() ) {
				if ( null != option ) {
					String optionKey = option.getKey();
					final Map<String, String> optionValue = option.getValue();
					logger.debug(className, function, "optionKey[{}]", optionKey);
					if ( null != optionValue ) {
						for ( Entry<String, String> parameters : optionValue.entrySet() ) {
							String key = parameters.getKey();
							String value = parameters.getValue();
							logger.debug(className, function, "optionKey[{}] key[{}] value[{}]", new Object[]{optionKey, key, value});
						}
					} else {
						logger.warn(className, function, "optionValue IS NULL");
					}
				} else {
					logger.warn(className, function, "options IS NULL");
				}
			}
		} else {
			logger.warn(className, function, "options IS NULL");
		}
		logger.debug(className, function, "Testing [{}] End", className);
	}
}
