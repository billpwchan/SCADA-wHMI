package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common;

import java.util.HashMap;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.ActionAttribute;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.control.SimultaneousLogin;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionSimultaneousLogin_i.UIEventActionSimultaneousLoginAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionExecute_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.OpmMgr;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i;

public class UIEventActionSimultaneousLogin extends UIEventActionExecute_i {

	private final String className = UIWidgetUtil.getClassSimpleName(UIEventActionSimultaneousLogin.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);

	public UIEventActionSimultaneousLogin() {
		supportedActions = new String[] { 
				  UIEventActionSimultaneousLoginAction.SimultaneousLogin.toString()
				, UIEventActionSimultaneousLoginAction.SimultaneousLogout.toString() 
				, UIEventActionSimultaneousLoginAction.JSSessionStart.toString() 
				, UIEventActionSimultaneousLoginAction.JSSessionEnd.toString()
				};
	}

	@Override
	public boolean executeAction(UIEventAction uiEventAction, HashMap<String, HashMap<String, Object>> override) {
		final String function = logPrefix + " executeAction";
		logger.begin(className, function);

		boolean bContinue = true;

		if (uiEventAction == null) {
			logger.warn(className, function, "uiEventAction IS NULL");
			return bContinue;
		}
		
		String action = (String) uiEventAction.getParameter(ActionAttribute.OperationString1.toString());

		if (action == null) {
			logger.warn(className, function, logPrefix + "action IS NULL");
			return bContinue;
		}
		
		if (action.equals(UIEventActionSimultaneousLoginAction.SimultaneousLogin.toString())) {
			
			logger.debug(className, function, logPrefix + UIEventActionSimultaneousLoginAction.SimultaneousLogout.toString());
			
			simultaneousLogin();
		}
		else if (action.equals(UIEventActionSimultaneousLoginAction.SimultaneousLogout.toString())) {
			
			logger.debug(className, function, logPrefix + UIEventActionSimultaneousLoginAction.SimultaneousLogout.toString());

			simultaneousLogout();
		}
		else if (action.equals(UIEventActionSimultaneousLoginAction.JSSessionStart.toString())) {
			
			logger.debug(className, function, logPrefix + UIEventActionSimultaneousLoginAction.JSSessionStart.toString());

			jsSessionStart();
		}
		else {

			logger.warn(className, function, logPrefix + "action[{}] IS UNKNOW", action);

		}

		logger.end(className, function);
		return bContinue;
	}
	
	private void simultaneousLogin() {
		final String function = logPrefix + " simultaneousLogin";
		logger.begin(className, function);
		
		logger.debug(className, function, logPrefix + UIEventActionSimultaneousLoginAction.SimultaneousLogin.toString());

		final String loginRequestProcedure = "login_request_procedure";

		SimultaneousLogin simultaneousLogin = SimultaneousLogin.getInstance();
		
		String scsEnvId = simultaneousLogin.getSelfScsEnvId();
		
		String alias = simultaneousLogin.getSelfAlias();
		
		String usrIdentity = simultaneousLogin.getUsrIdentity();

		String dbAttrResrvReserveReqID = simultaneousLogin.getDbAttriuteReserveReqID();
		
		logger.debug(className, function, "dbAttrResrvReserveReqID[{}]", dbAttrResrvReserveReqID);

		if (null != usrIdentity && null != alias && null != scsEnvId && null != dbAttrResrvReserveReqID) {

			String address = alias + dbAttrResrvReserveReqID;
			String value = usrIdentity;

			logger.debug(className, function, "scsEnvId[{}] address[{}] value[{}]",
					new Object[] { scsEnvId, address, value });

			String actionsetkey2 = loginRequestProcedure;
			
			logger.debug(className, function, "actionsetkey2[{}]", actionsetkey2);

			String actionkey2 = loginRequestProcedure;
			logger.debug(className, function, "actionkey2[{}]", actionkey2);
			
			HashMap<String, Object> parameter2 = new HashMap<String, Object>();
			parameter2.put(ActionAttribute.OperationString2.toString(), scsEnvId);
			parameter2.put(ActionAttribute.OperationString3.toString(), address);
			parameter2.put(ActionAttribute.OperationString4.toString(), value);
			
			HashMap<String, HashMap<String, Object>> override2 = new HashMap<String, HashMap<String, Object>>();
			override2.put(actionkey2, parameter2);

			uiEventActionProcessorCore_i.executeActionSet(actionsetkey2, override2);
		} else {
			logger.warn(className, function, "usrIdentity[{}] alias[{}] scsEnvId[{}] dbAttrResrvReserveReqID[{}]", new Object[]{usrIdentity, alias, scsEnvId, dbAttrResrvReserveReqID});
		}
		
		logger.end(className, function);
	}
	
