package com.thalesgroup.scadagen.wrapper.wrapper.client.opm;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.monitor.AsyncCallbackMwtAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.security.client.OperatorActionSecurityFault;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.security.client.SecurityExceptionC;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.rpc.IOperatorActionService;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.rpc.IOperatorActionServiceAsync;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.rpc.action.IOperatorActionReturn;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.rpc.action.OperatorActionFault;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.rpc.action.OperatorActionReturn;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.wrapper.wrapper.shared.SCADAgenChangePasswordAction;

public class SpringChangePassword {
	
	private final String className = UIWidgetUtil.getClassSimpleName(SpringChangePassword.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	public static final String OPERATOR_ACTION_SERVLET_URL = "OperatorAction";
	
	/** Operation action service */
	private final IOperatorActionServiceAsync operatorActionService_ = GWT.create(IOperatorActionService.class);
	
	public SpringChangePassword() {
        final ServiceDefTarget oaEndPoint = (ServiceDefTarget) operatorActionService_;
        final String oaServletURL = GWT.getModuleBaseURL() + SpringChangePassword.OPERATOR_ACTION_SERVLET_URL;
        oaEndPoint.setServiceEntryPoint(oaServletURL);
	}

	public void changePassword(String userId, String oldPass, String newPass, final UIWrapperRpcEvent_i uiWrapperRpcEvent_i) {
		String function = "changePassword";
		
		logger.begin(className, function);
			    
        final SCADAgenChangePasswordAction action = new SCADAgenChangePasswordAction();
        action.setUserId(userId);
        action.setOldPassword(oldPass);
        action.setNewPassword(newPass);
        
        operatorActionService_.sendOperatorAction(action, new AsyncCallbackMwtAbstract<IOperatorActionReturn>() {

			@Override
			public void onSuccessMwt(IOperatorActionReturn result) {
				String function = "onSuccessMwt";
				// TODO Auto-generated method stub
	            if (result instanceof OperatorActionReturn) {

	                // Everything is OK
	            	JSONObject jsobject = new JSONObject();
	            	jsobject.put("function", new JSONString(function));
	            	jsobject.put("resultinstanceof", new JSONString("OperatorActionReturn"));

	            	uiWrapperRpcEvent_i.event(jsobject);
	            	
	            } else if (result instanceof OperatorActionSecurityFault) {
	            	
	            	// Security exception occurred
	                final OperatorActionSecurityFault operatorActionSecurityFault = (OperatorActionSecurityFault) result;
	                final SecurityExceptionC se = operatorActionSecurityFault.getSecurityException();
	                final String errorMsg = se.getContext().getAccessedResource();

	            	JSONObject jsobject = new JSONObject();
	            	jsobject.put("function", new JSONString(function));
	            	jsobject.put("resultinstanceof", new JSONString("OperatorActionSecurityFault"));
	            	jsobject.put("getmessage", new JSONString(errorMsg));
	            	
	            	uiWrapperRpcEvent_i.event(jsobject);
	            	
	            } else if (result instanceof OperatorActionFault) {
	            	
	            	// Other type of exception occurred
	                final OperatorActionFault operatorActionFault = (OperatorActionFault) result;
	                final String errorMsg = operatorActionFault.getMessage();
	            	
	            	JSONObject jsobject = new JSONObject();
	            	jsobject.put("function", new JSONString(function));
	            	jsobject.put("resultinstanceof", new JSONString("OperatorActionFault"));
	            	jsobject.put("getmessage", new JSONString(errorMsg));
	            	
	            	uiWrapperRpcEvent_i.event(jsobject);
	            	
	            } else {
	            	
	            	JSONObject jsobject = new JSONObject();
	            	jsobject.put("function", new JSONString(function));
	            	jsobject.put("resultinstanceof", new JSONString("ERROR_UNKNOW"));
	            	
	            	uiWrapperRpcEvent_i.event(jsobject);
	            }
			}
        	
			@Override
			public void onFailureMwt(Throwable caught) {
				String function = "onFailureMwt";
				// TODO Auto-generated method stub
            	JSONObject jsobject = new JSONObject();
            	jsobject.put("function", new JSONString(function));
            	jsobject.put("getmessage", new JSONString(caught.getMessage()));
            	
            	uiWrapperRpcEvent_i.event(jsobject);
			}
        });
        logger.end(className, function);
	}
	
}