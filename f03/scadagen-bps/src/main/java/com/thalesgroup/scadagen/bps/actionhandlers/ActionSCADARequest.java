package com.thalesgroup.scadagen.bps.actionhandlers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thalesgroup.hv.common.HypervisorException;
import com.thalesgroup.hv.data_v1.attribute.AbstractAttributeType;
import com.thalesgroup.hv.data_v1.entity.AbstractEntityStatusesType;
import com.thalesgroup.hv.data_v1.operation.AbstractOperationRequestType;
import com.thalesgroup.scadagen.bps.conf.actions.IAction;
import com.thalesgroup.scadagen.bps.connector.operation.GenericOperationConnector;
import com.thalesgroup.scadagen.bps.connector.operation.IGenericOperationConnector;

public abstract class ActionSCADARequest implements IAction {
	
	protected static final Logger LOGGER = LoggerFactory.getLogger(ActionSCADARequest.class);

	private final static String OPERATIONTYPE = "com.thalesgroup.scadasoft.data.config.equipment.operation.OpSCADARequest";
	protected String operatorID_ = "";
	protected String entityID_ = "";
	protected String scsComponentName_ = "";
	protected String scsComponentRequest_ = "";
	protected String scsComponentRequestParam_ = "";
	protected Map<String, String> operationParam_ = new HashMap<String, String>();
	protected boolean includeCorrelationId_ = true;
	
	@Override
	public String getActionId() {
		return this.getClass().getName();
	}
	
	@Override
	public void execute(IGenericOperationConnector operationConnector, AbstractEntityStatusesType entity,
			Map<String, AbstractAttributeType> attributeMap, String actionConfigId) {
		
		GenericOperationConnector opConnector = (GenericOperationConnector)operationConnector;
		
		operationParam_.put("operatorID", operatorID_);
		operationParam_.put("entityID", getEntityID(operationConnector));
		operationParam_.put("action", getActionParam());
		
		try {
			AbstractOperationRequestType operationRequest = opConnector.createOperation(OPERATIONTYPE, operationParam_);
			if (operationRequest == null) {
				LOGGER.debug("Error creating operation using OperationEntry [{}]", actionConfigId);
				return;
			}
	
	    	if (operationConnector != null) {	    		
    			if (isIncludeCorrelationId()) {
    				UUID correlationId = UUID.randomUUID();
					operationConnector.requestOperation(correlationId, operationRequest);
	    		} else {
	    			operationConnector.requestOperation(operationRequest);
	    		}			
	    		LOGGER.debug("Sent requestOperation [{}]", actionConfigId);
	    	}
		} catch (HypervisorException e) {
			LOGGER.debug("Error creating/sending operation using OperationEntry [{}]. [{}]", actionConfigId, e);
		}
	}
	
	protected String getEntityID(IGenericOperationConnector operationConnector) {
		return operationConnector.getTools().getSystemConfiguration().getCurrentInstanceName();
	}

	protected String getActionParam() {
		StringBuilder actionParamBuilder = new StringBuilder();
//		actionParamBuilder.append("{&quot;component&quot;:&quot;");
//		actionParamBuilder.append(scsComponentName_);
//		actionParamBuilder.append("&quot;, &quot;request&quot;:&quot;");
//		actionParamBuilder.append(scsComponentRequest_);
//		actionParamBuilder.append("&quot;, &quot;parameters&quot;:");
//		actionParamBuilder.append(scsComponentRequestParam_);
//		actionParamBuilder.append("}");
		actionParamBuilder.append("{\"component\":\"");
		actionParamBuilder.append(scsComponentName_);
		actionParamBuilder.append("\", \"request\":\"");
		actionParamBuilder.append(scsComponentRequest_);
		actionParamBuilder.append("\", \"parameters\":");
		actionParamBuilder.append(scsComponentRequestParam_);
		actionParamBuilder.append("}");
		
		LOGGER.debug("getActionParam returns {}", actionParamBuilder.toString());
		return actionParamBuilder.toString();
	}
	
	protected boolean isIncludeCorrelationId() {
		return includeCorrelationId_;
	}
}
