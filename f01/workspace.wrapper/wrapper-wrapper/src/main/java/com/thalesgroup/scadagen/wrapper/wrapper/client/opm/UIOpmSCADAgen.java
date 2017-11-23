package com.thalesgroup.scadagen.wrapper.wrapper.client.opm;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.config.client.ConfigProvider;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.opm.client.checker.AuthorizationCheckerC;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.opm.client.checker.IAuthorizationCheckerC;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.opm.client.dto.OperatorOpmInfo;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.opm.client.dto.OpmRequestDto;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.opm.client.dto.RoleDto;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseMultiRead_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.factory.DatabaseMultiReadFactory;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.hom.UIHomFactory;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.hom.UIHom_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.uigeneric.UIGenericMgr;
import com.thalesgroup.scadagen.wrapper.wrapper.client.uigeneric.UIGenericMgrEvent;
import com.thalesgroup.scadagen.wrapper.wrapper.server.opm.uiaction.UIActionOpm_i;
import com.thalesgroup.scadagen.wrapper.wrapper.server.uigeneric.UIGenericServiceImpl_i;

/**
 * SCADAgen is the subclass of UIOpm_i, it implement the OPM functionality for SCADAgen WHMI.
 * 
 * @author syau
 *
 */
public class UIOpmSCADAgen implements UIOpm_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIOpmSCADAgen.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private static UIOpm_i instance = null;
	public static UIOpm_i getInstance() { 
		if ( null == instance ) instance = new UIOpmSCADAgen();
		return instance;
	}
	private UIOpmSCADAgen () {}
	
	private final String STR_UIHOM_SCADAGEN = "UIHomSCADAgen";
	private UIHom_i uiHom_i = null;
	private UIHom_i getUIHom() {
		if ( null == uiHom_i ) {
			uiHom_i = UIHomFactory.getInstance().get(STR_UIHOM_SCADAGEN);
		}
		return uiHom_i;
	}

	private DatabaseMultiRead_i databaseMultiRead_i = null;
	
	private String [] profileNames	= null;
	
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i#init()
	 */
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
		
		profileNames = null;
		
		// Prepare HostName
		getCurrentHostName();
		
		// Prepare IPAddress
		getCurrentIPAddress();
		
		logger.end(className, function);
	}
	
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i#checkAccess(java.util.Map)
	 */
	@Override
	public boolean checkAccess(Map<String, String> parameter) {
		String function = "checkAccess";
		logger.begin(className, function);
		
		boolean ret = false;
		
		if ( null != parameter ) {
			
			if ( logger.isTraceEnabled() ) {
				for ( Map.Entry<String, String> entry : parameter.entrySet() ) {
					String k = entry.getKey();
					String v = entry.getValue();
					logger.trace(className, function, "k[{}] v[{}]", k, v);
					if ( k == null || k.isEmpty() || v == null || v.isEmpty() ) {
						logger.warn(className, function, "k[{}] OR v[{}] is INVALID", k, v);
					}
				}
			}

			OpmRequestDto dto = new OpmRequestDto();
			dto.setRequestParameters(parameter);
			OperatorOpmInfo operatorOpmInfo = ConfigProvider.getInstance().getOperatorOpmInfo();
			IAuthorizationCheckerC checker = new AuthorizationCheckerC();
			ret = checker.checkOperationIsPermitted( operatorOpmInfo, dto );

		} else {
			logger.warn(className, function,  "parameter IS NULL");
		}
		logger.debug(className, function, "ret[{}]", ret);
		return ret;
	}
	
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i#checkAccess(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
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
		
		boolean ret = false;
		
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
			ret = checker.checkOperationIsPermitted( operatorOpmInfo, dto );
		} else {
			logger.warn(className, function, "args null, or empty - " 
				+ "  "+opmName1+"=" + opmValue1 
				+ ", "+opmName2+"=" + opmValue2 
				+ ", "+opmName3+"=" + opmValue3 
				+ ", "+opmName4+"=" + opmValue4
				+ " - checkAccess return 'false'" );
		}
		logger.debug(className, function, "ret[{}]", ret);
		return ret;
	}
	
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i#checkAccess(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean checkAccess(String functionValue, String locationValue, String actionValue, String modeValue) {
		final String function = "checkAccess";
		logger.begin(className, function);
		logger.debug(className, function, "function[{}] location[{}] action[{}] mode[{}]  ", new Object[]{functionValue, locationValue, actionValue, modeValue});

		boolean ret = checkAccess(
								  FUNCTION, functionValue
								, LOCATION, locationValue
								, ACTION, actionValue
								, MODE, modeValue
								);
		logger.debug(className, function, "function[{}] location[{}] action[{}] mode[{}] ret[{}] ", new Object[]{functionValue, locationValue, actionValue, modeValue, ret});
		logger.end(className, function);
		return ret;
	}
	
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i#changePassword(java.lang.String, java.lang.String, java.lang.String, com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIWrapperRpcEvent_i)
	 */
	@Override
	public void changePassword(String operator, String oldPass, String newPass, UIWrapperRpcEvent_i uiWrapperRpcEvent_i) {
		String function = "changePassword";
		logger.begin(className, function);
		
		logger.debug(className, function, "operator[{}]", operator);

		new SpringChangePassword().changePassword(operator, oldPass, newPass, uiWrapperRpcEvent_i); 
		
		logger.end(className, function);
	}
	
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i#getCurrentOperator()
	 */
	@Override
	public String getCurrentOperator() {
		String function = "getCurrentOperator";
		logger.begin(className, function);
		String operatorId = null;
		operatorId = ConfigProvider.getInstance().getOperatorOpmInfo().getOperator().getId();
		logger.debug(className, function, "operatorId[{}]", operatorId);
		return operatorId;
	}
	
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i#getCurrentProfile()
	 */
	@Override
	public String getCurrentProfile() {
		String function = "getCurrentProfile";
		logger.begin(className, function);
		String profile = null;
		if ( null == profileNames ) {
			getCurrentProfiles();
		}
		if ( null != profileNames && profileNames.length > 0 ) {
			profile = profileNames[0];
		}
		logger.debug(className, function, "profile[{}]", profile);
		logger.end(className, function);
		return profile;
	}
	
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i#getCurrentProfiles()
	 */
	@Override
	public String[] getCurrentProfiles() {
		String function = "getCurrentProfiles";
		logger.begin(className, function);
		
		if ( null == profileNames ) {
			List<String> roleIds = null;
			Map<String, RoleDto> roles = ConfigProvider.getInstance().getOperatorOpmInfo().getOperator().getRoleId();
			if ( null != roles ) {
				Set<String> keys = roles.keySet();
				
				if ( null == roleIds ) roleIds = new LinkedList<String>();
				for ( String key : keys ) {
					logger.debug(className, function, "key[{}]", key);
					
					String roleId = roles.get(key).getId();
					logger.debug(className, function, "roleId[{}]", roleId);

					roleIds.add(roleId);
				}
				
				profileNames = roleIds.toArray(new String[0]);
			} else {
				logger.warn(className, function, "roleId IS NULL");
			}
		}
		
		logger.debug(className, function, "profileNames[{}]", profileNames);
		logger.end(className, function);
		return profileNames;
	}
	
	private String currentHostName = null;
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i#getCurrentHostName()
	 */
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
	
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i#getCurrentIPAddress()
	 */
	@Override
	public String getCurrentIPAddress() {
		final String function = "getCurrentIPAddress";
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


	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i#login(java.lang.String, java.lang.String)
	 */
	@Override
	public void login(String operator, String password) {
		String function = "login";
		logger.begin(className, function);
		String SPRING_SEC_CHECK_URL = "j_spring_security_check";
		
		String user_name = "j_username";
		String pass_name = "j_password";
		
		new SpringLogin(SPRING_SEC_CHECK_URL, user_name, pass_name).login(operator, password);
		
		logger.end(className, function);
	}
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i#logout()
	 */
	@Override
	public void logout() {
		String function = "logout";
		logger.begin(className, function);
		
		String SPRING_SEC_LOGOUT_URL = "j_spring_security_logout";
		
		new SpringLogout(SPRING_SEC_LOGOUT_URL).logout();
		
		logger.end(className, function);
	}
	
	@Override
	public boolean checkHom(final int hdv, final String identity) {
		return getUIHom().checkHom(hdv, identity);
	}
	@Override
	public void checkAccessWithHom(
			String function, String location, String action, String mode
			, String scsEnvId, String dbAddress
			, CheckAccessWithHOMEvent_i resultEvent) {
		getUIHom().checkAccessWithHom(function, location, action, mode
				, scsEnvId, dbAddress
				, this
				, resultEvent);
	}
	@Override
	public void checkAccessWithHom(
			String function, String location, String action, String mode
			, int hdv, String identity
			, CheckAccessWithHOMEvent_i resultEvent) {
		getUIHom().checkAccessWithHom(
				function, location, action, mode
				, hdv, identity
				, this
				, resultEvent);
	}
	@Override
	public boolean isHOMAction(String action) {
		return getUIHom().isHOMAction(action);
	}
	@Override
	public boolean isBypassValue(int value) {
		return getUIHom().isBypassValue(value);
	}
	@Override
	public void getCurrentHOMValue(String scsEnvId, String dbAddress, GetCurrentHOMValueEvent_i event) {
		getUIHom().getCurrentHOMValue(scsEnvId, dbAddress, event);
	}
	
	private String env=null;
	@Override
	public String getCurrentEnv() {
		return this.env;
	}
	@Override
	public void setCurrentEnv(String env) {
		this.env=env;
	}
	private String alias=null;
	@Override
	public String getCurrentAlias() {
		return this.alias;
	}
	@Override
	public void setCurrentAlias(String alias) {
		this.alias=alias;
	}

}
