package com.thalesgroup.scadagen.calculated.symbol;

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
import com.thalesgroup.scadagen.calculated.common.SCSStatusComputer;
import com.thalesgroup.scadagen.calculated.util.Util;

public class Int2IntMapping extends SCSStatusComputer {
	
	private final Logger logger = LoggerFactory.getLogger(Int2IntMapping.class.getName());
	
	protected String classname				= null;
	protected String propertiesname			= null;
	
	protected final String mappingname		= ".valuemapping.";
	
	protected final String fieldname1		= ".fieldname1";
	protected final String fieldname2		= ".fieldname2";
	
	protected String field1					= "eqpType";
	protected String field2					= "symbol1";
	
	protected static Map<String, String> mappings	= new HashMap<String, String>();
	
	protected static Util util = new Util();

	@Override
	public String getComputerId() {
		return this.getClass().getName();
	}
	
    public Int2IntMapping() {
    	
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
    	String inValue1 = util.loadStringValue(inputStatusByName, field1);
    	logger.debug("compute inValue1[{}]", inValue1);

    	// Append the prefix if exists
    	String configPrefix = util.getConfigPrefix(mappings, propertiesname, inValue1);
    	logger.debug("compute configPrefix[{}]", configPrefix);
    	
    	util.setPrefix(inValue1);
    	
    	// Load Int value
    	int inValue2 = util.getInputStatusByConfigPrefixMappingIntValue(mappings, inputStatusByName, configPrefix, field2);
    	logger.debug("compute inValue2[{}]", inValue2);
    	
    	// Find mapping
    	int outValue1 = util.findMappingIntValue(mappings, mappingname, configPrefix, inValue2);;
    	
    	// Return value
    	IntAttribute ret = new IntAttribute();
        ret.setValue(outValue1);
        ret.setValid(true);
        ret.setTimestamp(new Date());

        logger.debug("compute outValue1[{}]", outValue1);
        logger.debug("compute ret.getValue()[{}]", ret.getValue());
        logger.debug("compute End");
		
		return ret;

	}
    

}
