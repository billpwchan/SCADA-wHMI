package com.thalesgroup.scadagen.wrapper.wrapper.client.opm.controlpriority;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.thalesgroup.scadagen.whmi.config.configenv.client.JSONUtil;
import com.thalesgroup.scadagen.whmi.config.configenv.client.ReadJson;
import com.thalesgroup.scadagen.whmi.config.configenv.client.ReadJsonFile;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseMultiRead_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabasePairEvent_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseWrite_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.factory.DatabaseMultiReadFactory;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.factory.DatabaseWriteFactory;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.util.DatabaseKey;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.OpmMgr;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.controlpriority.UIControlPrioritySCADAgen_i.AttributeLevel;

public class UIControlPrioritySCADAgen implements UIControlPriority_i {

	private final String className = this.getClass().getSimpleName();
	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
	private String logPrefix = null;
	
	private static Map<String, UIControlPriority_i> instanceList = new HashMap<String, UIControlPriority_i>();
	public static UIControlPriority_i getInstance(String key) { 
		if ( null == instanceList.get(key) ) instanceList.put(key, new UIControlPrioritySCADAgen(key));
		return instanceList.get(key);
	}
	
	private String cfgAttributeName = null;
	private String cfgLevelName = null;
	private UIControlPrioritySCADAgen (String key) {
		final String f = "UIControlPrioritySCADAgen";
		logger.debug(f+logPrefix, "key[{}]", key);
		
		logPrefix = " (" + key + ") ";
		
		cfgAttributeName = 
				UIControlPrioritySCADAgen_i.FILE_NAME_PREFIX_ATTRIBUTE 
				+ key 
				+ UIControlPrioritySCADAgen_i.FILE_NAME_APPEND_ATTRIBUTE
				+ UIControlPrioritySCADAgen_i.FILE_NAME_EXTENSION;
		cfgLevelName = 
				UIControlPrioritySCADAgen_i.FILE_NAME_PREFIX_LEVEL
				+ key 
				+ UIControlPrioritySCADAgen_i.FILE_NAME_APPEND_LEVEL
				+ UIControlPrioritySCADAgen_i.FILE_NAME_EXTENSION;
		
		logger.debug(f+logPrefix, "key[{}] cfgAttributeName[{}] cfgLevelName[{}]", new Object[]{key, cfgAttributeName, cfgLevelName});
	}
	
	// TODO: currently hard-coded. Support "o" for operator name (username), or "p" for profile.
	private String identifierKey_ = "o";
	
	private final String IDENTIFY_OPERATOR_KEY = "o";
	private final String IDENTIFY_PROFILE_KEY = "p";
	
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.opm.controlpriority.UIControlPriority_i#requestReservation(java.lang.String, java.lang.String)
	 */
	@Override
	public void requestReservation(final String scsEnvId, final String dbAddress, final UIControlPriorityCallback callBack) {
		String f = "requestReservation";
		logger.begin(f+logPrefix);

		// Using hard-coded value first, originally using "getUsrIdentity()"
		String reservationKey = getReservationKey();
		
		requestReservation(scsEnvId, dbAddress, reservationKey, callBack);

		logger.end(f+logPrefix);
	}
	
