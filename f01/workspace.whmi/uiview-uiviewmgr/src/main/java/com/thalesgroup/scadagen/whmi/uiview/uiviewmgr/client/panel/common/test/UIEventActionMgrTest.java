package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.test;

import java.util.Set;
import java.util.Map.Entry;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIEventActionMgr;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;

public class UIEventActionMgrTest {

//	private final String className = this.getClass().getSimpleName();
	private final UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
	
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
		logger.debug(function, "Testing [{}] Begin", className);
		final UIEventActionMgr uiEventActionMgr = new UIEventActionMgr(className, dictionariesCacheName, filename, tag);
		uiEventActionMgr.init();
		
		final String [] uiEventActionKeys = uiEventActionMgr.getKeys();
		if ( null != uiEventActionKeys ) {
			for (String uiEventActionKey : uiEventActionKeys ) {
				logger.debug(function, "uiEventActionKey[{}]", uiEventActionKey);
			}
		} else {
			logger.warn(function, "uiEventActionKeys IS NULL");
		}
	
		Set<Entry<String, UIEventAction>> uiEventActions = uiEventActionMgr.gets();
		if ( null != uiEventActions ) {
			for ( Entry<String, UIEventAction> entry : uiEventActions ) {
				if ( null != entry ) {
					final String uiEventActionKey = entry.getKey();
					final UIEventAction uiEventAction = entry.getValue();
					logger.debug(function, "key[{}]", uiEventActionKey);
					if ( null != uiEventAction ) {
						for ( String key : uiEventAction.getParameterKeys() ) {
							logger.info(function, "key[{}] key[{}]", uiEventActionKey, key);
							final String value = (String) uiEventAction.getParameter(key);
							logger.info(function, "key[{}] key[{}] value[{}]", new Object[]{uiEventActionKey, key, value});
						}
					} else {
						logger.warn(function, "uiEventAction IS NULL");
					}
				} else {
					logger.warn(function, "entry IS NULL");
				}
			}
		} else {
			logger.warn(function, "uiEventActions IS NULL");
		}
		logger.debug(function, "Testing [{}] End", className);
	}
}
