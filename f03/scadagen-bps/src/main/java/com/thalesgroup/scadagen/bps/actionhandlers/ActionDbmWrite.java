package com.thalesgroup.scadagen.bps.actionhandlers;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.thalesgroup.hv.common.HypervisorException;
import com.thalesgroup.hv.data.exception.EntityManipulationException;
import com.thalesgroup.hv.data_v1.attribute.AbstractAttributeType;
import com.thalesgroup.hv.data_v1.attribute.StringAttributeType;
import com.thalesgroup.hv.data_v1.entity.AbstractEntityStatusesType;
import com.thalesgroup.scadagen.binding.AttributeBinding;
import com.thalesgroup.scadagen.bps.conf.ConfManager;
import com.thalesgroup.scadagen.bps.conf.OperationConfigLoader;
import com.thalesgroup.scadagen.bps.conf.binding.data.IData;
import com.thalesgroup.scadagen.bps.conf.common.PropertyType;
import com.thalesgroup.scadagen.bps.conf.operation.Operation;
import com.thalesgroup.scadagen.bps.connector.operation.IGenericOperationConnector;
	
public class ActionDbmWrite extends ActionSCADARequest {

	protected static final Logger LOGGER = LoggerFactory.getLogger(ActionDbmWrite.class);
	private static final String SCS_PATH_SEPARATOR = ":";
	private static final String COMPONENT_NAME = "DbmComponent";
	private static final String REQUEST_NAME = "multiWriteValue";