	/**
	 * Make a Reservation Request on a Equipment
	 * 1. Reservation if the equipment available
	 * 2. 
	 * @param scsEnvId		Target ScsEnvId
	 * @param dbAddress		Target DbAddress
	 * @param usrIdentity	Identity to request the reservation
	 * @param callback		Return JSON String, JSONString Attribute "value" contain the requester (Who to request reservation)
	 */
	private void requestReservation(final String scsEnvId, final String dbAddress, final String usrIdentity, final UIControlPriorityCallback callBack) {
		String f1 = "requestReservation";
		logger.begin(f1+logPrefix);
		
		checkReservationAvailability(scsEnvId, dbAddress, new UIControlPriorityCallback() {
			
			@Override
			public void callBack(String strJson) {
				String f2 = "requestReservation:checkReservationAvailability:callBack";
				logger.begin(f2+logPrefix);

				int ret = ReadJson.readInt(ReadJson.readJson(strJson), UIControlPriority_i.FIELD_VALUE, UIControlPrioritySCADAgen_i.LEVEL_MIN);
				logger.debug(f2+logPrefix, "ret[{}]", ret);
				
				if ( UIControlPriority_i.AVAILABILITY_ERROR == ret ) {
					
					logger.warn(f2+logPrefix, "Reserve equipment, scsEnvId[{}] dbAddress[{}] ERROR", new Object[]{scsEnvId, dbAddress});
					
					if ( null != callBack ) {
						JSONObject jsObject = new JSONObject();
						jsObject.put(UIControlPriority_i.FIELD_VALUE, new JSONString(usrIdentity));
						jsObject.put(UIControlPriority_i.FIELD_CODE, new JSONNumber(UIControlPriority_i.REQUEST_ERROR_CHECKING));
						String strJson2 = jsObject.toString();
						logger.debug(f2+logPrefix, "strJson2[{}]", strJson2);
						callBack.callBack(strJson2);
					} else {
						logger.debug(f2+logPrefix, "callBack IS NULL");
					}
					
				} else if ( 
						UIControlPriority_i.AVAILABILITY_EMPTY == ret
						|| UIControlPriority_i.AVAILABILITY_RESERVED_BYSELF == ret 
						) {
					
					logger.debug(f2+logPrefix, "AVAILABILITY_RESERVED_BYSELF getCodeString({})[{}]", new Object[]{ret, getCheckReservationAvailabilityCodeString(ret)});
					
					String alias = dbAddress + getResrvReserveReqID();
					String key = databaseKey.getKey(className, f2, scsEnvId, alias, usrIdentity);
					
					logger.debug(f2+logPrefix, "scsEnvId[{}] alias[{}] usrIdentity[{}] key[{}]", new Object[]{scsEnvId, alias, usrIdentity, key});
					databaseWrite_i.addWriteStringValueRequest(key, scsEnvId, alias, usrIdentity);
					
					if ( null != callBack ) {
						JSONObject jsObject = new JSONObject();
						jsObject.put(UIControlPriority_i.FIELD_VALUE, new JSONString(usrIdentity));
						jsObject.put(UIControlPriority_i.FIELD_CODE, new JSONNumber(UIControlPriority_i.REQUEST_REQUESTED));
						String strJson2 = jsObject.toString();
						logger.debug(f2+logPrefix, "strJson2[{}]", strJson2);
						callBack.callBack(strJson2);
					} else {
						logger.debug(f2+logPrefix, "callBack IS NULL");
					}
				}
				else if ( UIControlPriority_i.AVAILABILITY_ALLOW_WITH_OVERRIDE == ret ) {
					
					logger.debug(f2+logPrefix, "AVAILABILITY_ALLOW_WITH_OVERRIDE getCodeString({})[{}]", new Object[]{ret, getCheckReservationAvailabilityCodeString(ret)});
					
					forceWithdrawReservation(scsEnvId, dbAddress, new UIControlPriorityCallback() {

						@Override
						public void callBack(String json) {
							String f3 = "requestReservation:checkReservationAvailability:callBack:forceWithdrawReservation:callBack";
							logger.begin(f3+logPrefix);
							
							String alias = dbAddress + getResrvReserveReqID();
							String key = databaseKey.getKey(f3+logPrefix, scsEnvId, alias, usrIdentity);
							
							logger.debug(f3+logPrefix, "scsEnvId[{}] alias[{}] usrIdentity[{}] key[{}]", new Object[]{scsEnvId, alias, usrIdentity, key});
							databaseWrite_i.addWriteStringValueRequest(key, scsEnvId, alias, usrIdentity);
							
							if ( null != callBack ) {
								JSONObject jsObject = new JSONObject();
								jsObject.put(UIControlPriority_i.FIELD_VALUE, new JSONString(usrIdentity));
								jsObject.put(UIControlPriority_i.FIELD_CODE, new JSONNumber(UIControlPriority_i.REQUEST_OVERRIDE_WITH_FORCE_WITHDRAW));
								String strJson3 = jsObject.toString();
								logger.debug(f3+logPrefix, "strJson3[{}]", strJson3);
								callBack.callBack(strJson3);
							} else {
								logger.debug(f3+logPrefix, "callBack IS NULL");
							}
							
							logger.end(f3+logPrefix);
						}
						
					});

				} 
				
				else if ( UIControlPriority_i.AVAILABILITY_DENIED == ret ) {
					
					logger.debug(f2+logPrefix, "Not enough right to reserver equipment, scsEnvId[{}] dbAddress[{}]", new Object[]{scsEnvId, dbAddress});
					
					if ( null != callBack ) {
						JSONObject jsObject = new JSONObject();
						jsObject.put(UIControlPriority_i.FIELD_VALUE, new JSONString(usrIdentity));
						jsObject.put(UIControlPriority_i.FIELD_CODE, new JSONNumber(UIControlPriority_i.REQUEST_REJECTED_AT_LOWER_LEVEL));
						String strJson2 = jsObject.toString();
						logger.debug(f2+logPrefix, "strJson2[{}]", strJson2);
						callBack.callBack(strJson2);
					} else {
						logger.debug(f2+logPrefix, "callBack IS NULL");
					}
				}
				else if ( UIControlPriority_i.AVAILABILITY_EQUAL == ret ) {
					
					logger.debug(f2+logPrefix, "Not enough right to reserver equipment, scsEnvId[{}] dbAddress[{}]", new Object[]{scsEnvId, dbAddress});
					
					if ( null != callBack ) {
						JSONObject jsObject = new JSONObject();
						jsObject.put(UIControlPriority_i.FIELD_VALUE, new JSONString(usrIdentity));
						jsObject.put(UIControlPriority_i.FIELD_CODE, new JSONNumber(UIControlPriority_i.REQUEST_REJECTED_AT_SAME_LEVEL));
						String strJson2 = jsObject.toString();
						logger.debug(f2+logPrefix, "strJson2[{}]", strJson2);
						callBack.callBack(strJson2);
					} else {
						logger.debug(f2+logPrefix, "callBack IS NULL");
					}
				}
				else {
	
					logger.warn(f2+logPrefix, "Reserver equipment, scsEnvId[{}] dbAddress[{}] UNKNOW ERROR", new Object[]{scsEnvId, dbAddress});
					
					if ( null != callBack ) {
						JSONObject jsObject = new JSONObject();
						jsObject.put(UIControlPriority_i.FIELD_VALUE, new JSONString(usrIdentity));
						jsObject.put(UIControlPriority_i.FIELD_CODE, new JSONNumber(UIControlPriority_i.REQUEST_ERROR_UNKNOW));
						String strJson2 = jsObject.toString();
						logger.debug(f2+logPrefix, "strJson2[{}]", strJson2);
						callBack.callBack(strJson2);
					} else {
						logger.debug(f2+logPrefix, "callBack IS NULL");
					}
	
				}

				logger.end(f2+logPrefix);
			}
		});
		
		logger.end(f1+logPrefix);
	}

	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.opm.controlpriority.UIControlPriority_i#withdrawReservation(java.lang.String, java.lang.String)
	 */
	@Override
	public void withdrawReservation(final String scsEnvId, final String dbAddress, final UIControlPriorityCallback callBack) {
		String f = "withdrawReservation";
		logger.begin(f+logPrefix);

		// Using hard-coded value first, originally using "getUsrIdentity()"
		String reservationKey = getReservationKey();
		
		withdrawReservation(scsEnvId, dbAddress, reservationKey, callBack);
		
		logger.end(f+logPrefix);
	}
	
