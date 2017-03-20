package com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.realize;

import com.google.gwt.event.shared.SimpleEventBus;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionProcessor_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIGeneric;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UILayoutSummaryAction_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIRealize_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIView_i.ViewAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetCtrl_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.UIActionEventTargetAttribute;

public class UIRealize extends UIWidget_i implements UIRealize_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIRealize.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	protected SimpleEventBus eventBus 	= null;

	protected UIGeneric uiGeneric = null;
	
	protected UIEventActionProcessor_i uiEventActionProcessor_i = null;
	
	protected UILayoutSummaryAction_i uiLayoutSummaryAction_i = null;
	
	protected UIWidgetCtrl_i uiWidgetCtrl_i = null;
	
	protected String logPrefix = "";
	
	@Override
	public void init() {
		final String function = "init";
		logger.begin(className, function);
		
		logPrefix = "element["+element+"] ";
		
		logger.end(className, function);
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