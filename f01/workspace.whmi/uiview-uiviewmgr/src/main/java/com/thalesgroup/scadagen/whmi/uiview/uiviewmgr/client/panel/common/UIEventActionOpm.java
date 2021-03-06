package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common;

import java.util.Map;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Timer;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.ActionAttribute;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionOpm_i.UIEventActionOpmAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionExecute_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.OpmMgr;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIWrapperRpcEvent_i;

public class UIEventActionOpm extends UIEventActionExecute_i {

	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());

	public UIEventActionOpm() {
		supportedActions = new String[] { 
				UIEventActionOpmAction.OpmLogin.toString()
				, UIEventActionOpmAction.OpmLogout.toString()
				, UIEventActionOpmAction.OpmChangePassword.toString()
				, UIEventActionOpmAction.OpmReloadPage.toString()
				};
	}

	@Override
	public boolean executeAction(UIEventAction uiEventAction, Map<String, Map<String, Object>> override) {
		final String function = logPrefix + " executeAction";
		logger.begin(function);

		boolean bContinue = true;

		if (uiEventAction == null) {
			logger.warn(function, "uiEventAction IS NULL");
			return bContinue;
		}

		String action	= (String) uiEventAction.getParameter(ActionAttribute.OperationString1.toString());
		String opmApi	= (String) uiEventAction.getParameter(ActionAttribute.OperationString2.toString());
		

		if (action == null) {
			logger.warn(function, logPrefix + "action IS NULL");
			return bContinue;
		}

		if (opmApi == null) {
			logger.warn(function, logPrefix + "opmapi IS NULL");
			return bContinue;
		}

		logger.debug(function, logPrefix + "action[{}]", action);

		if (action.equals(UIEventActionOpmAction.OpmLogout.toString())) {
			
			logger.debug(function, logPrefix + "OpmLogout");

			String waitms	= (String) uiEventAction.getParameter(ActionAttribute.OperationString3.toString());

			int wait = 0;
			if (waitms != null) {
				try {
					wait = Integer.parseInt(waitms);
					if ( wait < 0 ) wait = 0;
				} catch ( NumberFormatException ex ) {
					logger.error(function, logPrefix + "waitms[{}] IS INVALID", waitms);
				}
			} else {
				logger.warn(function, logPrefix + "waitms IS NULL");
			}
			
			logger.debug(function, logPrefix + "wait[{}]", wait);

			final UIOpm_i uiOpm_i = OpmMgr.getInstance().getOpm(opmApi);
			if (null != uiOpm_i) {
				logger.debug(function, "call opm_i logout");
				
				if ( wait <= 0 ) {
					uiOpm_i.logout();
				} else {
					
					Timer timer = new Timer() {
						
						@Override
						public void run() {
							final String function = "Timer run";
							logger.begin(function);
							logger.debug(function, logPrefix + "time to exexute logout");
							uiOpm_i.logout();
							
							logger.end(function);
						}
					};
					
					logger.debug(function, logPrefix + "schedule timer wait[{}]", wait);
					timer.schedule(wait);

				}
				
			} else {
				logger.warn(function, logPrefix + "opmapi[{}] instance IS NULL", opmApi);
			}
		}
		else if (action.equals(UIEventActionOpmAction.OpmLogin.toString())) {

			logger.debug(function, logPrefix + "OpmLogin");

			String operator = (String) uiEventAction.getParameter(ActionAttribute.OperationString3.toString());
			String password = (String) uiEventAction.getParameter(ActionAttribute.OperationString4.toString());

			if (operator == null) {
				logger.warn(function, logPrefix + "operator IS NULL");
				return bContinue;
			}

			if (password == null) {
				logger.warn(function, logPrefix + "password IS NULL");
				return bContinue;
			}

			OpmMgr opmMgr = OpmMgr.getInstance();
			UIOpm_i uiOpm_i = opmMgr.getOpm(opmApi);
			if (null != uiOpm_i) {
				logger.debug(function, "call opm_i login");
				uiOpm_i.login(operator, password);
			} else {
				logger.warn(function, logPrefix + "opmapi[{}] instance IS NULL", opmApi);
			}
		}
		else if (action.equals(UIEventActionOpmAction.OpmChangePassword.toString())) {

			logger.debug(function, logPrefix + "OpmChangePassword");

			String userid = (String) uiEventAction.getParameter(ActionAttribute.OperationString3.toString());
			String oldpass = (String) uiEventAction.getParameter(ActionAttribute.OperationString4.toString());
			String newpass = (String) uiEventAction.getParameter(ActionAttribute.OperationString5.toString());

			if (userid == null) {
				logger.warn(function, logPrefix + "userid IS NULL");
				return bContinue;
			}

			if (oldpass == null) {
				logger.warn(function, logPrefix + "oldpass IS NULL");
				return bContinue;
			}

			if (newpass == null) {
				logger.warn(function, logPrefix + "newpass IS NULL");
				return bContinue;
			}

			OpmMgr opmMgr = OpmMgr.getInstance();
			UIOpm_i uiOpm_i = opmMgr.getOpm(opmApi);
			if (null != uiOpm_i) {
				logger.debug(function, "call opm_i login");
				uiOpm_i.changePassword(userid, oldpass, newpass, new UIWrapperRpcEvent_i() {

					@Override
					public void event(JSONObject jsobject) {

						String function = null;
						String resultinstanceof = null;

						if (null != jsobject) {
							JSONValue v = null;

							v = jsobject.get("function");
							if (null != v) {
								JSONString s = v.isString();
								if (null != s) {
									function = s.stringValue();
								}
							}

							v = jsobject.get("resultinstanceof");
							if (null != v) {
								JSONString s = v.isString();
								if (null != s) {
									resultinstanceof = s.stringValue();
								}
							}

						}

						if (null != function && null != resultinstanceof && function.equalsIgnoreCase("onSuccessMwt")
								&& resultinstanceof.equalsIgnoreCase("OperatorActionReturn")) {

							uiEventActionProcessorCore_i.executeActionSet("set_result_value_valid");
						} else {
							uiEventActionProcessorCore_i.executeActionSet("set_result_value_invalid");
						}
					}
				});
			} else {
				logger.warn(function, logPrefix + "opmapi[{}] instance IS NULL", opmApi);
			}
		}
		else if (action.equals(UIEventActionOpmAction.OpmReloadPage.toString())) {

			logger.debug(function, logPrefix + "OpmReloadPage");
			OpmMgr opmMgr = OpmMgr.getInstance();
			UIOpm_i uiOpm_i = opmMgr.getOpm(opmApi);
			if (null != uiOpm_i) {
				logger.debug(function, "call opm_i reloadPage");
				uiOpm_i.reloadPage();
			} else {
				logger.warn(function, logPrefix + "opmapi[{}] instance IS NULL", opmApi);
			}
		}
		else {

			logger.warn(function, logPrefix + "action[{}] IS UNKNOW", action);
		}

		logger.end(function);
		return bContinue;
	}

}