	@Override
	public void execute(IGenericOperationConnector operationConnector, AbstractEntityStatusesType entity,
			Map<String, AbstractAttributeType> attributeMap, String actionConfigId) {
		
		scsComponentName_ = COMPONENT_NAME;
		scsComponentRequest_ = REQUEST_NAME;
		scsComponentRequestParam_ = getDbmWriteParam(operationConnector, entity, actionConfigId);
		
		super.execute(operationConnector, entity, attributeMap, actionConfigId);
	}

	
	//  return the "values" and "timeValues" node in string 
	//	{
	//		"component":"DbmComponent",
    //		"request":"multiWriteValue",
    //		"parameters":
    //		{
    //			"values":
    //			{
    //				"$dbAddress":"$valueToWrite"
    //			}
	//			"timeValues":
	//			{
	//				"$dbAddress":"$sec $ms"
	//			}
    //		}
	//	}
	//
	private String getDbmWriteParam(IGenericOperationConnector operationConnector,
			 AbstractEntityStatusesType entity, String actionConfigId) {
		String toReturn = "";
		Map<String, JsonNode> map = new HashMap<String, JsonNode>();
        Map<String, JsonNode> timeValueMap = new HashMap<String, JsonNode>();

		String eqpId = getEquipmentId(operationConnector, entity, actionConfigId);
		LOGGER.trace("eqpId=[{}]", eqpId);

		// Get binding id from hv2scs instance mapping
		String hv2scsBindingId = ConfManager.getScs2HvLoader().getScsEqpOfHVInstancesMap().get(eqpId);
		if (hv2scsBindingId == null) {
			LOGGER.warn("Unable to find binding id for HV id {}", eqpId);
			return null;
		}
		LOGGER.trace("hv2scsBindingId = {}", hv2scsBindingId);
		
		// Get scs eqp path from hv2scs instance mapping
		String scseqpPath = ConfManager.getScs2HvLoader().getHv2ScsInstancesMap().get(eqpId);
		if (scseqpPath == null) {
			LOGGER.warn("Unable to find scs eqp path for HV id {}", eqpId);
			return null;
		}
		LOGGER.trace("scseqpPath = {}", scseqpPath);
		
		// Get hv2scs class binding by binding ID
		Collection<AttributeBinding> bindings = ConfManager.getBindingEngine().getAttributeBindings(hv2scsBindingId);
		if (bindings == null || bindings.isEmpty()) {
			LOGGER.warn("Unable to find binding for binding id {}", hv2scsBindingId);
			return null;
		}
		HashSet<AttributeBinding> bindingSet = new HashSet<AttributeBinding>(bindings);
		LOGGER.trace("Number of attribute bindings for binding id [{}] found = [{}]", hv2scsBindingId, bindingSet.size());
		
		// Go through each attribute binding
		for (AttributeBinding binding: bindingSet) {
			LOGGER.trace("Start process attribute binding [{}]", binding.getId());
			IData data = ConfManager.getBindingEngine().getScsValue(entity, eqpId, binding);
			String dataType = ConfManager.getBindingEngine().getScsValueType(binding);				
			LOGGER.trace("data = [{}], dataType = [{}]", data.toString(), dataType);

			// Get scs point level db path from attribute binding
			String scsPointPath = binding.getId();
			String scsdbPath = scseqpPath + SCS_PATH_SEPARATOR + scsPointPath;
			
			if (dataType.compareTo("DATE") != 0) {

				Operation operation = null;
				try {
					operation = OperationConfigLoader.getInstance().getOperation(actionConfigId);
				} catch (HypervisorException e) {
					LOGGER.error("Error getting operation configuration [{}]", e);
				}	
		    	if (operation != null) {
		    		List<PropertyType> configPropertyList = operation.getConfigProperty();
		    		for (PropertyType property: configPropertyList) {
		    			if (property.getName().compareTo("writeStatus") == 0) {
		    				String writeStatusStr = property.getValue();
	    					String scsdbStatusPath = scsdbPath.substring(0, scsdbPath.lastIndexOf(".")+1) + writeStatusStr;
			                IntNode statusValueNode = new IntNode(1);
			                map.put(scsdbStatusPath, statusValueNode);
		    			}
		    			if (property.getName().compareTo("writeDate") == 0) {
		    				String writeDataStr = property.getValue();
		    				long millisec = System.currentTimeMillis();
		    				int sec = (int) (millisec/1000);
		    				int ms = (int) ((millisec % 1000) * 1000);
		    				String timeValueStr = Integer.toString(sec) + " " + Integer.toString(ms);
		    				TextNode node = new TextNode(timeValueStr);
		    				timeValueMap.put(scsdbPath.substring(0, scsdbPath.lastIndexOf(".")+1)+writeDataStr, node);
		    			}
		    		}
		    	}

                map.put(scsdbPath, ActionUtils.getValueNode(data, dataType));
			} else {
				timeValueMap.put(scsdbPath, ActionUtils.getValueNode(data, dataType));
			}
		}
		
		JsonNodeFactory jsonNodeFactory = JsonNodeFactory.instance;			
		ObjectMapper jsonMapper = new ObjectMapper();
		ObjectNode objParam = new ObjectNode(jsonNodeFactory);
		
		if (!map.isEmpty()) {
			ObjectNode objValues = new ObjectNode(jsonNodeFactory, map);
			objParam.set("values", objValues);
		}
		      
        if (!timeValueMap.isEmpty()) {
        	ObjectNode objTimeValues = new ObjectNode(jsonNodeFactory, timeValueMap);
        	objParam.set("timeValues", objTimeValues);
        }
        try {
        	toReturn = jsonMapper.writeValueAsString(objParam);
		} catch (JsonProcessingException e) {
			LOGGER.error("Error creating operation param json string [{}]", e);
		}

        LOGGER.debug("param json str = {}", toReturn);
        
		return toReturn;
	}
	
	private String getEquipmentId(IGenericOperationConnector operationConnector,
			 AbstractEntityStatusesType entity, String actionConfigId) {
		String eqpId = "";
		String hvEqpIdAttributeName = "id";

		Operation operation = null;
		
		try {
			operation = OperationConfigLoader.getInstance().getOperation(actionConfigId);
		} catch (HypervisorException e1) {
			e1.printStackTrace();
		}
		
		if (operation != null) {
    		List<PropertyType> propertyList = operation.getConfigProperty();
    		for (PropertyType property: propertyList) {
    			if (property.getName().compareTo("hvEqpId") == 0) {
    				hvEqpIdAttributeName = property.getValue();
    				LOGGER.trace("hvEqpId=[{}]", hvEqpIdAttributeName);
    			}
    		}
		}

		try {

			StringAttributeType eqpIdAtt = (StringAttributeType) operationConnector.getTools().getDataHelper().getAttribute(entity, hvEqpIdAttributeName);
			eqpId = eqpIdAtt.getValue();
			
		} catch (EntityManipulationException e) {
			LOGGER.error("Error getting equipment id from entity attribute [{}]", e);
		}
        
		return eqpId;
	}
}
