package com.thalesgroup.scadagen.common.calculated;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.text.ParseException;
import com.thalesgroup.hypervisor.mwt.core.util.config.loader.IConfigLoader;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.data.server.rpc.implementation.ServicesImplFactory;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.opm.client.dto.OperatorOpmInfo;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.AttributeClientAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.StringAttribute;

public abstract class GDGColumnSimpleDateFormat extends OlsDecoder {
	
	protected Logger logger					= null;
	
	protected String logPrefix				= null;
	
	protected String classname				= null;
	
	protected final String fieldname1		= ".fieldname1";
	
	protected final String sdfparsename		= ".simpledatetime.parse";
	protected final String sdfformatname	= ".simpledatetime.format";
	protected String sdfparse				= null;
	protected String sdfformat				= null;	
	
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
    	
    	logger.debug("[{}] classname[{}] getComputerId[{}]", new Object[]{logPrefix, classname, getComputerId()});
    	
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
		
		sdfparse		= mappings.get(sdfparsename);
		sdfformat		= mappings.get(sdfformatname);
		
		logger.debug("[{}] fieldname1[{}] field1[{}]", new Object[]{logPrefix,fieldname1,field1});
		
		m_name = field1;
	}
	
    @Override
    public AttributeClientAbstract<?> compute(OperatorOpmInfo operatorOpmInfo, String entityId,
            Map<String, AttributeClientAbstract<?>> inputStatusByName, Map<String, Object> inputPropertiesByName)
    {
    	AttributeClientAbstract<?> ret = super.compute(operatorOpmInfo, entityId, inputStatusByName, inputPropertiesByName);
    	if ( null != sdfparse && null != sdfformat ) {
    		if ( null != ret ) {
    			if ( ret instanceof StringAttribute ) {
    				
    				String inValue = ((StringAttribute)ret).getValue();
    				String outValue = inValue;
    				
    				SimpleDateFormat simpleDateTimeParse = new SimpleDateFormat(sdfparse);
    				SimpleDateFormat simpleDateTimeFormat = new SimpleDateFormat(sdfformat);
    				try {
    					Date date = simpleDateTimeParse.parse(inValue);
    					outValue = simpleDateTimeFormat.format(date);
    				} catch ( ParseException e ) {
    					logger.error("[{}] sdfparse[{}] sdfparse[{}]", new Object[]{logPrefix,sdfparse,sdfparse});
    					logger.error("[{}] inValue[{}] outValue[{}]", new Object[]{logPrefix,inValue,outValue});
    				}
    				
    				((StringAttribute)ret).setValue(outValue);
    				((StringAttribute)ret).setValid(true);
    				((StringAttribute)ret).setTimestamp(new Date());
    			} else {
    				logger.warn("[{}] ret IS NOT StringAttribute]", logPrefix);
    			}
    		} else {
    			logger.warn("[{}] ret IS NULL]", logPrefix);
    		}
    	}
    	return ret;
    }
}
