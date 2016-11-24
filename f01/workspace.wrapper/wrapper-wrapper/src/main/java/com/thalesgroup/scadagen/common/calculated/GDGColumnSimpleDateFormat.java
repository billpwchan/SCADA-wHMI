package com.thalesgroup.scadagen.common.calculated;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
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
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.DateTimeAttribute;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.StringAttribute;

public abstract class GDGColumnSimpleDateFormat extends OlsDecoder {
	
	protected Logger logger					= null;
	
	protected String logPrefix				= null;
	
	protected String classname				= null;
	
	protected final String fieldname1		= ".fieldname1";
	
	protected final String sdfparsename		= ".simpledatetime.parse";
	
	protected Map<String, String> mappings	= new HashMap<String, String>();
	
	@Override
	public String getComputerId() {
		return classname;
	}
	
	/**
	 * Loading and store the configuration according to the class name as prefix
	 */
	protected void loadCnf() {

		logger = LoggerFactory.getLogger(GDGColumnSimpleDateFormat.class.getName());
		
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

    	Date date = null;

        if (jsdata != null) {
            String data = jsdata.getValue();
            // decode json
            ObjectNode root = null;
			try {
				root = (ObjectNode) s_json_mapper.readTree(data);
				
				String listName = getListName(entityId);
				logger.debug("[{}] entityId[{}] listName[{}]", new Object[]{logPrefix, entityId, listName});
			
				String fieldname1key = getFieldKey(listName, fieldname1);
				logger.debug("[{}] listName[{}] fieldname1[{}] fieldname1key[{}]", new Object[]{logPrefix, listName, fieldname1, fieldname1key});
				
				String fieldname = mappings.get(fieldname1key);
				logger.debug("[{}] fieldname1key[{}] fieldname[{}]", new Object[]{logPrefix, fieldname1key, fieldname});
				
				JsonNode v = root.get(fieldname);
				
				String inValue = v.asText();
				
				String sdfparsenamekey = getFieldKey(listName, sdfparsename);
				logger.debug("[{}] sdfparsenamekey[{}] sdfparsename[{}]", new Object[]{logPrefix, sdfparsenamekey, sdfparsename});
				
				String sdfparse = mappings.get(sdfparsenamekey);
				logger.debug("[{}] fieldname1key[{}] sdfparse[{}]", new Object[]{logPrefix, sdfparsenamekey, sdfparse});
				
				logger.debug("[{}] sdfparse[{}] inValue[{}]", new Object[]{logPrefix, sdfparse, inValue});
				date = getDateValue(sdfparse, inValue);

			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        
    	DateTimeAttribute ret = new DateTimeAttribute();
    	ret.setAttributeClass(StringAttribute.class.getName());
    	ret.setValue(null!=date?date:new Date());
    	ret.setValid(true);
        ret.setTimestamp(new Date());
        
		return ret;

    }
    
    private Date getDateValue(String sdfparse, String inValue) {
    	Date date = null;
		SimpleDateFormat simpleDateTimeParse = new SimpleDateFormat(sdfparse);
		try {
			date = simpleDateTimeParse.parse(inValue);
		} catch ( ParseException e ) {
			logger.error("[{}] sdfparse[{}] sdfparse[{}]", new Object[]{logPrefix,sdfparse,sdfparse});
		}
		return date;
    }
}
