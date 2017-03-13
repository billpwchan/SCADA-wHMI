package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.test;

import java.util.HashMap;
import java.util.Map.Entry;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIOptionMgr;

public class UIOptionMgrTest {
	private final String className = UIWidgetUtil.getClassSimpleName(UIOptionMgrTest.class.getName());
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
	
	public void test() {
		final String function = "test";
		String className = UIWidgetUtil.getClassSimpleName(UIOptionMgr.class.getName());
		logger.info(className, function, "Testing [{}] Begin", className);
		
		UIOptionMgr uiOptionMgr = new UIOptionMgr(className);
		
		HashMap<String, HashMap<String, String>> options = uiOptionMgr.getOptions(dictionariesCacheName, filename, tag);
		if ( null != options ) {
			for ( Entry<String, HashMap<String, String>> option : options.entrySet() ) {
				if ( null != option ) {
					String optionKey = option.getKey();
					HashMap<String, String> optionValue = option.getValue();
					logger.info(className, function, "optionKey[{}]", optionKey);
					if ( null != optionValue ) {
						for ( Entry<String, String> parameters : optionValue.entrySet() ) {
							String key = parameters.getKey();
							String value = parameters.getValue();
							logger.info(className, function, "optionKey[{}] key[{}] value[{}]", new Object[]{optionKey, key, value});
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
		logger.info(className, function, "Testing [{}] End", className);
	}
}
