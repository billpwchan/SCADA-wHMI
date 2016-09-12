package com.thalesgroup.scadagen.common.calculated;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thalesgroup.hypervisor.mwt.core.util.config.loader.IConfigLoader;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.data.server.rpc.implementation.ServicesImplFactory;

public abstract class GDGColumn extends OlsDecoder {
	
	protected Logger logger					= null;
	
	protected String logPrefix				= null;
	
	protected String classname				= null;
	
	protected final String fieldname1		= ".fieldname1";

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
		
		logger.info("[{}] fieldname1[{}] field1[{}]", new Object[]{logPrefix,fieldname1,field1});
		
		m_name = field1;
	}

}