	/**
	 * @param scsEnvId		Target ScsEnvId
	 * @param dbAddress		Target DbAddress
	 * @param usrIdentity	Identity to request the reservation
	 * @param callback		Return JSON String, JSONString Attribute "value" contain the withdrawer (Value to Withdraw the Reservation)
	 */
	private void withdrawReservation(final String scsEnvId, final String dbAddress, final String usrIdentity, final UIControlPriorityCallback callBack) {
		String f = "withdrawReservation";
		logger.begin(f+logPrefix);
		
		String alias = dbAddress + getResrvUnreserveReqID();
		String key = databaseKey.getKey(f+logPrefix, scsEnvId, alias, usrIdentity);
		
		logger.debug(f+logPrefix, "scsEnvId[{}] alias[{}] value[{}] key[{}]", new Object[]{scsEnvId, alias, usrIdentity, key});
		databaseWrite_i.addWriteStringValueRequest(key, scsEnvId, alias, usrIdentity);
		
		if ( null != callBack ) {
			JSONObject jsObject = new JSONObject();
			jsObject.put(UIControlPriority_i.FIELD_VALUE, new JSONString(usrIdentity));
			jsObject.put(UIControlPriority_i.FIELD_CODE, new JSONNumber(UIControlPriority_i.WITHDRAW_REQUESTED));
			String strJson = jsObject.toString();
			logger.debug(f+logPrefix, "strJson[{}]", strJson);
			callBack.callBack(strJson);
		} else {
			logger.debug(f+logPrefix, "callBack IS NULL");
		}

		logger.end(f+logPrefix);
	}

	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.opm.controlpriority.UIControlPriority_i#getCurrentReservationBy(java.lang.String, java.lang.String, com.thalesgroup.scadagen.wrapper.wrapper.client.opm.controlpriority.UIControlPriority_i.UIControlPriorityCallback)
	 */
	@Override
	public void getCurrentReservationBy(final String scsEnvId, final String dbAddress, final UIControlPriorityCallback callBack) {
		String f1 = "getCurrentReservationBy";
		logger.begin(f1+logPrefix);
		String alias = dbAddress + getResrvReserveID();
		String dbAddresses [] = new String[]{alias};
		String key = databaseKey.getKey(className, f1, scsEnvId, dbAddress);
		logger.debug(f1+logPrefix, "scsEnvId[{}] alias[{}] key[{}]", new Object[]{scsEnvId, alias, key});
		databaseMultiRead_i.addMultiReadValueRequest(key, scsEnvId, dbAddresses, new DatabasePairEvent_i() {
			
			@Override
			public void update(final String key, final String[] dbAddresses, final String[] values) {
				String f2 = "getCurrentReservationBy:update";
				logger.begin(f2+logPrefix);
				
				if ( 
						( null == values) 
						|| ( null != values && values.length == 0 )
						|| ( null != values && values.length > 0 && null == values[0] )
						|| ( null != values && values.length > 0 && null != values[0] && values[0].equals("null") )
						) {
					if ( null != callBack ) {
				    	JSONObject jsObject = new JSONObject();
				    	jsObject.put(UIControlPriority_i.FIELD_VALUE, new JSONString(UIControlPrioritySCADAgen_i.STR_EMPTY));
				    	jsObject.put(UIControlPriority_i.FIELD_CODE, new JSONNumber(UIControlPriority_i.GETCURRENT_READ_INVALID));
				    	String strJson = jsObject.toString();
				    	logger.debug(f2+logPrefix, "strJson[{}]", strJson);
						callBack.callBack(strJson);
					} else {
						logger.debug(f2+logPrefix, "callBack IS NULL");
					}
				} else {
					
					String value = values[0];
					
					// Remove the String double quote
					logger.debug(f2+logPrefix, "BF value[{}]", value);
					if ( value.length() >= 2 ) {
						if ( value.charAt(0) == '\"' && value.charAt(value.length()-1) == '\"' ) {
							value = value.substring(1, value.length()-1);
						}
					}
					
					// Remove escape chars in returning JSON (returning value might contain backslash).
					value = value.replace("\\", "");
					logger.debug(f2+logPrefix, "AF value[{}]", value);
					
					if ( null != callBack ) {
				    	JSONObject jsObject = new JSONObject();
				    	jsObject.put(UIControlPriority_i.FIELD_VALUE, new JSONString(value));
				    	jsObject.put(UIControlPriority_i.FIELD_CODE, new JSONNumber(UIControlPriority_i.GETCURRENT_VALID));
				    	String strJson = jsObject.toString();
				    	logger.debug(f2+logPrefix, "strJson[{}]", strJson);
						callBack.callBack(strJson);
					} else {
						logger.debug(f2+logPrefix, "callBack IS NULL");
					}
				}
				
				logger.end(f2+logPrefix);
			}
		});
		logger.end(f1+logPrefix);
	}
	

	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.opm.controlpriority.UIControlPriority_i#checkReservationLevel(java.lang.String)
	 */
	@Override
	public int checkReservationLevel(String identity) {
		String f = "checkReservationLevel";
		logger.begin(f+logPrefix);
		logger.debug(f+logPrefix, "identity[{}]", identity);
		String extractedIdentity = identity;
		
		if (identity != null && identity != ""){
			identity = identity.replace("\\", "");
			logger.debug(f+logPrefix, "identity after replacing: [{}]", identity);
			if (null!=isJSONFormat(identity)){
				logger.debug(f+logPrefix, "It's JSON format!!!");
				extractedIdentity = getIdentityFromJson(identity, identifierKey_).replace("\"", "");
			} else{
				logger.debug(f+logPrefix, "It's NOT JSON format!!!");
			}
		} else {
			logger.debug(f+logPrefix, "identity is null or Empty!!");
		}
		
		int ret = UIControlPriority_i.LEVEL_ERROR;
		if ( null != extractedIdentity ) {
			if ( extractedIdentity.isEmpty() ) {
				ret = UIControlPriority_i.LEVEL_EMPTY;
			} else {
				String usrIdentity = getUsrIdentity();
				logger.debug(f+logPrefix, "usrIdentity[{}] extractedIdentity[{}]", usrIdentity, extractedIdentity);
				if ( extractedIdentity.equals(usrIdentity) ) {
					ret = UIControlPriority_i.LEVEL_IS_ITSELF;
				} else {
					int levelDiff = compareLevel(usrIdentity, extractedIdentity);
					logger.debug(f+logPrefix, "levelDiff[{}]", levelDiff);
					switch ( levelDiff ) {
					case -1:
						ret = UIControlPriority_i.LEVEL_HIGHER;
						break;
					case 0:
						ret = UIControlPriority_i.LEVEL_EQUAL;
						break;
					case 1:
						ret = UIControlPriority_i.LEVEL_LOWER;
						break;
					default:
						ret = UIControlPriority_i.LEVEL_ERROR;
						break;
					}
				}
			}
			logger.debug(f+logPrefix, "ret[{}]", ret);
		}
		logger.end(f+logPrefix);
		return ret;
	}
	
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.opm.controlpriority.UIControlPriority_i#checkReservationAvailability(java.lang.String)
	 */
	@Override
	public int checkReservationAvailability(String identity) {
		String f = "checkReservationAvailability";
		logger.begin(f+logPrefix);
		int level = checkReservationLevel(identity);
		int ret = UIControlPriority_i.AVAILABILITY_ERROR;
		switch ( level ) {
			case UIControlPriority_i.LEVEL_HIGHER:
				ret = UIControlPriority_i.AVAILABILITY_DENIED;
				break;
			case UIControlPriority_i.LEVEL_EQUAL:
				ret = UIControlPriority_i.AVAILABILITY_EQUAL;
				break;
			case UIControlPriority_i.LEVEL_IS_ITSELF:
				ret = UIControlPriority_i.AVAILABILITY_RESERVED_BYSELF;
				break;
			case UIControlPriority_i.LEVEL_EMPTY:
				ret = UIControlPriority_i.AVAILABILITY_EMPTY;
				break;
			case UIControlPriority_i.LEVEL_LOWER:
				ret = UIControlPriority_i.AVAILABILITY_ALLOW_WITH_OVERRIDE;
				break;
			default:
				ret = UIControlPriority_i.LEVEL_ERROR;
				break;
		}
		logger.debug(f+logPrefix, "ret[{}]", ret);
		logger.end(f+logPrefix);
		return ret;
	}
	
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.opm.controlpriority.UIControlPriority_i#checkReservationAvailability(java.lang.String, java.lang.String, com.thalesgroup.scadagen.wrapper.wrapper.client.opm.controlpriority.UIControlPriority_i.UIControlPriorityCallback)
	 */
	@Override
	public void checkReservationAvailability(final String scsEnvId, final String dbAddress, final UIControlPriorityCallback callBack) {
		String f1 = "checkReservationAvailability";
		logger.begin(f1+logPrefix);
		
		getCurrentReservationBy(scsEnvId, dbAddress, new UIControlPriorityCallback() {
			
			@Override
			public void callBack(String strJson) {
				String f2 = "checkReservationAvailability:callBack";
				logger.begin(f2+logPrefix);
				logger.debug(f2+logPrefix, "strJson[{}]", strJson);
				String value = ReadJson.readString(ReadJson.readJson(strJson), UIControlPriority_i.FIELD_VALUE, null);
				int valid = ReadJson.readInt(ReadJson.readJson(strJson), UIControlPriority_i.FIELD_CODE, -1);
				logger.debug(f2+logPrefix, "value[{}] valid[{}]", value, valid);
				
				if ( UIControlPriority_i.GETCURRENT_READ_INVALID == valid ) {
					if ( null != callBack ) {
					   	JSONObject jsObject = new JSONObject();
					   	jsObject.put(UIControlPriority_i.FIELD_VALUE, new JSONNumber(UIControlPriority_i.AVAILABILITY_ERROR));
					   	String jsonString = jsObject.toString();
					   	logger.debug(f2+logPrefix, "jsonString[{}]", jsonString);
				    	callBack.callBack(jsonString);	
					} else {
						logger.debug(f2+logPrefix, "callBack IS NULL");
					}
				}
				
				// Call the level checking
				int ret = checkReservationAvailability(value);
				
				if ( null != callBack ) {
				   	JSONObject jsObject = new JSONObject();
				   	jsObject.put(UIControlPriority_i.FIELD_VALUE, new JSONNumber(ret));
				   	String jsonString = jsObject.toString();
				   	logger.debug(f2+logPrefix, "jsonString[{}]", jsonString);
			    	callBack.callBack(jsonString);	
				} else {
					logger.debug(f2+logPrefix, "callBack IS NULL");
				}

				logger.end(f2+logPrefix);
			}
		});
		logger.end(f1+logPrefix);
	}

