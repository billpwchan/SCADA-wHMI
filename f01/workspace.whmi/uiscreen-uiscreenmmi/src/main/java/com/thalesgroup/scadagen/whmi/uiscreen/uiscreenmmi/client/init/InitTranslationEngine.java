package com.thalesgroup.scadagen.whmi.uiscreen.uiscreenmmi.client.init;

import com.thalesgroup.scadagen.whmi.config.configenv.client.ReadProp;
import com.thalesgroup.scadagen.whmi.translation.translationmgr.client.TranslationEngine;
import com.thalesgroup.scadagen.whmi.translation.translationmgr.client.TranslationMgr;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.wrapper.wrapper.client.util.Translation;

public class InitTranslationEngine {
	
	private final static String className = UIWidgetUtil.getClassSimpleName(InitTranslationEngine.class.getName());
	private final static UILogger logger = UILoggerFactory.getInstance().getLogger(className);

	private final static String strDot = ".";
	private final static String strProperties = "properties";
	private final static String strDCFolder = "UIInspectorPanel";
	private final static String strTranslation = "Translation";
	private final static String strTranslatePatten = "TranslatePatten";
	private final static String strTranslateFlag = "TranslateFlag";
	
	public static void init() {
		String function = "init";
		logger.begin(className, function);
		
		String dcName = strDCFolder;
		String dcFileName = strTranslation+strDot+strProperties;
		{
			String dcAttributePattern = strTranslation+strDot+strTranslatePatten;
			
			String translatePatten = ReadProp.readString(dcName, dcFileName, dcAttributePattern, null);
			
			logger.debug(className, function, "translatePatten[{}]", translatePatten);
			
			if ( null != translatePatten ) {
				Translation.setTranslatePatten(translatePatten);
			} else {
				logger.warn(className, function, "translatePatten[{}] in dcAttributePattern[{}] is null, using default translatePatten[{}]"
						, new Object[]{translatePatten, dcAttributePattern, Translation.getTranslatePatten()});
			}
		}
		{
			String dcAttributeFlag = strTranslation+strDot+strTranslateFlag;
			
			String translateFlag = ReadProp.readString(dcName, dcFileName, dcAttributeFlag, null);
			
			logger.debug(className, function, "translateFlag[{}]", translateFlag);
			
			if ( null != translateFlag ) {
				Translation.setTranslateFlag(translateFlag);
			} else {
				logger.warn(className, function, "translateFlag[{}] in dcAttributeFlag[{}] is null, using default translateFlag[{}]"
						, new Object[]{translateFlag, dcAttributeFlag, Translation.getTranslateFlag()});
			}
		}
		
		TranslationMgr.getInstance().setTranslationEngine(new TranslationEngine() {
			@Override
			public String getMessage(String message) {
				
				return Translation.getDBMessage(message);
			}
		});
		
		logger.end(className, function);
	}
}
