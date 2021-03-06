package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.jnsi;

import java.util.Map;

import com.google.gwt.json.client.JSONObject;
import com.thalesgroup.scadagen.whmi.config.configenv.client.ReadJson;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionExecute_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.ActionAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.UIActionEventAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIEventActionExecuteMgr;

public class CallGWTByJS {
	
	private final static String className = CallGWTByJS.class.getSimpleName();
	private final static UILogger_i logger = UILoggerFactory.getInstance().getUILogger(CallGWTByJS.class.getName());
	
	public static void executeAction(String jsdata) {
		final String function = "executeAction";
		logger.begin(function);
		
		logger.debug(function, "jsdata[{}]", jsdata);
		
		JSONObject json = ReadJson.readJson(jsdata);
		String oa = ReadJson.readString(json, UIActionEventAttribute.OperationAction.toString(), null);
		String ot = ReadJson.readString(json, UIActionEventAttribute.OperationType.toString(), null);
		
		logger.debug(function, "oa[{}]", oa);
		logger.debug(function, "ot[{}]", ot);
		
		UIEventAction uiEventAction = new UIEventAction();
		Map<String, Map<String, Object>> override = null;

		uiEventAction.setParameter(UIActionEventAttribute.OperationType.toString(), ot);
		uiEventAction.setParameter(UIActionEventAttribute.OperationAction.toString(), oa);
		
		for ( String osname : UIActionEventAttribute.toStrings() ) {
			String osstring = ReadJson.readString(json, osname, null);
			
			logger.debug(function, "osname[{}] osstring[{}]", osname, osstring);
			uiEventAction.setParameter(osname, osstring);
		}
		
		for ( String osname : ActionAttribute.toStrings() ) {
			String osstring = ReadJson.readString(json, osname, null);
			
			logger.debug(function, "osname[{}] osstring[{}]", osname, osstring);
			uiEventAction.setParameter(osname, osstring);
		}
			
		UIEventActionExecuteMgr uiEventActionExecuteMgr = UIEventActionExecuteMgr.getInstance();
		UIEventActionExecute_i uiEventActionExecute = uiEventActionExecuteMgr.getUIEventActionExecute(oa);
		if ( null != uiEventActionExecute ) {
			uiEventActionExecute.executeAction(uiEventAction, override);
		} else {
			logger.warn(function, "uiEventActionExecute IS NULL");
		}
		
//		UIEventActionProcessorMgr uiEventActionProcessorMgr = UIEventActionProcessorMgr.getInstance();
//		UIEventActionProcessor_i uiEventActionProcessor_i = uiEventActionProcessorMgr.getUIEventActionProcessor("UIEventActionProcessor");
//
//		if ( null != uiEventActionProcessor_i ) {
//			uiEventActionProcessor_i.setUINameCard(uiNameCard);
//			uiEventActionProcessor_i.setPrefix(className);
//			uiEventActionProcessor_i.setElement(element);
//			uiEventActionProcessor_i.setDictionariesCacheName("UIWidgetGeneric");
//			uiEventActionProcessor_i.setEventBus(eventBus);
//			uiEventActionProcessor_i.setOptsXMLFile(optsXMLFile);
//			uiEventActionProcessor_i.setUIGeneric(uiGeneric);
//			uiEventActionProcessor_i.setActionSetTagName(UIActionEventType.actionset.toString());
//			uiEventActionProcessor_i.setActionTagName(UIActionEventType.action.toString());
//			uiEventActionProcessor_i.init();
//		} else {
//			logger.warn(function, logPrefix+"uiEventActionProcessor_i IS NULL");
//		}
		
	}

	public static void callGWTByJS(String jsdata) {
		final String function = "callGWTByJS";
		logger.begin(function);
		logger.debug(function, "jsdata[{}]", jsdata);
		
		try {
			executeAction(jsdata);
		} catch ( Exception ex ) {
			logger.warn(function, "execute Exception["+ex.toString()+"]");
		}
		
		logger.end(function);
	}
	
	public static native void exportCallGWTByJS() /*-{
		$wnd.SCADAGEN.UIEVENTACTION.callGWTByJS = $entry(@com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.jnsi.CallGWTByJS::callGWTByJS(Ljava/lang/String;));
	}-*/;
}