	/**
	 * @param scsEnvId	Target ScsEnvId
	 * @param dbAddress	Target DbAddress
	 * @param callBack	Return JSON String, JSONString Attribute "value" contain the withdrawer (Value to Withdraw the Reservation)
	 */
	public void forceWithdrawReservation(final String scsEnvId, final String dbAddress, final UIControlPriorityCallback callBack) {
		String f1 = "forceWithdrawReservation";
		logger.begin(f1+logPrefix);
		
		getCurrentReservationBy(scsEnvId, dbAddress, new UIControlPriorityCallback() {
			
			@Override
			public void callBack(String strJson1) {
				final String f2 = "forceWithdrawReservation:getCurrentReservationBy:callBack";
				logger.begin(f2+logPrefix);
				
				String value = ReadJson.readString(ReadJson.readJson(strJson1), UIControlPriority_i.FIELD_VALUE, null);
				
				logger.debug(f2+logPrefix, "value[{}]", value);
				
				if ( null != value && ! value.isEmpty() ) {

					withdrawReservation(scsEnvId, dbAddress, value, new UIControlPriorityCallback() {
						
						@Override
						public void callBack(String strJson2) {
							final String f3 = "forceWithdrawReservation:getCurrentReservationBy:callBack:withdrawReservation:callBack";
							logger.begin(f3+logPrefix);
							
							String value = ReadJson.readString(ReadJson.readJson(strJson2), UIControlPriority_i.FIELD_VALUE, null);
							
							logger.debug(f3+logPrefix, "value[{}]", value);
							
							if ( null != callBack) {
								JSONObject jsObject = new JSONObject();
								jsObject.put(UIControlPriority_i.FIELD_VALUE, new JSONString(value));
								String strJson3 = jsObject.toString();
								logger.debug(f3+logPrefix, "strJson3[{}]", strJson3);
								callBack.callBack(strJson3);
							} else {
								logger.debug(f2+logPrefix, "callBack IS NULL");
							}
							
							logger.end(f3+logPrefix);
						}
					});
				} else {
					if ( null != callBack) {
						JSONObject jsObject = new JSONObject();
						jsObject.put(UIControlPriority_i.FIELD_VALUE, new JSONString(UIControlPrioritySCADAgen_i.STR_EMPTY));
						jsObject.put(UIControlPriority_i.FIELD_CODE, new JSONNumber(UIControlPriority_i.FORCE_WITHDRAW_INVALID));
						String strJson3 = jsObject.toString();
						logger.debug(f2+logPrefix, "strJson3[{}]", strJson3);
						callBack.callBack(strJson3);
					} else {
						logger.debug(f2+logPrefix, "callBack IS NULL");
					}
				}
				
				logger.end(f2+logPrefix);
				
			}
			
		});
		
		logger.end(f1+logPrefix);
	}

