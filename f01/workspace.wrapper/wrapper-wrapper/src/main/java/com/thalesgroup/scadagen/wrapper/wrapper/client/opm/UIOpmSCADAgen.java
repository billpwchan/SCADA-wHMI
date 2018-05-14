package com.thalesgroup.scadagen.wrapper.wrapper.client.opm;

import java.util.Map;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseMultiRead_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.factory.DatabaseMultiReadFactory;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.common.GetCurrentIpAddressCallback_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.access.UIAccessFactory;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.access.UIAccess_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.common.GetCurrentHostNameCallback_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.common.SetCurrentProfileCallback_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.hom.UIHomFactory;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.hom.UIHom_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.network.UINetwork_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.page.UIPageFactory;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.page.UIPage_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.user.UIUserFactory;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.user.UIUser_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.network.UINetworkFactory;

/**
 * SCADAgen is the subclass of UIOpm_i, it implement the OPM functionality for SCADAgen WHMI.
 * 
 * @author syau
 *
 */
public class UIOpmSCADAgen implements UIOpm_i {
	
	private final String className = this.getClass().getSimpleName();
	private UILogger logger = UILoggerFactory.getInstance().getLogger(this.getClass().getName());
	
	private static UIOpm_i instance = null;
	public static UIOpm_i getInstance() { 
		if ( null == instance ) instance = new UIOpmSCADAgen();
		return instance;
	}
	private UIOpmSCADAgen () {}
	
	private final String STR_UIACCESS_SCADAGEN = "UIAccessSCADAgen";
	private UIAccess_i uiAccess_i = null;
	private UIAccess_i getUIAccess() {
		if ( null == uiAccess_i ) {
			uiAccess_i = UIAccessFactory.getInstance().get(STR_UIACCESS_SCADAGEN);
		}
		return uiAccess_i;
	}
	
	private final String STR_UIPAGE_SCADAGEN = "UIPageSCADAgen";
	private UIPage_i uiPage_i = null;
	private UIPage_i getUIPage() {
		if ( null == uiPage_i ) {
			uiPage_i = UIPageFactory.getInstance().get(STR_UIPAGE_SCADAGEN);
		}
		return uiPage_i;
	}
	
	private final String STR_UIUSER_SCADAGEN = "UIUserSCADAgen";
	private UIUser_i uiUser_i = null;
	private UIUser_i getUIUser() {
		if ( null == uiUser_i ) {
			uiUser_i = UIUserFactory.getInstance().get(STR_UIUSER_SCADAGEN);
		}
		return uiUser_i;
	}	

	private final String STR_UINetwork_SCADAGEN = "UINetworkSCADAgen";
	private UINetwork_i uiNetwork_i = null;
	private UINetwork_i getUINetwork() {
		if ( null == uiNetwork_i ) {
			uiNetwork_i = UINetworkFactory.getInstance().get(STR_UINetwork_SCADAGEN);
		}
		return uiNetwork_i;
	}
	
	private final String STR_UIHOM_SCADAGEN = "UIHomSCADAgen";
	private UIHom_i uiHom_i = null;
	private UIHom_i getUIHom() {
		if ( null == uiHom_i ) {
			uiHom_i = UIHomFactory.getInstance().get(STR_UIHOM_SCADAGEN);
		}
		return uiHom_i;
	}

	private DatabaseMultiRead_i databaseMultiRead_i = null;

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
		return getUIAccess().checkAccess(parameter);
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
		return getUIAccess().checkAccess(
				opmName1, opmValue1
				, opmName2, opmValue2
				, opmName3, opmValue3
				, opmName4, opmValue4);
	}
	
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i#checkAccess(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean checkAccess(String function, String location, String action, String mode) {
		return getUIAccess().checkAccess(function, location, action, mode);
	}
	
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i#changePassword(java.lang.String, java.lang.String, java.lang.String, com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIWrapperRpcEvent_i)
	 */
	@Override
	public void changePassword(String operator, String oldPass, String newPass, UIWrapperRpcEvent_i uiWrapperRpcEvent_i) {
		getUIUser().changePassword(operator, oldPass, newPass, uiWrapperRpcEvent_i);
	}
	
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i#getCurrentOperator()
	 */
	@Override
	public String getCurrentOperator() {
		return getUIUser().getCurrentOperator();
	}
	
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i#getCurrentProfile()
	 */
	@Override
	public String getCurrentProfile() {
		return getUIUser().getCurrentProfile();
	}
	
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i#setCurrentProfile()
	 */
	@Override
	public String setCurrentProfile(final String profile) {
		return getUIUser().setCurrentProfile(profile);
	}	
	
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i#getCurrentProfiles()
	 */
	@Override
	public String[] getCurrentProfiles() {
		return getUIUser().getCurrentProfiles();
	}
	
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i#getCurrentIPAddress(com.thalesgroup.scadagen.wrapper.wrapper.client.opm.common.GetCurrentIpAddressCallback_i)
	 */
	@Override
	public void setCurrentProfile(final String role, final SetCurrentProfileCallback_i cb) {
		getUIUser().setCurrentProfile(role, cb);
	}

	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i#getCurrentHostName()
	 */
	@Override
	public String getCurrentHostName() {
		return getUINetwork().getCurrentHostName();
	}
	
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i#getCurrentHostName(com.thalesgroup.scadagen.wrapper.wrapper.client.opm.common.IGetCurrentHostNameCallback)
	 */
	@Override
	public void getCurrentHostName(final GetCurrentHostNameCallback_i cb) {
		getUINetwork().getCurrentHostName(cb);
	}

	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i#getCurrentIPAddress()
	 */
	@Override
	public String getCurrentIPAddress() {
		return getUINetwork().getCurrentIPAddress();
	}
	
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i#getCurrentIPAddress(com.thalesgroup.scadagen.wrapper.wrapper.client.opm.common.GetCurrentIpAddressCallback_i)
	 */
	@Override
	public void getCurrentIPAddress(final GetCurrentIpAddressCallback_i cb) {
		getUINetwork().getCurrentIPAddress(cb);
	}

	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i#login(java.lang.String, java.lang.String)
	 */
	@Override
	public void login(String operator, String password) {
		getUIPage().login(operator, password);
	}
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i#logout()
	 */
	@Override
	public void logout() {
		getUIPage().logout();
	}
	
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i#reloadPage()
	 */
	@Override
	public void reloadPage() {
		getUIPage().reloadPage();
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
	public boolean checkAccessWithHom(
			String function, String location, String action, String mode
			, int hdv) {
		return getUIHom().checkAccessWithHom(
				function, location, action, mode
				, hdv, this);
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
	@Override
	public String getHOMIdentityType() {
		return getUIHom().getHOMIdentityType();
	}	
	@Override
	public String getHOMIdentity() {
		return getUIHom().getHOMIdentity(this);
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
