package com.thalesgroup.scadagen.calculated;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thalesgroup.hypervisor.mwt.core.util.config.loader.IConfigLoader;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.data.server.rpc.implementation.ServicesImplFactory;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.opm.client.dto.OperatorOpmInfo;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.AttributeClientAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.IntAttribute;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.StringAttribute;
import com.thalesgroup.scadagen.common.calculated.SCSStatusComputer;

public class IntFormatting extends SCSStatusComputer {
	
	private final Logger logger = LoggerFactory.getLogger(IntFormatting.class.getName());
	
	protected String classname				= null;
	protected String propertiesname			= null;
	
	protected final String formatting		= ".formatting";
	
	protected final String fieldname1		= ".fieldname1";
	protected final String fieldname2		= ".fieldname2";
	
	protected String field1					= "eqpType";
	protected String field2					= "symbol1";
	
	protected Map<String, String> mappings	= new HashMap<String, String>();

	@Override
	public String getComputerId() {
		return this.getClass().getName();
	}
	
    public IntFormatting() {
    	
    	String classnames [] = getComputerId().split(Pattern.quote("."));
    	propertiesname = classnames[classnames.length-1];
    	
    	logger.debug(propertiesname+" getComputerId[{}]", getComputerId());
    	logger.debug(propertiesname+" propertiesname[{}]", propertiesname);
    	
    	IConfigLoader configLoader		= ServicesImplFactory.getInstance().getService(IConfigLoader.class);
		Map<String,String> properties	= configLoader.getProjectConfigurationMap();
		if (properties != null) {
			
			// Load all setting with class prefix into buffer
			for ( String key : properties.keySet() ) {
				if ( key.startsWith(propertiesname) ) {
					mappings.put(key, properties.get(key));
				}
			}
		}
		
		field1 = mappings.get(propertiesname+fieldname1);
		field2 = mappings.get(propertiesname+fieldname2);
		
		logger.debug(propertiesname+" field1[{}]", field1);
		logger.debug(propertiesname+" field2[{}]", field2);
    	
		m_statusSet.add(field1);
    	m_statusSet.add(field2);
    }

	@Override
    public AttributeClientAbstract<?> compute(OperatorOpmInfo operatorOpmInfo, String entityId,
            Map<String, AttributeClientAbstract<?>> inputStatusByName, Map<String, Object> inputPropertiesByName)

    {
		logger.debug("compute Begin");
		logger.debug("compute field1[{}]", field1);
		logger.debug("compute field2[{}]", field2);
    	
    	// Load eqpType value
    	String inValue1 = null;
    	{
	    	AttributeClientAbstract<?> obj1 = inputStatusByName.get(field1);
	    	if (obj1 != null && obj1 instanceof StringAttribute) {
	    		inValue1 = ((StringAttribute) obj1).getValue();
	    	} else {
	    		logger.warn("compute field1[{}] IS INVALID", field1);
	    	}
		}
    	logger.debug("compute inValue1[{}]", inValue1);
    	
    	// Load Int value
    	int inValue2 = 0;
    	{
	    	AttributeClientAbstract<?> obj1 = inputStatusByName.get(field2);
	    	if (obj1 != null && obj1 instanceof IntAttribute) {
	    		inValue2 = ((IntAttribute) obj1).getValue();
	    	} else {
	    		logger.warn("compute field2[{}] IS INVALID", field2);
	    	}
		}
    	logger.debug("compute inValue2[{}]", inValue2);

    	// Append the prefix if exists
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
    	
    	// Find mapping
    	String inValue3 = null;
    	{
	    	String keyToMap = configPrefix+formatting;
	    	logger.debug("compute keyToMap[{}]", keyToMap);
	    	inValue3 = mappings.get(keyToMap);
	    	logger.debug("compute inValue3[{}]", inValue3);    		
    	}
    	logger.debug("compute inValue3[{}]", inValue3);
    	
    	String outValue1 = null;
    	{
    		try {
    			outValue1 = String.format(inValue3, inValue2);
    		} catch (Exception e) {
    			logger.warn("compute INVALID FORMAT[{}] OR INVALID VALUE[{}]", inValue3, inValue2);
    			outValue1 = Integer.toString(inValue2);
    		}
    		
    	}
    	logger.debug("compute outValue1[{}]", outValue1);
    	
    	// Return value
    	StringAttribute ret = new StringAttribute();
        ret.setValue(outValue1);
        ret.setValid(true);
        ret.setTimestamp(new Date());

        logger.debug("compute outValue1[{}]", outValue1);
        logger.debug("compute ret.getValue()[{}]", ret.getValue());
        logger.debug("compute End");
		
		return ret;

	}
    

}
