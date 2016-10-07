package com.thalesgroup.scadagen.bps.actionhandlers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.thalesgroup.hv.data.exception.EntityManipulationException;
import com.thalesgroup.hv.data_v1.equipment.AbstractEquipmentStatusesType;
import com.thalesgroup.scadagen.bps.conf.binding.data.IData;
import com.thalesgroup.scadagen.bps.conf.operation.CommandParam;
import com.thalesgroup.scadagen.bps.conf.operation.ConfigValue;
import com.thalesgroup.scadagen.bps.connector.operation.IGenericOperationConnector;

public class ActionUtils {
	protected static final Logger LOGGER = LoggerFactory.getLogger(ActionUtils.class);

	public static String getWriteStatus(CommandParam param) {
		String statusStr = null;
		List<ConfigValue> configList = param.getParamConfig();
		if (!configList.isEmpty()) {
			for (ConfigValue config: configList) {
				if (config.getName().compareTo("writeStatus")==0) {
					statusStr = config.getValue();
				}
			}
		}
		
		return statusStr;
	}
	
	public static Map<String, JsonNode> getTimeValues(IGenericOperationConnector connector, AbstractEquipmentStatusesType eqp, CommandParam param, String dbPath) {
		Map<String, JsonNode> timeValueMap = new HashMap<String, JsonNode> ();
		
		
		List<ConfigValue>configList = param.getParamConfig();
		if (configList != null && !configList.isEmpty()) {
			Map<String, String> timeValueNameMap = new HashMap<String, String>();
			Map<String, String> timeValueSecMap = new HashMap<String, String>();
			Map<String, String> timeValueMsMap = new HashMap<String, String>();
			
			for (ConfigValue conf: configList) {
				String confName = conf.getName();
				if (confName.matches("writeTimeValue\\d+$")) {
					timeValueNameMap.put(confName, conf.getValue());
				} else if (confName.matches("writeTimeValue\\d+Sec$")) {
					timeValueSecMap.put(confName.substring(0, confName.lastIndexOf("Sec")), conf.getValue());
				} else if (confName.matches("writeTimeValue\\d+Ms$")) {
					timeValueMsMap.put(confName.substring(0, confName.lastIndexOf("Ms")), conf.getValue());
				}
			}
			
			for (String name: timeValueNameMap.keySet()) {
				int sec = (int) (System.currentTimeMillis() / 1000);
				int ms = (int) ((System.currentTimeMillis() % 1000) * 1000);

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

	public static ValueNode getValueNode(IData data, String valueType) {
        if (data != null && valueType != null) {
            if (valueType.compareToIgnoreCase("Integer")==0) {
            	return new IntNode(data.getIntValue());
            } else if (valueType.compareToIgnoreCase("Long")==0) {
            	return new LongNode(data.getLongValue());
            } else if (valueType.compareToIgnoreCase("Float")==0) {
            	return new FloatNode(data.getFloatValue());
            } else if (valueType.compareToIgnoreCase("Double")==0) {
            	return new DoubleNode(data.getDoubleValue());
            } else if (valueType.compareToIgnoreCase("Boolean")==0) {
            	BooleanNode valueNode = BooleanNode.FALSE;
            	if (data.getBooleanValue()) {
            		valueNode = BooleanNode.TRUE;
            	}
            	return valueNode;
            } else if (valueType.compareToIgnoreCase("String")==0) {
            	return new TextNode(data.getStringValue());
            }
        }
        return NullNode.getInstance();
	}

}
