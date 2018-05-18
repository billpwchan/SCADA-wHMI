package com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.filter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import com.allen_sauer.gwt.log.client.Log;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerEx.Formatter;

/**
 * @author t0096643
 *
 */
public class UILoggerExConfig {

	private Map<String, String>  mapMsgList   = new HashMap<String, String>();
	private Map<String, Integer> mapLevelList = new HashMap<String, Integer>();
	private UILoggerExConfig() {
		mapMsgList.put(UILoggerExConfig_i.AttributeMsg.PREFIX.toString(),   null);
		mapMsgList.put(UILoggerExConfig_i.AttributeMsg.MSG.toString(),      null);
		mapMsgList.put(UILoggerExConfig_i.AttributeMsg.OCB.toString(),      "{}");
		mapMsgList.put(UILoggerExConfig_i.AttributeMsg.NULL.toString(),     "null");
		mapMsgList.put(UILoggerExConfig_i.AttributeMsg.BEGIN.toString(),    "Begin");
		mapMsgList.put(UILoggerExConfig_i.AttributeMsg.END.toString(),      "End");
		mapMsgList.put(UILoggerExConfig_i.AttributeMsg.BEGINEND.toString(), "BeginEnd");
		
		mapLevelList.put(UILoggerExConfig_i.AttributeLevel.TRACE.toString(), Log.LOG_LEVEL_TRACE);
		mapLevelList.put(UILoggerExConfig_i.AttributeLevel.DEBUG.toString(), Log.LOG_LEVEL_DEBUG);
		mapLevelList.put(UILoggerExConfig_i.AttributeLevel.INFO.toString(),  Log.LOG_LEVEL_INFO);
		mapLevelList.put(UILoggerExConfig_i.AttributeLevel.WARN.toString(),  Log.LOG_LEVEL_WARN);
		mapLevelList.put(UILoggerExConfig_i.AttributeLevel.ERROR.toString(), Log.LOG_LEVEL_ERROR);
		mapLevelList.put(UILoggerExConfig_i.AttributeLevel.FATAL.toString(), Log.LOG_LEVEL_FATAL);
		mapLevelList.put(UILoggerExConfig_i.AttributeLevel.OFF.toString(),   Log.LOG_LEVEL_OFF);
	}
	private static UILoggerExConfig instance = null;
	public static UILoggerExConfig getInstance() { 
		 if (null==instance) { instance = new UILoggerExConfig(); }
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

	private String LOG_STR_OCB      = "{}";
	private String LOG_STR_NULL     = "null";
	
	private Formatter formatter = new Formatter() {
		
		@Override
		public String formatt(String m, Object[] args) {
			String msg = m;
			if ( null != args ) {
				String [] splits = msg.split(LOG_STR_OCB);
				final StringBuffer buffer = new StringBuffer();
				for ( int i = 0 ; i < splits.length ; ++i) {
					buffer.append(splits[i]);
					if ( i < splits.length - 1 ) {
						buffer.append( null != args[i] ? args[i] : LOG_STR_NULL );
					}
				}
				msg = buffer.toString();
			}
			return msg;
		}
	};
	public void setFormatter(Formatter formatter) { this.formatter = formatter; }
	public Formatter getFormatter() { return this.formatter; }

	public void setMsg(String msgname, String msg) { 
		mapMsgList.put(msgname, msg);
		
		if(0==UILoggerExConfig_i.AttributeMsg.OCB.toString().compareTo(msgname)){
			LOG_STR_OCB = msg;
		} else if(0==UILoggerExConfig_i.AttributeMsg.NULL.toString().compareTo(msgname)){
			LOG_STR_NULL = msg;
		}
	}
	public String getMsg(String msgname) { return mapMsgList.get(msgname); }
	
	public void setLevel(String levelname, int level) { mapLevelList.put(levelname, level); }
	public int getLevel(String levelname) { return mapLevelList.get(levelname); }
	
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
