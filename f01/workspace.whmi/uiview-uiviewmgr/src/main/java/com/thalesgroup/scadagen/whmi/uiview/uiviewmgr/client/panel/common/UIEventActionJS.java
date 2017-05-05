package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common;

import java.util.HashMap;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionJS_i.UIEventActionJSAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionExecute_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.ActionAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.UIActionEventAttribute;

public class UIEventActionJS extends UIEventActionExecute_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIEventActionJS.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	public UIEventActionJS ( ) {
		supportedActions = new String[] {
				UIEventActionJSAction.CallJSByGWT.toString()
		};
	}
	@Override
	public boolean executeAction(UIEventAction action, HashMap<String, HashMap<String, Object>> override) {
		final String function = logPrefix+" executeAction";
		logger.begin(className, function);
		
		boolean bContinue = true;
		
		String os1			= (String) action.getParameter(ActionAttribute.OperationString1.toString());

		if ( os1.equalsIgnoreCase(UIEventActionJSAction.CallJSByGWT.toString()) ) {
			
			JSONObject request = new JSONObject();
			
			for ( String osname : UIActionEventAttribute.toStrings() ) {
				String osdata	= (String) action.getParameter(osname);
				logger.debug(className, function, "osname[{}] osdata[{}]", osname, osdata);
				if ( null != osdata ) {
					request.put(osname, new JSONString(osdata));
				} else {
					logger.warn(className, function, "osdata IS NULL");
				}
			}
			
			for ( String osname : ActionAttribute.toStrings() ) {
				String osdata	= (String) action.getParameter(osname);
				logger.debug(className, function, "osname[{}] osdata[{}]", osname, osdata);
				if ( null != osdata ) {
					request.put(osname, new JSONString(osdata));
				} else {
					logger.warn(className, function, "osdata IS NULL");
				}
			}
			
			String jsondata = request.toString();
			
			logger.debug(className, function, "jsondata[{}]", jsondata);
			
			logger.debug(className, function, "Calling Begin callJsByGwt...");
			try {
				callJSByGWT(jsondata);
			} catch ( Exception ex ) {
				logger.warn(className, function, "execute Exception["+ex.toString()+"]");
			}
			logger.debug(className, function, "Calling End callJsByGwt.");
		}
		
		logger.end(className, function);
		return bContinue;
	}
	
	public native void callJSByGWT (String jsonstring) /*-{
		$wnd.uieventaction.callJSByGWT(jsonstring);
	}-*/;

}
