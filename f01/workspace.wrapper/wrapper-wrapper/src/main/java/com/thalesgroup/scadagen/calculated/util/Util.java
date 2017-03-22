package com.thalesgroup.scadagen.calculated.util;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thalesgroup.hypervisor.mwt.core.util.config.loader.IConfigLoader;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.data.server.rpc.implementation.ServicesImplFactory;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.AttributeClientAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.BooleanAttribute;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.CoordinatesAttribute;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.DateTimeAttribute;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.DoubleAttribute;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.FloatAttribute;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.IntAttribute;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.LongAttribute;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.MapStringByStringAttribute;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.StringAttribute;

public class Util {
	
	private final Logger logger = LoggerFactory.getLogger(Util.class.getName());
	
	private String prefix = "";
	public void setPrefix(String prefix) { this.prefix = prefix; }
	
	public Map<String, String> loadMapping(String className) {
		final String function = "loadStringValue";
		logger.debug("{} {} Begin", function, className);
		Map<String, String> mappings	= new HashMap<String, String>();
    	IConfigLoader configLoader		= ServicesImplFactory.getInstance().getService(IConfigLoader.class);
		Map<String,String> properties	= configLoader.getProjectConfigurationMap();
		if (properties != null) {
			int classNameLength = className.length();
			// Load all setting with class prefix into buffer
			for ( String key : properties.keySet() ) {
//				logger.debug("[{}] properties.keySet() key[{}]", new Object[]{logPrefix, key});
				if ( key.startsWith(className) ) {
					String keyname = key.substring(classNameLength);
					mappings.put(keyname, properties.get(key));
//					logger.debug("[{}] keyname[{}] properties.get(key)[{}]", new Object[]{logPrefix, keyname, properties.get(key)});
				}
			}
		} else {
			logger.warn("[{}] properties IS NULL", prefix);
		}
		logger.debug("{} {} End", function, className);
		return mappings;
	}
	
	public String loadStringValue(Map<String, AttributeClientAbstract<?>> inputStatusByName, String inValue) {
		final String function = "loadStringValue";
		logger.debug("{} {} Begin", function, prefix);
    	String outValue = null;
    	AttributeClientAbstract<?> obj1 = inputStatusByName.get(inValue);
    	if (obj1 != null && obj1 instanceof StringAttribute) {
    		outValue = ((StringAttribute) obj1).getValue();
    	} else {
    		logger.warn("{} {} inValue[{}] IS INVALID", new Object[]{function, prefix, inValue});
    		if ( logger.isDebugEnabled() ) checkAttributeType(inputStatusByName, inValue);
    	}
    	logger.debug("{} {} outValue[{}]", new Object[]{function, prefix, outValue});
    	logger.debug("{} {} End", prefix, function);
    	return outValue;
	}
	
	public int loadIntValue(Map<String, AttributeClientAbstract<?>> inputStatusByName, String inValue) {
		final String function = "loadIntValue";
		logger.debug("{} {} Begin", function, prefix);
    	int outValue = 0;
    	AttributeClientAbstract<?> obj1 = inputStatusByName.get(inValue);
    	if (obj1 != null && obj1 instanceof IntAttribute) {
    		outValue = ((IntAttribute) obj1).getValue();
    	} else {
    		logger.warn("{} {} inValue[{}] IS INVALID", new Object[]{function, prefix, inValue});
    		if ( logger.isDebugEnabled() ) checkAttributeType(inputStatusByName, inValue);
    	}
    	logger.debug("{} {} outValue[{}]", new Object[]{function, prefix, outValue});
    	logger.debug("{} {} End", prefix, function);
    	return outValue;
	}
	
	public String getConfigPrefixMappingValue(Map<String, String> mappings, String configPrefix, String fieldname) {
		final String function = "getConfigPrefixMappingValue";
		logger.debug("{} {} Begin", function, prefix);
    	String outValue = null;
    	{
    		String keyToMap = configPrefix+fieldname;
    		logger.debug("{} {} keyToMap[{}]", new Object[]{function, prefix, keyToMap});
	    	outValue = mappings.get(keyToMap);
    	}
    	logger.debug("{} {} outValue[{}]", new Object[]{function, prefix, outValue});
    	logger.debug("{} {} End", prefix, function);
    	return outValue;
	}
	
	public String getInputStatusByConfigPrefixMappingStringValue(Map<String, String> mappings, Map<String, AttributeClientAbstract<?>> inputStatusByName, String configPrefix, String fieldname) {
		final String function = "getInputStatusByConfigPrefixMappingStringValue";
		logger.debug("{} {} Begin", function, prefix);
		
    	String inValue = getConfigPrefixMappingValue(mappings, configPrefix, fieldname);
    	logger.debug("{} {} inValue[{}]", new Object[]{function, prefix, inValue});

    	String outValue = loadStringValue(inputStatusByName, inValue);
    	logger.debug("{} {} outValue[{}]", new Object[]{function, prefix, outValue});
    	
    	logger.debug("{} {} End", function, prefix);
    	return outValue;
	}
	
