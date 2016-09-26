package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.util;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;

public class ReadPropTest {
	
	private final String className = UIWidgetUtil.getClassSimpleName(ReadPropTest.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private final String dictionariesCacheName = "UIInspectorPanel";
	private final String propPrefix = "properties_test.";
	private final String prop = propPrefix+"properties";
	
	public ReadPropTest() {
		
	}
	
	public void test() {
		testReadBoolean();
		testReadInt();
		testReadString();
	}
	
	public void testReadBoolean() {
		final String function = "test";
		String targetClass = UIWidgetUtil.getClassSimpleName(ReadProp.class.getName());
		String targetFunction = "readBoolean";
		logger.info(className, function, "Testing [{}] [{}] Begin", targetClass, targetFunction);
		
		final String boolean_test1 = propPrefix+"boolean_test1";
		
		boolean boolean_testresult1 = ReadProp.readBoolean(dictionariesCacheName, prop, boolean_test1, false);
		
		logger.info(className, function, "name[{}] result[{}]", boolean_test1, boolean_testresult1);
		
		final String boolean_test2 = propPrefix+"boolean_test2";
		
		boolean boolean_testresult2 = ReadProp.readBoolean(dictionariesCacheName, prop, boolean_test2, false);
		
		logger.info(className, function, "name[{}] result[{}]", boolean_test2, boolean_testresult2);
		
		final String boolean_test3 = propPrefix+"boolean_test3";
		
		boolean boolean_testresult3 = ReadProp.readBoolean(dictionariesCacheName, prop, boolean_test3, false);
		
		logger.info(className, function, "name[{}] result[{}]", boolean_test3, boolean_testresult3);
		
		final String boolean_test4 = propPrefix+"boolean_test4";
		
		boolean boolean_testresult4 = ReadProp.readBoolean(dictionariesCacheName, prop, boolean_test4, true);
		
		logger.info(className, function, "name[{}] result[{}]", boolean_test4, boolean_testresult4);
		
		logger.info(className, function, "Testing [{}] [{}] End", className, targetFunction);
	}
	
	public void testReadInt() {
		final String function = "test";
		String targetClass = UIWidgetUtil.getClassSimpleName(ReadProp.class.getName());
		String targetFunction = "readInt";
		logger.info(className, function, "Testing [{}] [{}] Begin", targetClass, targetFunction);
		
		final String int_test1 = propPrefix+"int_test1";
		
		int int_testresult1 = ReadProp.readInt(dictionariesCacheName, prop, int_test1, 60*1000);

		logger.info(className, function, "name[{}] result[{}]", int_test1, int_testresult1);
		
		final String int_test2 = propPrefix+"int_test2";
		
		int int_testresult2 = ReadProp.readInt(dictionariesCacheName, prop, int_test2, 60*1000);
		
		logger.info(className, function, "name[{}] result[{}]", int_test2, int_testresult2);
		
		final String int_test3 = propPrefix+"int_test3";
		
		int int_testresult3 = ReadProp.readInt(dictionariesCacheName, prop, int_test3, 60*1000);
		
		logger.info(className, function, "name[{}] result[{}]", int_test3, int_testresult3);
		
		logger.info(className, function, "Testing [{}] [{}] End", className, targetFunction);
	}
	
	public void testReadString() {
		final String function = "test";
		String targetClass = UIWidgetUtil.getClassSimpleName(ReadProp.class.getName());
		String targetFunction = "readString";
		logger.info(className, function, "Testing [{}] [{}] Begin", targetClass, targetFunction);
		
		final String string_test1 = propPrefix+"string_test1";
		
		String string_testresult1 = ReadProp.readString(dictionariesCacheName, prop, string_test1, "");
		
		logger.info(className, function, "name[{}] result[{}]", string_test1, string_testresult1);
		
//		final String string_test2 = propPrefix+"string_test2";
//		
//		String string_testresult2 = ReadProp.readString(dictionariesCacheName, prop, string_test2, "notempty");
//		
//		logger.info(className, function, "name[{}] result[{}]", string_test2, string_testresult2);
		
		final String string_test2 = propPrefix+"string_test3";
		
		String string_testresult2 = ReadProp.readString(dictionariesCacheName, prop, string_test2, "");
		
		logger.info(className, function, "name[{}] result[{}]", string_test2, string_testresult2);
		
		logger.info(className, function, "Testing [{}] [{}] End", className, targetFunction);
	}
}
