package com.thalesgroup.scadagen.wrapper.widgetcontroller.client.init.opm;

import java.util.Map;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.common.InitReady_i;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.common.Init_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.user.UIUserFactory;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.user.UIUserFactory_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.user.UIUserSCADAgen;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.user.UIUser_i;

public class InitUser implements Init_i {

	private final String className = this.getClass().getSimpleName();
	private final UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
	
	private InitUser() {}
	private static InitUser instance = null;
	public static InitUser getInstance() { 
		if ( null == instance ) instance = new InitUser();
		return instance;
	}
	
	@Override
	public void init(Map<String, Object> params, InitReady_i initReady) {
		// TODO Auto-generated method stub
		
	}
	
	public void initFactory() {
		final String function = "initUserFactory";
		logger.begin(function);
		
		UIUserFactory userFactory = UIUserFactory.getInstance();
		userFactory.addFactory(className, new UIUserFactory_i() {
			
			@Override
			public UIUser_i get(String key) {
				UIUser_i uiUser_i = null;
				if ( null != key ) {

					if ( UIUserSCADAgen.class.getSimpleName().equalsIgnoreCase(key) ) {
						uiUser_i = UIUserSCADAgen.getInstance();
					}
				}
				return uiUser_i;
			}
		});
		
		logger.end(function);
	}
	
	public void initUser(String key) {
		final String function = "initUser";
		logger.begin(function);
		logger.debug(function, "Try to init key[{}]", key);
		UIUser_i uiUser_i = UIUserFactory.getInstance().get(key);
		if ( null != uiUser_i ) {
			uiUser_i.init();
		} else {
			logger.warn(function, "uiUser_i IS NULL");
		}
		logger.end(function);
	}
	
}