	public int getInputStatusByConfigPrefixMappingIntValue(Map<String, String> mappings, Map<String, AttributeClientAbstract<?>> inputStatusByName, String configPrefix, String fieldname) {
		final String function = "getInputStatusByConfigPrefixMappingIntValue";
		logger.debug("{} {} Begin", function, prefix);
		
    	String inValue = null;
    	inValue = getConfigPrefixMappingValue(mappings, configPrefix, fieldname);
    	logger.debug("{} {} inValue[{}]", new Object[]{function, prefix, inValue});

    	int outValue = loadIntValue(inputStatusByName, inValue);
    	logger.debug("{} {} outValue[{}]", new Object[]{function, prefix, outValue});
    	
    	logger.debug("{} {} End", function, prefix);
    	return outValue;
	}
	
	public String getConfigPrefix(Map<String, String> mappings, String propertiesname, String inValue1) {
    	String configPrefix = propertiesname;
    	if ( null != inValue1 ) {
    		String keyToFind = propertiesname+"."+inValue1;
    		for ( String key : mappings.keySet() ) {
    			if ( key.startsWith(keyToFind)) {
    				configPrefix = keyToFind;
    				break;
    			}
    		}
    	}
    	logger.debug("compute configPrefix[{}]", configPrefix);
    	return configPrefix;
	}
	
	public String findMappingStringValue(Map<String, String> mappings, String mappingname, String configPrefix, int inValue) {
		final String function = "findMappingStringValue";
		logger.debug("{} {} Begin", function, prefix);
		String outValue = null;
		{
			String keyToMap = null;
			try {
				keyToMap = configPrefix+mappingname+Integer.toString(inValue);
			} catch ( Exception e ) {
				logger.warn("{} {} Integer.toString Exception[{}]", new Object[]{function, prefix, e.toString()});
			}
			logger.debug("{} {} keyToMap[{}]", new Object[]{function, prefix, keyToMap});
	    	outValue = mappings.get(keyToMap);
		}
		logger.debug("{} {} outValue[{}]", new Object[]{function, prefix, outValue});
		logger.debug("{} {} End", function, prefix);
		return outValue;
	}
	
	public int findMappingIntValue(Map<String, String> mappings, String mappingname, String configPrefix, int inValue) {
		final String function = "findMappingIntValue";
		logger.debug("{} {} Begin", function, prefix);
		int outValue1 = 0;
		String keyToMap = null;
		try {
			keyToMap = configPrefix+mappingname+Integer.toString(inValue);
		} catch ( Exception e ) {
			logger.warn("compute Integer.toString Exception[{}]", e.toString());
		}
		logger.debug("{} {} keyToMap[{}]", new Object[]{function, prefix, keyToMap});
		outValue1 = Integer.parseInt(mappings.get(keyToMap));
		logger.debug("{} {} outValue1[{}]", new Object[]{function, prefix, outValue1});
		return outValue1;
	}
	
	public void checkAttributeType(Map<String, AttributeClientAbstract<?>> inputStatusByName, String fieldname) {
		final String function = "checkAttributeType";
		logger.debug("{} {} Begin", function, prefix);
		logger.debug("{} {} fieldname[{}]", new Object[]{function, prefix, fieldname});
		AttributeClientAbstract<?> obj1 = inputStatusByName.get(fieldname);
		if ( null != obj1 ) {
			
			logger.debug("{} {} fieldname[{}] obj1[{}]", new Object[]{function, prefix, fieldname, obj1});
			
			if ( obj1 instanceof StringAttribute) {
				logger.debug("{} {} fieldname[{}] obj1 IS StringAttribute", new Object[]{function, prefix, fieldname});
			} else if ( obj1 instanceof IntAttribute) {
				logger.debug("{} {} fieldname[{}] obj1 IS IntAttribute", new Object[]{function, prefix, fieldname});
			} else if ( obj1 instanceof BooleanAttribute ) {
				logger.debug("{} {} fieldname[{}] obj1 IS BooleanAttribute", new Object[]{function, prefix, fieldname});
			} else if ( obj1 instanceof CoordinatesAttribute ) {
				logger.debug("{} {} fieldname[{}] obj1 IS CoordinatesAttribute", new Object[]{function, prefix, fieldname});
			} else if ( obj1 instanceof DateTimeAttribute ) {
				logger.debug("{} {} fieldname[{}] obj1 IS DateTimeAttribute", new Object[]{function, prefix, fieldname});
			} else if ( obj1 instanceof DoubleAttribute ) {
				logger.debug("{} {} fieldname[{}] obj1 IS DoubleAttribute", new Object[]{function, prefix, fieldname});
			} else if ( obj1 instanceof FloatAttribute ) {
				logger.debug("{} {} fieldname[{}] obj1 IS FloatAttribute", new Object[]{function, prefix, fieldname});
			} else if ( obj1 instanceof LongAttribute ) {
				logger.debug("{} {} fieldname[{}] obj1 IS LongAttribute", new Object[]{function, prefix, fieldname});
			} else if ( obj1 instanceof MapStringByStringAttribute ) {
				logger.debug("{} {} fieldname[{}] obj1 IS MapStringByStringAttribute", new Object[]{function, prefix, fieldname});
			} else {
				logger.warn("{} {} fieldname[{}] IS UNKNOW TYPE", new Object[]{function, prefix, fieldname});
			}
		} else {
			logger.warn("{} {} fieldname[{}] IS NULL", new Object[]{function, prefix, fieldname});
			
		}
		
		logger.debug("{} {} inputStatusByName[{}]", new Object[]{function, prefix, inputStatusByName});
		
		logger.debug("{} {} End", function, prefix);
	}
}
