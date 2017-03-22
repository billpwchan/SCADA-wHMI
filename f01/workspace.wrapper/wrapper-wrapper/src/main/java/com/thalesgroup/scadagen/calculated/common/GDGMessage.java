package com.thalesgroup.scadagen.calculated.common;

import java.util.Date;
import java.util.HashMap;
import java.util.IllegalFormatException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thalesgroup.hypervisor.mwt.core.util.config.loader.IConfigLoader;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.data.server.rpc.implementation.ServicesImplFactory;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.opm.client.dto.OperatorOpmInfo;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.AttributeClientAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.MapStringByStringAttribute;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.StringAttribute;
import com.thalesgroup.scadagen.wrapper.wrapper.server.Translation;


/**
 * @author syau
 *
 */
public abstract class GDGMessage extends SCSStatusComputer {

	protected Logger logger					= null;
	
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
			logger.error("{} split content IS NULL", logPrefix);
			logger.error("{} split regexp[{}] rawMessage[{}]", new Object[]{logPrefix, regexp, raw});
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
	protected void loadCnf() {

		logger = LoggerFactory.getLogger(GDGMessage.class.getName());
		
    	logPrefix = m_name;
    	
    	logger.debug("{} getComputerId[{}]", logPrefix, getComputerId());
    	
    	IConfigLoader configLoader		= ServicesImplFactory.getInstance().getService(IConfigLoader.class);
		Map<String,String> properties	= configLoader.getProjectConfigurationMap();
		if (properties != null) {
			int classNameLength = m_name.length();
			// Load all setting with class prefix into buffer
			for ( String key : properties.keySet() ) {
				if ( key.startsWith(m_name) ) {
					String keyname = key.substring(classNameLength);
					mappings.put(keyname, properties.get(key));
				}
			}
		}
		
		field1		= mappings.get(fieldname1);
		keyindex1	= mappings.get(keyindexname1);
		key1		= mappings.get(keyname1);
		sperater	= mappings.get(speratername);
		
		if ( logger.isDebugEnabled() ) {
			logger.debug("{} {} field1[{}]", 		new Object[]{logPrefix,fieldname1,field1});
			logger.debug("{} {} keyindex1[{}]", 	new Object[]{logPrefix,keyindexname1,keyindex1});
			logger.debug("{} {} key1[{}]", 			new Object[]{logPrefix,keyname1,key1});
			logger.debug("{} {} sperater[{}]", 		new Object[]{logPrefix,sperater,sperater});
		}
		
		if ( isInteger(keyindex1) ) {
			keyindex = Integer.parseInt(keyindex1);
		} else {
			logger.error("{} keyindex[{}]", logPrefix, keyindex);
		}
		
		logger.debug("{} keyindex[{}]", logPrefix, keyindex);
		
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
				logger.debug("{} getStringAttribute MapStringByStringAttribute getValue IS NULL", logPrefix);
			}
		} else {
			logger.error("{} getStringAttribute AttributeClientAbstract IS NULL", logPrefix);
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
				logger.debug("{} printInputStatusByName PRINT inputStatusByName statusname[{}] value[{}]", new Object[]{logPrefix, statusname, inputStatusByName.get(statusname)});
				value += statusname+":"+inputStatusByName.get(statusname);
				
			}
		} else {
			
		}
		if ( null != value && 0 == value.length() ) {
			value += "|";
		}
		if ( null != inputPropertiesByName ) {
			for ( String statusname : inputPropertiesByName.keySet() ) {
				logger.debug("{} printInputStatusByName PRINT inputStatusByName statusname[{}] value[{}]", new Object[]{logPrefix, statusname, inputPropertiesByName.get(statusname)});
				value += statusname+":"+inputPropertiesByName.get(statusname);
			}
		} else {
			logger.error("{} printInputStatusByName PRINT inputPropertiesByName IS NULL", logPrefix);
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
				logger.debug("{} printStringArray PRINT rawToken[{}]", logPrefix, rawToken);
			}
		} else {
			logger.error("{} printStringArray PRINT rawToken IS NULL", logPrefix);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.thalesgroup.hypervisor.mwt.core.webapp.core.computers.StatusComputer#compute(com.thalesgroup.hypervisor.mwt.core.webapp.core.opm.client.dto.OperatorOpmInfo, java.lang.String, java.util.Map, java.util.Map)
	 */
	@Override
    public AttributeClientAbstract<?> compute(OperatorOpmInfo operatorOpmInfo, String entityId,
            Map<String, AttributeClientAbstract<?>> inputStatusByName, Map<String, Object> inputPropertiesByName)

    { 
		logger.debug("{} compute m_name[{}], field1[{}]", new Object[]{logPrefix, m_name, field1});
    	
//		logger.debug("{} compute PRINT operatorOpmInfo[{}]", logPrefix, operatorOpmInfo);
//		logger.debug("{} compute PRINT entityId[{}]", logPrefix, entityId);
//		
//		for ( AttributeClientAbstract<?> obj1 : inputStatusByName.values() ) {
//			if ( obj1 instanceof StringAttribute ) {
//				logger.warn("{} compute inputStatusByName obj1[{}]", logPrefix, ((StringAttribute) obj1).getValue());
//			} else {
//				logger.warn("{} compute inputStatusByName obj1 IS NOT instanceof StringAttribute ");
//			}
//		}
//
//		for ( String key : inputPropertiesByName.keySet() ) {
//			logger.warn("{} compute inputPropertiesByName key[{}] value[{}] ", new Object[]{logPrefix, key, inputPropertiesByName.get(key)});
//		}
		
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
	    			logger.warn("{} getStringAttribute AttributeClientAbstract IS NULL", logPrefix);
	    		}
	        	logger.debug("{} compute inValue1[{}]", logPrefix, inValue1);
	        	
	        	if ( null != inValue1 ) {
	        		
	        		inValue1 = Translation.getDBMessage(inValue1);
	        		
	            	String rawTokens []	= split(sperater, inValue1);
	            	
	            	if ( null != rawTokens ) {
	            		
	            		if ( rawTokens.length > keyindex) {
		                	String inValueType	= rawTokens[keyindex];
		                	logger.debug("{} compute inValueType[{}]", logPrefix, inValueType);
		                
		                	// Find mapping
		                	String keyToMap = mappingname+inValueType;
		            	    logger.debug("{} compute keyToMap[{}]", logPrefix, keyToMap);
		            	    
		            	    String inValueFormat = mappings.get(keyToMap);
		                	logger.debug("{} compute inValueFormat[{}]", logPrefix, inValueFormat);
		                	
		                	if ( null != inValueFormat ) {
			                	try {
			                		outValue1 = String.format(inValueFormat, (Object[])rawTokens);
			                		isvalid = true;
			                	} catch ( IllegalFormatException e) {
			                		logger.warn("{} compute Exception String.format inValueFormat[{}]", logPrefix, inValueFormat);
			                		logger.warn("{} compute String.format Exception[{}]", logPrefix, e.toString());
			                		if ( logger.isDebugEnabled() ) printStringArray(rawTokens);
			                	}
		                	} else {
		                		logger.error("{} compute inValueFormat IS NULL", logPrefix);
		                	}
	            		} else {
	            			logger.error("{} compute rawTokens.length[{}] <= keyindex[{}]", new Object[]{logPrefix, rawTokens.length, keyindex});
	            		}
	            	} else {
	            		logger.error("{} compute inValue1[{}]", logPrefix, inValue1);
	            	}
	        	}
	    	} else {
	    		logger.error("{} compute obj1 IS NULL", logPrefix);
	    	}
	    } else {
			logger.error("{} compute keyindex IS INVALID", logPrefix);
		}
		
		// Print original value if invalid
		if ( !isvalid ) {
			logger.debug("{} compute process IS INVALID", logPrefix);
			outValue1 = printAndJoin(inputStatusByName, inputPropertiesByName);
		}

    	// Return value
		StringAttribute ret = new StringAttribute();
		ret.setAttributeClass(StringAttribute.class.getName());
		ret.setTimestamp((obj1 != null) ? obj1.getTimestamp() : new Date());
		ret.setValid(true);
//		ret.setValid(obj1.isValid());
		ret.setValue(outValue1);

        logger.debug("{} compute ret.getValue()[{}]", logPrefix, ret.getValue());
		
		return ret;
    }
}
