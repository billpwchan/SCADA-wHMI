package com.thalesgroup.scadagen.wrapper.widgetcontroller.client.init;

import java.util.HashMap;
import java.util.Map;

import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionariesCache;
import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionariesCacheEvent;
import com.thalesgroup.scadagen.whmi.config.configenv.shared.DictionaryCacheInterface.ConfigurationType;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.common.InitReady_i;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.common.Init_i;

public class InitCacheXMLFile implements Init_i {
	
	private final UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
	
	private InitCacheXMLFile() {}
	private static InitCacheXMLFile instance = null;
	public static InitCacheXMLFile getInstance() { 
		if ( null == instance ) instance = new InitCacheXMLFile();
		return instance;
	}
	
	@Override
	public void init(Map<String, Object> params, InitReady_i initReady) {
		// TODO Auto-generated method stub
		
	}
	
	public void initCacheXMLFile(final String folder, final String extention) {
		initCacheXMLFile(folder, extention, null);
	}
	public void initCacheXMLFile(final String folder, final String extention, final InitReady_i initReady) {
		final String function = "initCacheXMLFile";
		logger.begin(function);
		logger.debug(function, "folder[{}] extention[{}]", folder, extention);
		
		final String header			= "header";
		final String option			= "option";
		final String action			= "action";
		final String actionset		= "actionset";
		final String [] tags = {header, option, action, actionset};
		String mode = ConfigurationType.XMLFile.toString();
		String module = null;
		DictionariesCache dictionariesCache = DictionariesCache.getInstance(folder);
		for(String tag : tags ) {
			dictionariesCache.add(folder, extention, tag);
		}
		dictionariesCache.init(mode, module, new DictionariesCacheEvent() {
			@Override
			public void dictionariesCacheEventReady(int received) {
				logger.debug(function, "dictionaryCacheEventReady received[{}]", received);
				if ( null != initReady ) {
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("received", received);
					initReady.ready(params);
				}
			}
		});
		
		logger.end(function);
	}
}
