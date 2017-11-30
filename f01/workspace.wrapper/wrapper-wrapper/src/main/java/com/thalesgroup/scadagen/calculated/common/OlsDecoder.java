package com.thalesgroup.scadagen.calculated.common;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.opm.client.dto.OperatorOpmInfo;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.AttributeClientAbstract;

public abstract class OlsDecoder extends SCSStatusComputer {
	
	protected Logger logger					= null;
	
	protected String logPrefix				= null;
	
//	protected String classname				= null;

	public static ObjectMapper s_json_mapper = null;
	
	protected final String listname1		= "listname";
	
	protected final String fieldname1		= ".fieldname1";
	
	protected final String defaultDataValue	= ".defaultDataValue";

	protected String field1					= null;
	
	protected Map<String, String> mappings	= new HashMap<String, String>();
	
    public OlsDecoder() {
    	
    	logger = LoggerFactory.getLogger(OlsDecoder.class.getName());
    	
    	logPrefix = this.getClass().getSimpleName();
    	
        m_statusSet.add("data");
        if (s_json_mapper == null) {
        	s_json_mapper = new ObjectMapper();
            // to prevent exception when encountering unknown property:
            s_json_mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            // to allow coercion of JSON empty String ("") to null Object value:
            s_json_mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        }
    }
    
    @Override
    public AttributeClientAbstract<?> compute(OperatorOpmInfo operatorOpmInfo, String entityId,
            Map<String, AttributeClientAbstract<?>> inputStatusByName, Map<String, Object> inputPropertiesByName)

    {
    	
		if ( logger.isDebugEnabled() ) {
			
			logger.debug("[{}] inputStatusByName keys...");
			for ( String key : inputStatusByName.keySet() ) {
				logger.debug("[{}] inputStatusByName.keySet() key[{}]", new Object[]{logPrefix, key});
			}
			
			logger.debug("[{}] mappings keys...");
			for ( String key : mappings.keySet() ) {
				logger.debug("[{}] mappings.keySet() key[{}]", new Object[]{logPrefix, key});
			}
		}
    	
    	// get value of data
    	AttributeClientAbstract<?> dataobj = inputStatusByName.get("data");
    	
    	return dataobj;
    }

}
