package com.thalesgroup.scadagen.bps.actionhandlers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thalesgroup.hv.common.HypervisorException;
import com.thalesgroup.hv.data_v1.alarm.AbstractAlarmType;
import com.thalesgroup.hv.data_v1.entity.AbstractEntityStatusesType;
import com.thalesgroup.hv.data_v1.entity.configuration.AbstractConfiguredEntityType;
import com.thalesgroup.hv.data_v1.operation.AbstractOperationRequestType;
import com.thalesgroup.scadagen.bps.conf.HvOperationConfigLoader;
import com.thalesgroup.scadagen.bps.conf.actions.IAction;
import com.thalesgroup.scadagen.bps.conf.hvoperation.CommandParam;
import com.thalesgroup.scadagen.bps.conf.hvoperation.OperationEntry;
import com.thalesgroup.scadagen.bps.connector.operation.GenericOperationConnector;
import com.thalesgroup.scadagen.bps.connector.operation.IGenericOperationConnector;

public class AlarmTypeAction implements IAction {
	protected static final Logger LOGGER = LoggerFactory.getLogger(AlarmTypeAction.class);

	public String getActionId() {
		return getClass().getName();
	}

	public void execute(IGenericOperationConnector operationConnector, String actionConfigId, Set<AbstractEntityStatusesType> entities)
	{
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
	    		
	    		String equipmentType = entry.getEquipmentType();
	    		Set<String> entityIds = new HashSet<String>();
	    		
	    		if (equipmentType == null) {
	    			LOGGER.trace("OperationEntry equipmentType is null");
	    			for (AbstractEntityStatusesType entity: entities) {
	    				if (entity instanceof AbstractAlarmType) {
	    					AbstractAlarmType alarm = (AbstractAlarmType)entity;
	    					if (alarm.getSourceID() != null && alarm.getSourceID().getValue() != null) {
	    						equipmentType = opConnector.getTools().getEquipmentTypeFromId(alarm.getSourceID().getValue());
	    						LOGGER.trace("OperationEntry equipmentType from alarm list [{}]", equipmentType);
	    					}
	    				}
	    			}
	    		}

	    		if (equipmentType == null || equipmentType.isEmpty()) {
	    			LOGGER.error("Cannot get the list of entities of type [" + equipmentType + "].");
	    			return;
	    		}
	    		
	    		try {
					Class<? extends AbstractConfiguredEntityType> clazz = (Class<? extends AbstractConfiguredEntityType>) Class.forName(equipmentType);

					Map<String, ? extends AbstractConfiguredEntityType> entitiesAsMap = opConnector.getTools().getSystemConfiguration().getEntitiesAsMap(clazz);

					entityIds = entitiesAsMap.keySet();
					
					LOGGER.trace("OperationEntry entityIDs [{}]", entityIds.toString());

				} catch (Exception e) {
					LOGGER.error("Cannot get the list of entities of type [" + equipmentType + "].", e);
				}

	    		for (String id: entityIds) {
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
