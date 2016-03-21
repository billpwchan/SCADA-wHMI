package com.thalesgroup.scadagen.whmi.config.configenv.client;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.thalesgroup.scadagen.whmi.config.config.shared.Dictionary;

public class DictionaryMgr implements AsyncCallback<Dictionary> {
	
	private static Logger logger = Logger.getLogger(DictionaryMgr.class.getName());
	
	private String xml = null;
	private String tag = null;
	
	public void setDactionaryMgrParam(String xml, String tag) {
		this.xml = xml;
		this.tag = tag;
	}

	private LinkedList<DictionaryMgrEvent> dictionaryMgrEvents = new LinkedList<DictionaryMgrEvent>();
	public void setDictionaryMgrEvent ( DictionaryMgrEvent dictionaryMgrEvent ) {
		
		logger.log(Level.FINE, "setTaskMgrEvent Begin");
		
		this.dictionaryMgrEvents.add(dictionaryMgrEvent);
		
		logger.log(Level.FINE, "setTaskMgrEvent End");
	}
	
	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final DictionaryServiceAsync dictionaryMgrService = GWT.create(DictionaryService.class);
	
	public void getDictionary(String module, String xml, String tag, DictionaryMgrEvent dictionaryMgrEvent) {
		
		logger.log(Level.FINE, "getDictionarys Begin");
		
		this.xml = xml;
		this.tag = tag;
		
		logger.log(Level.FINE, "getDictionarys this.xml["+this.xml+"] this.tag["+this.tag+"]");
		
		this.dictionaryMgrEvents.add(dictionaryMgrEvent);
		
		dictionaryMgrService.dictionaryServer(module, xml, tag, this);
		
		logger.log(Level.FINE, "getDictionarys End");
	}

	public void onFailure(Throwable caught) {
		// Show the RPC error message to the user
		
		logger.log(Level.FINE, "onFailure Begin");
				
		for (Iterator<DictionaryMgrEvent> iterator = dictionaryMgrEvents.iterator(); iterator.hasNext();) {
			DictionaryMgrEvent dictionaryMgrEvent = iterator.next();
			dictionaryMgrEvent.failed(xml);
		    iterator.remove();
		}

		logger.log(Level.FINE, "onFailure End");
	}// onFailure

	public void onSuccess(Dictionary dictionaryCur) {
		// Success on get Menu from server
		
		logger.log(Level.FINE, "onSuccess Begin");

		if ( null != dictionaryCur ) {

//			logger.log(Level.FINE, "onSuccess cfgsCur.size()["+dictionaryCur.getObjectSize()+"]");
					
			logger.log(Level.FINE, "onSuccess calling the callback: configMgrEvent.ready()");
			
			for (Iterator<DictionaryMgrEvent> iterator = dictionaryMgrEvents.iterator(); iterator.hasNext();) {
				DictionaryMgrEvent dictionaryMgrEvent = iterator.next();
				dictionaryMgrEvent.ready(dictionaryCur);
			    iterator.remove();
			}

		} else {
			
			logger.log(Level.FINE, "onSuccess dictionaryMgrEvent is null");
			
		}
		
		logger.log(Level.FINE, "onSuccess End");

	}// onSuccess

}
