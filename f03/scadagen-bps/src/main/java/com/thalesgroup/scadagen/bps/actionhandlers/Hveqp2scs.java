package com.thalesgroup.scadagen.bps.actionhandlers;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.thalesgroup.hv.common.HypervisorException;
import com.thalesgroup.hv.data_v1.attribute.AbstractAttributeType;
import com.thalesgroup.hv.data_v1.entity.AbstractEntityStatusesType;
import com.thalesgroup.hv.data_v1.equipment.AbstractEquipmentStatusesType;
import com.thalesgroup.hv.data_v1.operation.AbstractOperationRequestType;
import com.thalesgroup.scadagen.binding.AttributeBinding;
import com.thalesgroup.scadagen.bps.conf.ConfManager;
import com.thalesgroup.scadagen.bps.conf.OperationConfigLoader;
import com.thalesgroup.scadagen.bps.conf.actions.IAction;
import com.thalesgroup.scadagen.bps.conf.binding.data.IData;
import com.thalesgroup.scadagen.bps.conf.operation.CommandParam;
import com.thalesgroup.scadagen.bps.conf.operation.Operation;
import com.thalesgroup.scadagen.bps.connector.operation.GenericOperationConnector;
import com.thalesgroup.scadagen.bps.connector.operation.IGenericOperationConnector;

// This class converts HV attributes into SCADAsoft DBM component write request with JSON parameters
public class Hveqp2scs implements IAction {

	protected static final Logger LOGGER = LoggerFactory.getLogger(Hveqp2scs.class);
	private static final String SCS_PATH_SEPARATOR = ":";

	@Override
	public String getActionId() {
		return this.getClass().getName();
	}

