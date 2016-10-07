package com.thalesgroup.scadagen.bps.actionhandlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thalesgroup.hv.common.HypervisorException;
import com.thalesgroup.hv.data_v1.alarm.AbstractAlarmType;
import com.thalesgroup.hv.data_v1.entity.AbstractEntityStatusesType;
import com.thalesgroup.hv.data_v1.operation.AbstractOperationRequestType;
import com.thalesgroup.scadagen.bps.conf.OperationConfigLoader;
import com.thalesgroup.scadagen.bps.conf.actions.IAction;
import com.thalesgroup.scadagen.bps.conf.operation.CommandParam;
import com.thalesgroup.scadagen.bps.conf.operation.OperationEntry;
import com.thalesgroup.scadagen.bps.connector.operation.GenericOperationConnector;
import com.thalesgroup.scadagen.bps.connector.operation.IGenericOperationConnector;
import com.thalesgroup.scadagen.bps.data.EntityDataDescriptionAbstract;

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
	public void execute(IGenericOperationConnector operationConnector, Set<EntityDataDescriptionAbstract>desc, String actionConfigId, AbstractEntityStatusesType entity)
	{
	    AbstractOperationRequestType operationRequest = null;
	    GenericOperationConnector opConnector = (GenericOperationConnector)operationConnector;
	    String operationJavaClassName = null;
	    Map<String, String> operationParam = new HashMap<String, String>();
	    
	    LOGGER.debug("Try execute action with config [{}]", actionConfigId);
	    
	    try
	    {
	    	OperationEntry entry = OperationConfigLoader.getInstance().getOperationEntry(actionConfigId);
	    	
	    	if (entry != null) {
	    		LOGGER.trace("Found OperationEntry [{}]", actionConfigId);
	    		
	    		operationJavaClassName = entry.getCommandContent().getOperationJavaClassName();
	    		if (operationJavaClassName == null) {
	    			LOGGER.error("Error getting operationJavaClassName from OperationEntry [{}]", actionConfigId);
	    			return;
	    		}
	    		LOGGER.trace("OperationEntry javaClassName [{}]", operationJavaClassName);
	    		
	    		for (CommandParam param: entry.getCommandContent().getCommandParam()) {
	    			operationParam.put(param.getParamName(), param.getParamValue());
	    			LOGGER.trace("OperationEntry operationParam param=[{}] value=[{}]", param.getParamName(), param.getParamValue());
	    		}
	    		
	    		List<String> equipmentList = new ArrayList<String>();
	    		
	    		if (entry.getEquipmentList() != null && !entry.getEquipmentList().getEquipmentId().isEmpty()) {
	    			equipmentList = entry.getEquipmentList().getEquipmentId();
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
			    		if (entry.getCommandContent().isIncludeCorrelationId()) {
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
