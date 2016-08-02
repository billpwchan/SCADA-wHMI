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

public class IntMapping extends SCSStatusComputer {
	
	private static final Logger s_logger = LoggerFactory.getLogger(IntMapping.class.getName());
	
	protected static String classname				= null;
	protected static String propertiesname			= null;
	
	protected static final String mappingname		= ".valuemapping.";
	
	protected static final String fieldname1		= ".fieldname1";
	protected static final String fieldname2		= ".fieldname2";
	
	protected static String field1					= "eqpType";
	protected static String field2					= "symbol1";
	
	protected static Map<String, String> mappings	= new HashMap<String, String>();

	@Override
	public String getComputerId() {
		return this.getClass().getName();
	}
	
    public IntMapping() {
    	
    	String classnames [] = getComputerId().split(Pattern.quote("."));
    	propertiesname = classnames[classnames.length-1];
    	
    	s_logger.info(propertiesname+" getComputerId["+getComputerId()+"]");
    	s_logger.info(propertiesname+" propertiesname["+propertiesname+"]");
    	
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
		
		s_logger.info(propertiesname+" field1["+field1+"]");
		s_logger.info(propertiesname+" field2["+field2+"]");
    	
		m_statusSet.add(field1);
    	m_statusSet.add(field2);
        
    }

	@Override
    public AttributeClientAbstract<?> compute(OperatorOpmInfo operatorOpmInfo, String entityId,
            Map<String, AttributeClientAbstract<?>> inputStatusByName, Map<String, Object> inputPropertiesByName)

    {
    	s_logger.info("compute Begin");
    	s_logger.info("compute field1["+field1+"]");
    	s_logger.info("compute field2["+field2+"]");
    	
    	// Load eqpType value
    	String inValue1 = null;
    	{
	    	AttributeClientAbstract<?> obj1 = inputStatusByName.get(field1);
	    	if (obj1 != null && obj1 instanceof StringAttribute) {
	    		inValue1 = ((StringAttribute) obj1).getValue();
	    	} else {
	    		s_logger.warn("compute field1["+field1+"] IS INVALID");
	    	}
		}
    	s_logger.info("compute inValue1["+inValue1+"]");
    	
    	// Load Int value
    	int inValue2 = 0;
    	{
	    	AttributeClientAbstract<?> obj1 = inputStatusByName.get(field2);
	    	if (obj1 != null && obj1 instanceof IntAttribute) {
	    		inValue2 = ((IntAttribute) obj1).getValue();
	    	} else {
	    		s_logger.warn("compute field2["+field2+"] IS INVALID");
	    	}
		}
    	s_logger.info("compute inValue2["+inValue2+"]");

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
    	s_logger.info("compute configPrefix["+configPrefix+"]");
    	
    	// Find mapping
    	String outValue1 = null;
    	{
    		String keyToMap = null;
    		try {
    			keyToMap = configPrefix+mappingname+Integer.toString(inValue2);
    		} catch ( Exception e ) {
    			s_logger.warn("compute Integer.toString Exception["+e.toString()+"]");
    		}
	    	s_logger.info("compute keyToMap["+keyToMap+"]");
	    	outValue1 = mappings.get(keyToMap);
    	}
    	s_logger.info("compute outValue1["+outValue1+"]");	
    	
    	// Return value
    	StringAttribute ret = new StringAttribute();
        ret.setValue(outValue1);
        ret.setValid(true);
        ret.setTimestamp(new Date());

        s_logger.info("compute outValue1["+outValue1+"]");
        s_logger.info("compute ret.getValue()["+ret.getValue()+"]");
        s_logger.info("compute End");
		
		return ret;

	}
    

}
