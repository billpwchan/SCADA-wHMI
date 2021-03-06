package com.thalesgroup.scadagen.bps.actionhandlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thalesgroup.hv.common.HypervisorException;
import com.thalesgroup.hv.data_v1.alarm.AbstractAlarmType;
import com.thalesgroup.hv.data_v1.attribute.AbstractAttributeType;
import com.thalesgroup.hv.data_v1.entity.AbstractEntityStatusesType;
import com.thalesgroup.hv.data_v1.operation.AbstractOperationRequestType;
import com.thalesgroup.scadagen.bps.conf.OperationConfigLoader;
import com.thalesgroup.scadagen.bps.conf.actions.IAction;
import com.thalesgroup.scadagen.bps.conf.operation.CommandParam;
import com.thalesgroup.scadagen.bps.conf.operation.Operation;
import com.thalesgroup.scadagen.bps.connector.operation.GenericOperationConnector;
import com.thalesgroup.scadagen.bps.connector.operation.IGenericOperationConnector;

public class AlarmAction implements IAction {
	protected static final Logger LOGGER = LoggerFactory.getLogger(AlarmAction.class);

	/**
     * Get the action ID.
     * @return the corresponding class.
     */
	public String getActionId() {
		return getClass().getName();
	}

	/**
     * Get operation configuration and execute
     * @param operationConnector the interface to HV connector
     * @return the corresponding class.
     * @throws HypervisorException if the class is not found or not an action.
     */
	public void execute(IGenericOperationConnector operationConnector, AbstractEntityStatusesType entity,
			Map<String, AbstractAttributeType> attributeMap, String actionConfigId)
	{
	    AbstractOperationRequestType operationRequest = null;
	    GenericOperationConnector opConnector = (GenericOperationConnector)operationConnector;
	    String operationJavaClassName = null;
	    Map<String, String> operationParam = new HashMap<String, String>();
	    
	    LOGGER.debug("Try execute action with config [{}]", actionConfigId);
	    
	    try
	    {
	    	Operation operation = OperationConfigLoader.getInstance().getOperation(actionConfigId);
	    	
	    	if (operation != null) {
	    		LOGGER.trace("Found OperationEntry [{}]", actionConfigId);
	    		
	    		operationJavaClassName = operation.getCommandContent().getOperationJavaClassName();
	    		if (operationJavaClassName == null) {
	    			LOGGER.error("Error getting operationJavaClassName from OperationEntry [{}]", actionConfigId);
	    			return;
	    		}
	    		LOGGER.trace("OperationEntry javaClassName [{}]", operationJavaClassName);
	    		
	    		for (CommandParam param: operation.getCommandContent().getCommandParam()) {
	    			operationParam.put(param.getParamName(), param.getParamValue());
	    			LOGGER.trace("OperationEntry operationParam param=[{}] value=[{}]", param.getParamName(), param.getParamValue());
	    		}
	    		
	    		List<String> equipmentList = new ArrayList<String>();
	    		
	    		if (operation.getEquipmentList() != null && !operation.getEquipmentList().getEquipmentId().isEmpty()) {
	    			equipmentList = operation.getEquipmentList().getEquipmentId();
	    		} else {
    				if (entity instanceof AbstractAlarmType) {
    					AbstractAlarmType alarm = (AbstractAlarmType)entity;
    					if (alarm.getSourceID() != null && alarm.getSourceID().getValue() != null) {
    						equipmentList.add(alarm.getSourceID().getValue());
    					}
    				}
	    		}

	    		for (String id: equipmentList) {

	    			operationParam.put("entityID", id);

		    		operationRequest = opConnector.createOperation(operationJavaClassName, operationParam);
		    		if (operationRequest == null) {
		    			LOGGER.debug("Error creating operation using OperationEntry [{}]", actionConfigId);
		    			return;
		    		}
			
			    	if (operationConnector != null) {
			    		if (operation.getCommandContent().isIncludeCorrelationId() != null && operation.getCommandContent().isIncludeCorrelationId()) {
			    			UUID correlationId = UUID.randomUUID();
			    			operationConnector.requestOperation(correlationId, operationRequest);
			    		} else {
			    			operationConnector.requestOperation(operationRequest);
			    		}
			    		LOGGER.debug("Sent requestOperation [{}]", actionConfigId);
			    	}
	    		}
	    	} else {
	    		LOGGER.warn("OperationEntry [{}] not found", actionConfigId);
	    	}
	    } catch (HypervisorException e) {
	    	LOGGER.debug("Error creating operation using OperationEntry [{}] [{}]", actionConfigId, e);
	    }
	}
}
