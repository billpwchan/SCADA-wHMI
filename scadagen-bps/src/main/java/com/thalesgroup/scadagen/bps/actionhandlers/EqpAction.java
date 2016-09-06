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
import com.thalesgroup.hv.data_v1.entity.AbstractEntityStatusesType;
import com.thalesgroup.hv.data_v1.equipment.AbstractEquipmentStatusesType;
import com.thalesgroup.hv.data_v1.operation.AbstractOperationRequestType;
import com.thalesgroup.scadagen.bps.conf.HvOperationConfigLoader;
import com.thalesgroup.scadagen.bps.conf.actions.IAction;
import com.thalesgroup.scadagen.bps.conf.hvoperation.CommandParam;
import com.thalesgroup.scadagen.bps.conf.hvoperation.OperationEntry;
import com.thalesgroup.scadagen.bps.connector.operation.GenericOperationConnector;
import com.thalesgroup.scadagen.bps.connector.operation.IGenericOperationConnector;

public class EqpAction implements IAction {

	protected static final Logger LOGGER = LoggerFactory.getLogger(EqpAction.class);

	@Override
	public String getActionId() {
		return this.getClass().getName();
	}

	@Override
	public void execute(IGenericOperationConnector operationConnector, String actionConfigId,
			Set<AbstractEntityStatusesType> entities) {
		AbstractOperationRequestType operationRequest = null;
	    GenericOperationConnector opConnector = (GenericOperationConnector)operationConnector;
	    String operationJavaClassName = null;
	    Map<String, String> operationParam = new HashMap<String, String>();
	    
	    LOGGER.debug("Try execute action with config [{}]", actionConfigId);
	    
	    try
	    {
	    	OperationEntry entry = HvOperationConfigLoader.getInstance().getOperationEntry(actionConfigId);
	    	
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

	    			for (AbstractEntityStatusesType entity: entities) {
	    				if (entity instanceof AbstractEquipmentStatusesType) {
	    					AbstractEquipmentStatusesType eqp = (AbstractEquipmentStatusesType)entity;
	    					if (eqp.getId() != null) {
	    						equipmentList.add(eqp.getId());
	    					}
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
