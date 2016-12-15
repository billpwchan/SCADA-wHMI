package com.thalesgroup.scadagen.wrapper.wrapper.client.opm;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.action.ChangePasswordAction;
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

	private UIWrapperRpcEvent_i uiWrapperRpcEvent_i = null;
	public void changePassword(String oldPass, String newPass, UIWrapperRpcEvent_i uiWrapperRpcEvent_i) {
		String function = "changePassword";
		
		logger.begin(className, function);
		
		this.uiWrapperRpcEvent_i = uiWrapperRpcEvent_i;
	    
        final ChangePasswordAction action = new ChangePasswordAction();
        action.setOldPassword(oldPass);
        action.setNewPassword(newPass);

        operatorActionService_.sendOperatorAction(action, changePwdCb_);
        
        logger.end(className, function);
	    
	}
	
    /**
     * Call back called by the rpc change password.
     */
    private final AsyncCallback<IOperatorActionReturn> changePwdCb_ = new AsyncCallbackMwtAbstract<IOperatorActionReturn>() {

        @Override
        public void onSuccessMwt(final IOperatorActionReturn result) {
        	String function = "onSuccessMwt";
        	
        	logger.begin(className, function);

            if (result instanceof OperatorActionReturn) {

                // Everything is OK
            	
            	uiWrapperRpcEvent_i.CallbackEvent("changePassword", "OperatorActionReturn", null, null, null);
            	
//                hide();
            	
            } else if (result instanceof OperatorActionSecurityFault) {

                // Security exception occurred
                final OperatorActionSecurityFault operatorActionSecurityFault =
                    (OperatorActionSecurityFault) result;
                final SecurityExceptionC se = operatorActionSecurityFault.getSecurityException();
                
                uiWrapperRpcEvent_i.CallbackEvent("changePassword", "onSuccessMwt", "OperatorActionSecurityFault", se.getContext().getAccessedResource(), null);

//                final String errorMsg = Dictionary.getWording(LABEL_KEY_BASE + se.getContext().getAccessedResource());
//
//                logger.error("AbstractChangePassPanel : error : " + errorMsg);
//                showError(Dictionary.getWording(ERROR_PASS_CHANGE) + " " + errorMsg);

            } else if (result instanceof OperatorActionFault) {
                // Other type of exception occurred

                final OperatorActionFault operatorActionFault = (OperatorActionFault) result;
                
                uiWrapperRpcEvent_i.CallbackEvent("changePassword", "onSuccessMwt", "OperatorActionFault", operatorActionFault.getMessage(), null);

//                final String errorMsg = operatorActionFault.getMessage();
//
//                logger.error("AbstractChangePassPanel : error : " + errorMsg);
//                showError(Dictionary.getWording(ERROR_PASS_SERVER) + " " + errorMsg);
            } else {
            	
            	uiWrapperRpcEvent_i.CallbackEvent("changePassword", "onSuccessMwt", "ERROR_UNKNOW", null, null);
            	
//                logger.error("AbstractChangePassPanel : Unknown error.");
//                showError(Dictionary.getWording(ERROR_UNKNOW));
            }
            
            logger.end(className, function);
        }

        @Override
        public void onFailureMwt(final Throwable caught) {
        	String function = "onFailureMwt";
        	
        	logger.begin(className, function);
        	
        	uiWrapperRpcEvent_i.CallbackEvent("changePassword", "onFailureMwt", caught.getMessage(), null, null);
        	
//            logger.error("AbstractChangePassPanel : changePwdCb_onFailure : ", caught);
//            showError(Dictionary.getWording(ERROR_PASS_SERVER) + " " + caught.getMessage());
        	
        	logger.end(className, function);
        }
    };
}