package com.thalesgroup.scadagen.whmi.config.configenv.client.logger;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.core.factory.UILoggerCoreFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.filter.UILoggerExConfig;

public class LoggerConfig {

	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger("UILogger", this.getClass().getName());

	public Map<String, String> getMapEntrySetStrStr(String strLoggerNameFilterString) {
		final String f = "getMapEntrySetStrStr";
		logger.begin(f);
		Map<String, String> map = null;
		logger.debug(f, "strLoggerNameFilterString[{}]", strLoggerNameFilterString);
		if(null!=strLoggerNameFilterString) {
			map = new LinkedHashMap<String, String>();
			String strLoggerNameFilters[] = strLoggerNameFilterString.split(LoggerConfigLoader_i.STR_COMMA);
			if(null!=strLoggerNameFilters&&strLoggerNameFilters.length>0) {
				for(String strLoggerNameFilter: strLoggerNameFilters) {
					logger.debug(f, "strLoggerNameFilter[{}]", strLoggerNameFilter);
					if(null!=strLoggerNameFilter) {
						String strLoggerNameFilterElements[] = strLoggerNameFilter.split(LoggerConfigLoader_i.STR_SPLITER);
						if(null!=strLoggerNameFilterElements&&strLoggerNameFilterElements.length>1) {
							String strLoggerNameSpace = strLoggerNameFilterElements[0];
							String strLoggerNameSpaceLevel = strLoggerNameFilterElements[1];
							logger.debug(f, "strLoggerNameSpaceLevel[{}] strLoggerNameSpace[{}]", strLoggerNameSpaceLevel, strLoggerNameSpace);
							map.put(strLoggerNameSpace, strLoggerNameSpace);
						}
					}
				}
			}
		}

		logger.end(f);
		return map;
	}
	
	public Map<String, Integer> getMapEntrySetStrInt(String strLoggerNameFilterString) {
		final String f = "getMapEntrySetStrInt";
		logger.begin(f);
		Map<String, Integer> map = null;
		logger.debug(f, "strLoggerNameFilterString[{}]", strLoggerNameFilterString);
		if(null!=strLoggerNameFilterString) {
			map = new LinkedHashMap<String, Integer>();
			String strLoggerNameFilters[] = strLoggerNameFilterString.split(LoggerConfigLoader_i.STR_COMMA);
			if(null!=strLoggerNameFilters&&strLoggerNameFilters.length>0) {
				for(String strLoggerNameFilter: strLoggerNameFilters) {
					logger.debug(f, "strLoggerNameFilter[{}]", strLoggerNameFilter);
					if(null!=strLoggerNameFilter) {
						String strLoggerNameFilterElements[] = strLoggerNameFilter.split(LoggerConfigLoader_i.STR_SPLITER);
						if(null!=strLoggerNameFilterElements&&strLoggerNameFilterElements.length>1) {
							String strLoggerNameSpace = strLoggerNameFilterElements[0];
							String strLoggerNameSpaceLevel = strLoggerNameFilterElements[1];
							logger.debug(f, "strLoggerNameSpaceLevel[{}] strLoggerNameSpace[{}]", strLoggerNameSpaceLevel, strLoggerNameSpace);
							try{
								map.put(strLoggerNameSpace, Integer.parseInt(strLoggerNameSpaceLevel));
							} catch (NumberFormatException ex) {
								logger.warn(f, "NumberFormatException strLoggerNameSpaceLevel[{}]", strLoggerNameSpaceLevel);
								logger.warn(f, ex.toString());
							}
						}
					}
				}
			}
		}

		logger.end(f);
		return map;
	}
	
	public void setLoggerFilterLevel(Map<String, Integer> map) {
		final String f = "setLoggerFilterLevel";
		logger.begin(f);
		
		if(null!=map) {
			for(Entry<String, Integer> entry: map.entrySet()) {
				String filterName = entry.getKey();
				int filterLevel = entry.getValue();
				logger.debug(f, "filterName[{}] filterLevel[{}]", filterName, filterLevel);
				UILoggerExConfig.getInstance().addFilter(filterLevel, filterName);
			}
		}

		logger.end(f);
	}
	
