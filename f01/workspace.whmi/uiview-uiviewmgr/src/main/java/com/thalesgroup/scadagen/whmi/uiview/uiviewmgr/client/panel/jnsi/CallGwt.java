package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.jnsi;

import java.util.HashMap;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionExecute_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.ActionAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIEventActionExecuteMgr;

public class CallGwt {
	
	private final String className = UIWidgetUtil.getClassSimpleName(CallGwt.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private UIEventActionExecute_i uiEventActionExecute = null;
	public void callGwt(String ot, String os1, String os2, String os3, String os4) {
		final String function = "init";
		logger.begin(className, function);
		
		logger.debug(className, function, "ot[{}]", ot);
		
		UIEventActionExecuteMgr uiEventActionExecuteMgr = UIEventActionExecuteMgr.getInstance();
		uiEventActionExecute = uiEventActionExecuteMgr.getUIEventActionExecute(ot);
		if ( null == uiEventActionExecute ) {
			logger.warn(className, function, "uiEventActionExecute IS NULL");
		}
		
		if ( null != uiEventActionExecute ) {
			
			UIEventAction uiEventAction = new UIEventAction();
			HashMap<String, HashMap<String, Object>> override = null;
			
			logger.debug(className, function, "os1[{}]", os1);
			logger.debug(className, function, "os2[{}]", os2);
			logger.debug(className, function, "os3[{}]", os3);
			logger.debug(className, function, "os4[{}]", os4);
			
			uiEventAction.setParameter(ActionAttribute.OperationString1.toString(), os1);
			uiEventAction.setParameter(ActionAttribute.OperationString2.toString(), os2);
			uiEventAction.setParameter(ActionAttribute.OperationString3.toString(), os3);
			uiEventAction.setParameter(ActionAttribute.OperationString4.toString(), os4);
			uiEventActionExecute.executeAction(uiEventAction, override);
			
		} else {
			logger.warn(className, function, "uiEventActionExecute IS NULL");
		}
		
		logger.end(className, function);
	}
	
	public native void exportCallGwt() /*-{
		var that = this;
		$wnd.callGwt = $entry(function(ot, os1, os2, os3) {
		that.@com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.jnsi.CallGwt::callGwt(I)(ot, os1, os2, os3);
		});
	}-*/;
}
