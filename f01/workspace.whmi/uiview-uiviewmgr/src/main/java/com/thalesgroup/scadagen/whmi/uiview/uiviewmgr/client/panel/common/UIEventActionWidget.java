package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common;

import java.util.Map;

import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.ActionAttribute;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionWidget_i.UIEventActionWidgetAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionExecute_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetGeneric_i.WidgetStatus;

public class UIEventActionWidget extends UIEventActionExecute_i {
	
	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
	
	public UIEventActionWidget ( ) {
		supportedActions = new String[] {
				  UIEventActionWidgetAction.SetWidgetStatus.toString()
				, UIEventActionWidgetAction.SetWidgetValue.toString()
				, UIEventActionWidgetAction.SetWidgetFocus.toString()
				, UIEventActionWidgetAction.SetWidgetCSS.toString()
				, UIEventActionWidgetAction.AddWidgetCSS.toString()
				, UIEventActionWidgetAction.RemoveWidgetCSS.toString()
				};
	}
	
	@Override
	public boolean executeAction(UIEventAction uiEventAction, Map<String, Map<String, Object>> override) {
		final String function = logPrefix+" executeAction";
		logger.begin(function);
		
		boolean bContinue = true;
		
		if ( uiEventAction == null ) {
			logger.warn(function, "uiEventAction IS NULL");
			return bContinue;
		}
		
		String strWidgetAction	= (String) uiEventAction.getParameter(ActionAttribute.OperationString1.toString());
		String strWidget		= (String) uiEventAction.getParameter(ActionAttribute.OperationString2.toString());
		String widgetValue		= (String) uiEventAction.getParameter(ActionAttribute.OperationString3.toString());
		
		logger.debug(function, logPrefix+"strWidgetAction[{}] strWidget[{}] widgetValue[{}]", new Object[]{strWidgetAction, strWidget, widgetValue});
		
		if ( uiGeneric == null ) {
			logger.warn(function, logPrefix+"uiWidgetGeneric IS NULL");
			return bContinue;
		}
		
		if ( strWidgetAction == null ) {
			logger.warn(function, logPrefix+"strWidgetAction IS NULL");
			return bContinue;
		}
		
		if ( strWidget == null ) {
			logger.warn(function, logPrefix+"strWidget IS NULL");
			return bContinue;
		}
		
		if ( widgetValue == null ) {
			logger.warn(function, logPrefix+"widgetValue IS NULL");
			return bContinue;
		}
		
		if ( strWidgetAction.equals(UIEventActionWidgetAction.SetWidgetValue.toString()) ) {
			uiGeneric.setWidgetValue(strWidget, widgetValue);
		}
		else 
			if ( strWidgetAction.equals(UIEventActionWidgetAction.SetWidgetStatus.toString()) ) {
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
				logger.warn(function, logPrefix+"widgetStatus IS NULL");
			}
			
		}
		else 
			if ( strWidgetAction.equals(UIEventActionWidgetAction.SetWidgetFocus.toString()) ) {
				Widget widget = uiGeneric.getWidget(strWidget);
				if(widget instanceof Focusable) {
					Focusable focusable = (com.google.gwt.user.client.ui.Focusable) uiGeneric.getWidget(strWidget);
					focusable.setFocus(Boolean.parseBoolean(widgetValue));
				} else {
					logger.warn(function, logPrefix+"strWidget["+strWidget+"] IS NOT Focusable");
				}
		}
		else 
			if ( strWidgetAction.equals(UIEventActionWidgetAction.SetWidgetCSS.toString()) ) {
				Widget widget = uiGeneric.getWidget(strWidget);
				widget.setStyleName(widgetValue);
		}
		else 
			if ( strWidgetAction.equals(UIEventActionWidgetAction.AddWidgetCSS.toString()) ) {
				Widget widget = uiGeneric.getWidget(strWidget);
				widget.addStyleName(widgetValue);
		}
		else 
			if ( strWidgetAction.equals(UIEventActionWidgetAction.RemoveWidgetCSS.toString()) ) {
				Widget widget = uiGeneric.getWidget(strWidget);
				widget.removeStyleName(widgetValue);
		}
		else {
			logger.warn(function, "strWidgetAction[{}] IS UNKNOW TYPE", strWidgetAction);
		}
		
		logger.end(function);
		return bContinue;
	}

}
