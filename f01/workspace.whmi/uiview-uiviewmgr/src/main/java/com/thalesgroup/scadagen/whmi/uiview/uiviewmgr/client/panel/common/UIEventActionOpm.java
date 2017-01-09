package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common;

import java.util.HashMap;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIActionEventAttribute_i.ActionAttribute;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionOpm_i.UIEventActionOpmAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.OpmMgr;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i;

public class UIEventActionOpm extends UIEventActionExecute_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIEventActionOpm.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	public UIEventActionOpm ( ) {
		supportedActions = new String[] {
				  UIEventActionOpmAction.OpmLogin.toString()
				, UIEventActionOpmAction.OpmLogout.toString()
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
		
		String action		= (String) uiEventAction.getParameter(ActionAttribute.OperationString1.toString());
		String opmapi		= (String) uiEventAction.getParameter(ActionAttribute.OperationString2.toString());
		
		if ( action == null ) {
			logger.warn(className, function, logPrefix+"action IS NULL");
			return bContinue;
		}
		
		if ( opmapi == null ) {
			logger.warn(className, function, logPrefix+"opmapi IS NULL");
			return bContinue;
		}
		
		logger.info(className, function, logPrefix+"action[{}]", action);
		
		if ( action.equals(UIEventActionOpmAction.OpmLogout.toString()) ) {
			
			logger.info(className, function, logPrefix+"logout");
			
			UIOpm_i opm_i = OpmMgr.getInstance(opmapi);
			if ( null != opm_i ) {
				logger.debug(className, function, "call opm_i logout");
				opm_i.logout();
			} else {
				logger.warn(className, function, logPrefix+"opmapi[{}] instance IS NULL", opmapi);
			}
			
		} else if ( action.equals(UIEventActionOpmAction.OpmLogin.toString()) ) {
			
			logger.info(className, function, logPrefix+"login");
			
			String operator		= (String) uiEventAction.getParameter(ActionAttribute.OperationString3.toString());
			String password		= (String) uiEventAction.getParameter(ActionAttribute.OperationString4.toString());
			
			if ( operator == null ) {
				logger.warn(className, function, logPrefix+"operator IS NULL");
				return bContinue;
			}
			
			if ( password == null ) {
				logger.warn(className, function, logPrefix+"password IS NULL");
				return bContinue;
			}
			
			UIOpm_i uiOpm_i = OpmMgr.getInstance(opmapi);
			if ( null != uiOpm_i ) {
				logger.debug(className, function, "call opm_i login");
				uiOpm_i.login(operator, password);
			} else {
				logger.warn(className, function, logPrefix+"opmapi[{}] instance IS NULL", opmapi);
			}
			
		} else {
			
			logger.warn(className, function, logPrefix+"action[{}] IS UNKNOW", action);
			
		}
		
		logger.end(className, function);
		return bContinue;
	}

}
