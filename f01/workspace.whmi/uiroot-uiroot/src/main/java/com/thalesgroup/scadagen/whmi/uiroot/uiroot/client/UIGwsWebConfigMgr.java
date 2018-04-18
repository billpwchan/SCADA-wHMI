package com.thalesgroup.scadagen.whmi.uiroot.uiroot.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.thalesgroup.scadagen.whmi.config.config.shared.Dictionary;
import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionaryMgr;
import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionaryMgrEvent;
import com.thalesgroup.scadagen.whmi.config.configenv.client.WebConfigMgrEvent;
import com.thalesgroup.scadagen.whmi.config.configenv.client.WebConfigService;
import com.thalesgroup.scadagen.whmi.config.configenv.client.WebConfigServiceAsync;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;

public class UIGwsWebConfigMgr {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIGwsWebConfigMgr.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);

	private static UIGwsWebConfigMgr instance = null;
	public static UIGwsWebConfigMgr getInstance () { 
		if ( null == instance ) {
			instance = new UIGwsWebConfigMgr();
		}
		return instance;
	}
	private UIGwsWebConfigMgr() {};
	
	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final WebConfigServiceAsync webConfigService = GWT.create(WebConfigService.class);
	
	public void getWebConfig(final String key, final WebConfigMgrEvent webConfigMgrEvent) {
		
		webConfigService.webConfigServer(key, new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String value) {
				final String function = "onSuccess";
				logger.begin(className, function);
				final Map<String, String> hashMap = new HashMap<String, String>();
				hashMap.put(key, value);
				if ( null != webConfigMgrEvent ) {
					webConfigMgrEvent.updated(hashMap);
				} else {
					logger.warn(className, function, "webConfigMgrEvent IS NULL");
				}
				logger.end(className, function);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				final String function = "onFailure";
				logger.begin(className, function);
				if ( null != webConfigMgrEvent ) {
					webConfigMgrEvent.failed();
				} else {
					logger.warn(className, function, "webConfigMgrEvent IS NULL");
				}
				logger.end(className, function);
			}
		});	
		
	}
	
	public void getWebConfig(final String mode, final String module, final String folder, final String xml, final String tag, final List<String> keys, final WebConfigMgrEvent webConfigMgrEvent) {
		final String function = "getWebConfig";
		
		logger.begin(className, function);
		
		logger.info(className, function, "getWebConfig module[{}]", module);
		logger.info(className, function, "getWebConfig xml[{}]", xml);
		logger.info(className, function, "getWebConfig tag[{}]", tag);
		
		for ( String key : keys ) {
			logger.debug(className, function, "getWebConfig key[{}]", key);
		}
		
		final DictionaryMgr dictionaryMgr = new DictionaryMgr();
		
		dictionaryMgr.getDictionary(mode, module, folder, xml, tag, new DictionaryMgrEvent() {
			
			@Override
			public void dictionaryMgrEventReady(Dictionary dictionary) {
				
				Map<String, String> entrySet = null;
				
				if ( null != webConfigMgrEvent ) {
					if ( null != dictionary ) {
					
						entrySet = new HashMap<String, String>();
						
						for ( String key : keys ) {
							
							for ( Object subobject : dictionary.getValueKeys() ) {
								if ( subobject instanceof Dictionary ) {
									Dictionary subdictionary = (Dictionary) subobject;
									for ( Object dKey : subdictionary.getValueKeys() ) {
										if ( dKey instanceof String ) {
											String sKey = (String) dKey;
											if ( 0 == sKey.compareTo(key) ) {
												String value = (String) subdictionary.getValue(sKey);
												entrySet.put(sKey, value);
											}
										}
									}
								}
							}
							
						}
						
					} else {
						logger.warn(className, function, "dictionary IS NULL");
					}
					
					webConfigMgrEvent.updated(entrySet);
					
				} else {
					logger.warn(className, function, "webConfigMgrEvent IS NULL");
				}
				logger.end(className, function);
			}
			
			@Override
			public void dictionaryMgrEventFailed(String xmlFile) {
				final String function = "dictionaryMgrEventFailed";
				
				logger.begin(className, function);
				if ( null != webConfigMgrEvent ) {
					
					logger.error(className, function, "dictionaryMgrEventFailed");
					
					webConfigMgrEvent.failed();
				} else {
					logger.warn(className, function, "webConfigMgrEvent IS NULL");
				}
				logger.end(className, function+"dictionaryMgrEventFailed");
			}
		});
		
		logger.end(className, function);
	}
	
}
