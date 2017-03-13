package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common;

import java.util.HashMap;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.ActionAttribute;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionWidget_i.UIEventActionWidgetAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionExecute_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetGeneric_i.WidgetStatus;

public class UIEventActionWidget extends UIEventActionExecute_i {
	private final String className = UIWidgetUtil.getClassSimpleName(UIEventActionWidget.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	public UIEventActionWidget ( ) {
		supportedActions = new String[] {
				  UIEventActionWidgetAction.SetWidgetStatus.toString()
				, UIEventActionWidgetAction.SetWidgetValue.toString()
				};
	}
	
	@Override
	public boolean executeAction(UIEventAction uiEventAction, HashMap<String, HashMap<String, Object>> override) {
		final String function = logPrefix+" executeAction";
		logger.begin(className, function);
		
		boolean bContinue = true;
		
		if ( uiEventAction == null ) {
			logger.warn(className, function, "uiEventAction IS NULL");
			return bContinue;
		}
		
		String strWidgetAction	= (String) uiEventAction.getParameter(ActionAttribute.OperationString1.toString());
		String strWidget		= (String) uiEventAction.getParameter(ActionAttribute.OperationString2.toString());
		String widgetValue		= (String) uiEventAction.getParameter(ActionAttribute.OperationString3.toString());
		
		logger.info(className, function, logPrefix+"strWidgetAction[{}] strWidget[{}] widgetValue[{}]", new Object[]{strWidgetAction, strWidget, widgetValue});
		
		if ( uiGeneric == null ) {
			logger.warn(className, function, logPrefix+"uiWidgetGeneric IS NULL");
			return bContinue;
		}
		
		if ( strWidgetAction == null ) {
			logger.warn(className, function, logPrefix+"strWidgetAction IS NULL");
			return bContinue;
		}
		
		if ( strWidget == null ) {
			logger.warn(className, function, logPrefix+"strWidget IS NULL");
			return bContinue;
		}
		
		if ( widgetValue == null ) {
			logger.warn(className, function, logPrefix+"widgetValue IS NULL");
			return bContinue;
		}
		
		if ( strWidgetAction.equals(UIEventActionWidgetAction.SetWidgetValue.toString()) ) {
			uiGeneric.setWidgetValue(strWidget, widgetValue);
		} else if ( strWidgetAction.equals(UIEventActionWidgetAction.SetWidgetStatus.toString()) ) {
			WidgetStatus widgetStatus = null;
			for ( WidgetStatus cstWidgetStatus : WidgetStatus.values() ) {
				String name = cstWidgetStatus.toString();
				if ( name.equals(widgetValue) ) {
					widgetStatus = cstWidgetStatus;
					break;
				}
			}
			if ( null != widgetStatus ) {
				uiGeneric.setWidgetStatus(strWidget, widgetStatus);
			} else {
				logger.warn(className, function, logPrefix+"widgetStatus IS NULL");
			}
			
		} else {
			logger.warn(className, function, "strWidgetAction[{}] IS UNKNOW TYPE", strWidgetAction);
		}
		
		logger.end(className, function);
		return bContinue;
	}

}
