package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget;

import com.google.gwt.event.shared.SimpleEventBus;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEventHandler;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionBus;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionHandler;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIView_i.ParameterName;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;

import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UILayoutGeneric;

public class UIWidgetAction extends UIWidget_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIWidgetAction.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);

	// External
	private SimpleEventBus eventBus		= null;

	private UILayoutGeneric uiLayoutGeneric					= null;
	
	void onUIEvent(UIEvent uiEvent ) {
	}
	
	void onActionReceived(UIEventAction uiEventAction) {
	}

	@Override
	public void init() {
		final String function = "init";
		
		logger.begin(className, function);

		if ( containsParameterKey(ParameterName.SimpleEventBus.toString()) ) {
			Object o = parameters.get(ParameterName.SimpleEventBus.toString());
			if ( null != o ) {
				String eventBusName = (String) o;
				this.eventBus = UIEventActionBus.getInstance().getEventBus(eventBusName);
			}
		}

		uiLayoutGeneric = new UILayoutGeneric();
		
		uiLayoutGeneric.setUINameCard(this.uiNameCard);
		uiLayoutGeneric.setXMLFile(xmlFile);
		uiLayoutGeneric.init();
		rootPanel = uiLayoutGeneric.getMainPanel();
		
		handlerRegistrations.add(
			this.uiNameCard.getUiEventBus().addHandler(UIEvent.TYPE, new UIEventHandler() {
				@Override
				public void onEvenBusUIChanged(UIEvent uiEvent) {
					if ( uiEvent.getSource() != this ) {
						onUIEvent(uiEvent);
					}
				}
			})
		);
			
		handlerRegistrations.add(
			this.eventBus.addHandler(UIEventAction.TYPE, new UIEventActionHandler() {
				@Override
				public void onAction(UIEventAction uiEventAction) {
					if ( uiEventAction.getSource() != this ) {
						onActionReceived(uiEventAction);
					}
				}
			})
		);
		
		logger.end(className, function);
	}
}
