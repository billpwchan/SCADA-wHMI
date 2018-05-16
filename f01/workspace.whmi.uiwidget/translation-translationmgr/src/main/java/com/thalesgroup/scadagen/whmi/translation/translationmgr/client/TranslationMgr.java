package com.thalesgroup.scadagen.whmi.translation.translationmgr.client;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;

public class TranslationMgr {

	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
	
	private TranslationMgr() {};
	private static TranslationMgr instance = null;
	public static TranslationMgr getInstance() {
		if ( null == instance ) 
			instance = new TranslationMgr();
		return instance;
	}

	private TranslationEngine engines = null;
	public void setTranslationEngine(TranslationEngine translation ) { this.engines = translation; }
	public TranslationEngine getTranslationEngine() { return this.engines; }
	
	public String getTranslation(String message) {
		final String function = "getTranslation";
		logger.begin(function);
		logger.trace(function, "message[{}]", message);		
		String translation = (null != engines?engines.getMessage(message):message);
		logger.trace(function, "translation[{}]", translation);
		logger.end(function);
		return translation;
	}
	
	public String getTranslation(String msgWithPlaceHolder, Object[] msgParam) {
		final String function = "getTranslation";
		logger.begin(function);
		logger.trace(function, "message[{}]", msgWithPlaceHolder);
		String translation = (null != engines?engines.getMessage(msgWithPlaceHolder,msgParam):msgWithPlaceHolder);
		logger.trace(function, "translation[{}]", translation);
		logger.end(function);
		return translation;
	}
}