	public void setLoggerMsg(Map<String, String> map) {
		final String f = "setLoggerMsg";
		logger.begin(f);
		
		if(null!=map) {
			for(Entry<String, String> entry: map.entrySet()) {
				String logmsgname = entry.getKey();
				String logmsg = entry.getValue();
				logger.debug(f, "logmsgname[{}] logmsg[{}]", logmsgname, logmsg);
				UILoggerExConfig.getInstance().setMsg(logmsgname, logmsg);
			}
		}

		logger.end(f);
	}
	
	public void setLoggerLevel(Map<String, Integer> map) {
		final String f = "setLoggerLevel";
		logger.begin(f);
		
		if(null!=map) {
			for(Entry<String, Integer> entry: map.entrySet()) {
				String loglevelname = entry.getKey();
				int loglevel = entry.getValue();
				logger.debug(f, "loglevelname[{}] loglevel[{}]", loglevelname, loglevel);
				UILoggerExConfig.getInstance().setLevel(loglevelname, loglevel);
			}
		}

		logger.end(f);
	}
	
	public void setLoggerCore(String strNewLoggerCoreName) {
		final String f = "setLoggerCore";
		logger.begin(f);
		
		String strLoggerCoreName = UILoggerCoreFactory.getInstance().getCoreLoggerName();
		logger.debug(f, "strLoggerCoreName[{}]", strLoggerCoreName);
		
		logger.debug(f, "strNewLoggerCoreName[{}]", strNewLoggerCoreName);
		if(null!=strNewLoggerCoreName) {
			UILoggerCoreFactory.getInstance().setCoreLogger(strNewLoggerCoreName);
			
			// Reload Logger
			logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
		}
		strLoggerCoreName = UILoggerCoreFactory.getInstance().getCoreLoggerName();
		logger.debug(f, "strLoggerCoreName[{}]", strLoggerCoreName);
		
		logger.end(f);
	}
	
	public void setLogger(String strNewLoggerName) {
		final String f = "setLogger";
		logger.begin(f);
		
		String strLoggerName = UILoggerFactory.getInstance().getDefaultLoggerName();
		logger.debug(f, "strLoggerName[{}]", strLoggerName);
		
		logger.debug(f, "strLoggerLevel[{}]", strNewLoggerName);
		if(null!=strNewLoggerName) {
			UILoggerFactory.getInstance().setDefaultLogger(strNewLoggerName);
			logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
		}
		strLoggerName = UILoggerFactory.getInstance().getDefaultLoggerName();
		logger.debug(f, "strLoggerName[{}]", strLoggerName);
		
		logger.end(f);
	}
	
	public void setLoggerCategory(String category) {
		final String f = "setLoggerCategory";
		logger.begin(f);
		logger.debug(f, "category[{}]", category);
		UILoggerExConfig.getInstance().setCategory(category);
		logger.end(f);
	}
	
	public void setLoggerIsFullClassName(boolean isFullClassName) {
		final String f = "setLoggerIsFullClassName";
		logger.begin(f);

		logger.debug(f, "isFullClassName[{}]", isFullClassName);
		UILoggerExConfig.getInstance().setIsFullClassName(isFullClassName);
		
		logger.end(f);
	}
	
	public void setLoggerCurrentLevel(int intLoggerCurrentLevel) {
		final String f = "setLoggerCurrentLevel";
		logger.begin(f);

		logger.debug(f, "intLoggerCurrentLevel[{}]", intLoggerCurrentLevel);
		if(intLoggerCurrentLevel>0) logger.setCurrentLogLevel(intLoggerCurrentLevel);
		
		logger.end(f);
	}
}