	/**
	 * Return the Mapped Check Reservation Availability code string
	 * 
	 * @param code code to find mapping
	 * @return mapped string 
	 */
	public String getCheckReservationAvailabilityCodeString(final int code) {
		String f = "getCheckReservationAvailabilityCodeString";
		logger.begin(f+logPrefix);
		logger.debug(f+logPrefix, "code[{}]", code);
		String ret = null;
		if ( code == UIControlPriority_i.AVAILABILITY_ERROR ) {
			ret = UIControlPriority_i.STR_AVAILABILITY_ERROR;
		}
		else if ( code == UIControlPriority_i.AVAILABILITY_DENIED ) {
			ret = UIControlPriority_i.STR_AVAILABILITY_DENIED;
		}
		else if ( code == UIControlPriority_i.AVAILABILITY_EQUAL ) {
			ret = UIControlPriority_i.STR_AVAILABILITY_EQUAL;
		}
		else if ( code == UIControlPriority_i.AVAILABILITY_RESERVED_BYSELF ) {
			ret = UIControlPriority_i.STR_AVAILABILITY_RESERVED_BYSELF;
		}
		else if ( code == UIControlPriority_i.AVAILABILITY_EMPTY ) {
			ret = UIControlPriority_i.STR_AVAILABILITY_EMPTY;
		}
		else if ( code == UIControlPriority_i.AVAILABILITY_ALLOW_WITH_OVERRIDE ) {
			ret = UIControlPriority_i.STR_AVAILABILITY_ALLOW_WITH_OVERRIDE;
		}
		logger.debug(f+logPrefix, "ret[{}]", ret);
		logger.end(f+logPrefix);
		return ret;
	}
	
	/**
	 * Return the Mapped Request Reservation code string
	 * 
	 * @param code code to find mapping
	 * @return mapped string 
	 */
	public String getRequestReservationCodeString(final int code) {
		String f = "getRequestReservationCodeString";
		logger.begin(f+logPrefix);
		logger.debug(f+logPrefix, "code[{}]", code);
		String ret = null;
		if ( code == UIControlPriority_i.REQUEST_ERROR_UNKNOW ) {
			ret = UIControlPriority_i.STR_REQUEST_ERROR_UNKNOW;
		}
		else if ( code == UIControlPriority_i.REQUEST_ERROR_CHECKING ) {
			ret = UIControlPriority_i.STR_REQUEST_ERROR_CHECKING;
		}
		else if ( code == UIControlPriority_i.REQUEST_REQUESTED ) {
			ret = UIControlPriority_i.STR_REQUEST_REQUESTED;
		}
		else if ( code == UIControlPriority_i.REQUEST_OVERRIDE_WITH_FORCE_WITHDRAW ) {
			ret = UIControlPriority_i.STR_REQUEST_OVERRIDE_WITH_FORCE_WITHDRAW;
		}
		else if ( code == UIControlPriority_i.REQUEST_REJECTED_AT_LOWER_LEVEL ) {
			ret = UIControlPriority_i.STR_REQUEST_REJECTED_AT_LOWER_LEVEL;
		}
		else if ( code == UIControlPriority_i.REQUEST_REJECTED_AT_SAME_LEVEL ) {
			ret = UIControlPriority_i.STR_REQUEST_REJECTED_AT_SAME_LEVEL;
		}
		logger.debug(f+logPrefix, "ret[{}]", ret);
		logger.end(f+logPrefix);
		return ret;
	}
	
	/**
	 * Return the Mapped Withdraw Reservation code string
	 * 
	 * @param code code to find mapping
	 * @return mapped string 
	 */
	public String getWithdrawReservationCodeString(final int code) {
		String f = "getWithdrawReservationCodeString";
		logger.begin(f+logPrefix);
		logger.debug(f+logPrefix, "code[{}]", code);
		String ret = null;
		if ( code == UIControlPriority_i.WITHDRAW_REQUESTED ) {
			ret = UIControlPriority_i.STR_WITHDRAW_REQUESTED;
		}
		logger.debug(f+logPrefix, "ret[{}]", ret);
		logger.end(f+logPrefix);
		return ret;
	}
	
	/**
	 * Compare the Profile Priority Level
	 * Using Identity Name to retrieve the Priority Level
	 * 
	 * @param a Profile A to compare
	 * @param b Profile B to compare
	 * @return a > b = 1; a < b = -1; otherwise 0
	 */
	private int compareLevel(final String a, final String b) {
		String f = "compareLevel";
		logger.begin(f+logPrefix);
		int ret = 0;
		logger.debug(f+logPrefix, "a[{}] b[{}]", a, b);
		int al = getPriorityLevel(a);
		int bl = getPriorityLevel(b);
		logger.debug(f+logPrefix, "a[{}]=>al[{}] b[{}]=>bl[{}]", new Object[]{a, al, b, bl});
		if ( al > bl ) {
			ret = 1;
		} else if ( al < bl ) {
			ret = -1;
		}
		logger.debug(f+logPrefix, "a[{}] b[{}] ret[{}]", new Object[]{a, b, ret});
		logger.end(f+logPrefix);
		return ret;
	}

