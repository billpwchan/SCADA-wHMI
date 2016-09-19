package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.util;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;

public class ReadPropTest {
	
	private final String className = UIWidgetUtil.getClassSimpleName(ReadPropTest.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private final String dictionariesCacheName = "UIInspectorPanel";
	private final String inspDialogBoxPropPrefix = "inspectorpaneldialogbox.";
	private final String inspDialogBoxProp = inspDialogBoxPropPrefix+"properties";
	
	public ReadPropTest() {
		
	}
	
	public void test() {
		testReadBoolean();
		testReadInt();
		testReadString();
	}
	
	public void testReadBoolean() {
		final String function = "test";
		String className = UIWidgetUtil.getClassSimpleName(ReadProp.class.getName());
		String functionName = "readBoolean";
		logger.info(className, function, "Testing [{}] [{}] Begin", className, functionName);
		
		final String strModal = "modal";
		
		boolean modal_true = ReadProp.readBoolean(dictionariesCacheName, inspDialogBoxProp, inspDialogBoxPropPrefix+strModal, false);
		
		logger.info(className, function, "modal_true[{}]", modal_true);
		
		boolean modal_false = ReadProp.readBoolean(dictionariesCacheName, inspDialogBoxProp, inspDialogBoxPropPrefix+strModal, false);
		
		logger.info(className, function, "modal_false[{}]", modal_false);
		
		boolean modal_notExists_false = ReadProp.readBoolean(dictionariesCacheName, inspDialogBoxProp, inspDialogBoxPropPrefix+strModal+"0", false);
		
		logger.info(className, function, "modal_notExists_false[{}]", modal_notExists_false);
		
		boolean modal_notExists_true = ReadProp.readBoolean(dictionariesCacheName, inspDialogBoxProp, inspDialogBoxPropPrefix+strModal+"0", true);
		
		logger.info(className, function, "modal_notExists_true[{}]", modal_notExists_true);
		
		logger.info(className, function, "Testing [{}] [{}] End", className, functionName);
	}
	
	public void testReadInt() {
		final String function = "test";
		String className = UIWidgetUtil.getClassSimpleName(ReadProp.class.getName());
		String functionName = "readInt";
		logger.info(className, function, "Testing [{}] [{}] Begin", className, functionName);
		
		final String strAutoCloseExpiredMillSecond = "autoCloseExpiredMillSecond";
		
		int autoCloseExpiredMillSecond_valid_60000 = ReadProp.readInt(dictionariesCacheName, inspDialogBoxProp, inspDialogBoxPropPrefix+strAutoCloseExpiredMillSecond, 60*1000);
		
		logger.info(className, function, "autoCloseExpiredMillSecond_valid_60000[{}]", autoCloseExpiredMillSecond_valid_60000);
		
		int autoCloseExpiredMillSecond_valid_10000 = ReadProp.readInt(dictionariesCacheName, inspDialogBoxProp, inspDialogBoxPropPrefix+strAutoCloseExpiredMillSecond, 60*1000);
		
		logger.info(className, function, "autoCloseExpiredMillSecond_valid_10000[{}]", autoCloseExpiredMillSecond_valid_10000);
		
		int autoCloseExpiredMillSecond_notExists = ReadProp.readInt(dictionariesCacheName, inspDialogBoxProp, inspDialogBoxPropPrefix+strAutoCloseExpiredMillSecond+"0", 60*1000);
		
		logger.info(className, function, "autoCloseExpiredMillSecond_notExists[{}]", autoCloseExpiredMillSecond_notExists);
		
		logger.info(className, function, "Testing [{}] [{}] End", className, functionName);
	}
	
	public void testReadString() {
		final String function = "test";
		String className = UIWidgetUtil.getClassSimpleName(ReadProp.class.getName());
		String functionName = "readString";
		logger.info(className, function, "Testing [{}] [{}] Begin", className, functionName);
		
		final String strTitleName = "list";
		
		String value_valid = ReadProp.readString(dictionariesCacheName, inspDialogBoxPropPrefix, inspDialogBoxPropPrefix+strTitleName, "");
		
		logger.info(className, function, "value_valid[{}]", value_valid);
		
		String value_valid_empty = ReadProp.readString(dictionariesCacheName, inspDialogBoxPropPrefix, inspDialogBoxPropPrefix+strTitleName, "notempty");
		
		logger.info(className, function, "value_valid_empty[{}]", value_valid_empty);
		
		String value_invalid = ReadProp.readString(dictionariesCacheName, inspDialogBoxPropPrefix, inspDialogBoxPropPrefix+strTitleName+"0", "");
		
		logger.info(className, function, "value_invalid[{}]", value_invalid);
		
		logger.info(className, function, "Testing [{}] [{}] End", className, functionName);
	}
}
