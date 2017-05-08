package com.thalesgroup.scadagen.wrapper.wrapper.client.fascomponent.hilc;

import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.ClientLogger;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.context.PresenterInitContext;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.presenter.AScsComponentPresenterClient;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.update.ScsComponentUpdate;

public class FasHILCComponentAccess extends AScsComponentPresenterClient<IHILCComponentClient> {

	private final ClientLogger s_logger = ClientLogger.getClientLogger();

    private IHILCComponentClient m_view;	

    /**
     * Constructor.
     * 
     * @param view
     *            The interface which provides callback for each request method
     */
    public FasHILCComponentAccess(final IHILCComponentClient view) {
        super(new PresenterInitContext(), view);
        s_logger.debug("Create FasHILCComponentAccess");
        m_view = view;

        m_jsdata = new JSONObject();
        m_jsdata.put(ScsComponentUpdate.c_JSON_COMPONENT_ARG, new JSONString("HILCComponent"));

        m_view.setPresenter(this);
    }
    
    /**
     * 
     * Send HILCPreparationRequest to HILC server
     * 
     * @param key
     *            the id used by client to recognize the response
     * @param scsEnvId
     *            Specifies the environment name that supports the Database
     * @param name
     *            Variable name
     * @param status
     *            Status value.
     * @param value
     *            New variable value
     */
    public void hilcPreparationRequest(String key, String scsEnvId, String operatorName, String workstationName, int cmdType,
    		int cmdValue, int cmdValueDiv, String eqpAlias, String eqpType, String cmdName) {
        s_logger.debug("FasHILCComponentAccess hilcPreparationRequest: " + key + ", " + scsEnvId + ", " + operatorName + ", "
                + workstationName + ", " + cmdValue + ", " + cmdValueDiv + ", " + eqpAlias + ", " + eqpType + ", " + cmdName);
        JSONObject jsparam = new JSONObject();

        // build param list
        jsparam.put("operatorName", new JSONString(operatorName));
        jsparam.put("workstationName", new JSONString(workstationName));
        jsparam.put("cmdType", new JSONNumber(cmdType));
        jsparam.put("cmdValue", new JSONNumber(cmdValue));
        jsparam.put("cmdValueDiv", new JSONNumber(cmdValueDiv));
        jsparam.put("eqpAlias", new JSONString(eqpAlias));
        jsparam.put("eqpType", new JSONString(eqpType));
        jsparam.put("cmdName", new JSONString(cmdName));

        JSONObject jsdata = buildJSONRequest("HILCPreparationRequest", jsparam);

        sendJSONRequest(key, scsEnvId, jsdata);
    }

    private void sendHILCPreparationResult(String clientKey, JSONObject response, int errorCode,
            String errorMessage) {
        m_view.setHILCPreparationResult(clientKey, errorCode, errorMessage);
    }
    
    /**
     * 
     * Send HILCConfirmRequest to HILC server
     * 
     * @param key
     *            the id used by client to recognize the response
     * @param scsEnvId
     *            Specifies the environment name that supports the Database
     * @param name
     *            Variable name
     * @param status
     *            Status value.
     * @param value
     *            New variable value
     */
    public void hilcConfirmRequest(String key, String scsEnvId, String operatorName, String workstationName, int cmdType,
    		int cmdValue, int cmdValueDiv, String eqpAlias, String eqpType, String cmdName) {
        s_logger.debug("FasHILCComponentAccess hilcConfirmRequest: " + key + ", " + scsEnvId + ", " + operatorName + ", "
                + workstationName + ", " + cmdValue + ", " + cmdValueDiv + ", " + eqpAlias + ", " + eqpType + ", " + cmdName);
        JSONObject jsparam = new JSONObject();

        // build param list
        jsparam.put("operatorName", new JSONString(operatorName));
        jsparam.put("workstationName", new JSONString(workstationName));
        jsparam.put("cmdType", new JSONNumber(cmdType));
        jsparam.put("cmdValue", new JSONNumber(cmdValue));
        jsparam.put("cmdValueDiv", new JSONNumber(cmdValueDiv));
        jsparam.put("eqpAlias", new JSONString(eqpAlias));
        jsparam.put("eqpType", new JSONString(eqpType));
        jsparam.put("cmdName", new JSONString(cmdName));

        JSONObject jsdata = buildJSONRequest("HILCConfirmRequest", jsparam);

        sendJSONRequest(key, scsEnvId, jsdata);
    }

    private void sendHILCConfirmResult(String clientKey, JSONObject response, int errorCode,
            String errorMessage) {
        m_view.setHILCConfirmResult(clientKey, errorCode, errorMessage);
    }

	@Override
	protected void handleJSONUpdate(String clientKey, String requestName, JSONObject response, int errorCode,
			String errorMessage) {
		
		// Replace HV connector SDK error message with more meaningful message
		if (errorMessage.startsWith("Error: Error when requesting operation request with correlation id")) {
			errorMessage = "Connector connection issue or equipment not found";
		}
		
		s_logger.debug("FasHILCComponentAccess handleJSONUpdate for: " + requestName);
        if ("HILCPreparationRequest".equals(requestName)) {
        	sendHILCPreparationResult(clientKey, response, errorCode, errorMessage);

        } else if ("HILCConfirmRequest".equals(requestName)) {
        	sendHILCConfirmResult(clientKey, response, errorCode, errorMessage);
        }
		
	}
}
