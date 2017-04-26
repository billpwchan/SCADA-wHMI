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
		
		if ( null != key ) {
			if ( UIWidgetUtil.getClassSimpleName(UIInspectorInfo.class.getName())
					.equals(key) ) {
				uiInspectorTab_i = new UIInspectorInfo();
			} else if ( UIWidgetUtil.getClassSimpleName(UIInspectorControl.class.getName())
					.equals(key) ) {
				uiInspectorTab_i = new UIInspectorControl();
			} else if ( UIWidgetUtil.getClassSimpleName(UIInspectorTag.class.getName())
					.equals(key) ) {
				uiInspectorTab_i = new UIInspectorTag();
			} else if ( UIWidgetUtil.getClassSimpleName(UIInspectorAdvance.class.getName())
					.equals(key) ) {
				uiInspectorTab_i = new UIInspectorAdvance();
			} else if ( UIWidgetUtil.getClassSimpleName(UIInspectorHeader.class.getName())
					.equals(key) ) {
				uiInspectorTab_i = new UIInspectorHeader();
			}
		} else {
			logger.warn(className, function, "key IS NULL");
		}
		if ( null == uiInspectorTab_i ) logger.warn(className, function, "uiInspectorTab_i IS NULL");
		logger.end(className, function);
		return uiInspectorTab_i;
	}
}
