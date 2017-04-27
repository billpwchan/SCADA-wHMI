package com.thalesgroup.scadagen.calculated.util;

import java.util.Date;
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

	public Map<String, String> loadMapping(String m_name) {
		final String function = "loadStringValue";
		logger.debug("{} {} Begin", function, m_name);
		Map<String, String> mappings	= new HashMap<String, String>();
    	IConfigLoader configLoader		= ServicesImplFactory.getInstance().getService(IConfigLoader.class);
		Map<String,String> properties	= configLoader.getProjectConfigurationMap();
		if (properties != null) {
			int classNameLength = m_name.length();
			// Load all setting with class prefix into buffer
			for ( String key : properties.keySet() ) {
				if ( key.startsWith(m_name) ) {
					String keyname = key.substring(classNameLength);
					mappings.put(keyname, properties.get(key));
				}
			}
		} else {
			logger.warn("[{}] properties IS NULL", prefix);
		}
		logger.debug("{} {} End", function, m_name);
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
    		if ( logger.isDebugEnabled() ) dumpInputStatusByNameAttributes(inputStatusByName, inValue);
    	}
    	logger.debug("{} {} outValue[{}]", new Object[]{function, prefix, outValue});
    	logger.debug("{} {} End", function, prefix);
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
    		if ( logger.isDebugEnabled() ) dumpInputStatusByNameAttributes(inputStatusByName, inValue);
    	}
    	logger.debug("{} {} outValue[{}]", new Object[]{function, prefix, outValue});
    	logger.debug("{} {} End", function, prefix);
    	return outValue;
	}
	
	public IntAttribute getIntAttribute(int value, boolean isValid, Date date) {
		final String function = "getIntAttribute";
		logger.debug("{} {} value[{}] isValid[{}] date[{}]", new Object[]{function, prefix, value, date});
    	IntAttribute ret = new IntAttribute();
        ret.setValue(value);
        ret.setValid(isValid);
        ret.setTimestamp(date);
        logger.debug("{} {} ret.getValue()[{}]", new Object[]{function, prefix, ret.getValue()});
        return ret;
	}
	
	public StringAttribute getStringAttribute(String value, boolean isValid, Date date) {
		final String function = "getStringAttribute";
		logger.debug("{} {} value[{}] isValid[{}] date[{}]", new Object[]{function, prefix, value, date});
		StringAttribute ret = new StringAttribute();
	    ret.setValue(value);
	    ret.setValid(isValid);
	    ret.setTimestamp(date);
	    logger.debug("{} {} ret.getValue()[{}]", new Object[]{function, prefix, ret.getValue()});
	    return ret;
	}

	public String getConfitPrefix(Map<String, String> mappings, String m_name, String perConfigName) {
		final String function = "getConfitPrefix";
    	String configPrefix = "";
    	if ( null != perConfigName ) {
    		String keyToFind = configPrefix+"."+perConfigName;
    		for ( String key : mappings.keySet() ) {
    			if ( key.startsWith(keyToFind)) {
    				configPrefix = keyToFind;
    				break;
    			}
    		}
    	}
    	logger.debug("{} {} configPrefix[{}]", new Object[]{function, prefix, configPrefix});
    	return configPrefix;
	}
	
	public String getConfigPrefixMappingValue(Map<String, String> mappings, String configPrefix, String fieldname) {
		final String function = "getConfigPrefixMappingValue";
		logger.debug("{} {} Begin", function, prefix);
		String outValue = null;
		
    	String keyToMap = configPrefix+fieldname;
    	logger.debug("{} {} keyToMap[{}]", new Object[]{function, prefix, keyToMap});
    	outValue = mappings.get(keyToMap);
    	
    	logger.debug("{} {} outValue[{}]", new Object[]{function, prefix, outValue});
    	logger.debug("{} {} End", function, prefix);
    	return outValue;
	}
	public void dumpInputStatusByNameAttribute(AttributeClientAbstract<?> obj1) {
		final String function = "dumpInputStatusByNameAttribute";
		logger.debug("{} {} Begin", function, prefix);
		if ( null != obj1 ) {
			if ( obj1 instanceof StringAttribute) {
				logger.debug("{} {} obj1 IS StringAttribute", new Object[]{function, prefix});
			} else if ( obj1 instanceof IntAttribute) {
				logger.debug("{} {} obj1 IS IntAttribute", new Object[]{function, prefix});
			} else if ( obj1 instanceof BooleanAttribute ) {
				logger.debug("{} {} obj1 IS BooleanAttribute", new Object[]{function, prefix});
			} else if ( obj1 instanceof CoordinatesAttribute ) {
				logger.debug("{} {} obj1 IS CoordinatesAttribute", new Object[]{function, prefix});
			} else if ( obj1 instanceof DateTimeAttribute ) {
				logger.debug("{} {} obj1 IS DateTimeAttribute", new Object[]{function, prefix});
			} else if ( obj1 instanceof DoubleAttribute ) {
				logger.debug("{} {} obj1 IS DoubleAttribute", new Object[]{function, prefix});
			} else if ( obj1 instanceof FloatAttribute ) {
				logger.debug("{} {} obj1 IS FloatAttribute", new Object[]{function, prefix});
			} else if ( obj1 instanceof LongAttribute ) {
				logger.debug("{} {} obj1 IS LongAttribute", new Object[]{function, prefix});
			} else if ( obj1 instanceof MapStringByStringAttribute ) {
				logger.debug("{} {} obj1 IS MapStringByStringAttribute", new Object[]{function, prefix});
			} else {
				logger.warn("{} {} IS UNKNOW TYPE", new Object[]{function, prefix});
			}
		} else {
			logger.warn("{} {} fieldname[{}] IS NULL", new Object[]{function, prefix});
		}
		logger.debug("{} {} obj1[{}]", new Object[]{function, prefix, obj1});
		logger.debug("{} {} End", function, prefix);
	}
	public void dumpInputStatusByNameAttributes(Map<String, AttributeClientAbstract<?>> inputStatusByName) {
		final String function = "dumpInputStatusByNameAttributes";
		logger.debug("{} {} Begin", function, prefix);
		if ( null != inputStatusByName ) {
			for ( String key : inputStatusByName.keySet() ) {
				logger.debug("{} {} key[{}]", new Object[]{function, prefix, key});
				dumpInputStatusByNameAttribute(inputStatusByName.get(inputStatusByName.get(key)));
			}
		} else {
			logger.warn("{} {} inputStatusByName[{}] IS NULL", new Object[]{function, prefix, inputStatusByName});
		}
		logger.debug("{} {} inputStatusByName[{}]", new Object[]{function, prefix, inputStatusByName});
		logger.debug("{} {} End", function, prefix);
	}
	public void dumpInputStatusByNameAttributes(Map<String, AttributeClientAbstract<?>> inputStatusByName, String fieldname) {
		final String function = "dumpInputStatusByNameAttributes";
		logger.debug("{} {} Begin", function, prefix);
		logger.debug("{} {} fieldname[{}]", new Object[]{function, prefix, fieldname});
		dumpInputStatusByNameAttribute(inputStatusByName.get(fieldname));
		logger.warn("{} {} inputStatusByName[{}]", new Object[]{function, prefix, inputStatusByName});
		logger.debug("{} {} End", function, prefix);
	}
}