	private String usrIdentityType = null;
	/**
	 * Get UsrIdentityType DBAttribute in the configuration, 
	 * otherwise return the UsrIdentity.Profile defined in Interface
	 * 
	 * @return DB Attribute in String
	 */
	public String getUsrIdentityType() {
		final String f = "getUsrIdentityType";
		logger.begin(f+logPrefix);
		if ( null == usrIdentityType ) {
			usrIdentityType = ReadJsonFile.readString(
			UIControlPrioritySCADAgen_i.CACHE_NAME_DICTIONARYIES
			, this.cfgAttributeName
			, UIControlPrioritySCADAgen_i.Attribute.UsrIdentityType.toString()
			, UIControlPrioritySCADAgen_i.UsrIdentity.Profile.toString());
		}
		logger.debug(f+logPrefix, "usrIdentityType[{}]", usrIdentityType);
		logger.end(f+logPrefix);
		return usrIdentityType;
	}
	
//	private String usrIdentity = null;
//	public void setUsrIdentity(final String usrIdentity) { this.usrIdentity = usrIdentity; }
	
	/**
	 * Get the UsrIdentity
	 * 
	 * @return UsrIdentity in String
	 */
	public String getUsrIdentity() { return getUsrIdentity(getUIOpm(getUIOpmName()), getUsrIdentityType()); }
	
	/**
	 * Get the UsrIdentity from configuration file type and UIOpm API 
	 * Available Type a defined in Interface: UsrIdentity.Profile and UsrIdentity.Operator
	 * 
	 * @param uiOpm_i UIOpm API to call
	 * @param usrIdentityType UstIdentity Type
	 * @return UsrIdentity in String
	 */
	private String getUsrIdentity(final UIOpm_i uiOpm_i, final String usrIdentityType) {
		final String f = "getUsrIdentity";
		logger.begin(f+logPrefix);
		
		logger.debug(f+logPrefix, "usrIdentityType[{}]", usrIdentityType);

		String usrIdentity = null;
		if ( null == uiOpm_i ) {
			logger.warn(f+logPrefix, "uiOpm_i IS NULL");
		} 
		else if ( null == usrIdentityType ) {
			logger.warn(f+logPrefix, "usrIdentityType IS NULL");
		} 
		else if ( 0 == UIControlPrioritySCADAgen_i.UsrIdentity.Profile.toString().compareTo(usrIdentityType) ) {
			usrIdentity = uiOpm_i.getCurrentProfile();
			identifierKey_ = "p";
		} 
		else if ( 0 == UIControlPrioritySCADAgen_i.UsrIdentity.Operator.toString().compareTo(usrIdentityType) ) {
			usrIdentity = uiOpm_i.getCurrentOperator();
			identifierKey_ = "o";
		}
		else {
			logger.warn(f+logPrefix, "usrIdentityType[{}] IS UNKNOW", usrIdentityType);
		}
		
		logger.debug(f+logPrefix, "usrIdentity[{}]", usrIdentity);
		
		// Default value of the UsrIdentity is Profile
		if ( null == usrIdentity ) uiOpm_i.getCurrentProfile();
		
		logger.debug(f+logPrefix, "usrIdentity[{}]", usrIdentity);
		logger.end(f+logPrefix);
		return usrIdentity;
	}
	
	private String resrvReserveReqID = null;
	/**
	 * Get ResrvReserveReqID DBAttribute in the configuration, 
	 * otherwise return the RESRV_RESERVEREQID_DEFAULT_VALUE defined in Interface
	 * 
	 * @return DB Attribute in String
	 */
	public String getResrvReserveReqID() {
		String f = "getResrvReserveReqID";
		logger.begin(f+logPrefix);
		if ( null == resrvReserveReqID ) {
			resrvReserveReqID = ReadJsonFile.readString(
					UIControlPrioritySCADAgen_i.CACHE_NAME_DICTIONARYIES
					, this.cfgAttributeName
					, UIControlPrioritySCADAgen_i.DbAttribute.ResrvReserveReqID.toString()
					, UIControlPrioritySCADAgen_i.RESRV_RESERVEREQID_DEFAULT_VALUE);
			logger.debug(f+logPrefix, "resrvReserveReqID[{}]", resrvReserveReqID);
			logger.end(f+logPrefix);
		}
		logger.debug(f+logPrefix, "resrvReserveReqID[{}]", resrvReserveReqID);
		logger.end(f+logPrefix);
		return resrvReserveReqID;
	}
	
	private String resrvUnreserveReqID = null;
	/**
	 * Get ResrvUnreserveReqID DBAttribute in the configuration, 
	 * otherwise return the RESRV_UNRESERCEREQID_DEFAULT_VALUE defined in Interface
	 * 
	 * @return DB Attribute in String
	 */
	public String getResrvUnreserveReqID() {
		String f = "getResrvUnreserveReqID";
		logger.begin(f+logPrefix);
		if ( null == resrvUnreserveReqID ) {
			resrvUnreserveReqID = ReadJsonFile.readString(
					UIControlPrioritySCADAgen_i.CACHE_NAME_DICTIONARYIES
					, this.cfgAttributeName
					, UIControlPrioritySCADAgen_i.DbAttribute.ResrvUnreserveReqID.toString()
					, UIControlPrioritySCADAgen_i.RESRV_UNRESERCEREQID_DEFAULT_VALUE);
			logger.debug(f+logPrefix, "resrvUnreserveReqID[{}]", resrvUnreserveReqID);
			logger.end(f+logPrefix);
		}
		logger.debug(f+logPrefix, "resrvUnreserveReqID[{}]", resrvUnreserveReqID);
		logger.end(f+logPrefix);
		return resrvUnreserveReqID;
	}
	
