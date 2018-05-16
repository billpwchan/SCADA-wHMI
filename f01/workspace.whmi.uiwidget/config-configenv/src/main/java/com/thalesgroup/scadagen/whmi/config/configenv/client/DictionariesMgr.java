package com.thalesgroup.scadagen.whmi.config.configenv.client;

import java.util.Iterator;
import java.util.LinkedList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.thalesgroup.scadagen.whmi.config.config.shared.Dictionary_i;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;

public class DictionariesMgr implements AsyncCallback<Dictionary_i> {

	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
	
	private String folder		= null;
//	private String extension	= null;
//	private String tag			= null;
	
	public void setDactionaryMgrParam(String folder, String extension, String tag) {
		this.folder		= folder;
//		this.extension	= extension;
//		this.tag		= tag;
	}

	private LinkedList<DictionariesMgrEvent> dictionariesMgrEvents = new LinkedList<DictionariesMgrEvent>();
	public void setDictionariesMgrEvent ( DictionariesMgrEvent dictionariesMgrEvent ) {
		final String function = "setDictionariesMgrEvent";
		
		logger.begin(function);
		
		this.dictionariesMgrEvents.add(dictionariesMgrEvent);
		
		logger.end(function);
	}
	
	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final DictionariesServiceAsync dictionariesService = GWT.create(DictionariesService.class);
	
	public void getDictionaries(String mode, String module, String folder, String extension, String tag, DictionariesMgrEvent dictionariesMgrEvent) {
		final String function = "getDictionaries";
		
		logger.begin(function);
		
		this.folder		= folder;
//		this.extension	= extension;
//		this.tag		= tag;
		
		logger.info(function, "folder[{}] extension[{}] tag[{}]", new Object[]{folder, extension, tag});
		
		this.dictionariesMgrEvents.add(dictionariesMgrEvent);

		dictionariesService.dictionariesServer(mode, module, folder, extension, tag, this);
		
		logger.end(function);
	}

	public void onFailure(Throwable caught) {
		final String function = "onFailure";
		// Show the RPC error message to the user
		
		logger.begin(function);
				
		for (Iterator<DictionariesMgrEvent> iterator = dictionariesMgrEvents.iterator(); iterator.hasNext();) {
			DictionariesMgrEvent dictionaryMgrEvent = iterator.next();
			dictionaryMgrEvent.dictionariesMgrEventFailed(folder);
		    iterator.remove();
		}

		logger.end(function);
	}// onFailure

	public void onSuccess(Dictionary_i dictionaryCur) {
		final String function = "onSuccess";
		// Success on get Menu from server
		
		logger.begin(function);

		if ( null != dictionaryCur ) {
	
			logger.debug(function, "calling the callback: dictionariesMgrEvents.DictionariesMgrEventReady()");
			for (Iterator<DictionariesMgrEvent> iterator = dictionariesMgrEvents.iterator(); iterator.hasNext();) {
				DictionariesMgrEvent dictionariesMgrEvent = iterator.next();
				dictionariesMgrEvent.dictionariesMgrEventReady(dictionaryCur);
			    iterator.remove();
			}

		} else {
			
			logger.warn(function, "dictionaryCur is null");
			
		}
		
		logger.end(function);

	}// onSuccess

}
