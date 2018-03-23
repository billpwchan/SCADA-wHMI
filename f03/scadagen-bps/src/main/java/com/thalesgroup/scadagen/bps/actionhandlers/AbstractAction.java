package com.thalesgroup.scadagen.bps.actionhandlers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thalesgroup.hv.common.HypervisorException;
import com.thalesgroup.hv.data.tools.bean.BeanManipulationException;
import com.thalesgroup.hv.data.tools.bean.IBeanEditor;
import com.thalesgroup.hv.data_v1.attribute.AbstractAttributeType;
import com.thalesgroup.hv.data_v1.entity.AbstractEntityStatusesType;
import com.thalesgroup.hv.data_v1.operation.AbstractOperationRequestType;
import com.thalesgroup.scadagen.bps.conf.OperationConfigLoader;
import com.thalesgroup.scadagen.bps.conf.actions.IAction;
import com.thalesgroup.scadagen.bps.conf.operation.Operation;
import com.thalesgroup.scadagen.bps.connector.operation.GenericOperationConnector;
import com.thalesgroup.scadagen.bps.connector.operation.IGenericOperationConnector;

public abstract class AbstractAction implements IAction {

	protected static final Logger LOGGER = LoggerFactory.getLogger(ActionSCADARequest.class);

	protected String operationType_ = "com.thalesgroup.hv.data_v1.operation.AbstractOperationRequestType";
	protected String operatorID_ = "";
	protected String entityID_ = "";
	protected Map<String, String> operationParam_ = new HashMap<String, String>();
	protected Map<String, Object> operationComplexParam_ = new HashMap<String, Object>();
	protected boolean includeCorrelationId_ = true;
	
	@Override
	public String getActionId() {
		return this.getClass().getName();
	}
	
	@Override
	public void execute(IGenericOperationConnector operationConnector, AbstractEntityStatusesType entity,
			Map<String, AbstractAttributeType> attributeMap, String actionConfigId) {
		
		GenericOperationConnector opConnector = (GenericOperationConnector)operationConnector;

		try {			
			Operation operation = OperationConfigLoader.getInstance().getOperation(actionConfigId);
			
			if (entityID_ == null || entityID_.isEmpty()) {
				LOGGER.error("Error sending operation. EntityID is not set.");
				return;
			}
			operationParam_.put("entityID", entityID_);
			
			if (!operatorID_.isEmpty()) {
				operationParam_.put("operatorID", operatorID_);
			}
			AbstractOperationRequestType operationRequest = opConnector.createOperation(operationType_, operationParam_);
			if (operationRequest == null) {
				LOGGER.debug("Error creating operation using OperationEntry [{}]", actionConfigId);
				return;
			}
			
			if (!operationComplexParam_.isEmpty()) {
				IBeanEditor beanEditor = operationConnector.getTools().getOperationHelper().getBeanEditor();
				beanEditor.setValues(operationRequest, operationComplexParam_);
			}
	
	    	if (operationConnector != null) {	    		
    			if ((operation.getCommandContent().isIncludeCorrelationId() != null && operation.getCommandContent().isIncludeCorrelationId()) ||
    				(operation.getCommandContent().isIncludeCorrelationId() == null && isIncludeCorrelationId())) {
    				UUID correlationId = UUID.randomUUID();
					operationConnector.requestOperation(correlationId, operationRequest);
	    		} else {
	    			operationConnector.requestOperation(operationRequest);
	    		}			
	    		LOGGER.debug("Sent requestOperation [{}]", actionConfigId);
	    	}
		} catch (BeanManipulationException e) {
			LOGGER.error("Error setting operation values", e);
		} catch (HypervisorException e) {
			LOGGER.error("Error creating/sending operation using OperationEntry [{}]. [{}]", actionConfigId, e);
		}
	}

	protected boolean isIncludeCorrelationId() {
		return includeCorrelationId_;
	}
}
