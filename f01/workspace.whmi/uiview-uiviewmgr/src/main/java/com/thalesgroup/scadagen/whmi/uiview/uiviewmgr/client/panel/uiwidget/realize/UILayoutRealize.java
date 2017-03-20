package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.realize;

import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEventHandler;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionBus;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionHandler;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.UIActionEventType;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIEventActionProcessorMgr;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UILayoutGeneric;

public class UILayoutRealize extends UIRealize {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UILayoutRealize.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);

	protected UILayoutGeneric uiLayoutGeneric	= null;

	@Override
	public void init() {
		final String function = "init";
		logger.begin(className, function);
		
		logPrefix = "element["+element+"] ";
		
		String strEventBusName = getStringParameter(ParameterName.SimpleEventBus.toString());
		if ( null != strEventBusName ) eventBus = UIEventActionBus.getInstance().getEventBus(strEventBusName);
		logger.info(className, function, logPrefix+"strEventBusName[{}]", strEventBusName);
		
		uiLayoutGeneric = new UILayoutGeneric();
		
		uiLayoutGeneric.setUINameCard(uiNameCard);
		uiLayoutGeneric.setDictionaryFolder(dictionaryFolder);
		uiLayoutGeneric.setViewXMLFile(viewXMLFile);
		uiLayoutGeneric.setOptsXMLFile(optsXMLFile);
		uiLayoutGeneric.init();
		
		UIEventActionProcessorMgr uiEventActionProcessorMgr = UIEventActionProcessorMgr.getInstance();
		uiEventActionProcessor_i = uiEventActionProcessorMgr.getUIEventActionProcessorMgr("UIEventActionProcessor");

		if ( null != uiEventActionProcessor_i ) {
			uiEventActionProcessor_i.setUINameCard(uiNameCard);
			uiEventActionProcessor_i.setPrefix(className);
			uiEventActionProcessor_i.setElement(element);
			uiEventActionProcessor_i.setDictionariesCacheName("UIWidgetGeneric");
			uiEventActionProcessor_i.setEventBus(eventBus);
			uiEventActionProcessor_i.setOptsXMLFile(optsXMLFile);
			uiEventActionProcessor_i.setUIGeneric(uiWidgetGeneric);
			uiEventActionProcessor_i.setActionSetTagName(UIActionEventType.actionset.toString());
			uiEventActionProcessor_i.setActionTagName(UIActionEventType.action.toString());
			uiEventActionProcessor_i.init();
		} else {
			logger.warn(className, function, logPrefix+"uiEventActionProcessor_i IS NULL");
		}
		
		rootPanel = uiLayoutGeneric.getMainPanel();
		
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
	
	@Override
	public void envUp(String env) {
	}
	
	@Override
	public void envDown(String env) {
	}
	
	@Override
	public void terminate() {
	}
	
}
