package com.thalesgroup.scadagen.calculated.common;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.thalesgroup.hypervisor.mwt.core.util.config.loader.IConfigLoader;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.data.server.rpc.implementation.ServicesImplFactory;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.opm.client.dto.OperatorOpmInfo;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.AttributeClientAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.StringAttribute;
import com.thalesgroup.scadagen.wrapper.wrapper.server.Translation;

public abstract class GDGColumn extends OlsDecoder {
	
	protected Logger logger					= null;
	
	protected String logPrefix				= null;
	
	protected String classname				= null;
	
	protected String fieldName				= null;

	@Override
	public String getComputerId() {
		return classname;
	}
	
	/**
	 * Loading and store the configuration according to the class name as prefix
	 */
	protected void loadCnf() {

		logger = LoggerFactory.getLogger(GDGColumn.class.getName());
		
		classname = this.getClass().getSimpleName();
		
    	logPrefix = classname;
    	
    	logger.debug("[{}] classname[{}] getComputerId[{}]", new Object[]{logPrefix, classname, getComputerId()});
    	
    	IConfigLoader configLoader		= ServicesImplFactory.getInstance().getService(IConfigLoader.class);
		Map<String,String> properties	= configLoader.getProjectConfigurationMap();
		if (properties != null) {
			int classNameLength = classname.length();
			// Load all setting with class prefix into buffer
			for ( String key : properties.keySet() ) {
//				logger.debug("[{}] properties.keySet() key[{}]", new Object[]{logPrefix, key});
				if ( key.startsWith(classname) ) {
					String keyname = key.substring(classNameLength);
					mappings.put(keyname, properties.get(key));
//					logger.debug("[{}] keyname[{}] properties.get(key)[{}]", new Object[]{logPrefix, keyname, properties.get(key)});
				}
			}
		} else {
			logger.warn("[{}] properties IS NULL", logPrefix);
		}
		
		m_name = classname;
		
		logger.debug("[{}] m_name[{}] classname[{}]", new Object[]{logPrefix, m_name, classname});

	}
	
    @Override
    public AttributeClientAbstract<?> compute(OperatorOpmInfo operatorOpmInfo, String entityId,
            Map<String, AttributeClientAbstract<?>> inputStatusByName, Map<String, Object> inputPropertiesByName)

    {
        AttributeClientAbstract<?> dataobj = super.compute(operatorOpmInfo, entityId, inputStatusByName, inputPropertiesByName);
        
        StringAttribute jsdata = (dataobj instanceof StringAttribute ? (StringAttribute) dataobj : null);
        
        String dataValue = null;

        if (jsdata != null) {
            String data = jsdata.getValue();
            // decode json
            ObjectNode root = null;
			try {
				root = (ObjectNode) s_json_mapper.readTree(data);

				String listname = getListName(entityId);
				logger.debug("[{}] entityId[{}] listname[{}]", new Object[]{logPrefix, entityId, listname});

				String fieldname1key = getFieldKey(listname, fieldname1);
				logger.debug("[{}] listname[{}] fieldname1[{}] fieldname1key[{}]", new Object[]{logPrefix, listname, fieldname1, fieldname1key});
				
				fieldName = mappings.get(fieldname1key);
				logger.debug("[{}] fieldname1key[{}] fieldName[{}]", new Object[]{logPrefix, fieldname1key, fieldName});
			
				JsonNode v = root.get(fieldName);
	            
				dataValue = v.asText();
				
				logger.debug("[{}] dataValue[{}]", new Object[]{logPrefix, dataValue});
				
				dataValue = Translation.getDBMessage(dataValue);

			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }

        // prepare default response
    	StringAttribute ret = new StringAttribute();
        ret.setAttributeClass(StringAttribute.class.getName());
        ret.setValue( (dataValue!=null?dataValue:"") );
        ret.setValid(true);
        ret.setTimestamp(new Date());
        
        return ret;
    }

}
