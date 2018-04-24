package com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.realize;

import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEventHandler;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionHandler;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.UIActionEventType;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIEventActionBus;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIEventActionProcessorMgr;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIGenericMgr;

public class UILayoutRealize extends UIRealize {
	
	private final String className = this.getClass().getSimpleName();
	private final UILogger logger = UILoggerFactory.getInstance().getLogger(this.getClass().getName());

	@Override
	public void init() {
		super.init();
		
		final String function = "init";
		logger.begin(className, function);
		
		logPrefix = "element["+element+"] ";
		
		String strEventBusName = getStringParameter(ParameterName.SimpleEventBus.toString());
		if ( null != strEventBusName ) eventBus = UIEventActionBus.getInstance().getEventBus(strEventBusName);
		logger.info(className, function, logPrefix+"strEventBusName[{}]", strEventBusName);
		
		UIGenericMgr uiGenericMgr = UIGenericMgr.getInstance();
		uiGeneric = uiGenericMgr.getUIGeneric("UILayoutGeneric");
		
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
			logger.warn(className, function, logPrefix+"uiEventActionProcessor_i IS NULL");
		}
		
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
			logger.warn(className, function, logPrefix+"uiNameCard IS NULL");
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
			logger.warn(className, function, logPrefix+"eventBus IS NULL");
		}
			
		if ( null != uiEventActionProcessor_i ) uiEventActionProcessor_i.executeActionSetInit();
		
		logger.end(className, function);
	}
	
}

