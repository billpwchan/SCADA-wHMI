package com.thalesgroup.scadagen.wrapper.wrapper.client.opm.user;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.config.client.ConfigProvider;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.opm.client.dto.RoleDto;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIWrapperRpcEvent_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.common.SetCurrentProfileCallback_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.page.spring.SpringChangePassword;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.user.role.UIOpmRoleSelect;
import com.thalesgroup.scadagen.wrapper.wrapper.client.uigeneric.UIGenericMgr;
import com.thalesgroup.scadagen.wrapper.wrapper.client.uigeneric.UIGenericMgrEvent;
import com.thalesgroup.scadagen.wrapper.wrapper.server.opm.uiaction.UIActionOpm_i;
import com.thalesgroup.scadagen.wrapper.wrapper.server.uigeneric.UIGenericServiceImpl_i;

public class UIUserSCADAgen implements UIUser_i {

	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
	
	private static UIUser_i instance = null;
	public static UIUser_i getInstance() { 
		if ( null == instance ) instance = new UIUserSCADAgen();
		return instance;
	}
	private UIUserSCADAgen () {}
	
	private String [] profileNames	= null;
	
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i#changePassword(java.lang.String, java.lang.String, java.lang.String, com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIWrapperRpcEvent_i)
	 */
	@Override
	public void changePassword(String operator, String oldPass, String newPass, UIWrapperRpcEvent_i uiWrapperRpcEvent_i) {
		String function = "changePassword";
		logger.begin(function);
		
		logger.debug(function, "operator[{}]", operator);

		new SpringChangePassword().changePassword(operator, oldPass, newPass, uiWrapperRpcEvent_i); 
		
		logger.end(function);
	}
	
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i#getCurrentOperator()
	 */
	@Override
	public String getCurrentOperator() {
		String function = "getCurrentOperator";
		logger.begin(function);
		String operatorId = null;
		operatorId = ConfigProvider.getInstance().getOperatorOpmInfo().getOperator().getId();
		logger.debug(function, "operatorId[{}]", operatorId);
		return operatorId;
	}
	
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i#getCurrentProfile()
	 */
	@Override
	public String getCurrentProfile() {
		String function = "getCurrentProfile";
		logger.begin(function);
		String profile = null;
//		if ( null == profileNames ) {
			getCurrentProfiles();
//		}
		if ( null != profileNames && profileNames.length > 0 ) {
			profile = profileNames[0];
		}
		logger.debug(function, "profile[{}]", profile);
		logger.end(function);
		return profile;
	}
	
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i#setCurrentProfile()
	 */
	@Override
	public String setCurrentProfile(final String profile) {
		final String f = "setCurrentProfile";
		logger.begin(f);
		
		logger.debug(f, "profile[{}]", profile);
		this.setCurrentProfile(profile, new SetCurrentProfileCallback_i() {

			@Override
			public void callback(final String profile) {
				
				logger.debug(f, "callback profile[{}]", profile);
				new UIOpmRoleSelect().update(profile);
			}
		});

		logger.end(f);
		return profile;
	}	
	
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i#getCurrentProfiles()
	 */
	@Override
	public String[] getCurrentProfiles() {
		String function = "getCurrentProfiles";
		logger.begin(function);
		
//		if ( null == profileNames ) {
			List<String> roleIds = null;
			Map<String, RoleDto> roles = ConfigProvider.getInstance().getOperatorOpmInfo().getOperator().getRoleId();
			if ( null != roles ) {
				Set<String> keys = roles.keySet();
				
				if ( null == roleIds ) roleIds = new LinkedList<String>();
				for ( String key : keys ) {
					logger.debug(function, "key[{}]", key);
					
					String roleId = roles.get(key).getId();
					logger.debug(function, "roleId[{}]", roleId);

					roleIds.add(roleId);
				}
				profileNames = roleIds.toArray(new String[0]);
			} else {
				logger.warn(function, "roleId IS NULL");
			}
//		}
		
		logger.debug(function, "profileNames[{}]", profileNames);
		logger.end(function);
		return profileNames;
	}
	
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i#getCurrentIPAddress(com.thalesgroup.scadagen.wrapper.wrapper.client.opm.common.GetCurrentIpAddressCallback_i)
	 */
	@Override
	public void setCurrentProfile(final String role, final SetCurrentProfileCallback_i cb) {
		final String function = "setCurrentProfile";
		logger.begin(function);
		
		UIGenericMgr uiGenericMgr = new UIGenericMgr();
		JSONObject request = new JSONObject();
        request.put(UIGenericServiceImpl_i.OperationAttribute3, new JSONString(UIActionOpm_i.ComponentName));
        request.put(UIGenericServiceImpl_i.OperationAttribute4, new JSONString(UIActionOpm_i.SelectRole));
        request.put(UIGenericServiceImpl_i.OperationAttribute5, new JSONString(role));
		
		uiGenericMgr.execute(request, new UIGenericMgrEvent() {
			
			@Override
			public void uiGenericMgrEventReady(JSONObject response) {
				final String function2 = function + " uiGenericMgrEventReady";
				logger.begin(function2);
				
				String currentProfile = null;
				
				if ( null != response ) {
					logger.debug(function2, "response[{}]", response.toString());
					JSONValue v = response.get(UIGenericServiceImpl_i.OperationParameter1);
					if ( null != v && null != v.isObject() ) {
						JSONObject o = v.isObject();
						if ( null != o ) {
							JSONValue tv = o.get(UIGenericServiceImpl_i.OperationValue1);
							if ( null != tv && null != tv.isString() ) {
								currentProfile = tv.isString().stringValue();
							} else {
								logger.warn(function2, "tv[{}] IS INVALID", tv);
							}
						} else {
							logger.warn(function2, "o IS NULL");
						}
					} else {
						logger.warn(function2, "v[{}] IS INVALID", v);
					}
					logger.debug(function2, "currentProfile[{}]", currentProfile);
				} else {
					logger.debug(function2, "response IS NULL");
				}
				
				if(null!=currentProfile) new UIOpmRoleSelect().update(currentProfile);
				
				cb.callback(currentProfile);
				logger.end(function2);
			}

			@Override
			public void uiGenericMgrEventFailed(JSONObject response) {
				final String function2 = function + " uiGenericMgrEventFailed";
				logger.beginEnd(function2);
			}
		});

		logger.end(function);
	}
	
	@Override
	public void init() {
	}
}
