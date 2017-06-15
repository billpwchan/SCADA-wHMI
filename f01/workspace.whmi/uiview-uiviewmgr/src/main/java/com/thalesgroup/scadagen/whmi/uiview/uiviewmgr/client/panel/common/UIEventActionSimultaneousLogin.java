package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common;

import java.util.HashMap;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.ActionAttribute;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.control.SimultaneousLogin;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionOpm_i.UIEventActionOpmAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionExecute_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.OpmMgr;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i;

public class UIEventActionSimultaneousLogin extends UIEventActionExecute_i {

	private final String className = UIWidgetUtil.getClassSimpleName(UIEventActionSimultaneousLogin.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private final String dictionariesCacheName = "UIJson";
	private final String fileName = "simultaneousLogin.json";

	public UIEventActionSimultaneousLogin() {
		supportedActions = new String[] { 
				UIEventActionOpmAction.SimultaneousLogin.toString()
				, UIEventActionOpmAction.SimultaneousLogout.toString() 
				, UIEventActionOpmAction.JSSessionStart.toString() 
				, UIEventActionOpmAction.JSSessionEnd.toString()
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
		
		if (action.equals(UIEventActionOpmAction.SimultaneousLogin.toString())) {
			
			logger.debug(className, function, logPrefix + UIEventActionOpmAction.SimultaneousLogin.toString());

			final String strDbAttrResrvReserveReqID = "DbAttrResrvReserveReqID";

			final String loginRequestProcedure = "login_request_procedure";

			SimultaneousLogin simultaneousLogin = new SimultaneousLogin();

			String opmApi = simultaneousLogin.getOpmApi();
			String opmIdendifyType = simultaneousLogin.getOpmIdentityType();
			
			logger.debug(className, function, "opmApi[{}] opmIdendifyType[{}]", opmApi, opmIdendifyType);
			
			String selfIdentity = simultaneousLogin.getSelfIdentity(opmApi, opmIdendifyType);
			
			logger.debug(className, function, "selfIdentity[{}]", selfIdentity);
			
			String scsEnvId = simultaneousLogin.getScsEnvId(selfIdentity);
			String alias = simultaneousLogin.getAlias(selfIdentity);
			
			logger.debug(className, function, "scsEnvId[{}] alias[{}]", scsEnvId, alias);

			String dbAttrResrvReserveReqID = 
					simultaneousLogin.getStringFromJson(dictionariesCacheName, fileName, strDbAttrResrvReserveReqID);
			
			logger.debug(className, function, "dbAttrResrvReserveReqID[{}]", dbAttrResrvReserveReqID);

			if (null != alias && null != scsEnvId && null != dbAttrResrvReserveReqID) {

				String address = alias + dbAttrResrvReserveReqID;
				String value = selfIdentity;

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
				logger.warn(className, function, "alias[{}] scsEnvId[{}] dbAttrResrvReserveReqID[{}]", new Object[]{alias, scsEnvId, dbAttrResrvReserveReqID});
			}

		}
		else if (action.equals(UIEventActionOpmAction.SimultaneousLogout.toString())) {
			
			logger.debug(className, function, logPrefix + UIEventActionOpmAction.SimultaneousLogout.toString());

			final String strDbAttrResrvUnreserveReqID = "DbAttrResrvUnreserveReqID";

			final String logoutRequestProcedure = "logout_request_procedure";

			SimultaneousLogin simultaneousLogin = new SimultaneousLogin();

			String opmApi = simultaneousLogin.getOpmApi();
			String opmIdendifyType = simultaneousLogin.getOpmIdentityType();
			
			logger.debug(className, function, "opmApi[{}] opmIdendifyType[{}]", opmApi, opmIdendifyType);

			String selfIdentity = simultaneousLogin.getSelfIdentity(opmApi, opmIdendifyType);
			
			logger.debug(className, function, "selfIdentity[{}]", selfIdentity);

			String scsEnvId = simultaneousLogin.getScsEnvId(selfIdentity);
			String alias = simultaneousLogin.getAlias(selfIdentity);
			
			logger.debug(className, function, "scsEnvId[{}] alias[{}]", scsEnvId, alias);

			String dbAttrResrvUnreserveReqID = simultaneousLogin.getStringFromJson(dictionariesCacheName, fileName,
					strDbAttrResrvUnreserveReqID);
			
			logger.debug(className, function, "dbAttrResrvUnreserveReqID[{}]", dbAttrResrvUnreserveReqID);

			if (null != alias && null != scsEnvId && null != dbAttrResrvUnreserveReqID) {

				String address = alias + dbAttrResrvUnreserveReqID;
				String value = selfIdentity;

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
				logger.warn(className, function, "alias[{}] scsEnvId[{}] strDbAttrResrvUnreserveReqID[{}]", new Object[]{alias, scsEnvId, strDbAttrResrvUnreserveReqID});
			}

		}
		
		else if (action.equals(UIEventActionOpmAction.JSSessionStart.toString())) {
			
			logger.debug(className, function, logPrefix + UIEventActionOpmAction.JSSessionStart.toString());

			final String strDbAttrResrvReserveReqID = "DbAttrResrvReserveReqID";

			final String loginSessionStart = "login_session_start";

			SimultaneousLogin simultaneousLogin = new SimultaneousLogin();
			
			String opmApi = simultaneousLogin.getOpmApi();
			String opmIdendifyType = simultaneousLogin.getOpmIdentityType();
			
			logger.debug(className, function, "opmApi[{}] opmIdendifyType[{}]", opmApi, opmIdendifyType);

			String selfIdentity = simultaneousLogin.getSelfIdentity(opmApi, opmIdendifyType);
			
			logger.debug(className, function, "selfIdentity[{}]", selfIdentity);

			String scsEnvId = simultaneousLogin.getScsEnvId(selfIdentity);
			String alias = simultaneousLogin.getAlias(selfIdentity);
			
			logger.debug(className, function, "scsEnvId[{}] alias[{}]", scsEnvId, alias);

			String dbAttrResrvReserveReqID = 
					simultaneousLogin.getStringFromJson(dictionariesCacheName, fileName, strDbAttrResrvReserveReqID);
			
			logger.debug(className, function, "dbAttrResrvReserveReqID[{}]", dbAttrResrvReserveReqID);
			
			String currentOperator = null;
			String currentProfile = null;
			OpmMgr opmMgr = OpmMgr.getInstance();
			UIOpm_i uiOpm_i = opmMgr.getOpm(opmApi);
			if (null != uiOpm_i) {
				logger.debug(className, function, "call opm_i login");
				currentOperator = uiOpm_i.getCurrentOperator();
				currentProfile = uiOpm_i.getCurrentProfile();
			} else {
				logger.warn(className, function, logPrefix + "opmapi[{}] instance IS NULL", opmApi);
			}

			if (null != alias && null != scsEnvId && null != dbAttrResrvReserveReqID) {

				String address = alias + dbAttrResrvReserveReqID;
				String value = selfIdentity;

				logger.debug(className, function, "scsEnvId[{}] address[{}] value[{}]",
						new Object[] { scsEnvId, address, value });

				String actionsetkey2 = loginSessionStart;
				
				logger.debug(className, function, "actionsetkey2[{}]", actionsetkey2);

				HashMap<String, HashMap<String, Object>> override2 = new HashMap<String, HashMap<String, Object>>();

				String actionkey3 = loginSessionStart;
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
				logger.warn(className, function, "alias[{}] scsEnvId[{}] dbAttrResrvReserveReqID[{}]", new Object[]{alias, scsEnvId, dbAttrResrvReserveReqID});
			}

		}
		else {

			logger.warn(className, function, logPrefix + "action[{}] IS UNKNOW", action);

		}

		logger.end(className, function);
		return bContinue;
	}

}
