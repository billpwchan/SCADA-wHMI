package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;

public class UIOptionCaches {
	private final String className = UIWidgetUtil.getClassSimpleName(UIOptionCaches.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	private String logPrefix = "";
	
	private HashMap<String, HashMap<String, String>> options = null;
	
	public boolean isValid() { return null==options; }
	
	public String[] getKeys() { return options.keySet().toArray(new String[0]); }
	public int getKeySize() { return options.size(); }
	
	public HashMap<String, String> getOption(String key) { return options.get(key); }
	public Set<Entry<String,HashMap<String,String>>> getOptions() { return this.options.entrySet(); }
	
	private String dictionariesCacheName = null;
	private String fileName = null;
	private String tag = null;
	public UIOptionCaches(String logPrefix, String dictionariesCacheName, String fileName, String tag) {
		final String function = "UIOptionCaches";
		logger.begin(className, function);
		
		this.logPrefix = logPrefix;
		this.dictionariesCacheName = dictionariesCacheName;
		this.fileName = fileName;
		this.tag = tag;
		
		logger.info(className, function, this.logPrefix+"this.dictionariesCacheName[{}]", this.dictionariesCacheName);
		logger.info(className, function, this.logPrefix+"this.fileName[{}]", this.fileName);
		logger.info(className, function, this.logPrefix+"this.tag[{}]", this.tag);
		
		logger.end(className, function);
	}
	
	public void init() {
		final String function = "init";
		logger.begin(className, function);
		
		UIOptionMgr mgr = new UIOptionMgr(className);
		
		this.options = mgr.getOptions(dictionariesCacheName, fileName, tag);
		
		if ( null == this.options ) {
			logger.warn(className, function, "dictionariesCacheName[{}] fileName[{}] tag[{}] IS NULL", new Object[]{ dictionariesCacheName, fileName, tag} );
		}
		
		logger.end(className, function);
	}
}
