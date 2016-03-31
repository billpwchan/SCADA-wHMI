package com.thalesgroup.scadagen.whmi.config.configenv.client;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.thalesgroup.scadagen.whmi.config.config.shared.Dictionary;

public class DictionariesMgr implements AsyncCallback<Dictionary> {
	
	private static Logger logger = Logger.getLogger(DictionariesMgr.class.getName());
	
	private String path			= null;
	private String extension	= null;
	
	public void setDactionaryMgrParam(String path, String extension) {
		this.path		= path;
		this.extension	= extension;
	}

	private LinkedList<DictionariesMgrEvent> dictionariesMgrEvents = new LinkedList<DictionariesMgrEvent>();
	public void setDictionariesMgrEvent ( DictionariesMgrEvent dictionariesMgrEvent ) {
		
		logger.log(Level.FINE, "setDictionariesMgrEvent Begin");
		
		this.dictionariesMgrEvents.add(dictionariesMgrEvent);
		
		logger.log(Level.FINE, "setDictionariesMgrEvent End");
	}
	
	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final DictionariesServiceAsync dictionariesService = GWT.create(DictionariesService.class);
	
	public void getDictionaries(String module, String path, String extension, DictionariesMgrEvent dictionariesMgrEvent) {
		
		logger.log(Level.SEVERE, "getDictionaries Begin");
		
		this.path		= path;
		this.extension	= extension;
		
		logger.log(Level.SEVERE, "getDictionaries this.path["+this.path+"] this.extension["+this.extension+"]");
		
		this.dictionariesMgrEvents.add(dictionariesMgrEvent);
		
		dictionariesService.dictionariesServer(module, path, extension, this);
		
		logger.log(Level.SEVERE, "getDictionaries End");
	}

	public void onFailure(Throwable caught) {
		// Show the RPC error message to the user
		
		logger.log(Level.FINE, "onFailure Begin");
				
		for (Iterator<DictionariesMgrEvent> iterator = dictionariesMgrEvents.iterator(); iterator.hasNext();) {
			DictionariesMgrEvent dictionaryMgrEvent = iterator.next();
			dictionaryMgrEvent.dictionariesMgrEventFailed(path);
		    iterator.remove();
		}

		logger.log(Level.FINE, "onFailure End");
	}// onFailure

	public void onSuccess(Dictionary dictionaryCur) {
		// Success on get Menu from server
		
		logger.log(Level.FINE, "onSuccess Begin");

		if ( null != dictionaryCur ) {

//			logger.log(Level.FINE, "onSuccess cfgsCur.size()["+dictionaryCur.getObjectSize()+"]");
					
			logger.log(Level.FINE, "onSuccess calling the callback: dictionariesMgrEvents.DictionariesMgrEventReady()");
			
			for (Iterator<DictionariesMgrEvent> iterator = dictionariesMgrEvents.iterator(); iterator.hasNext();) {
				DictionariesMgrEvent dictionariesMgrEvent = iterator.next();
				dictionariesMgrEvent.dictionariesMgrEventReady(dictionaryCur);
			    iterator.remove();
			}

		} else {
			
			logger.log(Level.FINE, "onSuccess dictionaryCur is null");
			
		}
		
		logger.log(Level.FINE, "onSuccess End");

	}// onSuccess

}
