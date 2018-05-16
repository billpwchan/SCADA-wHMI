package com.thalesgroup.scadagen.whmi.config.configenv.client.logger;

import java.util.Map;
import com.google.gwt.json.client.JSONArray;
import com.thalesgroup.scadagen.whmi.config.configenv.client.ReadJson;
import com.thalesgroup.scadagen.whmi.config.configenv.client.ReadJsonFile;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;

public class LoggerConfigFolderLoader {

	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger("UILogger", this.getClass().getName());
	
	public Map<String, Integer> getLoggerFilter() {
		String f = "getLoggerFilter";
		logger.begin(f);
		
		Map<String, Integer> map = null;
		JSONArray jsonArray = ReadJsonFile.readArray(
				LoggerConfigLoader_i.CACHE_NAME_DICTIONARYIES
				, LoggerConfigLoader_i.FILE_NAME_ATTRIBUTE
				, LoggerConfigLoader_i.Attribute.loggerfilter.toString());
		logger.info(f, "jsonArray[{}]", jsonArray);
		
		if(null!=jsonArray) {
			map = ReadJson.readObjectEntrySetStrInt(jsonArray);
		} else {
			logger.warn(f, "jsonArray IS NULL");
		}

		logger.end(f);
		return map;
	}
	
	public Map<String, String> getLoggerMsg() {
		String f = "getLoggerMsg";
		logger.begin(f);
		
		Map<String, String> map = null;
		JSONArray jsonArray = ReadJsonFile.readArray(
				LoggerConfigLoader_i.CACHE_NAME_DICTIONARYIES
				, LoggerConfigLoader_i.FILE_NAME_ATTRIBUTE
				, LoggerConfigLoader_i.Attribute.loggermsg.toString());
		logger.info(f, "jsonArray[{}]", jsonArray);
		
		if(null!=jsonArray) {
			map = ReadJson.readObjectEntrySetStrStr(jsonArray);
		} else {
			logger.warn(f, "jsonArray IS NULL");
		}

		logger.end(f);
		return map;
	}
	
	public Map<String, Integer> getLoggerLevel() {
		String f = "getLoggerLevel";
		logger.begin(f);
		
		Map<String, Integer> map = null;
		JSONArray jsonArray = ReadJsonFile.readArray(
				LoggerConfigLoader_i.CACHE_NAME_DICTIONARYIES
				, LoggerConfigLoader_i.FILE_NAME_ATTRIBUTE
				, LoggerConfigLoader_i.Attribute.loggerlevel.toString());
		logger.info(f, "jsonArray[{}]", jsonArray);
		
		if(null!=jsonArray) {
			map = ReadJson.readObjectEntrySetStrInt(jsonArray);
		} else {
			logger.warn(f, "jsonArray IS NULL");
		}

		logger.end(f);
		return map;
	}
	
	public String getLoggerCoreName() {
		String f = "getLoggerCoreName";
		logger.begin(f);
		
		String ret = ReadJsonFile.readString(
					LoggerConfigLoader_i.CACHE_NAME_DICTIONARYIES
					, LoggerConfigLoader_i.FILE_NAME_ATTRIBUTE
					, LoggerConfigLoader_i.Attribute.loggercorename.toString()
					, null);

		logger.info(f, "ret[{}]", ret);
		logger.end(f);
		return ret;
	}
	
	public String getLoggerName() {
		String f = "getLoggerName";
		logger.begin(f);
		
		String ret = ReadJsonFile.readString(
					LoggerConfigLoader_i.CACHE_NAME_DICTIONARYIES
					, LoggerConfigLoader_i.FILE_NAME_ATTRIBUTE
					, LoggerConfigLoader_i.Attribute.loggername.toString()
					, null);

		logger.info(f, "ret[{}]", ret);
		logger.end(f);
		return ret;
	}
	
	public String getLoggerCategory() {
		String f = "getLoggerCategory";
		logger.begin(f);
		
		String ret = ReadJsonFile.readString(
					LoggerConfigLoader_i.CACHE_NAME_DICTIONARYIES
					, LoggerConfigLoader_i.FILE_NAME_ATTRIBUTE
					, LoggerConfigLoader_i.Attribute.loggercategory.toString()
					, null);

		logger.info(f, "ret[{}]", ret);
		logger.end(f);
		return ret;
	}

	public boolean getLoggerIsFullClassName() {
		String f = "getLoggerIsFullClassName";
		logger.begin(f);
		
		boolean ret = ReadJsonFile.readBoolean(
					LoggerConfigLoader_i.CACHE_NAME_DICTIONARYIES
					, LoggerConfigLoader_i.FILE_NAME_ATTRIBUTE
					, LoggerConfigLoader_i.Attribute.loggerisfullclassname.toString()
					, false);

		logger.info(f, "ret[{}]", ret);
		logger.end(f);
		return ret;
	}
	
	public int getLoggerCurrentLevel() {
		String f = "getLoggerCurrentLevel";
		logger.begin(f);
		
		int ret = ReadJsonFile.readInt(
					LoggerConfigLoader_i.CACHE_NAME_DICTIONARYIES
					, LoggerConfigLoader_i.FILE_NAME_ATTRIBUTE
					, LoggerConfigLoader_i.Attribute.loggercurrentlevel.toString()
					, -1);

		logger.info(f, "ret[{}]", ret);
		logger.end(f);
		return ret;
	}
	
}