	private String resrvReserveID = null;
	/**
	 * Get ResrvReserveID DBAttribute in the configuration, 
	 * otherwise return the RESRV_RESERVEID_DEFAULT_VALUE defined in Interface
	 * 
	 * @return DB Attribute in String
	 */
	public String getResrvReserveID() {
		String f = "getResrvReserveID";
		logger.begin(f+logPrefix);
		if ( null == resrvReserveID) {
			resrvReserveID = ReadJsonFile.readString(
					UIControlPrioritySCADAgen_i.CACHE_NAME_DICTIONARYIES
					, this.cfgAttributeName
					, UIControlPrioritySCADAgen_i.DbAttribute.ResrvReservedID.toString()
					, UIControlPrioritySCADAgen_i.RESRV_RESERVEDID_DEFAULT_VALUE);

			logger.debug(f+logPrefix, "resrvReserveID[{}]", resrvReserveID);
			logger.end(f+logPrefix);
		}
		logger.debug(f+logPrefix, "resrvReserveID[{}]", resrvReserveID);
		logger.end(f+logPrefix);
		return resrvReserveID;
	}
	
	/**
	 * Control Priority Identity and Control Priority Level Mapping
	 */
	private Map<String, Integer> priorityLevels = new HashMap<String, Integer>();
	/**
	 * Get the Control Priority Identity and Control Priority Level Mapping
	 * 
	 * @return Mapping in Map data type
	 */
	public Map<String, Integer> getPriorityLevels() { return priorityLevels; }
	/**
	 * Set the Control Priority Identity and Control Priority Level Mapping
	 * 
	 * @param levels Mapping in Map data type to set
	 */
	public void setPriorityLevels(final Map<String, Integer> levels) { this.priorityLevels = levels; } 
	
	/**
	 * Get the selected identity priority level from configuration file
	 * 
	 * @param usrIdentity Selected identity
	 * @return Related priority level in configuration file
	 */
	private int getPriorityLevel(final String usrIdentity) {
		String f = "getPriorityLevel";
		logger.begin(f+logPrefix);
		logger.debug(f+logPrefix, "usrIdentity[{}]]", usrIdentity);
		int ret = UIControlPrioritySCADAgen_i.LEVEL_NOT_DEFINED;
		
		if ( ! priorityLevels.containsKey(usrIdentity) ) {
			logger.debug(f+logPrefix, "usrIdentity[{}] NOT FOUND, Loading from configuration", usrIdentity);
			
			JSONArray jsonArray = ReadJsonFile.readArray(
					UIControlPrioritySCADAgen_i.CACHE_NAME_DICTIONARYIES
					, this.cfgLevelName
					, UIControlPrioritySCADAgen_i.AttributeLevel.Level.toString());
			
			JSONObject jsonObject = ReadJson.readObject(
					jsonArray
					, AttributeLevel.Key.toString()
					, usrIdentity);
			
			int level = ReadJson.readInt(
					jsonObject
					, AttributeLevel.Value.toString()
					, ret);
			
			logger.debug(f+logPrefix, "usrIdentity[{}] level[{}]!", usrIdentity, level);

			if ( 
					ret < UIControlPrioritySCADAgen_i.LEVEL_MIN
				|| 	ret > UIControlPrioritySCADAgen_i.LEVEL_MAX ) {
				ret = UIControlPrioritySCADAgen_i.LEVEL_NOT_DEFINED;
				logger.debug(f+logPrefix, "usrIdentity[{}] NOT FOUND!, set to default level[{}]", usrIdentity, level);
			}
				
			priorityLevels.put(usrIdentity, level);
		}
		
		if ( null != priorityLevels.get(usrIdentity) ) { 
			ret = priorityLevels.get(usrIdentity);
		} else {
			logger.warn(f+logPrefix, "usrIdentity[{}] NOT FOUND", usrIdentity);
		}

		logger.debug(f+logPrefix, "usrIdentity[{}] ret[{}]", usrIdentity, ret);
		logger.end(f+logPrefix);
		return ret;
	}
	
	private UIOpm_i uiOpm_i = null;
	public UIOpm_i getUIOpm(final String uiOpmName) {
		String f = "getUIOpm";
		logger.begin(f+logPrefix);
		if ( null == uiOpm_i ) {
			logger.debug(f+logPrefix, "uiOpmName[{}]", getUIOpmName());
			uiOpm_i = OpmMgr.getInstance().getOpm(getUIOpmName());
		}
		logger.debug(f+logPrefix, "uiOpm_i[{}]", uiOpm_i);
		logger.end(f+logPrefix);
		return uiOpm_i;
	}
	
	private String uiOpmName = null;
	public void setUIOpmName(final String uiOpmName) { this.uiOpmName = uiOpmName; }
	private String getUIOpmName() {
		String f = "getUIOpmName";
		logger.begin(f+logPrefix);
		if ( null == uiOpmName ) {
			uiOpmName = ReadJsonFile.readString(
					UIControlPrioritySCADAgen_i.CACHE_NAME_DICTIONARYIES
					, this.cfgAttributeName
					, UIControlPrioritySCADAgen_i.Attribute.UIOpmName.toString()
					, UIControlPrioritySCADAgen_i.UIOPM_NAME);
		}
		logger.debug(f+logPrefix, "uiOpmName[{}]", uiOpmName);
		logger.end(f+logPrefix);
		return uiOpmName;
	}
	
	private DatabaseWrite_i databaseWrite_i = null;
	public DatabaseWrite_i getDatabaseWrite(final String databaseWriteName) {
		String f = "getDatabaseWrite";
		logger.begin(f+logPrefix);
		// Loading the DB Reading API
		databaseWrite_i = DatabaseWriteFactory.get(databaseWriteName);
		if ( null != databaseWrite_i ) {
			databaseWrite_i.connect();
		} else {
			logger.warn(f+logPrefix, "databaseWrite_i from name databaseWriteName[{}] NOT FOUND!", databaseWriteName); 
		}
		logger.end(f+logPrefix);
		return databaseWrite_i;
	}
	
