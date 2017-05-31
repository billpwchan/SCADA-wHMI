package com.thalesgroup.scadagen.wrapper.widgetcontroller.client.init;

import java.util.Map;

import com.thalesgroup.scadagen.whmi.translation.translationmgr.client.TranslationEngine;
import com.thalesgroup.scadagen.whmi.translation.translationmgr.client.TranslationMgr;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.common.InitReady_i;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.common.Init_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.util.Translation;

public class InitTranslation implements Init_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(InitTranslation.class.getName());
	private final UILogger logger = UILoggerFactory.getInstance().getLogger(className);

	private InitTranslation() {}
	private static InitTranslation instance = null;
	public static InitTranslation getInstance() { 
		if ( null == instance ) instance = new InitTranslation();
		return instance;
	}
	
	@Override
	public void init(Map<String, Object> params, InitReady_i initReady) {
		// TODO Auto-generated method stub
		
	}
	
	public final static String strTranslatePatten = "Translation_TranslatePatten";
	public void setTranslationPattern(String translatePatten) {
		final String function = "initTranslation";
		logger.begin(className, function);
		
		if ( null != translatePatten ) Translation.setTranslatePatten(translatePatten);

		logger.end(className, function);
	}
	
	public final static String strTranslateFlag = "Translation_TranslateFlag";
	public void setTranslationFlag(String translateFlag) {
		final String function = "initTranslation";
		logger.begin(className, function);
		
		if ( null != translateFlag ) Translation.setTranslateFlag(translateFlag);
	
		logger.end(className, function);
	}
	
	public void initTranslationEngine() {
		final String function = "initTranslationEngine";
		logger.begin(className, function);

		TranslationMgr.getInstance().setTranslationEngine(new TranslationEngine() {
			@Override
			public String getMessage(String message) {
				
				return Translation.getDBMessage(message);
			}
		});
		
		logger.end(className, function);
	}

}
