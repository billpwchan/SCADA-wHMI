package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common;

import com.google.gwt.event.shared.SimpleEventBus;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UILayoutGeneric;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIWidgetGeneric;

public abstract class UIEventActionExecute_i {
	
	protected String logPrefix = "";
	protected String instance = "";
	protected SimpleEventBus simpleEventBus = null;
	protected UIWidgetGeneric uiWidgetGeneric = null;
	protected UILayoutGeneric uiLayoutGeneric = null;
	
	protected String [] supportedActions = null;
	
	public void setLogPrefix(String logPrefix) { this.logPrefix = "-> "+logPrefix+" "; }
	public void setInstance(String instance) { this.instance = instance; }
	public void setSimpleEventBus(SimpleEventBus simpleEventBus) { this.simpleEventBus = simpleEventBus; }
	public void setUIWidgetGeneric(UIWidgetGeneric uiWidgetGeneric) { this.uiWidgetGeneric = uiWidgetGeneric; }
	public void setUILayoutGeneric(UILayoutGeneric uiLayoutGeneric) { this.uiLayoutGeneric = uiLayoutGeneric; }
	
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
	public abstract void executeAction(final UIEventAction uiEventAction);
}
