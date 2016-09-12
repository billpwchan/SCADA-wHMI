package com.thalesgroup.scadagen.common.calculated;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thalesgroup.hypervisor.mwt.core.util.config.loader.IConfigLoader;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.data.server.rpc.implementation.ServicesImplFactory;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.opm.client.dto.OperatorOpmInfo;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.AttributeClientAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.StringAttribute;

public abstract class GDGColumnBitwise extends OlsDecoder {
	
	protected Logger logger					= null;
	
	protected String logPrefix				= null;
	
	protected String classname				= null;
	
	protected final String fieldname1		= ".fieldname1";
	
	protected final String bitwiseopname	= ".bitwise.operation";
	protected final String bitwisevaluename	= ".bitwise.value";
	protected String bitwiseop				= null;
	protected String bitwisevalue			= null;
	
	protected String field1					= null;
	
	protected Map<String, String> mappings	= new HashMap<String, String>();
	
	@Override
	public String getComputerId() {
		return classname;
	}
	
	/**
	 * Loading and store the configuration according to the class name as prefix
	 */
	protected void loadCnf() {

		logger = LoggerFactory.getLogger(GDGMessage.class.getName());
		
		classname = this.getClass().getSimpleName();
		
    	logPrefix = classname;
    	
    	logger.info("[{}] classname[{}] getComputerId[{}]", new Object[]{logPrefix, classname, getComputerId()});
    	
    	IConfigLoader configLoader		= ServicesImplFactory.getInstance().getService(IConfigLoader.class);
		Map<String,String> properties	= configLoader.getProjectConfigurationMap();
		if (properties != null) {
			int classNameLength = classname.length();
			// Load all setting with class prefix into buffer
			for ( String key : properties.keySet() ) {
				if ( key.startsWith(classname) ) {
					String keyname = key.substring(classNameLength);
					mappings.put(keyname, properties.get(key));
				}
			}
		}
		
		field1			= mappings.get(fieldname1);
		
		bitwiseop		= mappings.get(bitwiseopname);
		bitwisevalue	= mappings.get(bitwisevaluename);
		
		logger.info("[{}] fieldname1[{}] field1[{}]", new Object[]{logPrefix,fieldname1,field1});
		
		m_name = field1;
	}
	
    @Override
    public AttributeClientAbstract<?> compute(OperatorOpmInfo operatorOpmInfo, String entityId,
            Map<String, AttributeClientAbstract<?>> inputStatusByName, Map<String, Object> inputPropertiesByName)
    {
    	AttributeClientAbstract<?> ret = super.compute(operatorOpmInfo, entityId, inputStatusByName, inputPropertiesByName);
    	if ( null != bitwiseop && null != bitwisevalue ) {
     		if ( null != ret ) {
     			if ( ret instanceof StringAttribute ) {
     				String inValue = ((StringAttribute)ret).getValue();
    				String outValue = inValue;
    				boolean isInvalid = false;
    				int intValue = 0;
    				int intOpValue = 0;
    				try { 
    					intValue = Integer.parseInt(inValue);
    					intOpValue = Integer.parseInt(bitwisevalue);
    				} catch ( NumberFormatException e ) {
    					isInvalid = true;
    					logger.error("[{}] bitwiseop[{}] bitwisevalue[{}]", new Object[]{logPrefix,bitwiseop,bitwisevalue});
    					logger.error("[{}] inValue[{}] outValue[{}]", new Object[]{logPrefix,inValue,outValue});
    				}
    				int intOutValue = 0;
    				if ( !isInvalid ) {
    					bitwiseop = bitwiseop.trim();
    					logger.info("[{}] trim bitwiseop[{}]", new Object[]{logPrefix,bitwiseop});
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
    						logger.error("[{}] bitwiseop[{}] bitwisevalue[{}], UNKNOW OPERATOR TYPE", new Object[]{logPrefix,bitwiseop,bitwisevalue});
    					}
    				}
    				if ( !isInvalid ) {
    					outValue = Integer.toString(intOutValue);
    				}
    				logger.info("[{}] outValue[{}]", new Object[]{logPrefix,outValue});
    				((StringAttribute)ret).setValue(outValue);
    				((StringAttribute)ret).setValid(true);
    				((StringAttribute)ret).setTimestamp(new Date());    				
     			}
     		}
    	}
    	return ret;
    }
}
