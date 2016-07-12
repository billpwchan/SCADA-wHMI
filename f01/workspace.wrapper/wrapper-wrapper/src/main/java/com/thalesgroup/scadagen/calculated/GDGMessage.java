package com.thalesgroup.scadagen.calculated;

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
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.MapStringByStringAttribute;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.StringAttribute;

public abstract class GDGMessage extends SCSStatusComputer {
	
	private Logger s_logger	= LoggerFactory.getLogger(GDGMessage.class.getName());
	
	protected String propertiesname			= null;
	
	protected final String mappingname		= ".valuemapping.";
	
	protected final String fieldname1		= ".fieldname1";
	
	protected final String keyindexname1	= ".keyindex1";
	
	protected final String keyname1			= ".keyname1";
	
	protected final String speratername		= ".sperater";
	
	protected String field1					= "alarmText";
	
	protected String keyindex1				= "1";
	
	protected String key1					= "{P1}";
	
	protected String sperater				= null;
	
	protected int keyindex					= 0;
	
	protected Map<String, String> mappings	= new HashMap<String, String>();
	
	@Override
	public String getComputerId() {
		return this.getClass().getName();
	}
	
	protected String[] split(String regexp, String raw) {
		String [] content = {};
		if ( null != raw ) {
			content = raw.split( regexp );
		} else {
			s_logger.warn("split content IS NULL");
			s_logger.warn("split regexp["+regexp+"] rawMessage["+raw+"]");
		}
		return content;
	}
	
	public GDGMessage () {
    	
    	String classnames [] = getComputerId().split(Pattern.quote("."));
    	propertiesname = classnames[classnames.length-1];
    	
    	s_logger.info(propertiesname+" getComputerId["+getComputerId()+"]");
    	s_logger.info(propertiesname+" propertiesname["+propertiesname+"]");
    	
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
		
		keyindex1 = mappings.get(propertiesname+keyindexname1);
		
		key1 = mappings.get(propertiesname+keyname1);
		
		sperater = mappings.get(propertiesname+speratername);
		
		s_logger.info(propertiesname+fieldname1+	" field1["+field1+"]");
		
		s_logger.info(propertiesname+keyindexname1+	" keyindex1["+keyindex1+"]");
		
		s_logger.info(propertiesname+keyname1+		" key1["+key1+"]");
		
		s_logger.info(propertiesname+speratername+	" sperater["+sperater+"]");
    	
		keyindex = Integer.parseInt(keyindex1);
		
		s_logger.info(propertiesname+ " keyindex["+keyindex+"]");
		
		m_statusSet.add(field1);
		
	}
	
	@Override
    public AttributeClientAbstract<?> compute(OperatorOpmInfo operatorOpmInfo, String entityId,
            Map<String, AttributeClientAbstract<?>> inputStatusByName, Map<String, Object> inputPropertiesByName)

    { 
		
		s_logger.info(propertiesname+" compute getComputerId()["+getComputerId()+"]");
		
    	s_logger.info(propertiesname+" compute Begin");
    	s_logger.info(propertiesname+" compute field1["+field1+"]");
    	
    	{
    		s_logger.info("compute PRINT operatorOpmInfo["+operatorOpmInfo+"]");
    		
    		s_logger.info("compute PRINT entityId["+entityId+"]");
    		
	    	for ( String statusname : inputStatusByName.keySet() ) {
	    		s_logger.info("compute PRINT inputStatusByName statusname["+statusname+"] value["+inputStatusByName.get(statusname)+"]");
	    	}
	    	for ( String statusname : inputPropertiesByName.keySet() ) {
	    		s_logger.info("compute PRINT inputPropertiesByName statusname["+statusname+"] value["+inputPropertiesByName.get(statusname)+"]");
	    	}
		}
    	
    	// Load eqpType value
    	String inValue1 = null;
    	AttributeClientAbstract<?> obj1 = null;
    	{
    		 obj1 = inputStatusByName.get(field1);
    		 
    		 if ( null != obj1 ) {
    			 if ( obj1 instanceof StringAttribute ) {
    				 inValue1 = ((StringAttribute) obj1).getValue();
    			 } else if ( obj1 instanceof MapStringByStringAttribute ) {
 					Map<String,String> map = ((MapStringByStringAttribute) obj1).getValue();
        		    if ( null != map ) {
        		    	inValue1 = map.get( key1 );
        		    } else {
        		    	s_logger.info(propertiesname+" compute MapStringByStringAttribute getValue IS NULL");
        		    }
    			 } else {
    				 s_logger.warn(propertiesname+" compute field1 TYPE IS UNKNOW");
    			 }
    			 
    		 } else {
 	    		s_logger.warn(propertiesname+" compute field1["+field1+"] IS NULL");
 	    	}
		}
    	s_logger.info(propertiesname+" compute inValue1["+inValue1+"]");
    	
    	String rawTokens [] = null;
    	String inValueType = null;
    	{
    		rawTokens = split(sperater, inValue1);
    		inValueType = rawTokens[keyindex];
    	}
    	s_logger.warn(propertiesname+" compute inValueType["+inValueType+"]");
    	
//    	{
//    		for ( String rawToken : rawTokens ) {
//    			s_logger.info("compute PRINT rawToken["+rawToken+"]");
//    		}
//    	}
    	
    	String configPrefix = propertiesname;
    	
    	// Find mapping
    	String inValueFormat = null;
    	{
    		String keyToMap = null;
    		try {
    			keyToMap = configPrefix+mappingname+inValueType;
    		} catch ( Exception e ) {
    			s_logger.warn(propertiesname+" compute Integer.toString Exception["+e.toString()+"]");
    		}
	    	s_logger.info(propertiesname+" compute keyToMap["+keyToMap+"]");
	    	inValueFormat = mappings.get(keyToMap);
    	}
    	s_logger.info(propertiesname+" compute outValue1["+inValueFormat+"]");
    	
    	
    	String outValue1 = null;
    	{
    		try {
    			outValue1 = String.format(inValueFormat, (Object[])rawTokens);
    		} catch ( Exception e) {
    			s_logger.warn(propertiesname+" compute String.format Exception["+e.toString()+"]");
    		}
    	}
    	
    	// Return value       
		StringAttribute ret = new StringAttribute();
		ret.setAttributeClass(StringAttribute.class.getName());
		ret.setTimestamp((obj1 != null) ? obj1.getTimestamp() : new Date());
		ret.setValid(obj1.isValid());
		ret.setValue(outValue1);

        s_logger.info(propertiesname+" compute outValue1["+outValue1+"]");
        s_logger.info(propertiesname+" compute ret.getValue()["+ret.getValue()+"]");
        s_logger.info(propertiesname+" compute End");
		
		return ret;
    }
}
