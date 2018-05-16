package com.thalesgroup.scadagen.whmi.config.configenv.client.logger;

import java.util.Map;

import com.thalesgroup.scadagen.whmi.config.configenv.client.logger.LoggerConfig;
import com.thalesgroup.scadagen.whmi.config.configenv.client.logger.LoggerConfigFolderLoader;
import com.thalesgroup.scadagen.whmi.config.configenv.client.logger.LoggerConfigLoader_i;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;

public class LoggerConfigLoader {

	private final String STR_LOGGER_CORE_NAME			= LoggerConfigLoader_i.Attribute.loggercorename.toString();
	
	private final String STR_LOGGER_LEVEL				= LoggerConfigLoader_i.Attribute.loggerlevel.toString();
	
	private final String STR_LOGGER_IS_FULLCLASSNAME	= LoggerConfigLoader_i.Attribute.loggerisfullclassname.toString();
	
	private final String STR_LOGGER_NAME				= LoggerConfigLoader_i.Attribute.loggername.toString();
	private final String STR_LOGGER_CATEGORY			= LoggerConfigLoader_i.Attribute.loggercategory.toString();
	private final String STR_LOGGER_CURRENT_LEVEL		= LoggerConfigLoader_i.Attribute.loggercurrentlevel.toString();
	private final String STR_LOGGER_FILTER				= LoggerConfigLoader_i.Attribute.loggerfilter.toString();
	
	public UILogger_i load(UILogger_i logger, Map<String, String> settings, LoggerConfig logCfg, LoggerConfigFolderLoader logCfgFile) {
		final String f = "setup";
		logger.begin(f);

		String strNewLoggerCoreName = settings.get(STR_LOGGER_CORE_NAME);
		logger.info(f, "strNewLoggerCoreName[{}]", strNewLoggerCoreName);
		if(null!=strNewLoggerCoreName) {
			logCfg.setLoggerCore(strNewLoggerCoreName);
		} else {
			logCfg.setLoggerCore(logCfgFile.getLoggerCoreName());
		}
		
		String strLoggerLevelString = settings.get(STR_LOGGER_LEVEL);
		logger.info(f, "strLoggerLevelString[{}]", strLoggerLevelString);
		if(null!=strLoggerLevelString) {
			logCfg.setLoggerLevel(logCfg.getMapEntrySetStrInt(strLoggerLevelString));
		} else {
			logCfg.setLoggerLevel(logCfgFile.getLoggerLevel());
		}

		String strNewLoggerName = settings.get(STR_LOGGER_NAME);
		logger.info(f, "strNewLoggerName[{}]", strNewLoggerName);
		if(null!=strNewLoggerName) {
			logCfg.setLogger(strNewLoggerName);
		} else {
			logCfg.setLogger(logCfgFile.getLoggerName());
		}
		
		String strLoggerIsFullClassName = settings.get(STR_LOGGER_IS_FULLCLASSNAME);
		logger.info(f, "strLoggerIsFullClassName[{}]", strLoggerIsFullClassName);
		if(null!=strLoggerIsFullClassName) {
			boolean isFullClassName = Boolean.parseBoolean(strLoggerIsFullClassName);
			logCfg.setLoggerIsFullClassName(isFullClassName);
		} else {
			logCfg.setLoggerIsFullClassName(logCfgFile.getLoggerIsFullClassName());
		}
		
		String strLoggerCategory = settings.get(STR_LOGGER_CATEGORY);
		logger.info(f, "strLoggerCategory[{}]", strLoggerCategory);
		if(null!=strLoggerCategory) {
			logCfg.setLoggerCategory(strLoggerCategory);
		} else {
			logCfg.setLoggerCategory(logCfgFile.getLoggerCategory());
		}

		String strLoggerCurrentLevel = settings.get(STR_LOGGER_CURRENT_LEVEL);
		logger.info(f, "strLoggerCurrentLevel[{}]", strLoggerCurrentLevel);
		if(null!=strLoggerCurrentLevel) {
			try{
				int intLoggerCurrentLevel = Integer.parseInt(strLoggerCurrentLevel);
				logger.info(f, "strLoggerCurrentLevel[{}]", intLoggerCurrentLevel);
				logCfg.setLoggerCurrentLevel(intLoggerCurrentLevel);
			} catch (NumberFormatException ex) {
				logger.warn(f, "NumberFormatException strLoggerCurrentLevel[{}]", strLoggerCurrentLevel);
				logger.warn(f, ex.toString());
			}
		} else {
			logCfg.setLoggerCurrentLevel(logCfgFile.getLoggerCurrentLevel());
		}

		String strLoggerNameFilterString = settings.get(STR_LOGGER_FILTER);
		logger.info(f, "strLoggerNameFilterString[{}]", strLoggerNameFilterString);
		if(null!=strLoggerNameFilterString) {
			logCfg.setLoggerFilterLevel(logCfg.getMapEntrySetStrInt(strLoggerNameFilterString));
		} else {
			logCfg.setLoggerFilterLevel(logCfgFile.getLoggerFilter());
		}

		logger.end(f);
		
		return logger;
	}
}
