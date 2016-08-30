package com.thalesgroup.scadagen.whmi.config.configenv.client;

import java.util.Iterator;
import java.util.LinkedList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.thalesgroup.scadagen.whmi.config.config.shared.Dictionary;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;

public class DictionaryMgr implements AsyncCallback<Dictionary> {
	
	private final String className = UIWidgetUtil.getClassSimpleName(DictionaryMgr.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private String xml = null;
	private String tag = null;
	
	public void setDactionaryMgrParam(String xml, String tag) {
		this.xml = xml;
		this.tag = tag;
	}

	private LinkedList<DictionaryMgrEvent> dictionaryMgrEvents = new LinkedList<DictionaryMgrEvent>();
	public void setDictionaryMgrEvent ( DictionaryMgrEvent dictionaryMgrEvent ) {
		
		logger.info(className, "setDictionaryMgrEvent", "Begin");
		
		this.dictionaryMgrEvents.add(dictionaryMgrEvent);
		
		logger.info(className, "setDictionaryMgrEvent", "End");
	}
	
	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final DictionaryServiceAsync dictionaryService = GWT.create(DictionaryService.class);
	
	public void getDictionary(String mode, String module, String folder, String xml, String tag, DictionaryMgrEvent dictionaryMgrEvent) {
		final String function = "getDictionary";
		
		logger.begin(className, function);
		
		this.xml = xml;
		this.tag = tag;
		
		logger.info(className, function, "this.xml[{}] this.tag[{}]", this.xml, this.tag);
		
		this.dictionaryMgrEvents.add(dictionaryMgrEvent);
		
		dictionaryService.dictionaryServer(mode, module, folder, xml, tag, this);
		
		logger.end(className, function);
	}

	public void onFailure(Throwable caught) {
		final String function = "onFailure";
		// Show the RPC error message to the user
		
		logger.begin(className, function);
				
		for (Iterator<DictionaryMgrEvent> iterator = dictionaryMgrEvents.iterator(); iterator.hasNext();) {
			DictionaryMgrEvent dictionaryMgrEvent = iterator.next();
			dictionaryMgrEvent.dictionaryMgrEventFailed(xml);
		    iterator.remove();
		}

		logger.end(className, function);
	}// onFailure

	public void onSuccess(Dictionary dictionaryCur) {
		final String function = "onSuccess";
		// Success on get Menu from server
		
		logger.begin(className, function);

		if ( null != dictionaryCur ) {
					
			logger.info(className, function, "calling the callback: dictionaryMgrEvent.DictionaryMgrEventReady()");
			
			for (Iterator<DictionaryMgrEvent> iterator = dictionaryMgrEvents.iterator(); iterator.hasNext();) {
				DictionaryMgrEvent dictionaryMgrEvent = iterator.next();
				dictionaryMgrEvent.dictionaryMgrEventReady(dictionaryCur);
			    iterator.remove();
			}
		} else {
			logger.info(className, function, "dictionaryMgrEvent is null");
		}
		
		logger.end(className, function);

	}// onSuccess

}
