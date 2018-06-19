package com.thalesgroup.scadagen.wrapper.wrapper.client.opm.controlpriority;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
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
	
	private static UIControlPriority_i instance = null;
	public static UIControlPriority_i getInstance() { 
		if ( null == instance ) instance = new UIControlPrioritySCADAgen();
		return instance;
	}
	private UIControlPrioritySCADAgen () {}
	
	private String identifierKey_ = "o";
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.opm.controlpriority.UIControlPriority_i#requestReservation(java.lang.String, java.lang.String)
	 */
	@Override
	public void requestReservation(final String scsEnvId, final String dbAddress, final UIControlPriorityCallback callBack) {
		String function = "requestReservation";
		logger.begin(function);
		
		String reservationKey = getReservationKey();
		// Using hard-coded value first, originally using "getUsrIdentity()"
		
		requestReservation(scsEnvId, dbAddress, reservationKey, callBack);

		logger.end(function);
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
		String function1 = "requestReservation";
		logger.begin(function1);
		
		checkReservationAvailability(scsEnvId, dbAddress, new UIControlPriorityCallback() {
			
			@Override
			public void callBack(String strJson) {
				String function2 = "requestReservation:checkReservationAvailability:callBack";
				logger.begin(function2);

				int ret = ReadJson.readInt(ReadJson.readJson(strJson), UIControlPriority_i.FIELD_VALUE, UIControlPrioritySCADAgen_i.LEVEL_MIN);
				logger.debug(function2, "ret[{}]", ret);
				
				if ( UIControlPriority_i.AVAILABILITY_ERROR == ret ) {
					
					logger.warn(function2, "Eeserver equipment, scsEnvId[{}] dbAddress[{}] ERROR", new Object[]{scsEnvId, dbAddress});
					
					if ( null != callBack ) {
						JSONObject jsObject = new JSONObject();
						jsObject.put(UIControlPriority_i.FIELD_VALUE, new JSONString(usrIdentity));
						jsObject.put(UIControlPriority_i.FIELD_CODE, new JSONNumber(UIControlPriority_i.REQUEST_ERROR_CHECKING));
						String strJson2 = jsObject.toString();
						logger.debug(function2, "strJson2[{}]", strJson2);
						callBack.callBack(strJson2);
					} else {
						logger.debug(function2, "callBack IS NULL");
					}
					
				} else if ( 
						UIControlPriority_i.AVAILABILITY_EMPTY == ret
						|| UIControlPriority_i.AVAILABILITY_RESERVED_BYSELF == ret 
						) {
					
					logger.debug(function2, "AVAILABILITY_RESERVED_BYSELF getCodeString({})[{}]", new Object[]{ret, getCheckReservationAvailabilityCodeString(ret)});
					
					String alias = dbAddress + getResrvReserveReqID();
					String key = databaseKey.getKey(className, function2, scsEnvId, alias, usrIdentity);
					
					logger.debug(function2, "scsEnvId[{}] alias[{}] usrIdentity[{}] key[{}]", new Object[]{scsEnvId, alias, usrIdentity, key});
					databaseWrite_i.addWriteStringValueRequest(key, scsEnvId, alias, usrIdentity);
					
					if ( null != callBack ) {
						JSONObject jsObject = new JSONObject();
						jsObject.put(UIControlPriority_i.FIELD_VALUE, new JSONString(usrIdentity));
						jsObject.put(UIControlPriority_i.FIELD_CODE, new JSONNumber(UIControlPriority_i.REQUEST_REQUESTED));
						String strJson2 = jsObject.toString();
						logger.debug(function2, "strJson2[{}]", strJson2);
						callBack.callBack(strJson2);
					} else {
						logger.debug(function2, "callBack IS NULL");
					}
				}
				else if ( UIControlPriority_i.AVAILABILITY_ALLOW_WITH_OVERRIDE == ret ) {
					
					logger.debug(function2, "AVAILABILITY_ALLOW_WITH_OVERRIDE getCodeString({})[{}]", new Object[]{ret, getCheckReservationAvailabilityCodeString(ret)});
					
					forceWithdrawReservation(scsEnvId, dbAddress, new UIControlPriorityCallback() {

						@Override
						public void callBack(String json) {
							String function3 = "requestReservation:checkReservationAvailability:callBack:forceWithdrawReservation:callBack";
							logger.begin(function3);
							
							String alias = dbAddress + getResrvReserveReqID();
							String key = databaseKey.getKey(function3, scsEnvId, alias, usrIdentity);
							
							logger.debug(function3, "scsEnvId[{}] alias[{}] usrIdentity[{}] key[{}]", new Object[]{scsEnvId, alias, usrIdentity, key});
							databaseWrite_i.addWriteStringValueRequest(key, scsEnvId, alias, usrIdentity);
							
							if ( null != callBack ) {
								JSONObject jsObject = new JSONObject();
								jsObject.put(UIControlPriority_i.FIELD_VALUE, new JSONString(usrIdentity));
								jsObject.put(UIControlPriority_i.FIELD_CODE, new JSONNumber(UIControlPriority_i.REQUEST_OVERRIDE_WITH_FORCE_WITHDRAW));
								String strJson3 = jsObject.toString();
								logger.debug(function3, "strJson3[{}]", strJson3);
								callBack.callBack(strJson3);
							} else {
								logger.debug(function3, "callBack IS NULL");
							}
							
							logger.end(function3);
						}
						
					});

				} 
				
				else if ( UIControlPriority_i.AVAILABILITY_DENIED == ret ) {
					
					logger.debug(function2, "Not enough right to reserver equipment, scsEnvId[{}] dbAddress[{}]", new Object[]{scsEnvId, dbAddress});
					
					if ( null != callBack ) {
						JSONObject jsObject = new JSONObject();
						jsObject.put(UIControlPriority_i.FIELD_VALUE, new JSONString(usrIdentity));
						jsObject.put(UIControlPriority_i.FIELD_CODE, new JSONNumber(UIControlPriority_i.REQUEST_REJECTED_AT_LOWER_LEVEL));
						String strJson2 = jsObject.toString();
						logger.debug(function2, "strJson2[{}]", strJson2);
						callBack.callBack(strJson2);
					} else {
						logger.debug(function2, "callBack IS NULL");
					}
				}
				else if ( UIControlPriority_i.AVAILABILITY_EQUAL == ret ) {
					
					logger.debug(function2, "Not enough right to reserver equipment, scsEnvId[{}] dbAddress[{}]", new Object[]{scsEnvId, dbAddress});
					
					if ( null != callBack ) {
						JSONObject jsObject = new JSONObject();
						jsObject.put(UIControlPriority_i.FIELD_VALUE, new JSONString(usrIdentity));
						jsObject.put(UIControlPriority_i.FIELD_CODE, new JSONNumber(UIControlPriority_i.REQUEST_REJECTED_AT_SAME_LEVEL));
						String strJson2 = jsObject.toString();
						logger.debug(function2, "strJson2[{}]", strJson2);
						callBack.callBack(strJson2);
					} else {
						logger.debug(function2, "callBack IS NULL");
					}
				}
				else {
	
					logger.warn(function2, "Reserver equipment, scsEnvId[{}] dbAddress[{}] UNKNOW ERROR", new Object[]{scsEnvId, dbAddress});
					
					if ( null != callBack ) {
						JSONObject jsObject = new JSONObject();
						jsObject.put(UIControlPriority_i.FIELD_VALUE, new JSONString(usrIdentity));
						jsObject.put(UIControlPriority_i.FIELD_CODE, new JSONNumber(UIControlPriority_i.REQUEST_ERROR_UNKNOW));
						String strJson2 = jsObject.toString();
						logger.debug(function2, "strJson2[{}]", strJson2);
						callBack.callBack(strJson2);
					} else {
						logger.debug(function2, "callBack IS NULL");
					}
	
				}

				logger.end(function2);
			}
		});
		
		logger.end(function1);
	}

	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.opm.controlpriority.UIControlPriority_i#withdrawReservation(java.lang.String, java.lang.String)
	 */
	@Override
	public void withdrawReservation(final String scsEnvId, final String dbAddress, final UIControlPriorityCallback callBack) {
		String function = "withdrawReservation";
		logger.begin(function);
		
		String reservationKey = getReservationKey();
		// Using hard-coded value first, originally using "getUsrIdentity()"
		
		withdrawReservation(scsEnvId, dbAddress, reservationKey, callBack);
		
		logger.end(function);
	}
	
	/**
	 * @param scsEnvId		Target ScsEnvId
	 * @param dbAddress		Target DbAddress
	 * @param usrIdentity	Identity to request the reservation
	 * @param callback		Return JSON String, JSONString Attribute "value" contain the withdrawer (Value to Withdraw the Reservation)
	 */
	private void withdrawReservation(final String scsEnvId, final String dbAddress, final String usrIdentity, final UIControlPriorityCallback callBack) {
		String function = "withdrawReservation";
		logger.begin(function);
		
		String alias = dbAddress + getResrvUnreserveReqID();
		String key = databaseKey.getKey(function, scsEnvId, alias, usrIdentity);
		
		logger.debug(function, "scsEnvId[{}] alias[{}] value[{}] key[{}]", new Object[]{scsEnvId, alias, usrIdentity, key});
		databaseWrite_i.addWriteStringValueRequest(key, scsEnvId, alias, usrIdentity);
		
		if ( null != callBack ) {
			JSONObject jsObject = new JSONObject();
			jsObject.put(UIControlPriority_i.FIELD_VALUE, new JSONString(usrIdentity));
			jsObject.put(UIControlPriority_i.FIELD_CODE, new JSONNumber(UIControlPriority_i.WITHDRAW_REQUESTED));
			String strJson = jsObject.toString();
			logger.debug(function, "strJson[{}]", strJson);
			callBack.callBack(strJson);
		} else {
			logger.debug(function, "callBack IS NULL");
		}

		logger.end(function);
	}

	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.opm.controlpriority.UIControlPriority_i#getCurrentReservationBy(java.lang.String, java.lang.String, com.thalesgroup.scadagen.wrapper.wrapper.client.opm.controlpriority.UIControlPriority_i.UIControlPriorityCallback)
	 */
	@Override
	public void getCurrentReservationBy(final String scsEnvId, final String dbAddress, final UIControlPriorityCallback callBack) {
		String function1 = "getCurrentReservationBy";
		logger.begin(function1);
		String alias = dbAddress + getResrvReserveID();
		String dbAddresses [] = new String[]{alias};
		String key = databaseKey.getKey(className, function1, scsEnvId, dbAddress);
		logger.debug(function1, "scsEnvId[{}] alias[{}] key[{}]", new Object[]{scsEnvId, alias, key});
		databaseMultiRead_i.addMultiReadValueRequest(key, scsEnvId, dbAddresses, new DatabasePairEvent_i() {
			
			@Override
			public void update(final String key, final String[] dbAddresses, final String[] values) {
				String function2 = "getCurrentReservationBy:update";
				logger.begin(function2);
				
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
				    	logger.debug(function2, "strJson[{}]", strJson);
						callBack.callBack(strJson);
					} else {
						logger.debug(function2, "callBack IS NULL");
					}
				} else {
					
					String value = values[0];
					
					// Remove the String double quote
					logger.debug(function2, "BF value[{}]", value);
					if ( value.length() >= 2 ) {
						if ( value.charAt(0) == '\"' && value.charAt(value.length()-1) == '\"' ) {
							value = value.substring(1, value.length()-1);
						}
					}
					logger.debug(function2, "AF value[{}]", value);
					
					if ( null != callBack ) {
				    	JSONObject jsObject = new JSONObject();
				    	jsObject.put(UIControlPriority_i.FIELD_VALUE, new JSONString(value));
				    	jsObject.put(UIControlPriority_i.FIELD_CODE, new JSONNumber(UIControlPriority_i.GETCURRENT_VALID));
				    	String strJson = jsObject.toString();
				    	logger.debug(function2, "strJson[{}]", strJson);
						callBack.callBack(strJson);
					} else {
						logger.debug(function2, "callBack IS NULL");
					}
				}
				
				logger.end(function2);
			}
		});
		logger.end(function1);
	}
	

	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.opm.controlpriority.UIControlPriority_i#checkReservationLevel(java.lang.String)
	 */
	@Override
	public int checkReservationLevel(String identity) {
		String function = "checkReservationLevel";
		logger.begin(function);
		logger.debug(function, "identity[{}]", identity);
		String extractedIdentity = identity;
		
		if (identity != null && identity != ""){
			identity.replace("\\", "");
			logger.warn("identity after replacing:", identity);
			if (isJSONFormat(identity)){
				logger.warn("It's JSON format!!!");
				extractedIdentity = getIdentityFromJson(identity, identifierKey_).replace("\"", "");
			} else{
				logger.warn("It's NOT JSON format!!!");
			}
		} else {
			logger.warn(function, "identity is null or Empty!!");
		}
		
		int ret = UIControlPriority_i.LEVEL_ERROR;
		if ( null != extractedIdentity ) {
			if ( extractedIdentity.isEmpty() ) {
				ret = UIControlPriority_i.LEVEL_EMPTY;
			} else {
				String usrIdentity = getUsrIdentity();
				logger.debug(function, "usrIdentity[{}] extractedIdentity[{}]", usrIdentity, extractedIdentity);
				if ( extractedIdentity.equals(usrIdentity) ) {
					ret = UIControlPriority_i.LEVEL_IS_ITSELF;
				} else {
					int levelDiff = compareLevel(usrIdentity, extractedIdentity);
					logger.debug(function, "levelDiff[{}]", levelDiff);
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
			logger.debug(function, "ret[{}]", ret);
		}
		logger.end(function);
		return ret;
	}
	

	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.opm.controlpriority.UIControlPriority_i#checkReservationAvailability(java.lang.String)
	 */
	@Override
	public int checkReservationAvailability(String identity) {
		String function = "checkReservationAvailability";
		logger.begin(function);
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
		logger.debug(function, "ret[{}]", ret);
		logger.end(function);
		return ret;
	}
	
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.opm.controlpriority.UIControlPriority_i#checkReservationAvailability(java.lang.String, java.lang.String, com.thalesgroup.scadagen.wrapper.wrapper.client.opm.controlpriority.UIControlPriority_i.UIControlPriorityCallback)
	 */
	@Override
	public void checkReservationAvailability(final String scsEnvId, final String dbAddress, final UIControlPriorityCallback callBack) {
		String function1 = "checkReservationAvailability";
		logger.begin(function1);
		
		getCurrentReservationBy(scsEnvId, dbAddress, new UIControlPriorityCallback() {
			
			@Override
			public void callBack(String strJson) {
				String function2 = "checkReservationAvailability:callBack";
				logger.begin(function2);
				logger.debug(function2, "strJson[{}]", strJson);
				String value = ReadJson.readString(ReadJson.readJson(strJson), UIControlPriority_i.FIELD_VALUE, null);
				int valid = ReadJson.readInt(ReadJson.readJson(strJson), UIControlPriority_i.FIELD_CODE, -1);
				logger.debug(function2, "value[{}] valid[{}]", value, valid);
				
				if ( UIControlPriority_i.GETCURRENT_READ_INVALID == valid ) {
					if ( null != callBack ) {
					   	JSONObject jsObject = new JSONObject();
					   	jsObject.put(UIControlPriority_i.FIELD_VALUE, new JSONNumber(UIControlPriority_i.AVAILABILITY_ERROR));
					   	String jsonString = jsObject.toString();
					   	logger.debug(function2, "jsonString[{}]", jsonString);
				    	callBack.callBack(jsonString);	
					} else {
						logger.debug(function2, "callBack IS NULL");
					}
				}
				
				// Call the level checking
				int ret = checkReservationAvailability(value);
				
				if ( null != callBack ) {
				   	JSONObject jsObject = new JSONObject();
				   	jsObject.put(UIControlPriority_i.FIELD_VALUE, new JSONNumber(ret));
				   	String jsonString = jsObject.toString();
				   	logger.debug(function2, "jsonString[{}]", jsonString);
			    	callBack.callBack(jsonString);	
				} else {
					logger.debug(function2, "callBack IS NULL");
				}

				logger.end(function2);
			}
		});
		logger.end(function1);
	}

	/**
	 * @param scsEnvId	Target ScsEnvId
	 * @param dbAddress	Target DbAddress
	 * @param callBack	Return JSON String, JSONString Attribute "value" contain the withdrawer (Value to Withdraw the Reservation)
	 */
	public void forceWithdrawReservation(final String scsEnvId, final String dbAddress, final UIControlPriorityCallback callBack) {
		String function1 = "forceWithdrawReservation";
		logger.begin(function1);
		
		getCurrentReservationBy(scsEnvId, dbAddress, new UIControlPriorityCallback() {
			
			@Override
			public void callBack(String strJson1) {
				final String function2 = "forceWithdrawReservation:getCurrentReservationBy:callBack";
				logger.begin(function2);
				
				String value = ReadJson.readString(ReadJson.readJson(strJson1), UIControlPriority_i.FIELD_VALUE, null);
				
				logger.debug(function2, "value[{}]", value);
				
				if ( null != value && ! value.isEmpty() ) {

					withdrawReservation(scsEnvId, dbAddress, value, new UIControlPriorityCallback() {
						
						@Override
						public void callBack(String strJson2) {
							final String function3 = "forceWithdrawReservation:getCurrentReservationBy:callBack:withdrawReservation:callBack";
							logger.begin(function3);
							
							String value = ReadJson.readString(ReadJson.readJson(strJson2), UIControlPriority_i.FIELD_VALUE, null);
							
							logger.debug(function3, "value[{}]", value);
							
							if ( null != callBack) {
								JSONObject jsObject = new JSONObject();
								jsObject.put(UIControlPriority_i.FIELD_VALUE, new JSONString(value));
								String strJson3 = jsObject.toString();
								logger.debug(function3, "strJson3[{}]", strJson3);
								callBack.callBack(strJson3);
							} else {
								logger.debug(function2, "callBack IS NULL");
							}
							
							logger.end(function3);
						}
					});
				} else {
					if ( null != callBack) {
						JSONObject jsObject = new JSONObject();
						jsObject.put(UIControlPriority_i.FIELD_VALUE, new JSONString(UIControlPrioritySCADAgen_i.STR_EMPTY));
						jsObject.put(UIControlPriority_i.FIELD_CODE, new JSONNumber(UIControlPriority_i.FORCE_WITHDRAW_INVALID));
						String strJson3 = jsObject.toString();
						logger.debug(function2, "strJson3[{}]", strJson3);
						callBack.callBack(strJson3);
					} else {
						logger.debug(function2, "callBack IS NULL");
					}
				}
				
				logger.end(function2);
				
			}
			
		});
		
		logger.end(function1);
	}

	/**
	 * Return the Mapped Check Reservation Availability code string
	 * 
	 * @param code code to find mapping
	 * @return mapped string 
	 */
	public String getCheckReservationAvailabilityCodeString(final int code) {
		String function = "getCheckReservationAvailabilityCodeString";
		logger.begin(function);
		logger.debug(function, "code[{}]", code);
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
		logger.debug(function, "ret[{}]", ret);
		logger.end(function);
		return ret;
	}
	
	/**
	 * Return the Mapped Request Reservation code string
	 * 
	 * @param code code to find mapping
	 * @return mapped string 
	 */
	public String getRequestReservationCodeString(final int code) {
		String function = "getRequestReservationCodeString";
		logger.begin(function);
		logger.debug(function, "code[{}]", code);
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
		logger.debug(function, "ret[{}]", ret);
		logger.end(function);
		return ret;
	}
	
	/**
	 * Return the Mapped Withdraw Reservation code string
	 * 
	 * @param code code to find mapping
	 * @return mapped string 
	 */
	public String getWithdrawReservationCodeString(final int code) {
		String function = "getWithdrawReservationCodeString";
		logger.begin(function);
		logger.debug(function, "code[{}]", code);
		String ret = null;
		if ( code == UIControlPriority_i.WITHDRAW_REQUESTED ) {
			ret = UIControlPriority_i.STR_WITHDRAW_REQUESTED;
		}
		logger.debug(function, "ret[{}]", ret);
		logger.end(function);
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
		String function = "compareLevel";
		logger.begin(function);
		int ret = 0;
		logger.debug(function, "a[{}] b[{}]", a, b);
		int al = getPriorityLevel(a);
		int bl = getPriorityLevel(b);
		logger.debug(function, "a[{}]=>al[{}] b[{}]=>bl[{}]", new Object[]{a, al, b, bl});
		if ( al > bl ) {
			ret = 1;
		} else if ( al < bl ) {
			ret = -1;
		}
		logger.debug(function, "a[{}] b[{}] ret[{}]", new Object[]{a, b, ret});
		logger.end(function);
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
		final String function = "getUsrIdentityType";
		logger.begin(function);
		if ( null == usrIdentityType ) {
			usrIdentityType = ReadJsonFile.readString(
			UIControlPrioritySCADAgen_i.CACHE_NAME_DICTIONARYIES
			, UIControlPrioritySCADAgen_i.FILE_NAME_ATTRIBUTE
			, UIControlPrioritySCADAgen_i.Attribute.UsrIdentityType.toString()
			, UIControlPrioritySCADAgen_i.UsrIdentity.Profile.toString());
		}
		logger.debug(function, "usrIdentityType[{}]", usrIdentityType);
		logger.end(function);
		return usrIdentityType;
	}
	
	private String usrIdentity = null;
	public void setUsrIdentity(final String usrIdentity) { this.usrIdentity = usrIdentity; }
	
	/**
	 * Get the UsrIdentity
	 * 
	 * @return UsrIdentity in String
	 */
	public String getUsrIdentity() { return usrIdentity; }
	
	/**
	 * Get the UsrIdentity from configuration file type and UIOpm API 
	 * Available Type a defined in Interface: UsrIdentity.Profile and UsrIdentity.Operator
	 * 
	 * @param uiOpm_i UIOpm API to call
	 * @param usrIdentityType UstIdentity Type
	 * @return UsrIdentity in String
	 */
	private String getUsrIdentity(final UIOpm_i uiOpm_i, final String usrIdentityType) {
		final String function = "getUsrIdentity";
		logger.begin(function);
		
		logger.debug(function, "usrIdentityType[{}]", usrIdentityType);

		String usrIdentity = null;
		if ( null == uiOpm_i ) {
			logger.warn(function, "uiOpm_i IS NULL");
		} 
		else if ( null == usrIdentityType ) {
			logger.warn(function, "usrIdentityType IS NULL");
		} 
		else if ( 0 == UIControlPrioritySCADAgen_i.UsrIdentity.Profile.toString().compareTo(usrIdentityType) ) {
			usrIdentity = uiOpm_i.getCurrentProfile();
		} 
		else if ( 0 == UIControlPrioritySCADAgen_i.UsrIdentity.Operator.toString().compareTo(usrIdentityType) ) {
			usrIdentity = uiOpm_i.getCurrentOperator();
		}
		else {
			logger.warn(function, "usrIdentityType[{}] IS UNKNOW", usrIdentityType);
		}
		
		logger.debug(function, "usrIdentity[{}]", usrIdentity);
		
		// Default value of the UsrIdentity is Profile
		if ( null == usrIdentity ) uiOpm_i.getCurrentProfile();
		
		logger.debug(function, "usrIdentity[{}]", usrIdentity);
		logger.end(function);
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
		String function = "getResrvReserveReqID";
		logger.begin(function);
		if ( null == resrvReserveReqID ) {
			resrvReserveReqID = ReadJsonFile.readString(
					UIControlPrioritySCADAgen_i.CACHE_NAME_DICTIONARYIES
					, UIControlPrioritySCADAgen_i.FILE_NAME_ATTRIBUTE
					, UIControlPrioritySCADAgen_i.DbAttribute.ResrvReserveReqID.toString()
					, UIControlPrioritySCADAgen_i.RESRV_RESERVEREQID_DEFAULT_VALUE);
			logger.debug(function, "resrvReserveReqID[{}]", resrvReserveReqID);
			logger.end(function);
		}
		logger.debug(function, "resrvReserveReqID[{}]", resrvReserveReqID);
		logger.end(function);
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
		String function = "getResrvUnreserveReqID";
		logger.begin(function);
		if ( null == resrvUnreserveReqID ) {
			resrvUnreserveReqID = ReadJsonFile.readString(
					UIControlPrioritySCADAgen_i.CACHE_NAME_DICTIONARYIES
					, UIControlPrioritySCADAgen_i.FILE_NAME_ATTRIBUTE
					, UIControlPrioritySCADAgen_i.DbAttribute.ResrvUnreserveReqID.toString()
					, UIControlPrioritySCADAgen_i.RESRV_UNRESERCEREQID_DEFAULT_VALUE);
			logger.debug(function, "resrvUnreserveReqID[{}]", resrvUnreserveReqID);
			logger.end(function);
		}
		logger.debug(function, "resrvUnreserveReqID[{}]", resrvUnreserveReqID);
		logger.end(function);
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
		String function = "getResrvReserveID";
		logger.begin(function);
		if ( null == resrvReserveID) {
			resrvReserveID = ReadJsonFile.readString(
					UIControlPrioritySCADAgen_i.CACHE_NAME_DICTIONARYIES
					, UIControlPrioritySCADAgen_i.FILE_NAME_ATTRIBUTE
					, UIControlPrioritySCADAgen_i.DbAttribute.ResrvReserveID.toString()
					, UIControlPrioritySCADAgen_i.RESRV_RESERVEID_DEFAULT_VALUE);

			logger.debug(function, "resrvReserveID[{}]", resrvReserveID);
			logger.end(function);
		}
		logger.debug(function, "resrvReserveID[{}]", resrvReserveID);
		logger.end(function);
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
		String function = "getPriorityLevel";
		logger.begin(function);
		logger.debug(function, "usrIdentity[{}]]", usrIdentity);
		int ret = UIControlPrioritySCADAgen_i.LEVEL_NOT_DEFINED;
		
		if ( ! priorityLevels.containsKey(usrIdentity) ) {
			logger.debug(function, "usrIdentity[{}] NOT FOUND, Loading from configuration", usrIdentity);
			
			JSONArray jsonArray = ReadJsonFile.readArray(
					UIControlPrioritySCADAgen_i.CACHE_NAME_DICTIONARYIES
					, UIControlPrioritySCADAgen_i.FILE_NAME_LEVEL
					, UIControlPrioritySCADAgen_i.AttributeLevel.Level.toString());
			
			JSONObject jsonObject = ReadJson.readObject(
					jsonArray
					, AttributeLevel.Key.toString()
					, usrIdentity);
			
			int level = ReadJson.readInt(
					jsonObject
					, AttributeLevel.Value.toString()
					, ret);
			
			logger.debug(function, "usrIdentity[{}] level[{}]!", usrIdentity, level);

			if ( 
					ret < UIControlPrioritySCADAgen_i.LEVEL_MIN
				|| 	ret > UIControlPrioritySCADAgen_i.LEVEL_MAX ) {
				ret = UIControlPrioritySCADAgen_i.LEVEL_NOT_DEFINED;
				logger.debug(function, "usrIdentity[{}] NOT FOUND!, set to default level[{}]", usrIdentity, level);
			}
				
			priorityLevels.put(usrIdentity, level);
		}
		
		if ( null != priorityLevels.get(usrIdentity) ) { 
			ret = priorityLevels.get(usrIdentity);
		} else {
			logger.warn(function, "usrIdentity[{}] NOT FOUND", usrIdentity);
		}

		logger.debug(function, "usrIdentity[{}] ret[{}]", usrIdentity, ret);
		logger.end(function);
		return ret;
	}
	
	private UIOpm_i uiOpm_i = null;
	public UIOpm_i getUIOpm(final String uiOpmName) {
		String function = "getUIOpm";
		logger.begin(function);
		if ( null == uiOpm_i ) {
			logger.debug(function, "uiOpmName[{}]", getUIOpmName());
			uiOpm_i = OpmMgr.getInstance().getOpm(getUIOpmName());
		}
		logger.debug(function, "uiOpm_i[{}]", uiOpm_i);
		logger.end(function);
		return uiOpm_i;
	}
	
	private String uiOpmName = null;
	public void setUIOpmName(final String uiOpmName) { this.uiOpmName = uiOpmName; }
	private String getUIOpmName() {
		String function = "getUIOpmName";
		logger.begin(function);
		if ( null == uiOpmName ) {
			uiOpmName = ReadJsonFile.readString(
					UIControlPrioritySCADAgen_i.CACHE_NAME_DICTIONARYIES
					, UIControlPrioritySCADAgen_i.FILE_NAME_ATTRIBUTE
					, UIControlPrioritySCADAgen_i.Attribute.UIOpmName.toString()
					, UIControlPrioritySCADAgen_i.UIOPM_NAME);
		}
		logger.debug(function, "uiOpmName[{}]", uiOpmName);
		logger.end(function);
		return uiOpmName;
	}
	
	private DatabaseWrite_i databaseWrite_i = null;
	public DatabaseWrite_i getDatabaseWrite(final String databaseWriteName) {
		String function = "getDatabaseWrite";
		logger.begin(function);
		// Loading the DB Reading API
		databaseWrite_i = DatabaseWriteFactory.get(databaseWriteName);
		if ( null != databaseWrite_i ) {
			databaseWrite_i.connect();
		} else {
			logger.warn(function, "databaseWrite_i from name databaseWriteName[{}] NOT FOUND!", databaseWriteName); 
		}
		logger.end(function);
		return databaseWrite_i;
	}
	
	private String databaseWriteName = null;
	public void setDatabaseWriteName(final String databaseWriteName) { this.databaseWriteName = databaseWriteName; }
	private String getDatabaseWriteName() {
		String function = "getDatabaseWriteName";
		logger.begin(function);
		if ( null == databaseWriteName ) {
			databaseWriteName = ReadJsonFile.readString(
					UIControlPrioritySCADAgen_i.CACHE_NAME_DICTIONARYIES
					, UIControlPrioritySCADAgen_i.FILE_NAME_ATTRIBUTE
					, UIControlPrioritySCADAgen_i.Attribute.DatabaseWriteKey.toString()
					, UIControlPrioritySCADAgen_i.DB_WRITE_NAME);
		}
		logger.debug(function, "databaseWriteName[{}]", databaseWriteName);
		logger.end(function);
		return databaseWriteName;
	}
	
	private DatabaseMultiRead_i databaseMultiRead_i = null;
	public DatabaseMultiRead_i getDatabaseMultiRead(final String databaseMultiReadName) {
		String function = "getDatabaseMultiRead";
		logger.begin(function);
		// Loading the DB Reading API
		databaseMultiRead_i = DatabaseMultiReadFactory.get(databaseMultiReadName);
		if ( null != databaseMultiRead_i ) {
			databaseMultiRead_i.connect();
		} else {
			logger.warn(function, "databaseMultiRead_i from name strDbReadName[{}] NOT FOUND!", databaseMultiReadName); 
		}
		logger.end(function);
		return databaseMultiRead_i;
	}
	
	private String databaseMultiReadName = null;
	public void setDatabaseMultiReadName(final String databaseMultiReadName) { this.databaseMultiReadName = databaseMultiReadName; }
	private String getDatabaseMultiReadName() {
		String function = "getDatabaseMultiReadName";
		logger.begin(function);
		if ( null == databaseMultiReadName ) {
			databaseMultiReadName = ReadJsonFile.readString(
					UIControlPrioritySCADAgen_i.CACHE_NAME_DICTIONARYIES
					, UIControlPrioritySCADAgen_i.FILE_NAME_ATTRIBUTE
					, UIControlPrioritySCADAgen_i.Attribute.DatabaseMultiReadKey.toString()
					, UIControlPrioritySCADAgen_i.DB_READ_NAME);
		}
		logger.debug(function, "databaseMultiReadName[{}]", databaseMultiReadName);
		logger.end(function);
		return databaseMultiReadName;
	}
	
	private DatabaseKey databaseKey = new DatabaseKey();
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.opm.controlpriority.UIControlPriority_i#init()
	 */
	public void init() {
		String function = "getResrvReserveID";
		logger.begin(function);
		
		getDatabaseWrite(getDatabaseWriteName());
		
		getDatabaseMultiRead(getDatabaseMultiReadName());
		
		// Loading UIOpm Name and API
		getUIOpm(getUIOpmName());		
		
		// Loading the UsrIdentity
		setUsrIdentity(getUsrIdentity(getUIOpm(getUIOpmName()), getUsrIdentityType()));
		
		logger.debug(function, "usrIdentity[{}]", getUsrIdentity()); 

		logger.end(function);
	}
	
	@Override
	public String getReservationKey() {
		// Temporarily hard-coded
		String identity = "{\"o\":\"" + uiOpm_i.getCurrentOperator() + "\"" + ",\"p\":\"" + uiOpm_i.getCurrentProfile() + "\"}";
		JSONObject tempJSON = ReadJson.readJson(identity);
		logger.warn("[getReservationKey] tempJSON is" + tempJSON.toString());
		identity = tempJSON.toString();
		return identity;
	}
	
	@Override
	public String getDisplayIdentity(String valueFromDB) {
		if (isJSONFormat(valueFromDB)){
			JSONObject tempJSON = ReadJson.readJson(valueFromDB);
			String extractedOperator = tempJSON.get("o").toString();
			String extractedProfile = tempJSON.get("p").toString();
			String identity = extractedOperator + "," + extractedProfile;
			return identity;
		} else {
			return valueFromDB;
		}
	}
	
	public String getIdentityFromJson(String identity, String key){
		JSONObject tempJSON = ReadJson.readJson(identity);
		String extractedIdentity = tempJSON.get(key).toString();
		return extractedIdentity;
	}
	
	public boolean isJSONFormat (String test){
		
		if (ReadJson.readJson(test) != null){
			return true;
		} else {
			return false;
		}
		
	}
}
