package com.thalesgroup.scadagen.wrapper.wrapper.client.opm;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.config.client.ConfigProvider;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.opm.client.checker.AuthorizationCheckerC;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.opm.client.checker.IAuthorizationCheckerC;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.opm.client.dto.OperatorOpmInfo;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.opm.client.dto.OpmRequestDto;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.opm.client.dto.RoleDto;
import com.thalesgroup.scadagen.whmi.config.configenv.client.ReadJson;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseMultiRead_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabasePairEvent_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.factory.DatabaseMultiReadFactory;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.util.HVID2SCS;
import com.thalesgroup.scadagen.wrapper.wrapper.client.uigeneric.UIGenericMgr;
import com.thalesgroup.scadagen.wrapper.wrapper.client.uigeneric.UIGenericMgrEvent;
import com.thalesgroup.scadagen.wrapper.wrapper.server.opm.uiaction.UIActionOpm_i;
import com.thalesgroup.scadagen.wrapper.wrapper.server.uigeneric.UIGenericServiceImpl_i;

public class UIOpmSCADAgen implements UIOpm_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIOpmSCADAgen.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private static UIOpm_i instance = null;
	public static UIOpm_i getInstance() { 
		if ( null == instance ) instance = new UIOpmSCADAgen();
		return instance;
	}
	private UIOpmSCADAgen () {}

	private DatabaseMultiRead_i databaseMultiRead_i = null;
	
	private String [] profileNames	= null;
	@Override
	public void init() {
		String function = "init";
		logger.begin(className, function);

		String multiReadMethod1 = "DatabaseMultiReading";
		
		databaseMultiRead_i = DatabaseMultiReadFactory.get(multiReadMethod1);
		if ( null != databaseMultiRead_i ) {
			databaseMultiRead_i.connect();
		} else {
			logger.warn(className, function, "multiReadMethod1[{}] databaseMultiRead_i IS NULL", multiReadMethod1);
		}
		
		// Prepare HostName
		getCurrentHostName();
		
		// Prepare IPAddress
		getCurrentIPAddress();
		
		logger.end(className, function);
	}
	
	@Override
	public boolean checkAccess(
			  String opmName1, String opmValue1
			, String opmName2, String opmValue2
			, String opmName3, String opmValue3
			, String opmName4, String opmValue4) {
		String function = "checkAccess";
		logger.begin(className, function);
		
		logger.debug(className, function, "opmName1[{}] opmValue1[{}]", opmName1, opmValue1);
		logger.debug(className, function, "opmName2[{}] opmValue2[{}]", opmName2, opmValue2);
		logger.debug(className, function, "opmName3[{}] opmValue3[{}]", opmName3, opmValue3);
		logger.debug(className, function, "opmName4[{}] opmValue4[{}]", opmName4, opmValue4);
		
		boolean result = false;
		
		if ( 
				   opmName1 != null && ! opmName1.isEmpty() 
				&& opmValue1 != null && !opmValue1.isEmpty()
				
				&& opmName2 != null && !opmName2.isEmpty()
				&& opmValue2 != null && !opmValue2.isEmpty()
				
				&& opmName3 != null && !opmName3.isEmpty()
				&& opmValue3 != null && !opmValue3.isEmpty()
				
				&& opmName4 != null && !opmName4.isEmpty()
				&& opmValue4 != null && !opmValue4.isEmpty()
		) {
			OpmRequestDto dto = new OpmRequestDto();
			dto.addParameter( opmName1, opmValue1 );
			dto.addParameter( opmName2, opmValue2 );
			dto.addParameter( opmName3, opmValue3 );
			dto.addParameter( opmName4, opmValue4 );
		
			OperatorOpmInfo operatorOpmInfo = ConfigProvider.getInstance().getOperatorOpmInfo();
			
			// TO: remove the non target role in here
		
			IAuthorizationCheckerC checker = new AuthorizationCheckerC();
			result = checker.checkOperationIsPermitted( operatorOpmInfo, dto );
		} else {
			logger.debug( "checkAccess args null, or empty - " 
				+ "  "+opmName1+"=" + opmValue1 
				+ ", "+opmName2+"=" + opmValue2 
				+ ", "+opmName3+"=" + opmValue3 
				+ ", "+opmName4+"=" + opmValue4
				+ " - checkAccess return 'false'" );
		}
		
		return result;
	}
	
	@Override
	public boolean checkAccess(String functionValue, String locationValue, String actionValue, String modeValue) {
		final String function = "function";
		logger.begin(className, function);
		logger.info(className, function, "function[{}] location[{}] action[{}] mode[{}]  ", new Object[]{functionValue, locationValue, actionValue, modeValue});
		
		boolean result = false;
		
		result = checkAccess(
								  FUNCTION, functionValue
								, LOCATION, locationValue
								, ACTION, actionValue
								, MODE, modeValue
							);
		
		logger.end(className, function);
		return result;
	}

	@Override
	public void checkAccessWithHom(final String functionValue
			, final String locationValue
			, final String actionValue
			, final String modeValue
			, final String hvid
			, final String key
			, final CheckAccessWithHOMEvent_i resultEvent) {
		final String function = "checkAccess";
		logger.begin(className, function);
		logger.debug(className, function, "functionValue[{}] locationValue[{}] actionValue[{}] modeValue[{}] hvid[{}] key[{}]"
				, new Object[]{functionValue, locationValue, actionValue, modeValue, hvid, key});
		
		if ( null != resultEvent ) {
			if ( isHOMAction(actionValue) ) {
				getCurrentHOMValue(hvid, new GetCurrentHOMValueEvent_i() {
					@Override
					public void update(String dbaddress, int value) {

						boolean caResult = false;
						boolean homResult = false;
					
						if ( ! isByPassValue(value) ) {
							if ( (value & getConfigHOMMask(key)) > 0 ) {
								homResult = true;
							}
						} else {
							homResult = true;
						}
						
						caResult = checkAccess(
										  functionValue
										, locationValue
										, actionValue
										, modeValue
										);
						
						resultEvent.result(caResult && homResult);
					}
				});
			} else {
				boolean caResult = false;
				
				caResult = checkAccess(
						  functionValue
						, locationValue
						, actionValue
						, modeValue
						);
				
				resultEvent.result(caResult);
			}
		} else {
			logger.warn(className, function, "resultEvent IS NULL");
		}
		
		logger.end(className, function);
	}
	
	@Override
	public void checkAccessWithHostName(String functionValue, String locationValue, String actionValue, String modeValue, String hvid, CheckAccessWithHOMEvent_i resultEvent) {
		final String function = "checkAccessWithHostName";
		logger.begin(className, function);
		logger.debug(className, function, "function[{}] location[{}] action[{}] mode[{}] hvid[{}]", new Object[]{function, locationValue, actionValue, modeValue, hvid});
		
		checkAccessWithHom(
				  functionValue
				, locationValue
				, actionValue
				, modeValue
				, hvid
				, getCurrentHostName()
				, resultEvent
				);
		
		logger.end(className, function);
	}
	
	@Override
	public void checkAccessWithProfileName(String functionValue, String locationValue, String actionValue, String modeValue, String hvid, CheckAccessWithHOMEvent_i resultEvent) {
		final String function = "checkAccessWithProfileName";
		logger.begin(className, function);
		logger.debug(className, function, "function[{}] location[{}] action[{}] mode[{}] hvid[{}]", new Object[]{function, locationValue, actionValue, modeValue, hvid});

		checkAccessWithHom(
				  functionValue
				, locationValue
				, actionValue
				, modeValue
				, hvid
				, getCurrentProfile()
				, resultEvent
				);
		
		logger.end(className, function);
	}
	
	@Override
	public void changePassword(String operator, String oldPass, String newPass, UIWrapperRpcEvent_i uiWrapperRpcEvent_i) {
		String function = "changePassword";
		logger.begin(className, function);
		
		logger.debug(className, function, "operator[{}]", operator);

		new SpringChangePassword().changePassword(operator, oldPass, newPass, uiWrapperRpcEvent_i); 
		
		logger.end(className, function);
	}
	@Override
	public String getCurrentOperator() {
		String function = "getCurrentOperator";
		logger.begin(className, function);
		String operatorId = null;
		operatorId = ConfigProvider.getInstance().getOperatorOpmInfo().getOperator().getId();
		logger.debug(className, function, "operatorId[{}]", operatorId);
		return operatorId;
	}
	@Override
	public String getCurrentProfile() {
		String function = "getCurrentProfile";
		logger.begin(className, function);
		String profile = null;
		if ( null == profileNames ) {
			getCurrentProfiles();
		}
		if ( null != profileNames ) {
			if ( profileNames.length > 0 ) {
				profile = profileNames[0];
			}
		}
		logger.debug(className, function, "profile[{}]", profile);
		logger.end(className, function);
		return profile;
	}
	
	@Override
	public String[] getCurrentProfiles() {
		String function = "getCurrentProfiles";
		logger.begin(className, function);
		
		if ( null == profileNames ) {
			List<String> roleIds = null;
			Map<String, RoleDto> roles = ConfigProvider.getInstance().getOperatorOpmInfo().getOperator().getRoleId();
			if ( null != roles ) {
				Set<String> keys = roles.keySet();
				for ( String key : keys ) {
					logger.debug(className, function, "key[{}]", key);
					
					String roleId = roles.get(key).getId();
					logger.debug(className, function, "roleId[{}]", roleId);
					
					if ( null == roleIds ) roleIds = new LinkedList<String>();
					
					roleIds.add(roleId);
				}
			} else {
				logger.warn(className, function, "roleId IS NULL");
			}
			
			profileNames = roleIds.toArray(new String[0]);
		}
		
		logger.debug(className, function, "profileNames[{}]", profileNames);
		logger.end(className, function);
		return profileNames;
	}
	
	@Override
	public void setCurrentProfile() {
		// TODO Auto-generated method stub
		
	}
	
	private String currentHostName = null;
	@Override
	public String getCurrentHostName() {
		final String function = "getCurrentHostName";
		logger.begin(className, function);
		
		if ( null == currentHostName ) {
			JSONObject request = new JSONObject();
	        request.put(UIGenericServiceImpl_i.OperationAttribute3, new JSONString(UIActionOpm_i.ComponentName));
	        request.put(UIGenericServiceImpl_i.OperationAttribute4, new JSONString(UIActionOpm_i.GetCurrentHostName));
			
			UIGenericMgr uiGenericMgr = new UIGenericMgr();
			uiGenericMgr.execute(request, new UIGenericMgrEvent() {
				
				@Override
				public void uiGenericMgrEventReady(JSONObject response) {
					final String function2 = function + " uiGenericMgrEventReady";
					logger.begin(className, function2);
					if ( null != response ) {
						logger.debug(className, function2, "response[{}]", response.toString());
						JSONValue v = response.get(UIGenericServiceImpl_i.OperationParameter1);
						if ( null != v && null != v.isObject() ) {
							JSONObject o = v.isObject();
							if ( null != o ) {
								JSONValue tv = o.get(UIGenericServiceImpl_i.OperationValue1);
								if ( null != tv && null != tv.isString() ) {
									currentHostName = tv.isString().stringValue();
								} else {
									logger.warn(className, function2, "tv[{}] IS INVALID", tv);
								}
							} else {
								logger.warn(className, function2, "o IS NULL");
							}
						} else {
							logger.warn(className, function2, "v[{}] IS INVALID", v);
						}
						logger.debug(className, function2, "currentHostName[{}]", currentHostName);
					} else {
						logger.warn(className, function2, "response IS NULL");
					}
					logger.end(className, function2);
				}
	
				@Override
				public void uiGenericMgrEventFailed(JSONObject response) {
					final String function2 = function + " uiGenericMgrEventFailed";
					logger.beginEnd(className, function2);
				}
			});			
		}
		logger.debug(className, function, "currentHostName[{}]", currentHostName);
		logger.end(className, function);
		return currentHostName;
	}
	
	private String currentIPAddress = null;
	@Override
	public String getCurrentIPAddress() {
		final String function = "getIPAddress";
		logger.begin(className, function);
		
		if ( null == currentIPAddress ) {
			UIGenericMgr uiGenericMgr = new UIGenericMgr();
			JSONObject request = new JSONObject();
	        request.put(UIGenericServiceImpl_i.OperationAttribute3, new JSONString(UIActionOpm_i.ComponentName));
	        request.put(UIGenericServiceImpl_i.OperationAttribute4, new JSONString(UIActionOpm_i.GetCurrentIPAddress));
			
			uiGenericMgr.execute(request, new UIGenericMgrEvent() {
				
				@Override
				public void uiGenericMgrEventReady(JSONObject response) {
					final String function2 = function + " uiGenericMgrEventReady";
					logger.begin(className, function2);
					if ( null != response ) {
						logger.debug(className, function2, "response[{}]", response.toString());
						JSONValue v = response.get(UIGenericServiceImpl_i.OperationParameter1);
						if ( null != v && null != v.isObject() ) {
							JSONObject o = v.isObject();
							if ( null != o ) {
								JSONValue tv = o.get(UIGenericServiceImpl_i.OperationValue1);
								if ( null != tv && null != tv.isString() ) {
									currentIPAddress = tv.isString().stringValue();
								} else {
									logger.warn(className, function2, "tv[{}] IS INVALID", tv);
								}
							} else {
								logger.warn(className, function2, "o IS NULL");
							}
						} else {
							logger.warn(className, function2, "v[{}] IS INVALID", v);
						}
						logger.debug(className, function2, "currentIPAddress[{}]", currentIPAddress);
					} else {
						logger.debug(className, function2, "response IS NULL");
					}
					logger.end(className, function2);
				}

				@Override
				public void uiGenericMgrEventFailed(JSONObject response) {
					final String function2 = function + " uiGenericMgrEventFailed";
					logger.beginEnd(className, function2);
				}
			});
		}
		logger.debug(className, function, "currentIPAddress[{}]", currentIPAddress);
		logger.end(className, function);
		return currentIPAddress;
	}
	@Override
	public void getCurrentHOMValue(final String hvid, final GetCurrentHOMValueEvent_i getCurrentHOMValueEvent_i) {
		final String function = "getCurrentHOMValue";
		logger.begin(className, function);
		logger.debug(className, function, "hvid[{}]", hvid);
		HVID2SCS hvid2scs = new HVID2SCS();
		hvid2scs.setHVID(hvid);
		hvid2scs.init();
		
		String scsEnvId	= hvid;
		String parent	= hvid2scs.getDBAddress();
		
		String dbAddress = parent + getDbAttribute();
		String [] dbAddresses = new String[]{dbAddress};
		
		final String clientKey = className+function;
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
	
	private HashMap<String, Integer> confighommasks	= new HashMap<String, Integer>();
	@Override
	public int getConfigHOMMask(String key) {
		String function = "getConfigHOMMask";
		logger.begin(className, function);
		
		if ( ! confighommasks.containsKey(key) ) {
			
			String dictionariesCacheName = UIOpmSCADAgen_i.dictionariesCacheName;
			String fileName = UIOpmSCADAgen_i.fileName;
			String arraykey = UIOpmSCADAgen_i.homLevelsArrayKey;
			JSONArray jsonArray = ReadJson.readArray(dictionariesCacheName, fileName, arraykey);
			
			String objectkey = "Key";
			JSONObject jsonObject = ReadJson.readObject(jsonArray, objectkey, key);
			
			String valueKey = "Value";
			int confighommask = ReadJson.readInt(jsonObject, valueKey, -1);
			
			confighommasks.put(key, confighommask);
		}
		int confighommask = confighommasks.get(key);
		logger.debug(className, function, "confighommask[{}]", confighommask);
		logger.end(className, function);
		return confighommask;
	}
	
	@Override
	public boolean createOperator(String operator) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean removeOperator(String operator) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean addOperatorProfile(String operator, String profile) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean login(String operator, String password) {
		String function = "login";
		logger.begin(className, function);
		String SPRING_SEC_CHECK_URL = "j_spring_security_check";
		
		String user_name = "j_username";
		String pass_name = "j_password";
		
		new SpringLogin(SPRING_SEC_CHECK_URL, user_name, pass_name).login(operator, password);
		
		logger.end(className, function);
		return true;
	}
	@Override
	public boolean logout() {
		String function = "logout";
		logger.begin(className, function);
		
		String SPRING_SEC_LOGOUT_URL = "j_spring_security_logout";
		
		new SpringLogout(SPRING_SEC_LOGOUT_URL).logout();
		
		logger.end(className, function);
		return true;
	}

	private String dbAttribute = null;
	private String getDbAttribute() {
		String function = "getDbAttribute";
		logger.begin(className, function);

		if ( null == dbAttribute ) {
			String dictionariesCacheName = UIOpmSCADAgen_i.dictionariesCacheName;
			String fileName = UIOpmSCADAgen_i.fileName;
			String key = UIOpmSCADAgen_i.dbAttributekey;
			String defaultValue = "Can't not read db attribute";
			dbAttribute = ReadJson.readString(dictionariesCacheName, fileName, key, defaultValue);
		}
		logger.debug(className, function, "dbAttribute[{}]", dbAttribute);
		logger.end(className, function);
		return dbAttribute;
	}
	
	private String homActions[] = null;
	private String[] getHomActions() {
		final String function = "getHomActions";
		logger.begin(className, function);
		
		if ( null == homActions ) {
			String dictionariesCacheName = UIOpmSCADAgen_i.dictionariesCacheName;
			String fileName = UIOpmSCADAgen_i.fileName;
			String key = UIOpmSCADAgen_i.homActionsArrayKey;
			JSONArray array = ReadJson.readArray(dictionariesCacheName, fileName, key);
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
	
	public boolean isHOMAction(String action) {
		final String function = "isHOMAction";
		logger.begin(className, function);
		boolean result = false;
		String [] homActions = getHomActions();
		if ( null != homActions) {
			for ( int i = 0 ; i < homActions.length ; i++ ) {
				String homAction = homActions[i];
				if ( homAction != null ) {
					logger.debug(className, function, "homActions[{}] == action[{}]", homAction, action);
					if ( 0 == homAction.compareTo(action) ) {
						result = true;
						break;
					}
				} else {
					logger.warn(className, function, "homActions([{}]) IS NULL", i);
				}
			}
		} else {
			logger.warn(className, function, "homActions IS NULL");
		}
		logger.end(className, function);
		return result;
	}

	private int byPassValue				= -1;
	boolean byPassValueReady 			= false;
	private int getByPassValue() {
		String function = "getByPassValue";
		logger.begin(className, function);
		
		if ( ! byPassValueReady ) {
			String dictionariesCacheName = UIOpmSCADAgen_i.dictionariesCacheName;
			String fileName = UIOpmSCADAgen_i.fileName;
			String key = UIOpmSCADAgen_i.byPassValuekey;
			int defaultValue = -1;
			byPassValue = ReadJson.readInt(dictionariesCacheName, fileName, key, defaultValue);
			
			byPassValueReady = true;
		}
		logger.debug(className, function, "byPassValue[{}]", byPassValue);
		logger.end(className, function);
		return byPassValue;
	}
	
	public boolean isByPassValue(int value) {
		String function = "isByPassValue";
		logger.debug(className, function, "getByPassValue()[{}] == value[{}]", getByPassValue(), value);
		return (getByPassValue() == value);
	}

}