	@Override
	public void execute(IGenericOperationConnector operationConnector, AbstractEntityStatusesType entity,
			Map<String, AbstractAttributeType> attributeMap, String actionConfigId) {
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

	    		CommandParam actionParam = null;
	    		String actionParamValue = null;
	    		for (CommandParam param: operation.getCommandContent().getCommandParam()) {
	    			operationParam.put(param.getParamName(), param.getParamValue());
	    			if (param.getParamName().compareTo("action") == 0) {
	    				actionParam = param;
	    				actionParamValue = param.getParamValue();
	    			}
	    			
	    			LOGGER.trace("OperationEntry operationParam param=[{}] value=[{}]", param.getParamName(), param.getParamValue());
	    		}

	    		//  parse JSON and replace "values" node by run time values
	    		//	{
	    		//		"component":"DbmComponent",
	    	    //		"request":"multiWriteValue",
	    	    //		"parameters":
	    	    //		{
	    	    //			"values":
	    	    //			{
	    	    //				"$dbAddress":"$valueToWrite"
	    	    //			}
	    	    //		}
	    		//	}
	    		//
	    		
				ObjectMapper jsonMapper = new ObjectMapper();
	            ObjectNode actionNode = (ObjectNode) jsonMapper.readTree(actionParamValue);
	            JsonNodeFactory jsonNodeFactory = JsonNodeFactory.instance;
	            JsonNode paramNode = actionNode.get("parameters");
	            Map<String, JsonNode> map = new HashMap<String, JsonNode>();
	            Map<String, JsonNode> timeValueMap = new HashMap<String, JsonNode>();
	            
            	if (entity instanceof AbstractEquipmentStatusesType) {
            		AbstractEquipmentStatusesType eqp = (AbstractEquipmentStatusesType)entity;
            		String eqpId = eqp.getId();
            		LOGGER.trace("eqpId=[{}]", eqpId);

            		if (operationParam.get("entityID") == null) {
            			LOGGER.trace("operationParm entityID is null -> set to [{}]", operationConnector.getTools().getSystemConfiguration().getCurrentInstanceName());
            			operationParam.put("entityID", operationConnector.getTools().getSystemConfiguration().getCurrentInstanceName());
            		}
	                
            		// Get binding id from hv2scs instance mapping
            		String hv2scsBindingId = ConfManager.getScs2HvLoader().getScsEqpOfHVInstancesMap().get(eqpId);
        			if (hv2scsBindingId == null) {
        				LOGGER.warn("Unable to find binding id for HV id {}", eqpId);
        				return;
        			}
        			LOGGER.trace("hv2scsBindingId = {}", hv2scsBindingId);
        			
        			// Get scs eqp path from hv2scs instance mapping
        			String scseqpPath = ConfManager.getScs2HvLoader().getHv2ScsInstancesMap().get(eqpId);
        			if (scseqpPath == null) {
        				LOGGER.warn("Unable to find scs eqp path for HV id {}", eqpId);
        				return;
        			}
        			LOGGER.trace("scseqpPath = {}", scseqpPath);
        			
        			// Get hv2scs class binding by binding ID
        			HashSet<AttributeBinding> bindings = new HashSet<AttributeBinding>(ConfManager.getBindingEngine().getAttributeBindings(hv2scsBindingId));
        			if (bindings == null || bindings.isEmpty()) {
        				LOGGER.warn("Unable to find binding for binding id {}", hv2scsBindingId);
        				return;
        			}
        			LOGGER.trace("Number of attribute bindings for binding id [{}] found = [{}]", hv2scsBindingId, bindings.size());
        			
        			// Go through each attribute binding
        			for (AttributeBinding binding: bindings) {
        				LOGGER.trace("Start process attribute binding [{}]", binding.getId());
        				IData data = ConfManager.getBindingEngine().getScsValue(eqp, eqpId, binding);
        				String dataType = ConfManager.getBindingEngine().getScsValueType(binding);
        				
    
        				LOGGER.trace("data = [{}], dataType = [{}]", data.toString(), dataType);

	        			// Get scs point level db path from attribute binding
	        			String scsPointPath = binding.getId();
	        			String scsdbPath = scseqpPath + SCS_PATH_SEPARATOR + scsPointPath;
	        			
	        			if (dataType.compareTo("DATE") != 0) {
	
		        			// Write to status before value if paramConfig "writeStatus" is set
		        			String statusStr = ActionUtils.getWriteStatus(actionParam);
		        			if (statusStr != null) {
		    					String scsdbStatusPath = scsdbPath.substring(0, scsdbPath.lastIndexOf(".")+1) + statusStr;
				                IntNode statusValueNode = new IntNode(1);
				                map.put(scsdbStatusPath, statusValueNode);
		        			}
		        			
		        			// Check paramConfig "writeTimeValue*"
	    	                Map<String, JsonNode> tMap = ActionUtils.getTimeValues(operationConnector, eqp, actionParam, scsdbPath);
	    	                if (!tMap.isEmpty()) {
	    	                	for (String key: tMap.keySet()) {
	    	                		timeValueMap.put(key, tMap.get(key));
	    	                	}
	    	                }
	
			                map.put(scsdbPath, ActionUtils.getValueNode(data, dataType));
	        			} else {
	        				timeValueMap.put(scsdbPath, ActionUtils.getValueNode(data, dataType));
	        			}
        			}
        			
        			ObjectNode obj = new ObjectNode(jsonNodeFactory, map);
		            ((ObjectNode)paramNode).set("values", obj);
		            
		            if (!timeValueMap.isEmpty()) {
		            	ObjectNode objTimeValue = new ObjectNode(jsonNodeFactory, timeValueMap);
		            	((ObjectNode)paramNode).set("timeValues", objTimeValue);
		            }
		            
        			String newActionString = jsonMapper.writeValueAsString(actionNode);
		            LOGGER.debug("action json str = {}", newActionString);
		            operationParam.put("action", newActionString);
            	}
            	
	    		operationRequest = opConnector.createOperation(operationJavaClassName, operationParam);
	    		if (operationRequest == null) {
	    			LOGGER.debug("Error creating operation using OperationEntry [{}]", actionConfigId);
	    			return;
	    		}
		
		    	if (operationConnector != null) {
		    		if (operation.getCommandContent().isIncludeCorrelationId() != null &&  operation.getCommandContent().isIncludeCorrelationId()) {
		    			UUID correlationId = UUID.randomUUID();
		    			operationConnector.requestOperation(correlationId, operationRequest);
		    		} else {
		    			operationConnector.requestOperation(operationRequest);
		    		}
		    		LOGGER.debug("Sent requestOperation [{}]", actionConfigId);
		    	}

	    		
	    	} else {
	    		LOGGER.warn("OperationEntry [{}] not found", actionConfigId);
	    	}
	    } catch (HypervisorException e) {
	    	LOGGER.debug("Error creating operation using OperationEntry [{}] [{}]", actionConfigId, e);
	    } catch (JsonProcessingException e) {
	    	LOGGER.debug("Error creating operation using OperationEntry [{}] [{}]", actionConfigId, e);
		} catch (IOException e) {
			LOGGER.debug("Error creating operation using OperationEntry [{}] [{}]", actionConfigId, e);
		}
	}
	
}
