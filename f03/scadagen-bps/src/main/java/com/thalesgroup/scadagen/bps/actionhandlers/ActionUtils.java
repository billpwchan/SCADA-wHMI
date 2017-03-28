package com.thalesgroup.scadagen.bps.actionhandlers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.DoubleNode;
import com.fasterxml.jackson.databind.node.FloatNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.LongNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import com.thalesgroup.hv.common.HypervisorException;
import com.thalesgroup.hv.data.exception.EntityManipulationException;
import com.thalesgroup.hv.data_v1.attribute.AbstractAttributeType;
import com.thalesgroup.hv.data_v1.entity.AbstractEntityStatusesType;
import com.thalesgroup.hv.data_v1.equipment.AbstractEquipmentStatusesType;
import com.thalesgroup.scadagen.bps.conf.binding.data.IData;
import com.thalesgroup.scadagen.bps.conf.common.PropertyType;
import com.thalesgroup.scadagen.bps.conf.operation.CommandParam;
import com.thalesgroup.scadagen.bps.connector.operation.IGenericOperationConnector;
import com.thalesgroup.scadagen.bps.data.ConfiguredEntityStatusesInstancesDataDescription;
import com.thalesgroup.scadagen.bps.data.ConfiguredEntityStatusesTypeDataDescription;
import com.thalesgroup.scadagen.bps.data.EntityDataDescriptionAbstract;
import com.thalesgroup.scadagen.bps.data.TransientEntityDataDescription;

public class ActionUtils {
	protected static final Logger LOGGER = LoggerFactory.getLogger(ActionUtils.class);

	public static String getWriteStatus(CommandParam param) {
		String statusStr = null;
		List<PropertyType> configList = param.getConfigProperty();
		for (PropertyType config: configList) {
			if (config.getName().compareTo("writeStatus")==0) {
				statusStr = config.getValue();
			}
		}
		return statusStr;
	}
	
	public static Map<String, JsonNode> getTimeValues(IGenericOperationConnector connector, AbstractEquipmentStatusesType eqp, CommandParam param, String dbPath) {
		Map<String, JsonNode> timeValueMap = new HashMap<String, JsonNode> ();
		
		
		List<PropertyType>propertyList = param.getConfigProperty();
		if (propertyList != null && !propertyList.isEmpty()) {
			Map<String, String> timeValueNameMap = new HashMap<String, String>();
			Map<String, String> timeValueSecMap = new HashMap<String, String>();
			Map<String, String> timeValueMsMap = new HashMap<String, String>();
			
			for (PropertyType property: propertyList) {
				String propertyName = property.getName();
				if (propertyName.matches("writeTimeValue\\d+$")) {
					timeValueNameMap.put(propertyName, property.getValue());
				} else if (propertyName.matches("writeTimeValue\\d+Sec$")) {
					timeValueSecMap.put(propertyName.substring(0, propertyName.lastIndexOf("Sec")), property.getValue());
				} else if (propertyName.matches("writeTimeValue\\d+Ms$")) {
					timeValueMsMap.put(propertyName.substring(0, propertyName.lastIndexOf("Ms")), property.getValue());
				}
			}
			
			for (String name: timeValueNameMap.keySet()) {
				long millisec = System.currentTimeMillis();
				int sec = (int) (millisec/1000);
				int ms = (int) ((millisec % 1000) * 1000);

				String timeValueName = timeValueNameMap.get(name);
				String timeValueSec = timeValueSecMap.get(name);
				String timeValueMs = timeValueMsMap.get(name);
				
				LOGGER.trace("timeValueName {}", timeValueName);
				LOGGER.trace("timeValueSec {}", timeValueSec);
				LOGGER.trace("timeValueMs {}", timeValueMs);

				if (timeValueSec != null) {
					// Try retrieve sec and ms value from entity
					try {
						sec = (Integer)connector.getTools().getDataHelper().getAttributeValue(eqp, timeValueSec);
					
						if (timeValueMs != null) {
							ms = (Integer)connector.getTools().getDataHelper().getAttributeValue(eqp, timeValueMs);
						} else {
							ms = 0;
						}
					} catch (EntityManipulationException e) {
						LOGGER.error("Unable to get timeValue for {}. {}", name, e);
					}
				}
				String timeValueStr = Integer.toString(sec) + " " + Integer.toString(ms);
				TextNode node = new TextNode(timeValueStr);
				timeValueMap.put(dbPath.substring(0, dbPath.lastIndexOf(".")+1)+timeValueName, node);
			}
		}
		
		return timeValueMap;
	}

