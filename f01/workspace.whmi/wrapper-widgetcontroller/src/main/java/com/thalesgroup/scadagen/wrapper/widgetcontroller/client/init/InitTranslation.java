package com.thalesgroup.scadagen.wrapper.widgetcontroller.client.init;

import java.util.Map;

import com.thalesgroup.scadagen.whmi.translation.translationmgr.client.TranslationEngine;
import com.thalesgroup.scadagen.whmi.translation.translationmgr.client.TranslationMgr;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.common.InitReady_i;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.common.Init_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.util.Translation;

public class InitTranslation implements Init_i {
	
	private final UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());

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
		logger.begin(function);
		
		if ( null != translatePatten ) Translation.setTranslatePattern(translatePatten);

		logger.end(function);
	}
	
	public final static String strTranslateFlag = "Translation_TranslateFlag";
	public void setTranslationFlag(String translateFlag) {
		final String function = "initTranslation";
		logger.begin(function);
		
		if ( null != translateFlag ) Translation.setTranslateFlag(translateFlag);
	
		logger.end(function);
	}
	
	public void initTranslationEngine() {
		final String function = "initTranslationEngine";
		logger.begin(function);

		TranslationMgr.getInstance().setTranslationEngine(new TranslationEngine() {
			@Override
			public String getMessage(String message) {
				
				return Translation.getDBMessage(message);
			}
			
			@Override
			public String getMessage(String msgWithPlaceHolder, Object[] msgParam) {
				String result = Translation.getDBMessage(msgWithPlaceHolder); 
				if (null != msgParam && msgParam.length > 0)
				{
					String [] splits = result.split("{}");
					final StringBuffer buffer = new StringBuffer();
					for ( int i = 0 ; i < splits.length ; ++i) {
						buffer.append(splits[i]);
						if ( i < splits.length - 1 ) {
							if ( null != msgParam[i] ) {
								buffer.append(msgParam[i]);
							} else {
								buffer.append("{}");
							}
						}
					}
					result = buffer.toString();
				}
				return result;
			}
		});
		
		logger.end(function);
	}

}
