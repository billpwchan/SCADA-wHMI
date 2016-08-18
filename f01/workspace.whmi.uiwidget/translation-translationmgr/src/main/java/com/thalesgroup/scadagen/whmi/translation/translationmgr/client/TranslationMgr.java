package com.thalesgroup.scadagen.whmi.translation.translationmgr.client;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;

public class TranslationMgr {
	
	private final String className = "TranslationMgr";
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
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
		logger.info(className, "getTranslation", "message[{}]", message);		
		String translation = (null != engines?engines.getMessage(message):message);
		logger.info(className, "getTranslation", "translation[{}]", translation);
		return translation;
	}

}
