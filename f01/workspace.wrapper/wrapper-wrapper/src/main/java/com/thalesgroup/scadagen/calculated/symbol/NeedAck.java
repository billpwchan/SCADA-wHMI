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
import com.thalesgroup.scadagen.wrapper.wrapper.server.UIOpmSCADAgen;
import com.thalesgroup.scadagen.wrapper.wrapper.server.UIOpm_i;

public class NeedAck extends SCSStatusComputer {
	
	private final Logger logger = LoggerFactory.getLogger(NeedAck.class.getName());
	
	protected String classname				= null;
	protected String propertiesname			= null;
	
	protected final String fieldname1		= ".fieldname1";
	protected final String fieldname2		= ".fieldname2";
	
	protected final String fieldname3		= ".fieldname3";
	
	protected final String fieldname4		= ".fieldname4";
	protected final String fieldname5		= ".fieldname5";
	protected final String fieldname6		= ".fieldname6";
	protected final String fieldname7		= ".fieldname7";
	
	protected final String fieldname8		= ".fieldname8";
	protected final String fieldname9		= ".fieldname9";
	protected final String fieldname10		= ".fieldname10";
	protected final String fieldname11		= ".fieldname11";
	
	protected String field1					= "eqpType";
	protected String field2					= "needack";
	
	protected static Map<String, String> mappings	= new HashMap<String, String>();
	
	protected static Util util = new Util();

	@Override
	public String getComputerId() {
		return this.getClass().getName();
	}
	
    public NeedAck() {
    	
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
		
		logger.debug(propertiesname+" field1[{}]", field1);
    	
		m_statusSet.add(field1);
		m_statusSet.add(field2);
        
    }

	@Override
    public AttributeClientAbstract<?> compute(OperatorOpmInfo operatorOpmInfo, String entityId,
            Map<String, AttributeClientAbstract<?>> inputStatusByName, Map<String, Object> inputPropertiesByName)

    {
		logger.debug("compute Begin");
		logger.debug("compute field1[{}]", field1);
		
    	// Load eqpType value
    	String inValue1 = util.loadStringValue(inputStatusByName, field1);
    	logger.debug("compute inValue1[{}]", inValue1);
    	
    	// Load inValue value
    	int inValue2 = util.loadIntValue(inputStatusByName, field2);
    	logger.debug("compute inValue2[{}]", inValue2);
    	
    	// Append the prefix if exists
    	String configPrefix = util.getConfigPrefix(mappings, propertiesname, inValue1);
    	logger.debug("compute configPrefix[{}]", configPrefix);

    	util.setPrefix(inValue1);
    	
    	int outValue1 = 0;
    	
    	if ( inValue2 > 0 ) {
        		
        	outValue1 = inValue2;
        	
        	String opmKey = util.getConfigPrefixMappingValue(mappings, configPrefix, fieldname3);
        	
        	logger.debug("compute opmKey[{}]", opmKey);
	    	
        	// Field Name: function, location, action, mode
        	String opmName1 = null, opmName2 = null, opmName3 = null, opmName4 = null;
        	// Field Value: function, location, action, mode
        	String opmValue1 = null, opmValue2 = null, opmValue3 = null, opmValue4 = null;
        	
        	int iOpmValue1 = util.getInputStatusByConfigPrefixMappingIntValue(mappings, inputStatusByName, configPrefix, fieldname4);
	    	opmValue1 = Integer.toString(iOpmValue1);
	    	
	    	int iOpmValue2 = util.getInputStatusByConfigPrefixMappingIntValue(mappings, inputStatusByName, configPrefix, fieldname4);
	    	opmValue2 = Integer.toString(iOpmValue2);
	    	
	    	opmValue3 = util.getConfigPrefixMappingValue(mappings, configPrefix, fieldname6);
	    	opmValue4 = util.getConfigPrefixMappingValue(mappings, configPrefix, fieldname7);
	    	
	    	logger.debug("compute opmValue1[{}] opmValue2[{}] opmValue3[{}] opmValue4[{}]", new Object[]{opmValue1, opmValue2, opmValue3, opmValue4});
	    	
	    	opmName1 = util.getConfigPrefixMappingValue(mappings, configPrefix, fieldname8);
	    	if ( null != opmName1 ) {
		    	opmName2 = util.getConfigPrefixMappingValue(mappings, configPrefix, fieldname9);
		    	opmName3 = util.getConfigPrefixMappingValue(mappings, configPrefix, fieldname10);
		    	opmName4 = util.getConfigPrefixMappingValue(mappings, configPrefix, fieldname11);
		    	
		    	logger.debug("compute opmName1[{}] opmName2[{}] opmName3[{}] opmName4[{}]", new Object[]{opmName1, opmName2, opmName3, opmName4});
	    	}

        	// OPM
        	// Return value equals to number of point should be ack (Matched to .alarmSynthesis(3) / needack )
        	// Equals to 0 : no alarm need ack
        	// Positive value : number of alarm need ack in the same equipment with right
        	// Negative value : number of alarm need ack in the same equipment without right
        	
        	boolean result = false;

        	UIOpm_i uiOpm_i = UIOpmSCADAgen.getInstance();
        	
        	if ( null != uiOpm_i ) {
            	if ( null == opmName1 || null == opmName2 || null == opmName3 || null == opmName4 ) {
            		result = uiOpm_i.checkAccess(opmValue1, opmValue2, opmValue3, opmValue4);
            	} else {
            		result = uiOpm_i.checkAccess(operatorOpmInfo, opmName1, opmValue1, opmName2, opmValue2, opmName3, opmValue3, opmName4, opmValue4);
            	}
        	} else {
        		 logger.debug("compute uiOpm_i IS NULL");
        	}
        	
        	if ( ! result ) {
        		outValue1 = inValue2 * -1; 
        	}
    	}
//    	else 
//    	{
//    		outValue1 = inValue2;
//    	}
    	
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
