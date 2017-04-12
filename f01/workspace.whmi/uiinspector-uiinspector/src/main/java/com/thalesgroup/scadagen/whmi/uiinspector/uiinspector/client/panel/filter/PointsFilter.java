package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.panel.filter;

import java.util.List;

import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.thalesgroup.scadagen.whmi.config.configenv.client.ReadProp;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.common.UIPanelInspector_i;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;

public class PointsFilter {
	
	private static final String className = UIWidgetUtil.getClassSimpleName(PointsFilter.class.getName());
	private static final UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private static boolean isRegExpMatch(RegExp regExp, String input) {
		final String function = "isRegExpMatch";
		
		logger.begin(className, function);
		logger.debug(className, function, "regExp[{}] input[{}]", regExp, input);
		MatchResult matcher = regExp.exec(input);
		boolean matchFound = matcher != null;
		if ( matchFound ) {
			if ( logger.isDebugEnabled() ) {
				for ( int i = 0 ; i < matcher.getGroupCount(); i++) {
					String groupStr = matcher.getGroup(i);
					logger.debug(className, function, "input[{}] groupStr[{}]", input, groupStr);
				}
			}
		}
		logger.debug(className, function, "matchFound[{}]", matchFound);
		logger.end(className, function);
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
		logger.begin(className, function);
		logger.debug(className, function, "listConfigName[{}]", listConfigName);
		if ( null != filters ) {
			String fileName = UIPanelInspector_i.strConfigPrefix+tab+UIPanelInspector_i.strConfigExtension;
			String keyNumName = UIPanelInspector_i.strConfigPrefix+tab+listConfigName+UIPanelInspector_i.strConfigNameSize;
			logger.debug(className, function, "fileName[{}] keyNumName[{}]", fileName, keyNumName);
			int numOfFilter = ReadProp.readInt(dictionariesCacheName, fileName, keyNumName, 0);
			for ( int y = 0 ; y < numOfFilter ; ++y ) {
				String key = UIPanelInspector_i.strConfigPrefix+tab+listConfigName+UIPanelInspector_i.strDot+y;
				String value = ReadProp.readString(dictionariesCacheName, fileName, key, "");
				logger.debug(className, function, "key[{}] value[{}]", key, value);
				filters.add(value);
			}
		} else {
			logger.warn(className, function, "filters IS NULL");
		}
		logger.end(className, function);
	}

	public static void applyFiltedList(List<String> list, List<String> regExpPatternBlackList, List<String> regExpPatternWhiteList, String dbaddress) {
		final String function = "applyFiltedList";
		logger.begin(className, function);
		boolean blackListMatch=false;
		boolean whileListMatch=false;
		blackListMatch = isRegExpMatch(regExpPatternBlackList, dbaddress);
		if ( !blackListMatch ) { 
			whileListMatch = isRegExpMatch(regExpPatternWhiteList, dbaddress);
		}
		if ( whileListMatch ) { list.add(dbaddress); }
		logger.end(className, function);
	}
}
