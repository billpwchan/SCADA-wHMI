package com.thalesgroup.scadagen.scadagenba.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.thalesgroup.scadagen.scadagenba.services.proxy.ScadagenHILCServicesProxy;
import com.thalesgroup.scadasoft.hvconnector.configuration.SCSConfManager;
import com.thalesgroup.scadasoft.jsoncomponent.AbstractJSComponent;
import com.thalesgroup.scadasoft.jsoncomponent.JSComponentMgr;
import com.thalesgroup.scadasoft.jsoncomponent.SCSJSONParameter;
import com.thalesgroup.scadasoft.jsoncomponent.SCSJSONParameter.SCSJSONParamType;
import com.thalesgroup.scadasoft.jsoncomponent.SCSJSONRequest;

public class HILCComponent extends AbstractJSComponent {
	
	private static final Logger s_logger = LoggerFactory.getLogger(HILCComponent.class);
	private static final String LOG_PREFIX = "[HILCComponent] ";

    private ScadagenHILCServicesProxy m_hilcService = null;

	@Override
	public String getName() {
		return "HILCComponent";
	}

	@Override
	public String getDescription() {
		return "Use this component to send commands to the Scadasoft HILC server";
	}

	/**
     *  Create an instance of HILC services proxy.
     */
	@Override
	protected void doComponentInit() {
		m_hilcService = new ScadagenHILCServicesProxy(SCSConfManager.instance().getRemoteEnv(), "SigHILCServer");
	}

	/**
     * Send HILCPrepationRequest to HILC server
     * 
     * @param req
     *            request object node
     * @param sourceId
     *            id of the source
     * @return object node containing operation result
     */
    @SCSJSONRequest(value = "HILCPreparationRequest", inputParam = {
            @SCSJSONParameter(value = "operatorName", type = SCSJSONParamType.STRING, mandatory = true),
            @SCSJSONParameter(value = "workstationName", type = SCSJSONParamType.STRING, mandatory = true),
            @SCSJSONParameter(value = "cmdType", type = SCSJSONParamType.NUMBER, mandatory = true),
            @SCSJSONParameter(value = "cmdValue", type = SCSJSONParamType.NUMBER, mandatory = true),
            @SCSJSONParameter(value = "cmdValueDiv", type = SCSJSONParamType.NUMBER, mandatory = true),
            @SCSJSONParameter(value = "eqpAlias", type = SCSJSONParamType.STRING, mandatory = true),
            @SCSJSONParameter(value = "eqpType", type = SCSJSONParamType.STRING, mandatory = true),
            @SCSJSONParameter(value = "cmdName", type = SCSJSONParamType.STRING, mandatory = true)})

    public ObjectNode doHILCPreparationRequest(ObjectNode req, String sourceId) {
        if (!m_hilcService.isConnected()) {
            s_logger.error("HILCComponent FAILURE: no connection to HILCServer");
            return buildErrorObject(c_SERVER_NOT_CONNECTED,
                    "HILCComponent FAILURE: no connection to HILCServer");
        }
        // inputs
        String operatorName = this.getStringParam("operatorName", req);
        String workstationName = this.getStringParam("workstationName", req);
        int cmdType = this.getIntParam("cmdType", req);
        int cmdValue = this.getIntParam("cmdValue", req);
        int cmdValueDiv = this.getIntParam("cmdValueDiv", req);
        String eqpAlias = this.getStringParam("eqpAlias", req);
        String eqpType = this.getStringParam("eqpType", req);
        String cmdName = this.getStringParam("cmdName", req);
        
        ObjectNode resp = JSComponentMgr.s_json_factory.objectNode();

        // Call Control Service
        try {
        	//name = m_notifManager.convertEqpIdFromRemoteId(name);
            int operationResult = m_hilcService.hilcPreparationRequest(operatorName, workstationName, cmdType, cmdValue, cmdValueDiv, eqpAlias, eqpType, cmdName);
            if (operationResult != 0) {
                return buildErrorObject(c_UNKNOWN_SERVER_ERROR,
                        "HILCComponent:doHILCPreparationRequest FAILURE: error when calling SCS server");
            }
            resp.put("operationResult", operationResult);
            return resp;
        } catch (Throwable t) {
            s_logger.error(LOG_PREFIX + t.getMessage(), t);
            return buildErrorObject(c_UNKNOWN_SERVER_ERROR,
                    "HILCComponent:HILCComponent:doHILCPreparationRequest FAILURE: error when calling HILCServer: " + t.getMessage());
        }
    }
    
    /**
     * Send HILCConfirmRequest to HILC server
     * 
     * @param req
     *            request object node
     * @param sourceId
     *            id of the source
     * @return object node containing operation result
     */
    @SCSJSONRequest(value = "HILCConfirmRequest", inputParam = {
            @SCSJSONParameter(value = "operatorName", type = SCSJSONParamType.STRING, mandatory = true),
            @SCSJSONParameter(value = "workstationName", type = SCSJSONParamType.STRING, mandatory = true),
            @SCSJSONParameter(value = "cmdType", type = SCSJSONParamType.NUMBER, mandatory = true),
            @SCSJSONParameter(value = "cmdValue", type = SCSJSONParamType.NUMBER, mandatory = true),
            @SCSJSONParameter(value = "cmdValueDiv", type = SCSJSONParamType.NUMBER, mandatory = true),
            @SCSJSONParameter(value = "eqpAlias", type = SCSJSONParamType.STRING, mandatory = true),
            @SCSJSONParameter(value = "eqpType", type = SCSJSONParamType.STRING, mandatory = true),
            @SCSJSONParameter(value = "cmdName", type = SCSJSONParamType.STRING, mandatory = true)})

    public ObjectNode doHILCConfirmRequest(ObjectNode req, String sourceId) {
        if (!m_hilcService.isConnected()) {
            s_logger.error("HILCComponent FAILURE: no connection to HILCServer");
            return buildErrorObject(c_SERVER_NOT_CONNECTED,
                    "HILCComponent FAILURE: no connection to HILCServer");
        }
        // inputs
        String operatorName = this.getStringParam("operatorName", req);
        String workstationName = this.getStringParam("workstationName", req);
        int cmdType = this.getIntParam("cmdType", req);
        int cmdValue = this.getIntParam("cmdValue", req);
        int cmdValueDiv = this.getIntParam("cmdValueDiv", req);
        String eqpAlias = this.getStringParam("eqpAlias", req);
        String eqpType = this.getStringParam("eqpType", req);
        String cmdName = this.getStringParam("cmdName", req);
        
        ObjectNode resp = JSComponentMgr.s_json_factory.objectNode();

        // Call Control Service
        try {
        	//name = m_notifManager.convertEqpIdFromRemoteId(name);
            int operationResult = m_hilcService.hilcConfirmRequest(operatorName, workstationName, cmdType, cmdValue, cmdValueDiv, eqpAlias, eqpType, cmdName);
            if (operationResult != 0) {
                return buildErrorObject(c_UNKNOWN_SERVER_ERROR,
                        "HILCComponent:doHILCConfirmRequest FAILURE: error when calling SCS server");
            }
            resp.put("operationResult", operationResult);
            return resp;
        } catch (Throwable t) {
            s_logger.error(LOG_PREFIX + t.getMessage(), t);
            return buildErrorObject(c_UNKNOWN_SERVER_ERROR,
                    "HILCComponent:HILCComponent:doHILCConfirmRequest FAILURE: error when calling HILCServer: " + t.getMessage());
        }
    }
}