	private String databaseWriteName = null;
	public void setDatabaseWriteName(final String databaseWriteName) { this.databaseWriteName = databaseWriteName; }
	private String getDatabaseWriteName() {
		String f = "getDatabaseWriteName";
		logger.begin(f+logPrefix);
		if ( null == databaseWriteName ) {
			databaseWriteName = ReadJsonFile.readString(
					UIControlPrioritySCADAgen_i.CACHE_NAME_DICTIONARYIES
					, this.cfgAttributeName
					, UIControlPrioritySCADAgen_i.Attribute.DatabaseWriteKey.toString()
					, UIControlPrioritySCADAgen_i.DB_WRITE_NAME);
		}
		logger.debug(f+logPrefix, "databaseWriteName[{}]", databaseWriteName);
		logger.end(f+logPrefix);
		return databaseWriteName;
	}
	
	private DatabaseMultiRead_i databaseMultiRead_i = null;
	public DatabaseMultiRead_i getDatabaseMultiRead(final String databaseMultiReadName) {
		String f = "getDatabaseMultiRead";
		logger.begin(f+logPrefix);
		// Loading the DB Reading API
		databaseMultiRead_i = DatabaseMultiReadFactory.get(databaseMultiReadName);
		if ( null != databaseMultiRead_i ) {
			databaseMultiRead_i.connect();
		} else {
			logger.warn(f+logPrefix, "databaseMultiRead_i from name strDbReadName[{}] NOT FOUND!", databaseMultiReadName); 
		}
		logger.end(f+logPrefix);
		return databaseMultiRead_i;
	}
	
	private String databaseMultiReadName = null;
	public void setDatabaseMultiReadName(final String databaseMultiReadName) { this.databaseMultiReadName = databaseMultiReadName; }
	private String getDatabaseMultiReadName() {
		String f = "getDatabaseMultiReadName";
		logger.begin(f+logPrefix);
		if ( null == databaseMultiReadName ) {
			databaseMultiReadName = ReadJsonFile.readString(
					UIControlPrioritySCADAgen_i.CACHE_NAME_DICTIONARYIES
					, this.cfgAttributeName
					, UIControlPrioritySCADAgen_i.Attribute.DatabaseMultiReadKey.toString()
					, UIControlPrioritySCADAgen_i.DB_READ_NAME);
		}
		logger.debug(f+logPrefix, "databaseMultiReadName[{}]", databaseMultiReadName);
		logger.end(f+logPrefix);
		return databaseMultiReadName;
	}
	
	private DatabaseKey databaseKey = new DatabaseKey();
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.opm.controlpriority.UIControlPriority_i#init()
	 */
	public void init() {
		String f = "getResrvReserveID";
		logger.begin(f+logPrefix);
		
		getDatabaseWrite(getDatabaseWriteName());
		
		getDatabaseMultiRead(getDatabaseMultiReadName());
		
		// Loading UIOpm Name and API
		getUIOpm(getUIOpmName());		
		
		// Loading the UsrIdentity
//		setUsrIdentity(getUsrIdentity(getUIOpm(getUIOpmName()), getUsrIdentityType()));
		
		logger.debug(f+logPrefix, "usrIdentity[{}]", getUsrIdentity()); 

		logger.end(f+logPrefix);
	}
	
	@Override
	public String getReservationKey() {
		String f = "getReservationKey";
		logger.begin(f+logPrefix);
		String ret = null;
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put(IDENTIFY_OPERATOR_KEY, uiOpm_i.getCurrentOperator());
		map.put(IDENTIFY_PROFILE_KEY, uiOpm_i.getCurrentProfile());
		ret = JSONUtil.convertMapToJSONObject(map).toString();
		logger.debug(f+logPrefix, "ret[{}]", ret); 
		logger.end(f+logPrefix);
		return ret;
	}
	
	@Override
	public String getDisplayIdentity(String valueFromModel) {
		String f = "getDisplayIdentity";
		logger.begin(f+logPrefix);
		logger.debug(f+logPrefix, "valueFromModel[{}]", valueFromModel);
		String ret = valueFromModel;
		JSONObject jsonObject = isJSONFormat(valueFromModel);
		if (null!=jsonObject){
			String extractedOperator = ReadJson.readString(jsonObject, IDENTIFY_OPERATOR_KEY, "");
			extractedOperator = extractedOperator.replace("\"", "");
			String extractedProfile = ReadJson.readString(jsonObject, IDENTIFY_PROFILE_KEY, "");
			extractedProfile = extractedProfile.replace("\"", "");
			ret = extractedOperator + "," + extractedProfile;
		}
		logger.debug(f+logPrefix, "ret[{}]", ret); 
		logger.end(f+logPrefix);
		return ret;
	}
	
	public String getIdentityFromJson(String identity, String key){
		String f = "getIdentityFromJson";
		logger.begin(f+logPrefix);
		logger.debug(f+logPrefix, "identity[{}] key[{}]", identity, key);
		String ret = identity;
		JSONObject jsonObject = ReadJson.readJson(identity);
		if(null!=jsonObject) {
			JSONValue jsonValue = jsonObject.get(key);
			if(null!=jsonValue) {
				ret = jsonValue.toString();
			}
		}
		logger.debug(f+logPrefix, "ret[{}]", ret); 
		logger.end(f+logPrefix);
		return ret;
	}
	
	public JSONObject isJSONFormat(String test){
		String f = "isJSONFormat";
		logger.begin(f+logPrefix);
		logger.debug(f+logPrefix, "test[{}]", test);
		JSONObject ret = null;
		JSONObject tmpStr = ReadJson.readJson(test);
		if (tmpStr != null){
			ret = tmpStr;
		}
		logger.debug(f+logPrefix, "ret[{}]", ret); 
		logger.end(f+logPrefix);
		return ret;
		
	}
}
