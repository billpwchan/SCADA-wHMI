package com.thalesgroup.scadagen.whmi.uiroot.uiroot.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
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

	String key = "scadagen.launcher.module";
	
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
				if ( null != webConfigMgrEvent ) {
					webConfigMgrEvent.updated(value);
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
	
	public void getWebConfig(final String mode, final String module, final String folder, final String xml, final String tag, final String key, final WebConfigMgrEvent webConfigMgrEvent) {
		final String function = "getWebConfig";
		
		logger.begin(className, function);
		
		logger.info(className, function, "getWebConfig module[{}]", module);
		logger.info(className, function, "getWebConfig xml[{}]", xml);
		logger.info(className, function, "getWebConfig tag[{}]", tag);
		logger.info(className, function, "getWebConfig key[{}]", key);
		
		DictionaryMgr dictionaryMgr = new DictionaryMgr();
		
		dictionaryMgr.getDictionary(mode, module, folder, xml, tag, new DictionaryMgrEvent() {
			
			@Override
			public void dictionaryMgrEventReady(Dictionary dictionary) {
				if ( null != webConfigMgrEvent ) {
					if ( null != dictionary ) {
						for ( Object subobject : dictionary.getValueKeys() ) {
							if ( subobject instanceof Dictionary ) {
								Dictionary subdictionary = (Dictionary) subobject;
								for ( Object dKey : subdictionary.getValueKeys() ) {
									if ( dKey instanceof String ) {
										String sKey = (String) dKey;
										if ( 0 == sKey.compareTo(key) ) {
											String value = (String) subdictionary.getValue(sKey);
											if ( null != webConfigMgrEvent ) {
												webConfigMgrEvent.updated(value);
											}
										}
									}
								}
							}
						}
					} else {
						logger.warn(className, function, "dictionary IS NULL");
					}
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
	
	public void dictionaryMgrEventReady(Dictionary dictionary) {
		final String function = "dictionaryMgrEventReady";
		
		if ( null != dictionary ) {
			
			for ( Object dKey : dictionary.getAttributeKeys() ) {
				
				Window.alert("getWebConfig dictionary getAttributeKeys["+dKey+"]");
				
				Window.alert("getWebConfig dictionary getAttribute["+dictionary.getAttribute(dKey)+"]");
				
			}
			
			for ( Object dKey : dictionary.getValueKeys() ) {
				
				Window.alert("getWebConfig dictionary getValueKeys["+dKey+"]");
				
				Window.alert("getWebConfig dictionary getValue["+dictionary.getValue(dKey)+"]");
				
			}
			
			for ( Object subobject : dictionary.getValueKeys() ) {
				
				Dictionary subdictionary = (Dictionary) subobject;
				
				Window.alert("getWebConfig dictionaryMgrEventReady key instanceof Dictionary");
				
				for ( Object dKey : subdictionary.getAttributeKeys() ) {
					
					Window.alert("getWebConfig subdictionary getAttributeKeys["+dKey+"]");
					
					Window.alert("getWebConfig subdictionary getAttribute["+subdictionary.getAttribute(dKey)+"]");
					
				}
				
				for ( Object dKey : subdictionary.getValueKeys() ) {

					Window.alert("getWebConfig subdictionary getValueKeys["+dKey+"]");
					
					Window.alert("getWebConfig subdictionary getValue["+subdictionary.getValue(dKey)+"]");
				}
			}
		}

	}
}
