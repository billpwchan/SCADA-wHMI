package com.thalesgroup.scadagen.calculated.common;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.opm.client.dto.OperatorOpmInfo;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.AttributeClientAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.StringAttribute;
import com.thalesgroup.scadagen.calculated.util.Util;
import com.thalesgroup.scadagen.wrapper.wrapper.server.Translation;

public abstract class GDGColumn extends OlsDecoder {
	
	protected Logger logger					= null;
	
	protected String logPrefix				= null;
	
	protected String className				= null;
	
	protected String fieldName				= null;
	
	protected Util util 					= new Util();

	@Override
	public String getComputerId() {
		return className;
	}
	
	/**
	 * Loading and store the configuration according to the class name as prefix
	 */
	protected void loadCnf() {

		logger = LoggerFactory.getLogger(GDGColumn.class.getName());
		
		className = this.getClass().getSimpleName();
		
    	logPrefix = className;
    	
    	logger.debug("[{}] classname[{}] getComputerId[{}]", new Object[]{logPrefix, className, getComputerId()});
    	
    	mappings = util.loadMapping(className);
		
		m_name = className;
		
		logger.debug("[{}] m_name[{}] classname[{}]", new Object[]{logPrefix, m_name, className});

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
