package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common;

import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIView_i.ViewAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetGeneric_i.WidgetStatus;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIWidgetGeneric;

public class UIWidgetGenericAction {
	private final String className = UIWidgetUtil.getClassSimpleName(UIWidgetGenericAction.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	private String logPrefix = "";
	public UIWidgetGenericAction ( String logPrefix ) {
		this.logPrefix = "-> "+logPrefix+" ";
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
	
	public void action(UIWidgetGeneric uiWidgetGeneric, UIEventAction uiEventAction) {
	final String function = "action";
		
		logger.begin(className, function);
		
		if ( uiWidgetGeneric == null ) {
			logger.warn(className, function, "uiWidgetGeneric IS NULL");
			return;
		}
		
		if ( uiEventAction == null ) {
			logger.warn(className, function, "uiEventAction IS NULL");
			return;
		}
		
		String ot	= (String) uiEventAction.getParameter(ViewAttribute.OperationTarget.toString());
		String op	= (String) uiEventAction.getParameter(ViewAttribute.Operation.toString());
		String os1	= (String) uiEventAction.getParameter(ViewAttribute.OperationString1.toString());
		String os2	= (String) uiEventAction.getParameter(ViewAttribute.OperationString2.toString());
		String os3	= (String) uiEventAction.getParameter(ViewAttribute.OperationString3.toString());
		
		String strWidgetAction	= op;
		String strWidget		= os1;
		String widgetValue		= os2;
		
		logger.info(className, function, logPrefix+"ot["+ot+"]");
		logger.info(className, function, logPrefix+"op["+op+"]");
		logger.info(className, function, logPrefix+"os1["+os1+"]");
		logger.info(className, function, logPrefix+"os2["+os2+"]");
		logger.info(className, function, logPrefix+"os3["+os3+"]");
		
		
		logger.info(className, function, logPrefix+"strWidgetAction["+strWidgetAction+"] strWidget["+strWidget+"] widgetValue["+widgetValue+"]");
		
		action(uiWidgetGeneric, strWidgetAction, strWidget, widgetValue);
		
		logger.end(className, function);
	}
	public void action(UIWidgetGeneric uiWidgetGeneric, String strWidgetAction, String strWidget, String widgetValue) {
		final String function = "action";
		
		logger.begin(className, function);
		
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
		
		Widget widget = uiWidgetGeneric.getWidget(strWidget);
		
		if ( null != widget ) {
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

		} else {
			logger.warn(className, function, "strWidget[{}] IS NULL", strWidget);
		}

		logger.end(className, function);
	}

}
