package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.tab;

import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.common.UIInspectorTab_i;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;

public class UIInspectorTabFactory {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIInspectorTabFactory.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private UIInspectorTabFactory() {}
	private static UIInspectorTabFactory instance = null;
	public static UIInspectorTabFactory getInstance() {
		if ( null == instance ) instance = new UIInspectorTabFactory();
		return instance;
	}
	
	public UIInspectorTab_i getUIInspectorTabFactory(String key) {
		final String function = "getUIInspectorTabFactory";
		logger.begin(className, function);
		logger.debug(className, function, "key[{}]", key);
		
		UIInspectorTab_i uiInspectorTab_i = null;
		
		String strUIInspectorInfo				= UIWidgetUtil.getClassSimpleName(UIInspectorInfo.class.getName());
		String strUIInspectorControl			= UIWidgetUtil.getClassSimpleName(UIInspectorControl.class.getName());
		String strUIInspectorTag				= UIWidgetUtil.getClassSimpleName(UIInspectorTag.class.getName());
		String strUIInspectorAdvance			= UIWidgetUtil.getClassSimpleName(UIInspectorAdvance.class.getName());
		
		String strUIInspectorHeader				= UIWidgetUtil.getClassSimpleName(UIInspectorHeader.class.getName());
		String strUIInspectorEquipmentReserve	= UIWidgetUtil.getClassSimpleName(UIInspectorEquipmentReserve.class.getName());
		
		if ( null != key ) {
			if ( key.equals(strUIInspectorInfo) ) {
				uiInspectorTab_i = new UIInspectorInfo();
			} else 
			if ( key.equals(strUIInspectorControl) ) {
				uiInspectorTab_i = new UIInspectorControl();
			} else 
			if ( key.equals(strUIInspectorTag) ) {
				uiInspectorTab_i = new UIInspectorTag();
			} else 
			if ( key.equals(strUIInspectorAdvance) ) {
				uiInspectorTab_i = new UIInspectorAdvance();
			} else 
			if ( key.equals(strUIInspectorHeader) ) {
				uiInspectorTab_i = new UIInspectorHeader();
			} else 
			if ( key.equals(strUIInspectorEquipmentReserve) ) {
				uiInspectorTab_i = new UIInspectorEquipmentReserve();
			}
		} else {
			logger.warn(className, function, "key IS NULL");
		}
		if ( null == uiInspectorTab_i ) logger.warn(className, function, "uiInspectorTab_i IS NULL");
		logger.end(className, function);
		return uiInspectorTab_i;
	}
}
