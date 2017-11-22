package com.thalesgroup.scadagen.wrapper.wrapper.client.fascomponent.presenter;

import java.util.Date;

import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.ClientLogger;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.context.IPresenterContext;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.update.IPresenterUpdate;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.view.HypervisorView;
import com.thalesgroup.scadagen.wrapper.wrapper.client.fascomponent.error.FasComponentErrorManager;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.action.ScsComponentActionReturn;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.presenter.AScsComponentPresenterClient;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.update.ScsComponentUpdate;

public abstract class FasComponentPresenterClient<V extends HypervisorView> extends AScsComponentPresenterClient<V> {

	private final ClientLogger s_logger = ClientLogger.getClientLogger();
	
	protected FasComponentPresenterClient(IPresenterContext context, V view) {
		super(context, view);
		// TODO Auto-generated constructor stub
	}

	abstract protected void handleJSONUpdate(String clientKey, String requestName, JSONObject response, int errorCode,
			String errorMessage);
	
	protected void updateError(String clientKey, int errorCode, String message, String requestName) {
        JSONObject resp = new JSONObject();
        resp.put(ScsComponentUpdate.c_JSON_ERRORCODE_ARG, new JSONNumber(errorCode));
        if (message != null) {
            resp.put(ScsComponentUpdate.c_JSON_MESSAGE_ARG, new JSONString(message));
        } else {
            resp.put(ScsComponentUpdate.c_JSON_MESSAGE_ARG,
                    new JSONString(ScsComponentActionReturn.GetErrorText(errorCode)));
        }
        handleJSONUpdate(clientKey, requestName, resp, errorCode, message);
    }
	
	/**
     * {@inheritDoc}
     */
    @Override
    public void update(IPresenterUpdate presUpdate) {

    	if (presUpdate instanceof ScsComponentUpdate) {
            ScsComponentUpdate au = (ScsComponentUpdate) presUpdate;
            s_logger.debug("FasComponentPresenterClient update is:" + au.toString());

            try {

                if (au.getData() != null && JsonUtils.safeToEval(au.getData())) {
                    JSONObject res = JSONParser.parseStrict(au.getData()).isObject();
                    if (res != null) {
                        s_logger.debug("FasComponentPresenterClient update is JSON" + au.getData());
                        int errCode = au.getErrorCode();					// This error code is from HV operation
                        JSONValue err = res.get(ScsComponentUpdate.c_JSON_ERRORCODE_ARG);
                        String componentName = "";
                        if (err != null && err.isNumber() != null) {
                            int connErrCode = (int) err.isNumber().doubleValue();	// This error code is from SCS connector
                            s_logger.debug("FasComponentPresenterClient connector return errorCode=" + connErrCode);
                                
                            JSONValue cn = res.get(ScsComponentUpdate.c_JSON_COMPONENT_ARG);
                            if (cn != null && cn.isString() != null) {
                            	componentName = cn.isString().stringValue();
                            	
                            	// Fix for overlapped error code values between HV operation and error code from SCS connector
                                errCode = FasComponentErrorManager.getInstance().getMappedErrorCode(componentName, connErrCode);
                                
                                s_logger.debug("FasComponentPresenterClient mapped errorCode=" + errCode);
                            } else {
                            	s_logger.warn("FasComponentPresenterClient componentName not found");
                            }
                        }

                        String message = "";
                        JSONValue m = res.get(ScsComponentUpdate.c_JSON_MESSAGE_ARG);
                        if (m != null && m.isString() != null) {
                            message = m.isString().stringValue();
                        }

                        String requestName = "";
                        JSONValue rn = res.get(ScsComponentUpdate.c_JSON_REQUEST_ARG);
                        if (rn != null && rn.isString() != null) {
                            requestName = rn.isString().stringValue();
                        }

                        JSONValue v = res.get(ScsComponentUpdate.c_JSON_RESPONSE_ARG);
                        if (v != null && v.isObject() != null) {
                            handleJSONUpdate(au.getClientKey(), requestName, v.isObject(), errCode, message);
                            Date now = new Date();
                            JSONValue beginTS = res.get(ScsComponentUpdate.c_JSON_REQUEST_TIME_ARG);
                            if (beginTS != null && beginTS.isNumber() != null) {
                                long delay = now.getTime() - (long)beginTS.isNumber().doubleValue();
                                s_logger.info("==PERF== JSON api exec delay " + delay + " ms for : " + componentName + "." + requestName);
                            }
                        } else {
                            if (errCode == ScsComponentActionReturn.c_OK) {
                                s_logger.warn("FasComponentPresenterClient no answer object in JSON:" + au.getData());
                                updateError(au.getClientKey(), c_CANNOT_PARSE_JSON,
                                        "FasComponentPresenterClient no response object in JSON:" + au.getData(),
                                        requestName);
                            } else {
                                // answer may contains a message
                                if (message != null) {
                                    updateError(au.getClientKey(), errCode, message, requestName);
                                } else {
                                    updateError(au.getClientKey(), errCode, au.getData(), requestName);
                                }
                            }
                        }

                    } else {
                        s_logger.warn("FasComponentPresenterClient JSON does eval as an object:" + au.getData());
                        updateError(au.getClientKey(), c_CANNOT_PARSE_JSON,
                                "FasComponentPresenterClient JSON does eval as an object:" + au.getData(), "");
                    }

                } else if (au.getData() == null) {
                    s_logger.warn("FasComponentPresenterClient cannot eval NULL string as JSON.");
                    updateError(au.getClientKey(), au.getErrorCode(), null, "");
                } else {
                    s_logger.warn("FasComponentPresenterClient cannot eval safely as JSON:" + au.getData());
                    updateError(au.getClientKey(), c_CANNOT_PARSE_JSON,
                            "FasComponentPresenterClient cannot eval safely as JSON:" + au.getData(), "");
                }

            } catch (IllegalArgumentException e) {
                s_logger.warn("FasComponentPresenterClient cannot eval as JSON:" + au.getData());
                updateError(au.getClientKey(), c_CANNOT_PARSE_JSON,
                        "FasComponentPresenterClient cannot eval as JSON:" + au.getData(), "");
            }
        } else {
            // update is not a ScsComponentUpdate
            s_logger.warn("FasComponentPresenterClient unknown update type:" + presUpdate.getClass().getName());
        }
    }

}
