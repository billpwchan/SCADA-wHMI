package com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;

public class UIOptionCaches {

	private final String className = this.getClass().getSimpleName();
	private final UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
	private String logPrefix = "";
	
	private Map<String, Map<String, String>> options = null;
	
	public boolean isValid() { return null==options; }
	
	public String[] getKeys() { return options.keySet().toArray(new String[0]); }
	public int getKeySize() { return options.size(); }
	
	public Map<String, String> getOption(String key) { return options.get(key); }
	public Set<Entry<String,Map<String,String>>> getOptions() { return this.options.entrySet(); }
	
	private String dictionariesCacheName = null;
	private String fileName = null;
	private String tag = null;
	public UIOptionCaches(String logPrefix, String dictionariesCacheName, String fileName, String tag) {
		final String f = "UIOptionCaches";
		logger.begin(f);
		
		this.logPrefix = logPrefix;
		this.dictionariesCacheName = dictionariesCacheName;
		this.fileName = fileName;
		this.tag = tag;
		
		logger.trace(f, this.logPrefix+"this.dictionariesCacheName[{}]", this.dictionariesCacheName);
		logger.trace(f, this.logPrefix+"this.fileName[{}]", this.fileName);
		logger.trace(f, this.logPrefix+"this.tag[{}]", this.tag);
		
		logger.end(f);
	}
	
	public void init() {
		final String f = "init";
		logger.begin(f);
		
		UIOptionMgr mgr = new UIOptionMgr(className);
		
		this.options = mgr.getOptions(dictionariesCacheName, fileName, tag);
		
		if ( null == this.options ) {
			logger.warn(f, "dictionariesCacheName[{}] fileName[{}] tag[{}] IS NULL", new Object[]{ dictionariesCacheName, fileName, tag} );
		}
		
		logger.end(f);
	}
}
