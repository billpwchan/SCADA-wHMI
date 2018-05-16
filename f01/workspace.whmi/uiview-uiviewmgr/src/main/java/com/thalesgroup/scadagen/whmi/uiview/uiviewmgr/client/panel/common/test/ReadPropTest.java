package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.test;

import com.thalesgroup.scadagen.whmi.config.configenv.client.ReadProp;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;

public class ReadPropTest {

	private final String className = this.getClass().getSimpleName();
	private final UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
	
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
		String targetClass = ReadProp.class.getSimpleName();
		String targetFunction = "readBoolean";
		logger.debug(function, "Testing [{}] [{}] Begin", targetClass, targetFunction);
		
		final String boolean_test1 = propPrefix+"boolean_test1";
		
		boolean boolean_testresult1 = ReadProp.readBoolean(dictionariesCacheName, prop, boolean_test1, false);
		
		logger.debug(function, "name[{}] result[{}]", boolean_test1, boolean_testresult1);
		
		final String boolean_test2 = propPrefix+"boolean_test2";
		
		boolean boolean_testresult2 = ReadProp.readBoolean(dictionariesCacheName, prop, boolean_test2, false);
		
		logger.debug(function, "name[{}] result[{}]", boolean_test2, boolean_testresult2);
		
		final String boolean_test3 = propPrefix+"boolean_test3";
		
		boolean boolean_testresult3 = ReadProp.readBoolean(dictionariesCacheName, prop, boolean_test3, false);
		
		logger.debug(function, "name[{}] result[{}]", boolean_test3, boolean_testresult3);
		
		final String boolean_test4 = propPrefix+"boolean_test4";
		
		boolean boolean_testresult4 = ReadProp.readBoolean(dictionariesCacheName, prop, boolean_test4, true);
		
		logger.debug(function, "name[{}] result[{}]", boolean_test4, boolean_testresult4);
		
		logger.debug(function, "Testing [{}] [{}] End", className, targetFunction);
	}
	
	public void testReadInt() {
		final String function = "test";
		String targetClass = ReadProp.class.getSimpleName();
		String targetFunction = "readInt";
		logger.debug(function, "Testing [{}] [{}] Begin", targetClass, targetFunction);
		
		final String int_test1 = propPrefix+"int_test1";
		
		int int_testresult1 = ReadProp.readInt(dictionariesCacheName, prop, int_test1, 60*1000);

		logger.debug(function, "name[{}] result[{}]", int_test1, int_testresult1);
		
		final String int_test2 = propPrefix+"int_test2";
		
		int int_testresult2 = ReadProp.readInt(dictionariesCacheName, prop, int_test2, 60*1000);
		
		logger.debug(function, "name[{}] result[{}]", int_test2, int_testresult2);
		
		final String int_test3 = propPrefix+"int_test3";
		
		int int_testresult3 = ReadProp.readInt(dictionariesCacheName, prop, int_test3, 60*1000);
		
		logger.debug(function, "name[{}] result[{}]", int_test3, int_testresult3);
		
		logger.debug(function, "Testing [{}] [{}] End", className, targetFunction);
	}
	
	public void testReadString() {
		final String function = "test";
		String targetClass = ReadProp.class.getSimpleName();
		String targetFunction = "readString";
		logger.debug(function, "Testing [{}] [{}] Begin", targetClass, targetFunction);
		
		final String string_test1 = propPrefix+"string_test1";
		
		String string_testresult1 = ReadProp.readString(dictionariesCacheName, prop, string_test1, "");
		
		logger.debug(function, "name[{}] result[{}]", string_test1, string_testresult1);
		
//		final String string_test2 = propPrefix+"string_test2";
//		
//		String string_testresult2 = ReadProp.readString(dictionariesCacheName, prop, string_test2, "notempty");
//		
//		logger.debug(function, "name[{}] result[{}]", string_test2, string_testresult2);
		
		final String string_test2 = propPrefix+"string_test3";
		
		String string_testresult2 = ReadProp.readString(dictionariesCacheName, prop, string_test2, "");
		
		logger.debug(function, "name[{}] result[{}]", string_test2, string_testresult2);
		
		logger.debug(function, "Testing [{}] [{}] End", className, targetFunction);
	}
}
