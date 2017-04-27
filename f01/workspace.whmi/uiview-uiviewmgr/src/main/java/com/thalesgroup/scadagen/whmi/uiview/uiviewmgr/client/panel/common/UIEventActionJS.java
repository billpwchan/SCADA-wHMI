package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common;

import java.util.HashMap;
import java.util.Map.Entry;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionJS_i.UIEventActionJSAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionExecute_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.ActionAttribute;

public class UIEventActionJS extends UIEventActionExecute_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIEventActionJS.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	public UIEventActionJS ( ) {
		supportedActions = new String[] {
				UIEventActionJSAction.CallJS.toString()
		};
	}
	@Override
	public boolean executeAction(UIEventAction action, HashMap<String, HashMap<String, Object>> override) {
		final String function = logPrefix+" executeAction";
		logger.begin(className, function);
		
		boolean bContinue = true;
		
		String os1			= (String) action.getParameter(ActionAttribute.OperationString1.toString());
		
		if ( logger.isInfoEnabled() ) {
			for ( Entry<String, Object> entry : action.getParameters() ) {
				String key = entry.getKey();
				Object obj = entry.getValue();
				logger.info(className, function, "key[{}] obj[{}]", key, obj);
			}
		}
		
		if ( os1.equalsIgnoreCase(UIEventActionJSAction.CallJS.toString()) ) {
			String os2			= (String) action.getParameter(ActionAttribute.OperationString2.toString());
			String os3			= (String) action.getParameter(ActionAttribute.OperationString3.toString());
			String os4			= (String) action.getParameter(ActionAttribute.OperationString4.toString());
			callJS(os2, os3, os4);
		}
		
		logger.end(className, function);
		return bContinue;
	}
	
	public native void callJS (String os2, String os3, String os4) /*-{
		$wnd.uiactionevent.callJS(os2, {os3:os4});
	}-*/;

}
