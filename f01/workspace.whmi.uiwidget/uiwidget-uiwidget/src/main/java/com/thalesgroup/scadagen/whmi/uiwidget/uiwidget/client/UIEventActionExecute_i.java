package com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client;

import java.util.HashMap;

import com.google.gwt.event.shared.SimpleEventBus;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionProcessorCore_i;

public abstract class UIEventActionExecute_i {
	
	protected String logPrefix = "";
	protected String instance = "";
	protected UINameCard uiNameCard = null;
	protected SimpleEventBus simpleEventBus = null;
	protected UIGeneric uiGeneric = null;
	protected UIEventActionProcessorCore_i uiEventActionProcessorCore_i = null;
	
	protected String [] supportedActions = null;
	
	public void setUIEventActionProcessor(UIEventActionProcessorCore_i uiEventActionProcessorCore_i) { this.uiEventActionProcessorCore_i = uiEventActionProcessorCore_i; }
	
	public void setLogPrefix(String logPrefix) { this.logPrefix = "-> "+logPrefix+" "; }
	public void setInstance(String instance) { this.instance = instance; }
	public void setSimpleEventBus(SimpleEventBus simpleEventBus) { this.simpleEventBus = simpleEventBus; }
	public void setUIGeneric(UIGeneric uiGeneric) { this.uiGeneric = uiGeneric; }
	public void setUINameCard(UINameCard uiNameCard) { if ( null != uiNameCard ) { this.uiNameCard = new UINameCard(uiNameCard); } }
	
	public boolean isSupportedAction(String operation) {
		boolean supported = false;
		if ( null != supportedActions ) {
			for ( String string : supportedActions ) {
				if ( string.equals(operation) ) {
					supported = true;
					break;
				}
			}
		}
		return supported;
	}
	public abstract boolean executeAction(final UIEventAction uiEventAction, HashMap<String, HashMap<String, Object>> override);
}
