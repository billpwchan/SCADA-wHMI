package com.thalesgroup.scadagen.whmi.uiroot.uiroot.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.thalesgroup.scadagen.whmi.config.config.shared.Dictionary;
import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionaryMgr;
import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionaryMgrEvent;
import com.thalesgroup.scadagen.whmi.config.configenv.client.WebConfigMgrEvent;
import com.thalesgroup.scadagen.whmi.config.configenv.client.WebConfigService;
import com.thalesgroup.scadagen.whmi.config.configenv.client.WebConfigServiceAsync;

public class UIGwsWebConfigMgr {
	
	private static Logger logger = Logger.getLogger(UIGwsWebConfigMgr.class.getName());

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
				logger.log(Level.SEVERE, "getWebConfig onSuccess Begin");
				if ( null != webConfigMgrEvent ) {
					webConfigMgrEvent.updated(value);
				} else {
					logger.log(Level.SEVERE, "getWebConfig onSuccess webConfigMgrEvent IS NULL");
				}
				logger.log(Level.SEVERE, "getWebConfig onSuccess End");
			}
			
			@Override
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, "getWebConfig onFailure Begin");
				if ( null != webConfigMgrEvent ) {
					webConfigMgrEvent.failed();
				} else {
					logger.log(Level.SEVERE, "getWebConfig webConfigMgrEvent IS NULL");
				}
				logger.log(Level.SEVERE, "getWebConfig onFailure End");
			}
		});	
		
	}
	
	public void getWebConfig(final String module, final String folder, final String xml, final String tag, final String key, final WebConfigMgrEvent webConfigMgrEvent) {
		
		logger.log(Level.SEVERE, "getWebConfig Begin");
		
		logger.log(Level.SEVERE, "getWebConfig module["+module+"]");
		
		logger.log(Level.SEVERE, "getWebConfig xml["+xml+"]");
		
		logger.log(Level.SEVERE, "getWebConfig tag["+tag+"]");
		
		logger.log(Level.SEVERE, "getWebConfig key["+key+"]");
		
		DictionaryMgr dictionaryMgr = new DictionaryMgr();
		
		dictionaryMgr.getDictionary(module, folder, xml, tag, new DictionaryMgrEvent() {
			
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
						Window.alert("getWebConfig dictionary IS NULL");
					}
				} else {
					logger.log(Level.SEVERE, "getWebConfig dictionaryMgrEventReady webConfigMgrEvent IS NULL");
				}
				logger.log(Level.SEVERE, "getWebConfig dictionaryMgrEventReady End");
			}
			
			@Override
			public void dictionaryMgrEventFailed(String xmlFile) {
				logger.log(Level.SEVERE, "getWebConfig dictionaryMgrEventFailed Begin");
				if ( null != webConfigMgrEvent ) {
					
					Window.alert("getWebConfig dictionaryMgrEventFailed");
					
					webConfigMgrEvent.failed();
				} else {
					logger.log(Level.SEVERE, "getWebConfig dictionaryMgrEventFailed webConfigMgrEvent IS NULL");
				}
				logger.log(Level.SEVERE, "getWebConfig dictionaryMgrEventFailed End");
			}
		});
		
		logger.log(Level.SEVERE, "getWebConfig End");
	}
	
	public void dictionaryMgrEventReady(Dictionary dictionary) {
		
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
