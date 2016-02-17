package com.thalesgroup.scadagen.whmi.config.configenv.client;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.thalesgroup.scadagen.whmi.config.config.shared.Configs;

public class ConfigMgr implements AsyncCallback<Configs> {
	
	private static Logger logger = Logger.getLogger(ConfigMgr.class.getName());
	
//	private ConfigMgr () {}
//	private static ConfigMgr instance = null;
//	public static ConfigMgr getInstance() {
//		if ( instance == null ) {
//			instance = GWT.create(ConfigMgr.class);
//		}
//		return instance;
//	}
	
	private String xml = null;
	private String tag = null;
	
	public void setConfigMgrParam(String xml, String tag) {
		this.xml = xml;
		this.tag = tag;
	}

	private LinkedList<ConfigMgrEvent> configMgrEvents = new LinkedList<ConfigMgrEvent>();
	public void setConfigMgrEvent ( ConfigMgrEvent configMgrEvent ) {
		
		logger.log(Level.FINE, "setTaskMgrEvent Begin");
		
		this.configMgrEvents.add(configMgrEvent);
		
		logger.log(Level.FINE, "setTaskMgrEvent End");
	}
	
	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final ConfigServiceAsync configMgrService = GWT.create(ConfigService.class);
	
	public void getConfigs(String module, String xml, String tag, ConfigMgrEvent configMgrEvent) {
		
		logger.log(Level.FINE, "getConfigs Begin");
		
		this.xml = xml;
		this.tag = tag;
		
		logger.log(Level.FINE, "getConfigs this.xml["+this.xml+"] this.tag["+this.tag+"]");
		
		this.configMgrEvents.add(configMgrEvent);
		
		configMgrService.configServer(module, xml, tag, this);
		
		logger.log(Level.FINE, "getConfigs End");
	}

	public void onFailure(Throwable caught) {
		// Show the RPC error message to the user
		
		logger.log(Level.FINE, "onFailure Begin");
				
		for (Iterator<ConfigMgrEvent> iterator = configMgrEvents.iterator(); iterator.hasNext();) {
			ConfigMgrEvent configMgrEvent = iterator.next();
			configMgrEvent.failed(xml);
		    iterator.remove();
		}

		logger.log(Level.FINE, "onFailure End");
	}// onFailure

	public void onSuccess(Configs cfgsCur) {
		// Success on get Menu from server
		
		logger.log(Level.FINE, "onSuccess Begin");

		if ( null != cfgsCur ) {

			logger.log(Level.FINE, "onSuccess cfgsCur.size()["+cfgsCur.getObjectSize()+"]");
					
			logger.log(Level.FINE, "onSuccess calling the callback: configMgrEvent.ready()");
			
			for (Iterator<ConfigMgrEvent> iterator = configMgrEvents.iterator(); iterator.hasNext();) {
				ConfigMgrEvent configMgrEvent = iterator.next();
				configMgrEvent.ready(cfgsCur);
			    iterator.remove();
			}

		} else {
			
			logger.log(Level.FINE, "onSuccess configMgrEvent is null");
			
		}
		
		logger.log(Level.FINE, "onSuccess End");

	}// onSuccess

}
