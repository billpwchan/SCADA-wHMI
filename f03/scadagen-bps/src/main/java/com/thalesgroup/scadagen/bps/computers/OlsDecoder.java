package com.thalesgroup.scadagen.bps.computers;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.thalesgroup.hv.data_v1.attribute.AbstractAttributeType;
import com.thalesgroup.hv.data_v1.attribute.StringAttributeType;

public class OlsDecoder extends BpsStatusComputer {

	public static ObjectMapper json_mapper_ = null;
	
	protected static final Logger LOGGER = LoggerFactory.getLogger(OlsDecoder.class);
	
	public OlsDecoder() {
		name_ = this.getClass().getName();
        statusSet_.add("data");
        if (json_mapper_ == null) {
        	json_mapper_ = new ObjectMapper();
            // to prevent exception when encountering unknown property:
            json_mapper_.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            // to allow coercion of JSON empty String ("") to null Object value:
            json_mapper_.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        }
    }
	
	@Override
	public AbstractAttributeType compute(Map<String, AbstractAttributeType> inputStatusByName, Map<String, String>configProperties) {
		StringAttributeType ret = new StringAttributeType();
		
		// get value
		AbstractAttributeType att = inputStatusByName.get("data");
		if (att != null && att instanceof StringAttributeType) {
			String data = ((StringAttributeType)att).getValue();
            // decode json
            ObjectNode root = null;
			try {
				root = (ObjectNode) json_mapper_.readTree(data);
				String olsColumnName = configProperties.get("olsColumnName");
				if (olsColumnName == null) {
					olsColumnName = name_;
				}
				LOGGER.debug("Try to extract value in ols column [{}]", olsColumnName);
				JsonNode v = root.get(olsColumnName);
	            ret.setValue(v.asText());
	            ret.setValid(true);
	            ret.setTimestamp(System.currentTimeMillis());
			} catch (JsonProcessingException e) {
				LOGGER.error("Error processing json data. {}", e);
			} catch (IOException e) {
				LOGGER.error("Error reading json data. {}", e);
			}
		} else {
			LOGGER.warn("Unable to find attribute [data] in input");
		}
		
		return ret;
	}

}
