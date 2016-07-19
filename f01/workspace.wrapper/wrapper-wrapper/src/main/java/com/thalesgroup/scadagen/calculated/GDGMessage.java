package com.thalesgroup.scadagen.calculated;

import java.util.Date;
import java.util.HashMap;
import java.util.IllegalFormatException;
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
import com.thalesgroup.scadagen.calculated.common.SCSStatusComputer;


/**
 * @author syau
 *
 */
public abstract class GDGMessage extends SCSStatusComputer {
	
	private final Logger s_logger			= LoggerFactory.getLogger(GDGMessage.class.getName());
	
	protected String classname				= null;
	
	protected String logPrefix				= null;
	
	protected final String mappingname		= ".valuemapping.";
	
	protected final String fieldname1		= ".fieldname1";
	
	protected final String keyindexname1	= ".keyindex1";
	
	protected final String keyname1			= ".keyname1";
	
	protected final String speratername		= ".sperater";
	
	protected String field1					= null;
	
	protected String keyindex1				= null;
	
	protected String key1					= null;
	
	protected String sperater				= null;
	
	protected int keyindex					= -1;
	
	protected Map<String, String> mappings	= new HashMap<String, String>();
	
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.calculated.SCSStatusComputer#getComputerId()
	 */
	@Override
	public String getComputerId() {
		return this.getClass().getName();
	}
	
	/**
	 * Split input string
	 * @param regexp : Regexp Rule to
	 * @param raw : RAW String
	 * @return String array or null in regexp exception
	 */
	public String[] split(String regexp, String raw) {
		String [] content = {};
		if ( null != raw ) {
			content = raw.split( regexp );
		} else {
			s_logger.warn("{} split content IS NULL", logPrefix);
			s_logger.warn("{} split regexp[{}] rawMessage[{}]", new Object[]{logPrefix, regexp, raw});
		}
		return content;
	}
	
	/**
	 * Check the string is a valid integer.
	 * @param str
	 * @return true for is integer, otherwise return false
	 */
	public boolean isInteger(String str) {
	    if (str == null) {
	        return false;
	    }
	    int length = str.length();
	    if (length == 0) {
	        return false;
	    }
	    int i = 0;
	    if (str.charAt(0) == '-') {
	        if (length == 1) {
	            return false;
	        }
	        i = 1;
	    }
	    for (; i < length; i++) {
	        char c = str.charAt(i);
	        if (c < '0' || c > '9') {
	            return false;
	        }
	    }
	    return true;
	}
	
	/**
	 * Loading and store the configuration according to the class name as prefix
	 */
	public GDGMessage () {
		
    	String classnames [] = getComputerId().split(Pattern.quote("."));
    	classname = classnames[classnames.length-1];
    	logPrefix = classname;
    	
    	s_logger.info("{} getComputerId[{}]", logPrefix, getComputerId());
    	s_logger.info("{} classname[{}]", logPrefix, classname);
    	
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
		
		field1		= mappings.get(fieldname1);
		keyindex1	= mappings.get(keyindexname1);
		key1		= mappings.get(keyname1);
		sperater	= mappings.get(speratername);
		
		s_logger.info("{} {} field1[{}]", 		new Object[]{logPrefix,fieldname1,field1});
		s_logger.info("{} {} keyindex1[{}]", 	new Object[]{logPrefix,keyindexname1,keyindex1});
		s_logger.info("{} {} key1[{}]", 		new Object[]{logPrefix,keyname1,key1});
		s_logger.info("{} {} sperater[{}]", 	new Object[]{logPrefix,sperater,sperater});

		if ( isInteger(keyindex1) ) {
			keyindex = Integer.parseInt(keyindex1);
		} else {
			s_logger.warn("{} keyindex[{}]", logPrefix, keyindex);
		}
		
		s_logger.info("{} keyindex[{}]", logPrefix, keyindex);
		
		m_statusSet.add(field1);
		
	}
	
	/**
	 * Value in getMapStringByStringAttribute
	 * @param obj: AttributeClientAbstract<String> object
	 * @param key: for MapStringByStringAttribute
	 * @return String value
	 */
	protected String getMapStringByStringAttribute(AttributeClientAbstract<?> obj, String key1) {
		String result = null;
		if ( null != obj ) {
			Map<String,String> map = ((MapStringByStringAttribute) obj).getValue();
			if ( null != map ) {
				result = map.get( key1 );
			} else {
				s_logger.info("{} getStringAttribute MapStringByStringAttribute getValue IS NULL", logPrefix);
			}
		} else {
			s_logger.warn("{} getStringAttribute AttributeClientAbstract IS NULL", logPrefix);
		}
		return result;
	}
	