	private void simultaneousLogout() {
		final String function = logPrefix + " simultaneousLogout";
		logger.begin(className, function);
		
		final String strDbAttrResrvUnreserveReqID = "DbAttrResrvUnreserveReqID";

		final String logoutRequestProcedure = "logout_request_procedure";

		SimultaneousLogin simultaneousLogin = SimultaneousLogin.getInstance();
		
		String scsEnvId = simultaneousLogin.getSelfScsEnvId();
		
		String alias = simultaneousLogin.getSelfAlias();
		
		String usrIdentity = simultaneousLogin.getUsrIdentity();

		String dbAttrResrvUnreserveReqID = simultaneousLogin.getDbAttriuteUnreserveReqID();
		
		logger.debug(className, function, "dbAttrResrvUnreserveReqID[{}]", dbAttrResrvUnreserveReqID);

		if (null != usrIdentity && null != alias && null != scsEnvId && null != dbAttrResrvUnreserveReqID) {

			String address = alias + dbAttrResrvUnreserveReqID;
			String value = usrIdentity;

			logger.debug(className, function, "scsEnvId[{}] address[{}] value[{}]",
					new Object[] { scsEnvId, address, value });

			String actionsetkey2 = logoutRequestProcedure;
			String actionkey2 = logoutRequestProcedure;

			logger.debug(className, function, "actionsetkey2[{}] actionkey2[{}]", actionsetkey2, actionkey2);

			HashMap<String, Object> parameter2 = new HashMap<String, Object>();
			parameter2.put(ActionAttribute.OperationString2.toString(), scsEnvId);
			parameter2.put(ActionAttribute.OperationString3.toString(), address);
			parameter2.put(ActionAttribute.OperationString4.toString(), value);

			HashMap<String, HashMap<String, Object>> override2 = new HashMap<String, HashMap<String, Object>>();
			override2.put(actionkey2, parameter2);

			uiEventActionProcessorCore_i.executeActionSet(actionsetkey2, override2);
		} else {
			logger.warn(className, function, "usrIdentity[{}] alias[{}] scsEnvId[{}] strDbAttrResrvUnreserveReqID[{}]", new Object[]{usrIdentity, alias, scsEnvId, strDbAttrResrvUnreserveReqID});
		}
		
		logger.end(className, function);
	}
	
	private void jsSessionStart() {
		final String function = logPrefix + " jsSessionStart";
		logger.begin(className, function);
		
		final String loginJSSession = "login_js_session";

		SimultaneousLogin simultaneousLogin = SimultaneousLogin.getInstance();
		
		String scsEnvId = simultaneousLogin.getSelfScsEnvId();
		
		String alias = simultaneousLogin.getSelfAlias();
		
		String usrIdentity = simultaneousLogin.getUsrIdentity();

		String opmApi = simultaneousLogin.getOpmApi();
		
		String dbAttrResrvReserveReqID = simultaneousLogin.getDbAttriuteReserveReqID();
		
		logger.debug(className, function, "dbAttrResrvReserveReqID[{}]", dbAttrResrvReserveReqID);
		
		String currentOperator = null;
		String currentProfile = null;
		OpmMgr opmMgr = OpmMgr.getInstance();
		UIOpm_i uiOpm_i = opmMgr.getOpm(opmApi);
		if (null != uiOpm_i) {
			logger.debug(className, function, "call opm_i login");
			currentOperator = uiOpm_i.getCurrentOperator();
			currentProfile = uiOpm_i.getCurrentProfile();
			
			logger.debug(className, function, "currentOperator[{}] currentProfile[{}]", currentOperator, currentProfile);
		} else {
			logger.warn(className, function, logPrefix + "opmapi[{}] instance IS NULL", opmApi);
		}

		if (null != usrIdentity && null != alias && null != scsEnvId && null != dbAttrResrvReserveReqID) {

			String address = alias + dbAttrResrvReserveReqID;
			String value = usrIdentity;

			logger.debug(className, function, "scsEnvId[{}] address[{}] value[{}]",
					new Object[] { scsEnvId, address, value });

			String actionsetkey2 = loginJSSession;
			
			logger.debug(className, function, "actionsetkey2[{}]", actionsetkey2);

			HashMap<String, HashMap<String, Object>> override2 = new HashMap<String, HashMap<String, Object>>();

			String actionkey3 = loginJSSession;
			logger.debug(className, function, "actionkey3[{}]", actionkey3);
			
			HashMap<String, Object> parameter3 = new HashMap<String, Object>();
				
			parameter3.put(ActionAttribute.OperationString3.toString(), currentOperator);
			parameter3.put(ActionAttribute.OperationString4.toString(), currentProfile);
				
			parameter3.put(ActionAttribute.OperationString5.toString(), scsEnvId);
			parameter3.put(ActionAttribute.OperationString6.toString(), address);
			parameter3.put(ActionAttribute.OperationString7.toString(), value);
				
			override2.put(actionkey3, parameter3);

			uiEventActionProcessorCore_i.executeActionSet(actionsetkey2, override2);
		} else {
			logger.warn(className, function, "usrIdentity[{}] alias[{}] scsEnvId[{}] dbAttrResrvReserveReqID[{}]", new Object[]{usrIdentity, alias, scsEnvId, dbAttrResrvReserveReqID});
		}
		
		logger.end(className, function);
	}

}
