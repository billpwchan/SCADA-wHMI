package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.realize;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.SimpleEventBus;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEventHandler;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.UIActionEventTargetAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.UIActionEventType;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionBus;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIEventActionProcessorMgr;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionProcessor_i;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIView_i.ViewAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionHandler;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UILayoutSummaryAction_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetCtrl_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.event.UIWidgetEventOnClickHandler;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIWidgetGeneric;

public class UIWidgetRealize extends UIWidget_i implements UIRealize_i {

	private final String className = UIWidgetUtil.getClassSimpleName(UIWidgetRealize.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	protected SimpleEventBus eventBus 	= null;

	protected UIWidgetGeneric uiWidgetGeneric = null;
	
	protected UIEventActionProcessor_i uiEventActionProcessor_i = null;
	
	protected UILayoutSummaryAction_i uiLayoutSummaryAction_i = null;
	
	protected UIWidgetCtrl_i uiWidgetCtrl_i = null;
	
	private String logPrefix = "";

	@Override
	public void init() {
		final String function = "init";
		logger.begin(className, function);
		
		logPrefix = "element["+element+"] ";
		
		String strEventBusName = getStringParameter(ParameterName.SimpleEventBus.toString());
		if ( null != strEventBusName ) eventBus = UIEventActionBus.getInstance().getEventBus(strEventBusName);
		logger.info(className, function, logPrefix+"strEventBusName[{}]", strEventBusName);
		
		uiWidgetGeneric = new UIWidgetGeneric();
		uiWidgetGeneric.setUINameCard(uiNameCard);
		uiWidgetGeneric.setDictionaryFolder(dictionaryFolder);
		uiWidgetGeneric.setViewXMLFile(viewXMLFile);
		uiWidgetGeneric.setOptsXMLFile(optsXMLFile);
		uiWidgetGeneric.init();
		
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
		
		uiWidgetGeneric.setUIWidgetEvent(new UIWidgetEventOnClickHandler() {
			@Override
			public void onClickHandler(ClickEvent event) {
				if ( null != uiWidgetCtrl_i ) uiWidgetCtrl_i.onClick(event);
			}
		});
		
		rootPanel = uiWidgetGeneric.getMainPanel();

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
	
	/**
	 *  ActionSet: from_uilayoutsummary_init, from_uilayoutsummary_envup, from_uilayoutsummary_envdown, from_uilayoutsummary_terminate
	 */
	protected boolean fromUILayoutSummaryAction(UIEventAction uiEventAction) {
		final String function = "fromUILayoutSummaryAction";
		logger.begin(className, function);
		
		boolean result = false;

		String oe	= (String) uiEventAction.getParameter(UIActionEventTargetAttribute.OperationElement.toString());
		String os1	= (String) uiEventAction.getParameter(ViewAttribute.OperationString1.toString());
		String os2	= (String) uiEventAction.getParameter(ViewAttribute.OperationString2.toString());
		
		logger.debug(className, function, logPrefix+"oe[{}] os1[{}] os2[{}]", new Object[]{oe, os1, os2});
		
		if ( null != oe ) {
			if ( oe.equals(element) ) {
				
				if ( null == uiEventActionProcessor_i ) logger.warn(className, function, logPrefix+"uiEventActionProcessor_i IS NULL");
				if ( null == uiLayoutSummaryAction_i ) logger.warn(className, function, logPrefix+"uiLayoutSummaryAction_i IS NULL");
				
				if ( os1.equals(ActionSetName.from_uilayoutsummary_init.toString()) ) {
					result = fromUILayoutSummaryInit(uiEventAction);
				} else if ( os1.equals(ActionSetName.from_uilayoutsummary_envup.toString()) ) {
					result = fromUILayoutSummaryEnvUp(uiEventAction);
				} else if ( os1.equals(ActionSetName.from_uilayoutsummary_envdown.toString()) ) {
					result = fromUILayoutSummaryEnvDown(uiEventAction);
				} else if ( os1.equals(ActionSetName.from_uilayoutsummary_terminate.toString()) ) {
					result = fromUILayoutSummaryTerminate(uiEventAction);
				}
			}
		}
		logger.end(className, function);
		return result;
	}
	
	@Override
	public boolean fromUILayoutSummaryInit(UIEventAction uiEventAction) {
		if ( null != uiEventActionProcessor_i ) uiEventActionProcessor_i.executeActionSet(ActionSetName.from_uilayoutsummary_init.toString());
		if ( null != uiLayoutSummaryAction_i ) uiLayoutSummaryAction_i.init();
		return true;
	}
	
	@Override
	public boolean fromUILayoutSummaryEnvUp(UIEventAction uiEventAction) {
		String os2	= (String) uiEventAction.getParameter(ViewAttribute.OperationString2.toString());
		if ( null != uiEventActionProcessor_i ) uiEventActionProcessor_i.executeActionSet(ActionSetName.from_uilayoutsummary_envup.toString());
		if ( null != uiLayoutSummaryAction_i ) uiLayoutSummaryAction_i.envUp(os2);
		return true;
	}
	
	@Override
	public boolean fromUILayoutSummaryEnvDown(UIEventAction uiEventAction) {
		String os2	= (String) uiEventAction.getParameter(ViewAttribute.OperationString2.toString());
		if ( null != uiEventActionProcessor_i ) uiEventActionProcessor_i.executeActionSet(ActionSetName.from_uilayoutsummary_envdown.toString());
		if ( null != uiLayoutSummaryAction_i ) uiLayoutSummaryAction_i.envDown(os2);
		return true;
	}
	
	@Override
	public boolean fromUILayoutSummaryTerminate(UIEventAction uiEventAction) {
		if ( null != uiEventActionProcessor_i ) uiEventActionProcessor_i.executeActionSet(ActionSetName.from_uilayoutsummary_terminate.toString());
		if ( null != uiLayoutSummaryAction_i ) uiLayoutSummaryAction_i.terminate();
		return true;
	}

}
