package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.test;

import java.util.Set;
import java.util.Map.Entry;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIEventActionMgr;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;

public class UIEventActionMgrTest {

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
	
	public UIEventActionMgrTest() {
	}
	public void test() {
		final String function = "test";
		String className = UIEventActionMgr.class.getSimpleName();
		logger.debug(className, function, "Testing [{}] Begin", className);
		final UIEventActionMgr uiEventActionMgr = new UIEventActionMgr(className, dictionariesCacheName, filename, tag);
		uiEventActionMgr.init();
		
		final String [] uiEventActionKeys = uiEventActionMgr.getKeys();
		if ( null != uiEventActionKeys ) {
			for (String uiEventActionKey : uiEventActionKeys ) {
				logger.debug(className, function, "uiEventActionKey[{}]", uiEventActionKey);
			}
		} else {
			logger.warn(className, function, "uiEventActionKeys IS NULL");
		}
	
		Set<Entry<String, UIEventAction>> uiEventActions = uiEventActionMgr.gets();
		if ( null != uiEventActions ) {
			for ( Entry<String, UIEventAction> entry : uiEventActions ) {
				if ( null != entry ) {
					final String uiEventActionKey = entry.getKey();
					final UIEventAction uiEventAction = entry.getValue();
					logger.debug(className, function, "key[{}]", uiEventActionKey);
					if ( null != uiEventAction ) {
						for ( String key : uiEventAction.getParameterKeys() ) {
							logger.info(className, function, "key[{}] key[{}]", uiEventActionKey, key);
							final String value = (String) uiEventAction.getParameter(key);
							logger.info(className, function, "key[{}] key[{}] value[{}]", new Object[]{uiEventActionKey, key, value});
						}
					} else {
						logger.warn(className, function, "uiEventAction IS NULL");
					}
				} else {
					logger.warn(className, function, "entry IS NULL");
				}
			}
		} else {
			logger.warn(className, function, "uiEventActions IS NULL");
		}
		logger.debug(className, function, "Testing [{}] End", className);
	}
}
