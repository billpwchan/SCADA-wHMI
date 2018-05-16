package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.tab;

import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.common.UIInspectorTab_i;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;

public class UIInspectorTabFactory {

	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
	
	private UIInspectorTabFactory() {}
	private static UIInspectorTabFactory instance = null;
	public static UIInspectorTabFactory getInstance() {
		if ( null == instance ) instance = new UIInspectorTabFactory();
		return instance;
	}
	
	public UIInspectorTab_i getUIInspectorTabFactory(String key) {
		final String function = "getUIInspectorTabFactory";
		logger.begin(function);
		logger.debug(function, "key[{}]", key);
		
		UIInspectorTab_i uiInspectorTab_i = null;
		
		if ( null != key ) {
			if ( UIInspectorInfo.class.getSimpleName()
					.equals(key)) {
				uiInspectorTab_i = new UIInspectorInfo();
			} else if ( UIInspectorControl.class.getSimpleName()
					.equals(key)) {
				uiInspectorTab_i = new UIInspectorControl();
			} else if ( UIInspectorTag.class.getSimpleName()
					.equals(key)) {
				uiInspectorTab_i = new UIInspectorTag();
			} else if ( UIInspectorAdvance.class.getSimpleName()
					.equals(key)) {
				uiInspectorTab_i = new UIInspectorAdvance();
			} else if ( UIInspectorHeader.class.getSimpleName()
					.equals(key)) {
				uiInspectorTab_i = new UIInspectorHeader();
			}
		} else {
			logger.warn(function, "key IS NULL");
		}
		if ( null == uiInspectorTab_i ) logger.warn(function, "uiInspectorTab_i IS NULL");
		logger.end(function);
		return uiInspectorTab_i;
	}
}
