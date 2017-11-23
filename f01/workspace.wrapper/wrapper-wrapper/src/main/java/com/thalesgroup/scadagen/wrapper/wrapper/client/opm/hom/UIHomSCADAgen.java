package com.thalesgroup.scadagen.wrapper.wrapper.client.opm.hom;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.thalesgroup.scadagen.whmi.config.configenv.client.ReadJson;
import com.thalesgroup.scadagen.whmi.config.configenv.client.ReadJsonFile;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseMultiRead_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabasePairEvent_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.factory.DatabaseMultiReadFactory;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.util.DatabaseKey;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpmSCADAgen_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i.CheckAccessWithHOMEvent_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i.GetCurrentHOMValueEvent_i;

public class UIHomSCADAgen implements UIHom_i {

	private final String className = UIWidgetUtil.getClassSimpleName(UIHomSCADAgen.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private static UIHom_i instance = null;
	public static UIHom_i getInstance() { 
		if ( null == instance ) instance = new UIHomSCADAgen();
		return instance;
	}
	private UIHomSCADAgen () {}

	@Override
	public boolean checkHom(final int hdvValue, final String key) {
		final String function = "checkHom";
		logger.begin(className, function);
		logger.debug(className, function, "hdvValue[{}] key[{}]"
				, new Object[]{hdvValue, key});
		boolean ret = false;
		if ( ! isBypassValue(hdvValue) ) {
			if ( (hdvValue & getConfigHOMMask(key)) > 0 ) {
				ret = true;
			}
		} else {
			ret = true;
		}
		logger.debug(className, function, "hdvValue[{}] key[{}] ret[{}]"
				, new Object[]{hdvValue, key, ret});
		logger.end(className, function);
		return ret;
	}
	
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i#checkAccessWithHom(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i.CheckAccessWithHOMEvent_i)
	 */
	@Override
	public void checkAccessWithHom(
			  final String function, final String location, final String action, final String mode
			, final String scsEnvId, final String dbAddress
			, final UIOpm_i uiOpm_i
			, final CheckAccessWithHOMEvent_i resultEvent) {
		final String functionName = "checkAccessWithHom";
		logger.begin(className, functionName);
		logger.debug(className, functionName, "function[{}] location[{}] action[{}] mode[{}] scsEnvId[{}] dbAddress[{}]"
				, new Object[]{function, location, action, mode, scsEnvId, dbAddress});

		String identityType = getHOMIdentityType();
		logger.debug(className, functionName, "identityType[{}]", identityType);
		
		String identity = null;
		if ( null != identityType ) {
			if ( identityType.equals(UIOpmSCADAgen_i.HOMIdentityType.Profile.toString()) ) {
				identity = uiOpm_i.getCurrentProfile();
			} 
			else if ( identityType.equals(UIOpmSCADAgen_i.HOMIdentityType.Operator.toString()) ) {
				identity = uiOpm_i.getCurrentOperator();
			}
			else if ( identityType.equals(UIOpmSCADAgen_i.HOMIdentityType.HostName.toString()) ) {
				identity = uiOpm_i.getCurrentHostName();
			}
			else if ( identityType.equals(UIOpmSCADAgen_i.HOMIdentityType.IpAddress.toString()) ) {
				identity = uiOpm_i.getCurrentIPAddress();
			}
			
			if ( null == identity ) {
				identity = uiOpm_i.getCurrentProfile();
			}
		}
		logger.debug(className, functionName, "identityType[{}] identity[{}]", identityType, identity);
		
		if ( null != resultEvent ) {
			if ( isHOMAction(action) ) {
				final String key = identity;
				getCurrentHOMValue(scsEnvId, dbAddress, new GetCurrentHOMValueEvent_i() {
					@Override
					public void update(String dbaddress, int value) {
						
						checkAccessWithHom(
								function, location, action, mode
								, value, key
								, uiOpm_i
								, resultEvent);

					}
				});
			} else {
				boolean ret = false;
				
				ret = uiOpm_i.checkAccess(function, location, action, mode );
				logger.debug(className, functionName, "function[{}] location[{}] action[{}] mode[{}] scsEnvId[{}] dbAddress[{}] identity[{}] ret[{}]"
						, new Object[]{function, location, action, mode, scsEnvId, dbAddress, identity, ret});
				resultEvent.result(ret);
			}
		} else {
			logger.warn(className, functionName, "resultEvent IS NULL");
		}
		
		logger.end(className, functionName);
	}
	
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i#checkAccessWithHom(java.lang.String, java.lang.String, java.lang.String, java.lang.String, int, java.lang.String, com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i.CheckAccessWithHOMEvent_i)
	 */
	@Override
	public void checkAccessWithHom(
			final String function , final String location , final String action , final String mode
			, final int hdv, final String identity
			, final UIOpm_i uiOpm_i
			, final CheckAccessWithHOMEvent_i resultEvent) {
		final String functionName = "checkAccessWithHom";
		logger.begin(className, functionName);
		logger.debug(className, functionName, "function[{}] location[{}] action[{}] mode[{}] hdv[{}] identity[{}]"
				, new Object[]{function, location, action, mode, hdv, identity});
		
		boolean isHOMRequested = false;
		boolean caResult = false;
		boolean homResult = false;
		
		if ( null != resultEvent ) {
			if ( isHOMAction(action) ) {
				isHOMRequested = true;
				homResult = checkHom(hdv, identity);
			}
			
			caResult = uiOpm_i.checkAccess(function, location, action, mode);
			
			boolean ret = isHOMRequested?(caResult && homResult):caResult;
			logger.debug(className, functionName, "function[{}] location[{}] action[{}] mode[{}] hdv[{}] identity[{}] ret[{}]"
					, new Object[]{function, location, action, mode, hdv, identity, ret});
			resultEvent.result(ret);
			
		} else {
			logger.warn(className, functionName, "resultEvent IS NULL");
		}
		
		logger.end(className, functionName);
	}

	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i#getCurrentHOMValue(java.lang.String, java.lang.String, com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i.GetCurrentHOMValueEvent_i)
	 */
	@Override
	public void getCurrentHOMValue(final String scsEnvId, final String dbAddress, final GetCurrentHOMValueEvent_i getCurrentHOMValueEvent_i) {
		final String function = "getCurrentHOMValue";
		logger.begin(className, function);
		logger.debug(className, function, "scsEnvId[{}] dbAddress[{}]", scsEnvId, dbAddress);
		
		String [] dbAddresses = new String[]{dbAddress + getDbAttribute()};
		
		final String clientKey = databaseKey.getKey(className, function, scsEnvId, dbAddress);
		DatabaseMultiRead_i databaseMultiRead_i = getDatabaseMultiRead(getDatabaseMultiReadName());
		if ( null != databaseMultiRead_i ) {
			databaseMultiRead_i.addMultiReadValueRequest(clientKey, scsEnvId, dbAddresses, new DatabasePairEvent_i() {

				@Override
				public void update(String key, String[] dbAddresses, String[] dbValues) {
					if ( null != key ) {
						if ( key.equals(clientKey) ) {
							if ( null != getCurrentHOMValueEvent_i ) {
								if ( null != dbAddresses && null != dbValues ) {
									if ( dbAddresses.length > 0 && dbValues.length > 0 ) {
										String dbaddress = dbAddresses[0];
										String dbvalue = dbValues[0];
										int value = Integer.parseInt(dbvalue);
										getCurrentHOMValueEvent_i.update(dbaddress, value);
									} else {
										logger.warn(className, function, "dbAddresses.length > 0 || dbAddresses.length > 0 IS INVALID");
									}
								} else {
									logger.warn(className, function, "dbAddresses || dbValues IS NULL");
								}
							} else {
								logger.warn(className, function, "getCurrentHOMValueEvent_i IS NULL");
							}
						}
					}
				}
			});
		} else {
			logger.warn(className, function, "databaseMultiRead_i IS NULL");
		}

		logger.end(className, function);
	}
	
	private Map<String, Integer> confighommasks	= new HashMap<String, Integer>();
	/**
	 * Get HOM Mask value in the configuration, defined in the hom.json with key "HOMLevels"
	 * 
	 * @param identity HOM Mask Key for retrieve the value
	 * @return    Value defined in the configuration file related to key
	 */
	private int getConfigHOMMask(String identity) {
		String function = "getConfigHOMMask";
		logger.begin(className, function);
		logger.debug(className, function, "identity[{}]", identity);
		
		if ( ! confighommasks.containsKey(identity) ) {
			
			String dictionariesCacheName = UIOpmSCADAgen_i.dictionariesCacheName;
			String fileName = UIOpmSCADAgen_i.fileNameLevel;

			int defaultValue = getHOMLevelDefaultValue();
			logger.debug(className, function, "defaultValue[{}]", defaultValue);
			
			String arraykey = UIOpmSCADAgen_i.homLevelsArrayKey;
			JSONArray jsonArray = ReadJsonFile.readArray(dictionariesCacheName, fileName, arraykey);
			
			JSONObject jsonObject = ReadJson.readObject(jsonArray, UIOpmSCADAgen_i.homLevelsArrayKeyKeyName, identity);
			
			int confighommask = ReadJson.readInt(jsonObject, UIOpmSCADAgen_i.homLevelsArrayKeyValueName, defaultValue);
			
			confighommasks.put(identity, confighommask);
		}
		int ret = confighommasks.get(identity);
		logger.debug(className, function, "ret[{}]", ret);
		logger.end(className, function);
		return ret;
	}
	
	private String dbAttribute = null;
	/**
	 * @return
	 */
	private String getDbAttribute() {
		String function = "getDbAttribute";
		logger.begin(className, function);

		if ( null == dbAttribute ) {
			String dictionariesCacheName = UIOpmSCADAgen_i.dictionariesCacheName;
			String fileName = UIOpmSCADAgen_i.fileName;
			String key = UIOpmSCADAgen_i.dbAttributekey;
			String defaultValue = "Can't not read db attribute";
			dbAttribute = ReadJsonFile.readString(dictionariesCacheName, fileName, key, defaultValue);
		}
		logger.debug(className, function, "dbAttribute[{}]", dbAttribute);
		logger.end(className, function);
		return dbAttribute;
	}
	
	private String homActions[] = null;
	/**
	 * @return
	 */
	private String[] getHomActions() {
		final String function = "getHomActions";
		logger.begin(className, function);
		
		if ( null == homActions ) {
			String dictionariesCacheName = UIOpmSCADAgen_i.dictionariesCacheName;
			String fileName = UIOpmSCADAgen_i.fileName;
			String key = UIOpmSCADAgen_i.homActionsArrayKey;
			JSONArray array = ReadJsonFile.readArray(dictionariesCacheName, fileName, key);
			if ( null != array ) {
				homActions = new String[array.size()];
				for ( int i = 0 ; i < array.size() ; ++i ) {
					JSONValue v = array.get(i);
					if ( null != v && null != v.isString() ) {
						homActions[i] = v.isString().stringValue();
						logger.debug(className, function, "homActions({})[{}]", i, homActions[i]);
					}
				}
			} else {
				logger.warn(className, function, "array IS NULL");
			}
		}
		logger.end(className, function);
		return homActions;
	}
	
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i#isHOMAction(java.lang.String)
	 */
	@Override
	public boolean isHOMAction(String action) {
		final String function = "isHOMAction";
		logger.begin(className, function);
		logger.debug(className, function, "action[{}]", action);
		boolean ret = false;
		String [] homActions = getHomActions();
		if ( null != homActions) {
			for ( int i = 0 ; i < homActions.length ; i++ ) {
				String homAction = homActions[i];
				if ( homAction != null ) {
					logger.debug(className, function, "homActions[{}] == action[{}]", homAction, action);
					if ( 0 == homAction.compareTo(action) ) {
						ret = true;
						break;
					}
				} else {
					logger.warn(className, function, "homActions([{}]) IS NULL", i);
				}
			}
		} else {
			logger.warn(className, function, "homActions IS NULL");
		}
		logger.debug(className, function, "action[{}] result[{}]", action, ret);
		logger.end(className, function);
		return ret;
	}

	private int [] byPassValues	= null;
	/**
	 * @return
	 */
	private int[] getBypassValues() {
		String function = "getBypassValues";
		logger.begin(className, function);
		
		if ( null == byPassValues ) {
			String dictionariesCacheName = UIOpmSCADAgen_i.dictionariesCacheName;
			String fileName = UIOpmSCADAgen_i.fileName;
			String key = UIOpmSCADAgen_i.byPassValueskey;
			
			JSONArray array = ReadJsonFile.readArray(dictionariesCacheName, fileName, key);
			if ( null != array ) {
				byPassValues = new int[array.size()];
				for ( int i = 0 ; i < array.size() ; ++i ) {
					JSONValue v = array.get(i);
					if ( null != v && null != v.isNumber() ) {
						byPassValues[i] = (int) v.isNumber().doubleValue();
						logger.debug(className, function, "byPassValues({})[{}]", i, byPassValues[i]);
					}
				}
			} else {
				logger.warn(className, function, "array IS NULL");
			}
		}
		logger.debug(className, function, "byPassValues.length[{}]", byPassValues.length);
		logger.end(className, function);
		return byPassValues;
	}
	
	private int homLevelDefaultValue = 0;
	boolean homLevelDefaultValueReady = false;
	
	/**
	 * @return default value in configuration
	 */
	private int getHOMLevelDefaultValue() {
		String function = "getHOMLevelDefaultValue";
		logger.begin(className, function);
		
		if ( ! homLevelDefaultValueReady ) {
			String dictionariesCacheName = UIOpmSCADAgen_i.dictionariesCacheName;
			String fileName = UIOpmSCADAgen_i.fileName;
			String key = UIOpmSCADAgen_i.homLevelDefaultValueKey;
			int defaultValue = 0;
			homLevelDefaultValue = ReadJsonFile.readInt(dictionariesCacheName, fileName, key, defaultValue);
			
			homLevelDefaultValueReady = true;
		}
		logger.debug(className, function, "homLevelDefaultValue[{}]", homLevelDefaultValue);
		logger.end(className, function);
		return homLevelDefaultValue;
	}	
	
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i#isByPassValue(int)
	 */
	@Override
	public boolean isBypassValue(int value) {
		String function = "isByPassValue";
		logger.begin(className, function);
		logger.debug(className, function, "value[{}]", value);
		boolean ret = false;
		int [] byPassValues = getBypassValues();
		if ( null != byPassValues ) {
			for ( int i = 0 ; i < byPassValues.length ; ++i ) {
				if (byPassValues[i] == value) {
					ret = true;
					break;
				}
			}
		} else {
			logger.warn(className, function, "byPassValues IS NULL");
		}
		logger.debug(className, function, "value[{}] ret[{}]", value, ret);
		logger.end(className, function);
		return ret;
	}
	
	private String getHOMIdentityType = null;
	/**
	 * @return default value in configuration
	 */
	private String getHOMIdentityType() {
		String function = "getHOMIdentityType";
		logger.begin(className, function);
		
		if ( null == getHOMIdentityType ) {
			String dictionariesCacheName = UIOpmSCADAgen_i.dictionariesCacheName;
			String fileName = UIOpmSCADAgen_i.fileName;
			String key = UIOpmSCADAgen_i.homIdentityTypeKeyName;
			getHOMIdentityType = ReadJsonFile.readString(dictionariesCacheName, fileName, key, UIOpmSCADAgen_i.homIdentityTypeDefaultValue);
		}
		logger.debug(className, function, "getHOMIdentityType[{}]", getHOMIdentityType);
		logger.end(className, function);
		return getHOMIdentityType;
	}

	private DatabaseMultiRead_i databaseMultiRead_i = null;
	public DatabaseMultiRead_i getDatabaseMultiRead(String databaseMultiReadName) {
		String function = "getDatabaseMultiRead";
		logger.begin(className, function);
		// Loading the DB Reading API
		if ( null == databaseMultiRead_i ) {
			databaseMultiRead_i = DatabaseMultiReadFactory.get(databaseMultiReadName);
			if ( null != databaseMultiRead_i ) {
				databaseMultiRead_i.connect();
			} else {
				logger.warn(className, function, "databaseMultiRead_i from name databaseMultiReadName[{}] NOT FOUND!", databaseMultiReadName); 
			}
		}
		logger.end(className, function);
		return databaseMultiRead_i;
	}
	
	private String databaseMultiReadName = null;
	public void setDatabaseMultiReadName(final String databaseMultiReadName) { this.databaseMultiReadName = databaseMultiReadName; }
	private String getDatabaseMultiReadName() {
		String function = "getDatabaseMultiReadName";
		logger.begin(className, function);
		if ( null == databaseMultiReadName ) {
			databaseMultiReadName = ReadJsonFile.readString(
					UIHomSCADAgen_i.CACHE_NAME_DICTIONARYIES
					, UIHomSCADAgen_i.FILE_NAME_ATTRIBUTE
					, UIHomSCADAgen_i.Attribute.DatabaseMultiReadKey.toString()
					, UIHomSCADAgen_i.DB_READ_NAME);
		}
		logger.debug(className, function, "databaseMultiReadName[{}]", databaseMultiReadName);
		logger.end(className, function);
		return databaseMultiReadName;
	}
	
	private DatabaseKey databaseKey = new DatabaseKey();
	@Override
	public void init() {
		String function = "init";
		logger.begin(className, function);
		
		getDatabaseMultiRead(getDatabaseMultiReadName());
		
		logger.end(className, function);
	}
}
