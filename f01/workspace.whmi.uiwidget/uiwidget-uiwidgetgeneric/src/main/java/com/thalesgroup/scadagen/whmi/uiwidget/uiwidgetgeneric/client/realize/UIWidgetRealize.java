package com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.realize;

import com.google.gwt.event.dom.client.ClickEvent;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEventHandler;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIEventActionBus;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIEventActionProcessorMgr;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIGenericMgr;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.UIActionEventType;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionHandler;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.event.UIWidgetEventOnClickHandler;

public class UIWidgetRealize extends UIRealize {

	private final String className = this.getClass().getSimpleName();
	private final UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
	
	private String logPrefix = "";

	@Override
	public void init() {
		super.init();
		
		final String function = "init";
		logger.begin(function);
		
		logPrefix = "element["+element+"] ";
		
		String strEventBusName = getStringParameter(ParameterName.SimpleEventBus.toString());
		if ( null != strEventBusName ) eventBus = UIEventActionBus.getInstance().getEventBus(strEventBusName);
		logger.info(function, logPrefix+"strEventBusName[{}]", strEventBusName);
		
		UIGenericMgr uiGenericMgr = UIGenericMgr.getInstance();
		uiGeneric = uiGenericMgr.getUIGeneric("UIWidgetGeneric");
		
		uiGeneric.setUINameCard(uiNameCard);
		uiGeneric.setDictionaryFolder(dictionaryFolder);
		uiGeneric.setViewXMLFile(viewXMLFile);
		uiGeneric.setOptsXMLFile(optsXMLFile);
		uiGeneric.init();
		
		UIEventActionProcessorMgr uiEventActionProcessorMgr = UIEventActionProcessorMgr.getInstance();
		uiEventActionProcessor_i = uiEventActionProcessorMgr.getUIEventActionProcessor("UIEventActionProcessor");

		if ( null != uiEventActionProcessor_i ) {
			uiEventActionProcessor_i.setUINameCard(uiNameCard);
			uiEventActionProcessor_i.setPrefix(className);
			uiEventActionProcessor_i.setElement(element);
			uiEventActionProcessor_i.setDictionariesCacheName("UIWidgetGeneric");
			uiEventActionProcessor_i.setEventBus(eventBus);
			uiEventActionProcessor_i.setOptsXMLFile(optsXMLFile);
			uiEventActionProcessor_i.setUIGeneric(uiGeneric);
			uiEventActionProcessor_i.setActionSetTagName(UIActionEventType.actionset.toString());
			uiEventActionProcessor_i.setActionTagName(UIActionEventType.action.toString());
			uiEventActionProcessor_i.init();
		} else {
			logger.warn(function, logPrefix+"uiEventActionProcessor_i IS NULL");
		}
		
		uiGeneric.setUIWidgetEvent(new UIWidgetEventOnClickHandler() {
			@Override
			public void onClickHandler(ClickEvent event) {
				if ( null != uiWidgetCtrl_i ) uiWidgetCtrl_i.onClick(event);
			}
		});
		
		rootPanel = uiGeneric.getMainPanel();

		if ( null != uiNameCard ) {
			handlerRegistrations.add(
				uiNameCard.getUiEventBus().addHandler(UIEvent.TYPE, new UIEventHandler() {
					@Override
					public void onEvenBusUIChanged(UIEvent uiEvent) {
						if ( uiEvent.getSource() != this ) {
							if ( null != uiWidgetCtrl_i ) uiWidgetCtrl_i.onUIEvent(uiEvent);
						}
					}
				})
			);
		} else {
			logger.warn(function, logPrefix+"uiNameCard IS NULL");
		}

		if ( null != eventBus ) {
			handlerRegistrations.add(
				eventBus.addHandler(UIEventAction.TYPE, new UIEventActionHandler() {
					@Override
					public void onAction(UIEventAction uiEventAction) {
						if ( uiEventAction.getSource() != this ) {
							if ( ! fromUILayoutSummaryAction(uiEventAction) ) {
								if ( null != uiWidgetCtrl_i ) uiWidgetCtrl_i.onActionReceived(uiEventAction);
							}
						}
					}
				})
			);
		} else {
			logger.warn(function, logPrefix+"eventBus IS NULL");
		}
		
		if ( null != uiEventActionProcessor_i ) uiEventActionProcessor_i.executeActionSetInit();
				
		logger.end(function);
	}
	
}
