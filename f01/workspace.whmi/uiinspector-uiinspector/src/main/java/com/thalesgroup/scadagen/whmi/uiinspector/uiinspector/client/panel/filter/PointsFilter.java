package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.panel.filter;

import java.util.List;

import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.thalesgroup.scadagen.whmi.config.configenv.client.ReadProp;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.common.UIPanelInspector_i;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;

public class PointsFilter {

	private final static UILogger_i logger = UILoggerFactory.getInstance().getUILogger(PointsFilter.class.getName());
	
	private static boolean isRegExpMatch(RegExp regExp, String input) {
		final String function = "isRegExpMatch";
		
		logger.begin(function);
		logger.debug(function, "regExp[{}] input[{}]", regExp, input);
		MatchResult matcher = regExp.exec(input);
		boolean matchFound = matcher != null;
		if ( matchFound ) {
			if ( logger.isDebugEnabled() ) {
				for ( int i = 0 ; i < matcher.getGroupCount(); i++) {
					String groupStr = matcher.getGroup(i);
					logger.debug(function, "input[{}] groupStr[{}]", input, groupStr);
				}
			}
		}
		logger.debug(function, "matchFound[{}]", matchFound);
		logger.end(function);
		return matchFound;
	}
	
	private static boolean isRegExpMatch(List<String> regExpPatterns, String dbaddress) {
		boolean listMatch = false;
		for ( String regExpPattern : regExpPatterns ) {
			if ( isRegExpMatch( RegExp.compile(regExpPattern), dbaddress) )	{ 
				listMatch = true;
				break;
			}
		}
		return listMatch;
	}

	public static void prepareFilter(List<String> filters, String dictionariesCacheName, String tab, String listConfigName) {
		final String function = "prepareFilter";
		logger.begin(function);
		logger.debug(function, "listConfigName[{}]", listConfigName);
		if ( null != filters ) {
			String fileName = UIPanelInspector_i.strConfigPrefix+tab+UIPanelInspector_i.strConfigExtension;
			String keyNumName = UIPanelInspector_i.strConfigPrefix+tab+listConfigName+UIPanelInspector_i.strConfigNameSize;
			logger.debug(function, "fileName[{}] keyNumName[{}]", fileName, keyNumName);
			int numOfFilter = ReadProp.readInt(dictionariesCacheName, fileName, keyNumName, 0);
			for ( int y = 0 ; y < numOfFilter ; ++y ) {
				String key = UIPanelInspector_i.strConfigPrefix+tab+listConfigName+UIPanelInspector_i.strDot+y;
				String value = ReadProp.readString(dictionariesCacheName, fileName, key, "");
				logger.debug(function, "key[{}] value[{}]", key, value);
				filters.add(value);
			}
		} else {
			logger.warn(function, "filters IS NULL");
		}
		logger.end(function);
	}

	public static void applyFiltedList(List<String> list, List<String> regExpPatternBlackList, List<String> regExpPatternWhiteList, String dbaddress) {
		final String function = "applyFiltedList";
		logger.begin(function);
		boolean blackListMatch=false, whileListMatch=false;
		blackListMatch = isRegExpMatch(regExpPatternBlackList, dbaddress);
		if ( !blackListMatch ) { whileListMatch = isRegExpMatch(regExpPatternWhiteList, dbaddress); }
		if ( whileListMatch ) { list.add(dbaddress); }
		logger.end(function);
	}
}
