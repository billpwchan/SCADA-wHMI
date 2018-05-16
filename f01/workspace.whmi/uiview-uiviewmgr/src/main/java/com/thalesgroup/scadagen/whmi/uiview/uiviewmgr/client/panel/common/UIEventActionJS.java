package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common;

import java.util.Map;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionJS_i.UIEventActionJSAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionExecute_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.ActionAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.UIActionEventAttribute;

public class UIEventActionJS extends UIEventActionExecute_i {
	
	private final String className = this.getClass().getSimpleName();
	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
	
	public UIEventActionJS ( ) {
		supportedActions = new String[] {
				UIEventActionJSAction.CallJSByGWT.toString()
		};
	}
	@Override
	public boolean executeAction(UIEventAction action, Map<String, Map<String, Object>> override) {
		final String function = logPrefix+" executeAction";
		logger.begin(function);
		
		boolean bContinue = true;
		
		String os1			= (String) action.getParameter(ActionAttribute.OperationString1.toString());

		if ( os1.equalsIgnoreCase(UIEventActionJSAction.CallJSByGWT.toString()) ) {
			
			JSONObject request = new JSONObject();
			
			for ( String osname : UIActionEventAttribute.toStrings() ) {
				String osdata	= (String) action.getParameter(osname);
				logger.debug(function, "osname[{}] osdata[{}]", osname, osdata);
				if ( null != osdata ) {
					request.put(osname, new JSONString(osdata));
				} else {
					logger.warn(function, "osdata IS NULL");
				}
			}
			
			for ( String osname : ActionAttribute.toStrings() ) {
				String osdata	= (String) action.getParameter(osname);
				logger.debug(function, "osname[{}] osdata[{}]", osname, osdata);
				if ( null != osdata ) {
					request.put(osname, new JSONString(osdata));
				} else {
					logger.warn(function, "osdata IS NULL");
				}
			}
			
			String jsondata = request.toString();
			logger.debug(function, "jsondata[{}]", jsondata);

			logger.debug(function, "Entry callJsByGwt Try Black...");
			try {
				logger.debug(function, "Calling Begin callJsByGwt...");
				callJSByGWT(jsondata);
				logger.debug(function, "Calling End callJsByGwt.");
			} catch ( Exception ex ) {
				logger.warn(function, "execute Exception["+ex.toString()+"]");
			}
			logger.debug(function, "Exit callJsByGwt Try Black.");
		}
		
		logger.end(function);
		return bContinue;
	}
	
	public native void callJSByGWT (String jsonstring) /*-{
		$wnd.SCADAGEN.UIEVENTACTION.callJSByGWT(jsonstring);
	}-*/;

}
