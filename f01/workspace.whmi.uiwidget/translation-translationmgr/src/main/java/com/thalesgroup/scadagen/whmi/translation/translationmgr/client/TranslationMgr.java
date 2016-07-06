package com.thalesgroup.scadagen.whmi.translation.translationmgr.client;

import java.util.logging.Level;
import java.util.logging.Logger;

public class TranslationMgr {
	
	private static Logger logger = Logger.getLogger(TranslationMgr.class.getName());
	
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
		logger.log(Level.FINE, "getTranslation message["+message+"]");		
		String translation = (null != engines?engines.getMessage(message):message);
		logger.log(Level.FINE, "getTranslation translation["+translation+"]");
		return translation;
	}

}