	public static ValueNode getValueNode(IData data, String dataType) {
        if (data != null && dataType != null) {            
            if (dataType.compareTo("INT") == 0) {
            	return new IntNode(data.getIntValue());
    		} else if (dataType.compareTo("LONG") == 0) {
            	return new LongNode(data.getLongValue());
            } else if (dataType.compareTo("FLOAT") == 0) {
            	return new FloatNode(data.getFloatValue());
            } else if (dataType.compareTo("DOUBLE") == 0) {
            	return new DoubleNode(data.getDoubleValue());
            } else if (dataType.compareTo("STRING") == 0) {
            	return new TextNode(data.getStringValue());
            } else if (dataType.compareTo("BOOL") == 0) {
            	return data.getBooleanValue()?BooleanNode.getTrue():BooleanNode.getFalse();
            } else if (dataType.compareTo("DATE") == 0) {
            	long millisec = data.getDateValue().getTime();
            	StringBuilder scsTimeBuilder = new StringBuilder();
            	scsTimeBuilder.append(millisec/1000);
            	scsTimeBuilder.append(" ");
            	scsTimeBuilder.append((millisec%1000)*1000);
            	return new TextNode(scsTimeBuilder.toString());
            }
        }
        return NullNode.getInstance();
	}
	
	public static Set<String> getAttributeNamesFromConfig(IGenericOperationConnector operationConnector, AbstractEntityStatusesType entity, Set<EntityDataDescriptionAbstract>descSet) throws HypervisorException {

		for (EntityDataDescriptionAbstract desc: descSet) {
			if (desc instanceof ConfiguredEntityStatusesTypeDataDescription) {
				ConfiguredEntityStatusesTypeDataDescription cfgDesc = (ConfiguredEntityStatusesTypeDataDescription)desc;
				if (cfgDesc.getEntityType().compareTo(operationConnector.getTools().getEquipmentTypeFromId(entity.getId())) == 0) {		
					return cfgDesc.getStatusesName();
				}
			} else if (desc instanceof ConfiguredEntityStatusesInstancesDataDescription) {
				ConfiguredEntityStatusesInstancesDataDescription cfgDesc = (ConfiguredEntityStatusesInstancesDataDescription)desc;
				if (cfgDesc.getEntityIds().contains(entity.getId())) {
					return cfgDesc.getStatusesName();
				}
			} else if (desc instanceof TransientEntityDataDescription) {
				TransientEntityDataDescription transDesc = (TransientEntityDataDescription)desc;
				return operationConnector.getTools().getDataHelper().getDeclaredStatusNames(transDesc.getType());
			}
		}
		
		return null;
	}
	
	public static Map<String, AbstractAttributeType> getAttributeMapFromConfig(IGenericOperationConnector operationConnector, AbstractEntityStatusesType entity, Set<EntityDataDescriptionAbstract>descSet) throws HypervisorException {

		Map<String, AbstractAttributeType> attMap = new HashMap<String, AbstractAttributeType>();
		for (EntityDataDescriptionAbstract desc: descSet) {
			if (desc instanceof ConfiguredEntityStatusesTypeDataDescription) {
				ConfiguredEntityStatusesTypeDataDescription cfgDesc = (ConfiguredEntityStatusesTypeDataDescription)desc;
				if (cfgDesc.getEntityType().compareTo(operationConnector.getTools().getEquipmentTypeFromId(entity.getId())) == 0) {		
					for (String attName : cfgDesc.getStatusesName()) {
						AbstractAttributeType att = operationConnector.getTools().getDataHelper().getAttribute(entity, attName);
						if (att != null) {
							attMap.put(attName, att);
						}
					}
				}
			} else if (desc instanceof ConfiguredEntityStatusesInstancesDataDescription) {
				ConfiguredEntityStatusesInstancesDataDescription cfgDesc = (ConfiguredEntityStatusesInstancesDataDescription)desc;
				if (cfgDesc.getEntityIds().contains(entity.getId())) {
					for (String attName : cfgDesc.getStatusesName()) {
						AbstractAttributeType att = operationConnector.getTools().getDataHelper().getAttribute(entity, attName);
						if (att != null) {
							attMap.put(attName, att);
						}
					}
				}
			} else if (desc instanceof TransientEntityDataDescription) {
				TransientEntityDataDescription transDesc = (TransientEntityDataDescription)desc;
				Set<String> attributeNames = operationConnector.getTools().getDataHelper().getDeclaredStatusNames(transDesc.getType());
				for (String attName: attributeNames) {
					AbstractAttributeType att = operationConnector.getTools().getDataHelper().getAttribute(entity, attName);
					if (att != null) {
						attMap.put(attName, att);
					}
				}
			}
		}
		
		return attMap;
	}

}
