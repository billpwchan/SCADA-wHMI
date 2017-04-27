package com.thalesgroup.scadagen.wrapper.wrapper.server.uigeneric.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JsonUtil {
	
	private static Logger logger = LoggerFactory.getLogger(JsonUtil.class.getName());
	
	public static String getString(ObjectNode on, String f) {
		String s = null;
		JsonNode m = on.get(f);
        if (m != null && m.isTextual()) {
        	s = m.asText();
        } else {
			logger.warn("m[{}] s INVALID", m);
		}
        return s;
	}
	
	public static long getLong(ObjectNode on, String f) {
		long l = -1;
		JsonNode m = on.get(f);
        if (m != null && m.isLong()) {
        	l = m.asLong();
        } else {
			logger.warn("m[{}] l IS INVALID", l);
		}
        return l;
	}
}