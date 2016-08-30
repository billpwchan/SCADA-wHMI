package com.thalesgroup.scadagen.whmi.translation.translationmgr.client;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;

public class TranslationMgr {
	
	private final String className = UIWidgetUtil.getClassSimpleName(TranslationMgr.class.getName());
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
		final String function = "getTranslation";
		logger.begin(className, function);
		logger.debug(className, function, "message[{}]", message);		
		String translation = (null != engines?engines.getMessage(message):message);
		logger.debug(className, function, "translation[{}]", translation);
		logger.end(className, function);
		return translation;
	}

}
