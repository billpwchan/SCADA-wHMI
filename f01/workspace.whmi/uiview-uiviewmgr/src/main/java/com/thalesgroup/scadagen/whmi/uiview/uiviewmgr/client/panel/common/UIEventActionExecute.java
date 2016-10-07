package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIActionEventAttribute_i.WidgetExecuteAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetGeneric_i.WidgetStatus;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIWidgetGeneric;

public class UIEventActionExecute {
	private final String className = UIWidgetUtil.getClassSimpleName(UIEventActionExecute.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	private String logPrefix = "";
	private UIWidgetGeneric uiWidgetGeneric = null;
	public UIEventActionExecute ( String logPrefix, UIWidgetGeneric uiWidgetGeneric) {
		this.logPrefix = "-> "+logPrefix+" ";
		this.uiWidgetGeneric = uiWidgetGeneric;
	}
	
	private static final String strSetWidgetStatus	= "SetWidgetStatus";
	private static final String strSetWidgetValue	= "SetWidgetValue";
	
	public static boolean isSupportedAction(String operation) {
		boolean result = false;
		if ( null != operation ) {
			if ( operation.equals(strSetWidgetStatus) ) {
				result = true;
			} else if ( operation.equals(strSetWidgetValue) ) {
				result = true;
			}
		}
		return result;
	}
	
	public void executeAction(UIEventAction uiEventAction) {
	final String function = "action";
		
		logger.begin(className, function);

		if ( uiEventAction == null ) {
			logger.warn(className, function, "uiEventAction IS NULL");
			return;
		}
		
		String strWidgetAction	= (String) uiEventAction.getParameter(WidgetExecuteAttribute.OperationString1.toString());
		String strWidget		= (String) uiEventAction.getParameter(WidgetExecuteAttribute.OperationString2.toString());
		String widgetValue		= (String) uiEventAction.getParameter(WidgetExecuteAttribute.OperationString3.toString());
		
		logger.info(className, function, logPrefix+"strWidgetAction[{}] strWidget[{}] widgetValue[{}]", new Object[]{strWidgetAction, strWidget, widgetValue});
		
		if ( uiWidgetGeneric == null ) {
			logger.warn(className, function, logPrefix+"uiWidgetGeneric IS NULL");
			return;
		}
		
		if ( strWidgetAction == null ) {
			logger.warn(className, function, logPrefix+"strWidgetAction IS NULL");
			return;
		}
		
		if ( strWidget == null ) {
			logger.warn(className, function, logPrefix+"strWidget IS NULL");
			return;
		}
		
		if ( widgetValue == null ) {
			logger.warn(className, function, logPrefix+"widgetValue IS NULL");
			return;
		}
		
		if ( strWidgetAction.equals(strSetWidgetValue) ) {
			uiWidgetGeneric.setWidgetValue(strWidget, widgetValue);
		} else if ( strWidgetAction.equals(strSetWidgetStatus) ) {
			WidgetStatus widgetStatus = null;
			if ( widgetValue.equals(WidgetStatus.Up.toString()) ) {
				widgetStatus = WidgetStatus.Up;
			} else if ( widgetValue.equals(WidgetStatus.Down.toString()) ) {
				widgetStatus = WidgetStatus.Down;
			} else if ( widgetValue.equals(WidgetStatus.Disable.toString()) ) {
				widgetStatus = WidgetStatus.Disable;
			} else {
				logger.warn(className, function, logPrefix+"strWidgetAction[{}] IS UNSUPPORT TYPE", strWidgetAction);
			}
			if ( null != widgetStatus ) {
				uiWidgetGeneric.setWidgetStatus(strWidget, widgetStatus);
			} else {
				logger.warn(className, function, logPrefix+"widgetStatus IS NULL");
			}
			
		} else {
			logger.warn(className, function, "strWidgetAction[{}] IS UNKNOW TYPE", strWidgetAction);
		}
		
		logger.end(className, function);
	}

}