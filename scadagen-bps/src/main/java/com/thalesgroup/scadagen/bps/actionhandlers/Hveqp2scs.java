package com.thalesgroup.scadagen.bps.actionhandlers;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.FloatNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.thalesgroup.hv.common.HypervisorException;
import com.thalesgroup.hv.data_v1.entity.AbstractEntityStatusesType;
import com.thalesgroup.hv.data_v1.equipment.AbstractEquipmentStatusesType;
import com.thalesgroup.hv.data_v1.operation.AbstractOperationRequestType;
import com.thalesgroup.scadagen.binding.AttributeBinding;
import com.thalesgroup.scadagen.binding.MappedVarBinding;
import com.thalesgroup.scadagen.bps.conf.ConfManager;
import com.thalesgroup.scadagen.bps.conf.OperationConfigLoader;
import com.thalesgroup.scadagen.bps.conf.actions.IAction;
import com.thalesgroup.scadagen.bps.conf.operation.CommandParam;
import com.thalesgroup.scadagen.bps.conf.operation.OperationEntry;
import com.thalesgroup.scadagen.bps.connector.operation.GenericOperationConnector;
import com.thalesgroup.scadagen.bps.connector.operation.IGenericOperationConnector;
import com.thalesgroup.scadagen.bps.data.EntityDataDescriptionAbstract;

// This class is customized to handle SCADAsoft component request request with JSON parameters
public class Hveqp2scs implements IAction {

	protected static final Logger LOGGER = LoggerFactory.getLogger(Hveqp2scs.class);
	private static final String SCS_PATH_SEPARATOR = ":";

	@Override
	public String getActionId() {
		return this.getClass().getName();
	}

	@Override
	public void execute(IGenericOperationConnector operationConnector, Set<EntityDataDescriptionAbstract>desc, String actionConfigId,
			AbstractEntityStatusesType entity) {
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

	    		CommandParam actionParam = null;
	    		String actionParamValue = null;
	    		for (CommandParam param: entry.getCommandContent().getCommandParam()) {
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
        			
        			// Get hv2scs class binding
        			String inputName = eqpId.substring(eqpId.lastIndexOf(":")+1);
        			LOGGER.trace("inputName = {}", inputName);
        			HashSet<AttributeBinding> bindings = new HashSet<AttributeBinding>(ConfManager.getBindingEngine().getAttributeBindings(hv2scsBindingId, inputName));
        			if (bindings == null || bindings.isEmpty()) {
        				LOGGER.warn("Unable to find binding for binding id {} and input {}", hv2scsBindingId, inputName);
        				return;
        			}
        			
        			// Support only 1 to 1 binding for now
        			AttributeBinding binding = bindings.iterator().next();
        			
        			if (!(binding instanceof MappedVarBinding)) {
        				LOGGER.warn("Incompatible binding found for binding id {} and input {}", hv2scsBindingId, inputName);
        				return;
        			}
        			
        			MappedVarBinding mappedBinding = (MappedVarBinding)binding;
        			String mappedValue = mappedBinding.getInputMapping().getMappedValue();
        			String mappedValueType = mappedBinding.getInputMapping().getMappedValueType();
        			
        			// Get scs point level db path from attribute binding
        			String scsPointPath = binding.getId();
        			String scsdbPath = scseqpPath + SCS_PATH_SEPARATOR + scsPointPath;
        			
        			// Write to status before value if paramConfig "writeStatus" is set
        			String statusStr = ActionUtils.getWriteStatus(actionParam);
        			if (statusStr != null) {
    					String scsdbStatusPath = scsdbPath.substring(0, scsdbPath.lastIndexOf(".")+1) + statusStr;
		                IntNode statusValueNode = new IntNode(1);
		                map.put(scsdbStatusPath, statusValueNode);
        			}
	                
	                String valueStr = (String)operationConnector.getTools().getDataHelper().getAttributeValue(eqp, mappedValue);
	                String valueType = (String)operationConnector.getTools().getDataHelper().getAttributeValue(eqp, mappedValueType);
	                LOGGER.trace("valueType=[{}]", valueType);
	                if (valueType.compareTo("integer") == 0) {
	                	IntNode valueNode = new IntNode(Integer.parseInt(valueStr));
	                	map.put(scsdbPath, valueNode);
	                	LOGGER.trace("IntNode value=[{}]", valueStr);
	                } else if (valueType.compareTo("float") == 0) {
	                	FloatNode valueNode = new FloatNode(Float.parseFloat(valueStr));
	                	map.put(scsdbPath, valueNode);
	                	LOGGER.trace("FloatNode value=[{}]", valueStr);
	                } else if (valueType.compareTo("asciistring") == 0 || valueType.compareTo("hexstring") == 0) {
	                	TextNode valueNode = new TextNode(valueStr);
	                	map.put(scsdbPath, valueNode);
	                	LOGGER.trace("TextNode value=[{}]", valueStr);
	                }                 

		            ObjectNode obj = new ObjectNode(jsonNodeFactory, map);
		            ((ObjectNode)paramNode).set("values", obj);
			    	
		            // Check paramConfig "writeTimeValue*"
		            Map<String, JsonNode> timeValueMap = ActionUtils.getTimeValues(operationConnector, eqp, actionParam, scsdbPath);
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
		    		if (entry.getCommandContent().isIncludeCorrelationId()) {
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
