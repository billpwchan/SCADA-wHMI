package com.thalesgroup.scadagen.calculated.common;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.thalesgroup.hypervisor.mwt.core.util.config.loader.IConfigLoader;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.data.server.rpc.implementation.ServicesImplFactory;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.opm.client.dto.OperatorOpmInfo;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.AttributeClientAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.StringAttribute;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.server.UILogger_i;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.server.factory.UILoggerFactory;

public abstract class GDGColumnBitwise extends OlsDecoder {
	
	protected UILogger_i logger					= null;
	
	protected String logPrefix				= null;
	
	protected String classname				= null;
	
	protected final String fieldname1		= ".fieldname1";
	
	protected final String bitwiseopname	= ".bitwise.operation";
	protected final String bitwisevaluename	= ".bitwise.value";
	
	protected Map<String, String> mappings	= new HashMap<String, String>();
	
	@Override
	public String getComputerId() {
		return classname;
	}
	
	/**
	 * Loading and store the configuration according to the class name as prefix
	 */
	protected void loadCnf() {

		logger = UILoggerFactory.getInstance().get(this.getClass().getName());
		
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
	}
	
    @Override
    public AttributeClientAbstract<?> compute(OperatorOpmInfo operatorOpmInfo, String entityId,
            Map<String, AttributeClientAbstract<?>> inputStatusByName, Map<String, Object> inputPropertiesByName)
    {
        AttributeClientAbstract<?> dataobj = super.compute(operatorOpmInfo, entityId, inputStatusByName, inputPropertiesByName);
        
        StringAttribute jsdata = (dataobj instanceof StringAttribute ? (StringAttribute) dataobj : null);
        
        String outValue = null;

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
				logger.debug("[{}] fieldname1key[{}] fieldname[}{]", new Object[]{logPrefix, fieldname1key, fieldname});
				
				JsonNode v = root.get(fieldname);
				String inValue = v.asText();
				
				String bitwiseopkey = getFieldKey(listName, bitwiseopname);
				String bitwiseop = mappings.get(bitwiseopkey);
				logger.debug("[{}] bitwiseopkey[{}] bitwiseop[{}]", new Object[]{logPrefix, bitwiseopkey, bitwiseop});
				
				String bitwisevaluekey = getFieldKey(listName, bitwisevaluename);
				String bitwisevalue = mappings.get(bitwisevaluekey);
				logger.debug("[{}] bitwisevaluekey[{}] bitwisevalue[{}]", new Object[]{logPrefix, bitwisevaluekey, bitwisevalue});
				
				logger.debug("[{}] inValue[{}] bitwiseop[{}] bitwisevalue[{}]", new Object[]{logPrefix, inValue, bitwiseop, bitwisevalue});
				outValue = getBitWiseValue(inValue, bitwiseop, bitwisevalue);

			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
        }
        
		StringAttribute ret = new StringAttribute();
		ret.setAttributeClass(StringAttribute.class.getName());
		ret.setValue( (null!=outValue?outValue:"") );
		ret.setValid(true);
		ret.setTimestamp(new Date());

    	return ret;
    }
    
    protected String getBitWiseValue(String inValue, String bitwiseop, String bitwisevalue) {
		String outValue = inValue;
		boolean isInvalid = false;
		
		int intValue = 0;
		int intOpValue = 0;
		try { 
			intValue = Integer.parseInt(inValue);
			intOpValue = Integer.parseInt(bitwisevalue);
		} catch ( NumberFormatException e ) {
			isInvalid = true;
			logger.error("[{}] bitwiseop[{}] bitwisevalue[{}]", new Object[]{logPrefix, bitwiseop, bitwisevalue});
			logger.error("[{}] inValue[{}] outValue[{}]", new Object[]{logPrefix, inValue, outValue});
		}
		int intOutValue = 0;
		if ( !isInvalid ) {
			bitwiseop = bitwiseop.trim();
			logger.debug("[{}] trim bitwiseop[{}]", new Object[]{logPrefix, bitwiseop});
			if ( "&".equals(bitwiseop) ) {
				intOutValue = intOpValue & intValue;
			} else if ( "|".equals(bitwiseop) ) {
				intOutValue = intOpValue | intValue;
			} else if ( "^".equals(bitwiseop) ) {
				intOutValue = intOpValue ^ intValue;
			} else if ( "<<".equals(bitwiseop) ) {
				intOutValue = intOpValue << intValue;
			} else if ( ">>".equals(bitwiseop) ) {
				intOutValue = intOpValue >> intValue;
			} else if ( ">>>".equals(bitwiseop) ) {
				intOutValue = intOpValue >>> intValue;
			}  else {
				isInvalid = true;
				logger.error("[{}] bitwiseop[{}] bitwisevalue[{}], UNKNOW OPERATOR TYPE", new Object[]{logPrefix, bitwiseop, bitwisevalue});
			}
		}
		if ( !isInvalid ) {
			outValue = Integer.toString(intOutValue);
		}
		
		return outValue;
    }
}
