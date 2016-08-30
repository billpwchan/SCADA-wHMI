package com.thalesgroup.scadagen.whmi.config.configenv.client;

import java.util.Iterator;
import java.util.LinkedList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.thalesgroup.scadagen.whmi.config.config.shared.Dictionary;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;

public class DictionariesMgr implements AsyncCallback<Dictionary> {
	
	private final String className = UIWidgetUtil.getClassSimpleName(DictionariesMgr.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private String folder		= null;
	private String extension	= null;
	private String tag			= null;
	
	public void setDactionaryMgrParam(String folder, String extension, String tag) {
		this.folder		= folder;
		this.extension	= extension;
		this.tag		= tag;
	}

	private LinkedList<DictionariesMgrEvent> dictionariesMgrEvents = new LinkedList<DictionariesMgrEvent>();
	public void setDictionariesMgrEvent ( DictionariesMgrEvent dictionariesMgrEvent ) {
		final String function = "setDictionariesMgrEvent";
		
		logger.begin(className, function);
		
		this.dictionariesMgrEvents.add(dictionariesMgrEvent);
		
		logger.end(className, function);
	}
	
	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final DictionariesServiceAsync dictionariesService = GWT.create(DictionariesService.class);
	
	public void getDictionaries(String mode, String module, String folder, String extension, String tag, DictionariesMgrEvent dictionariesMgrEvent) {
		final String function = "getDictionaries";
		
		logger.begin(className, function);
		
		this.folder		= folder;
		this.extension	= extension;
		this.tag		= tag;
		
		logger.info(className, function, "folder[{}] extension[{}] tag[{}]", new Object[]{folder, extension, tag});
		
		this.dictionariesMgrEvents.add(dictionariesMgrEvent);

		dictionariesService.dictionariesServer(mode, module, folder, extension, tag, this);
		
		logger.end(className, function);
	}

	public void onFailure(Throwable caught) {
		final String function = "onFailure";
		// Show the RPC error message to the user
		
		logger.begin(className, function);
				
		for (Iterator<DictionariesMgrEvent> iterator = dictionariesMgrEvents.iterator(); iterator.hasNext();) {
			DictionariesMgrEvent dictionaryMgrEvent = iterator.next();
			dictionaryMgrEvent.dictionariesMgrEventFailed(folder);
		    iterator.remove();
		}

		logger.end(className, function);
	}// onFailure

	public void onSuccess(Dictionary dictionaryCur) {
		final String function = "onSuccess";
		// Success on get Menu from server
		
		logger.begin(className, function);

		if ( null != dictionaryCur ) {
	
			logger.info(className, function, "calling the callback: dictionariesMgrEvents.DictionariesMgrEventReady()");
			for (Iterator<DictionariesMgrEvent> iterator = dictionariesMgrEvents.iterator(); iterator.hasNext();) {
				DictionariesMgrEvent dictionariesMgrEvent = iterator.next();
				dictionariesMgrEvent.dictionariesMgrEventReady(dictionaryCur);
			    iterator.remove();
			}

		} else {
			
			logger.warn(className, function, "dictionaryCur is null");
			
		}
		
		logger.end(className, function);

	}// onSuccess

}