	/**
	 * Print and Join the input keyset (String)
	 * @param inputStatusByName
	 * @param inputPropertiesByName
	 */
	private String printAndJoin(Map<String, AttributeClientAbstract<?>> inputStatusByName, Map<String, Object> inputPropertiesByName) {
		String value = "";
		if ( null != inputStatusByName ) {
			for ( String statusname : inputStatusByName.keySet() ) {
				s_logger.warn("{} printInputStatusByName PRINT inputStatusByName statusname[{}] value[{}]", new Object[]{logPrefix, statusname, inputStatusByName.get(statusname)});
				value += statusname+":"+inputStatusByName.get(statusname);
				
			}
		} else {
			
		}
		if ( null != value && 0 == value.length() ) {
			value += "|";
		}
		if ( null != inputPropertiesByName ) {
			for ( String statusname : inputPropertiesByName.keySet() ) {
				s_logger.info("{} printInputStatusByName PRINT inputStatusByName statusname[{}] value[{}]", new Object[]{logPrefix, statusname, inputPropertiesByName.get(statusname)});
				value += statusname+":"+inputPropertiesByName.get(statusname);
			}
		} else {
			s_logger.warn("{} printInputStatusByName PRINT inputPropertiesByName IS NULL");
		}
		return value;
	}
	
	/**
	 * Print Array Value and Join
	 * @param values: String Array
	 * @return Joined String
	 */
	private void printStringArray(String [] rawTokens) {
		if ( null != rawTokens ) {
			for ( String rawToken : rawTokens ) {
				s_logger.info("{} printStringArray PRINT rawToken[{}]", logPrefix, rawToken);
			}
		} else {
			s_logger.info("{} printStringArray PRINT rawToken IS NULL", logPrefix);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.thalesgroup.hypervisor.mwt.core.webapp.core.computers.StatusComputer#compute(com.thalesgroup.hypervisor.mwt.core.webapp.core.opm.client.dto.OperatorOpmInfo, java.lang.String, java.util.Map, java.util.Map)
	 */
	@Override
    public AttributeClientAbstract<?> compute(OperatorOpmInfo operatorOpmInfo, String entityId,
            Map<String, AttributeClientAbstract<?>> inputStatusByName, Map<String, Object> inputPropertiesByName)

    { 
		s_logger.info("{} compute Begin", logPrefix);
		s_logger.info("{} compute getComputerId()[{}]", logPrefix, getComputerId());
    	s_logger.info("{} compute field1[{}]", logPrefix, field1);
    	
		s_logger.info("{} compute PRINT operatorOpmInfo[{}]", logPrefix, operatorOpmInfo);
		s_logger.info("{} compute PRINT entityId[{}]", logPrefix, entityId);
		
		boolean isvalid = false;
		
		// Return String value
		String outValue1 = null;
		AttributeClientAbstract<?> obj1 = null;
		
		if ( keyindex >= 0 ) {

	    	obj1 = inputStatusByName.get(field1);
	    	
	    	if ( null != obj1 ) {
	    		
	    		// Load eqpType value
	    		String inValue1 = null;
	        	if ( obj1 instanceof StringAttribute ) {
	        		inValue1 = ((StringAttribute) obj1).getValue();
	        	} else if ( obj1 instanceof MapStringByStringAttribute ) {
	        		inValue1 = getMapStringByStringAttribute(obj1, key1);
	        	} else {
	    			s_logger.warn("{} getStringAttribute AttributeClientAbstract IS NULL", logPrefix);
	    		}
	        	s_logger.info("{} compute inValue1[{}]", logPrefix, inValue1);
	        	
	        	if ( null != inValue1 ) {
	            	String rawTokens []	= split(sperater, inValue1);
	            	
	            	if ( null != rawTokens ) {
	                	
	                	String inValueType	= rawTokens[keyindex];
	                	s_logger.warn("{} compute inValueType[{}]", logPrefix, inValueType);
	                
	                	// Find mapping
	                	String keyToMap = mappingname+inValueType;
	            	    s_logger.info("{} compute keyToMap[{}]", logPrefix, keyToMap);
	            	    
	            	    String inValueFormat = mappings.get(keyToMap);
	                	s_logger.info("{} compute inValueFormat[{}]", logPrefix, inValueFormat);
	                	
	                	if ( null != inValueFormat ) {
		                	try {
		                		outValue1 = String.format(inValueFormat, (Object[])rawTokens);
		                		isvalid = true;
		                	} catch ( IllegalFormatException e) {
		                		s_logger.warn("{} compute Exception String.format inValueFormat[{}]", logPrefix, inValueFormat);
		                		s_logger.warn("{} compute String.format Exception[{}]", logPrefix, e.toString());
		                		printStringArray(rawTokens);
		                	}
		                	
	                	} else {
	                		s_logger.info("{} compute inValueFormat IS NULL", logPrefix);
	                	}
	            	} else {
	            		s_logger.info("{} compute inValue1[{}]", logPrefix, inValue1);
	            	}
	        	}
	    	} else {
	    		s_logger.info("{} compute obj1 IS NULL", logPrefix);
	    	}
	    } else {
			s_logger.info("{} compute keyindex IS INVALID", logPrefix);
		}
		
		// Print original value if invalid
		if ( !isvalid ) {
			s_logger.info("{} compute process IS INVALID", logPrefix);
			outValue1 = printAndJoin(inputStatusByName, inputPropertiesByName);
		}

    	// Return value
		StringAttribute ret = new StringAttribute();
		ret.setAttributeClass(StringAttribute.class.getName());
		ret.setTimestamp((obj1 != null) ? obj1.getTimestamp() : new Date());
		ret.setValid(true);
//		ret.setValid(obj1.isValid());
		ret.setValue(outValue1);

        s_logger.info("{} compute ret.getValue()[{}]", logPrefix, ret.getValue());
        s_logger.info("{} compute End", logPrefix);
		
		return ret;
    }
}
