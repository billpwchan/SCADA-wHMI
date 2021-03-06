package com.thalesgroup.scadagen.wrapper.wrapper.client.opm.page;

import com.google.gwt.user.client.Window;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.page.spring.SpringLogin;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.page.spring.SpringLogout;

public class UIPageSCADAgen implements UIPage_i {

	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
	
	private static UIPage_i instance = null;
	public static UIPage_i getInstance() { 
		if ( null == instance ) instance = new UIPageSCADAgen();
		return instance;
	}
	private UIPageSCADAgen () {}
	
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i#login(java.lang.String, java.lang.String)
	 */
	@Override
	public void login(String operator, String password) {
		String function = "login";
		logger.begin(function);
		String SPRING_SEC_CHECK_URL = "j_spring_security_check";
		
		String user_name = "j_username";
		String pass_name = "j_password";
		
		new SpringLogin(SPRING_SEC_CHECK_URL, user_name, pass_name).login(operator, password);
		
		logger.end(function);
	}
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i#logout()
	 */
	@Override
	public void logout() {
		String function = "logout";
		logger.begin(function);
		
		String SPRING_SEC_LOGOUT_URL = "j_spring_security_logout";
		
		new SpringLogout(SPRING_SEC_LOGOUT_URL).logout();
		
		logger.end(function);
	}
	
	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i#reloadPage()
	 */
	@Override
	public void reloadPage() {
		String function = "reloadPage";
		logger.begin(function);
		
		Window.Location.reload();
		
		logger.end(function);
	}

	@Override
	public void init() {
	}
}
