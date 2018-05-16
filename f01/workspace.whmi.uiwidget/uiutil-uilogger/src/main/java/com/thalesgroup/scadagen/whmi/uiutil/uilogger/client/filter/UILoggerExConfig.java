package com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.filter;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import com.allen_sauer.gwt.log.client.Log;

public class UILoggerExConfig {

	private UILoggerExConfig() {}
	private static UILoggerExConfig instance = null;
	public static UILoggerExConfig getInstance() { 
		 if (null==instance) instance = new UILoggerExConfig();
		 return instance;
	}

	private Map<String, Integer> filters = new LinkedHashMap<String, Integer>();
	public Map<String, Integer> getFilters() { return filters; }
	public void addFilter(int level, String name) { filters.put(name, level); }
	public void removeFilter(String name) { filters.remove(name); }
	public void clearFilters() { filters.clear(); }
	
	private int currentLogLevel = Log.getCurrentLogLevel();
	public void setCurrentLogLevel(int level) { this.currentLogLevel = level; }
	public int getCurrentLogLevel() { return this.currentLogLevel; }
	
	public int LOG_LEVEL_TRACE	 = Log.LOG_LEVEL_TRACE;
	public int LOG_LEVEL_DEBUG	 = Log.LOG_LEVEL_DEBUG;
	public int LOG_LEVEL_INFO	 = Log.LOG_LEVEL_INFO;
	public int LOG_LEVEL_WARN	 = Log.LOG_LEVEL_WARN;
	public int LOG_LEVEL_ERROR	 = Log.LOG_LEVEL_ERROR;
	public int LOG_LEVEL_FATAL	 = Log.LOG_LEVEL_FATAL;
	public int LOG_LEVEL_OFF	 = Log.LOG_LEVEL_OFF;
	
	public String LOG_STR_PREFIX = "[{}] {}";
	public String LOG_STR_MSG    = "{} {} ";
	
	public String LOG_STR_OCB    = "{}";
	
	public String LOG_STR_BEGIN  = "Begin";
	public String LOG_STR_END    = "End";
	
	public String LOG_STR_NULL   = "NULL";

	public void setMsg(String msgname, String msg) {
		if(0==UILoggerExConfig_i.AttributeMsg.PREFIX.toString().compareTo(msgname))		{	LOG_STR_PREFIX	= msg; }
		else if(0==UILoggerExConfig_i.AttributeMsg.MSG.toString().compareTo(msgname))	{	LOG_STR_MSG		= msg; }
		else if(0==UILoggerExConfig_i.AttributeMsg.BEGIN.toString().compareTo(msgname))	{	LOG_STR_BEGIN	= msg; }
		else if(0==UILoggerExConfig_i.AttributeMsg.END.toString().compareTo(msgname))	{	LOG_STR_END		= msg; }
		else if(0==UILoggerExConfig_i.AttributeMsg.NULL.toString().compareTo(msgname))	{	LOG_STR_NULL	= msg; }
	}
	
	public void setLevel(String levelname, int level) {
		if(0==UILoggerExConfig_i.AttributeLevel.TRACE.toString().compareTo(levelname))		{	LOG_LEVEL_TRACE	= level; }
		else if(0==UILoggerExConfig_i.AttributeLevel.DEBUG.toString().compareTo(levelname))	{	LOG_LEVEL_DEBUG	= level; }
		else if(0==UILoggerExConfig_i.AttributeLevel.INFO.toString().compareTo(levelname))	{	LOG_LEVEL_INFO	= level; }
		else if(0==UILoggerExConfig_i.AttributeLevel.WARN.toString().compareTo(levelname))	{	LOG_LEVEL_WARN	= level; }
		else if(0==UILoggerExConfig_i.AttributeLevel.ERROR.toString().compareTo(levelname))	{	LOG_LEVEL_ERROR	= level; }
		else if(0==UILoggerExConfig_i.AttributeLevel.FATAL.toString().compareTo(levelname))	{	LOG_LEVEL_FATAL	= level; }
		else if(0==UILoggerExConfig_i.AttributeLevel.OFF.toString().compareTo(levelname))	{	LOG_LEVEL_OFF	= level; }
	}
	
	public int getLevel(String levelname) {
		int ret = 0;
		if(0==UILoggerExConfig_i.AttributeLevel.TRACE.toString().compareTo(levelname)) 		{	ret = LOG_LEVEL_TRACE;	}
		else if(0==UILoggerExConfig_i.AttributeLevel.DEBUG.toString().compareTo(levelname))	{	ret = LOG_LEVEL_DEBUG;	}
		else if(0==UILoggerExConfig_i.AttributeLevel.INFO.toString().compareTo(levelname))	{	ret = LOG_LEVEL_INFO; 	}
		else if(0==UILoggerExConfig_i.AttributeLevel.WARN.toString().compareTo(levelname))	{	ret = LOG_LEVEL_WARN; 	}
		else if(0==UILoggerExConfig_i.AttributeLevel.ERROR.toString().compareTo(levelname))	{	ret = LOG_LEVEL_ERROR;	}
		else if(0==UILoggerExConfig_i.AttributeLevel.FATAL.toString().compareTo(levelname))	{	ret = LOG_LEVEL_FATAL;	}
		else if(0==UILoggerExConfig_i.AttributeLevel.OFF.toString().compareTo(levelname))	{	ret = LOG_LEVEL_OFF;	}
		return ret;
	}
	
	private boolean isFullClassName = false;
	public void setIsFullClassName(boolean isFullClassName) { this.isFullClassName = isFullClassName; }
	public boolean isFullClassName() { return isFullClassName; }
	
	private String category = "gwt-log";
	public void setCategory(String category) { this.category = category; }
	public String getCategory() { return this.category; }
	
	public boolean isEnabled(int logLevel, String logNamespace) {
//		Log.info("logLevel["+logLevel+"] namespace["+logNamespace+"]");
		boolean ret = false;
		if(null!=logNamespace&&null!=filters&&filters.size()>0){
			Iterator<Entry<String, Integer>> iter = filters.entrySet().iterator();
			while(iter.hasNext()) {
				Entry<String, Integer> entry = iter.next();
				String setNamespace = entry.getKey();
				int cfgLevel = entry.getValue();
				if(logNamespace.startsWith(setNamespace)) {
//					Log.info("startsWith logLevel["+logLevel+"] namespace["+logNamespace+"]");
					if(logLevel>=cfgLevel) {
						ret = true;
//						Log.info("ret["+ret+"] startsWith logLevel["+logLevel+"] >= cfgLevel["+cfgLevel+"] logNamespace["+logNamespace+"]");
						break;
					}
				}
			}
		}
		return ret;
	}
}
