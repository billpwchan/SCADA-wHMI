package com.thalesgroup.scadagen.wrapper.widgetcontroller.client.init.opm;

import java.util.Map;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.common.InitReady_i;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.common.Init_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.access.UIAccessFactory;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.access.UIAccessFactory_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.access.UIAccessSCADAgen;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.access.UIAccess_i;

public class InitAccess implements Init_i {

	private final String className = this.getClass().getSimpleName();
	private final UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
	
	private InitAccess() {}
	private static InitAccess instance = null;
	public static InitAccess getInstance() { 
		if ( null == instance ) instance = new InitAccess();
		return instance;
	}
	
	@Override
	public void init(Map<String, Object> params, InitReady_i initReady) {
		// TODO Auto-generated method stub
		
	}
	
	public void initFactory() {
		final String function = "initAccessFactory";
		logger.begin(function);
		
		UIAccessFactory accessFactory = UIAccessFactory.getInstance();
		accessFactory.addFactory(className, new UIAccessFactory_i() {
			
			@Override
			public UIAccess_i get(String key) {
				UIAccess_i uiAccess_i = null;
				if ( null != key ) {

					if ( UIAccessSCADAgen.class.getSimpleName().equalsIgnoreCase(key) ) {
						uiAccess_i = UIAccessSCADAgen.getInstance();
					}
				}
				return uiAccess_i;
			}
		});
		
		logger.end(function);
	}
	
	public void initAccess(String key) {
		final String function = "initAccess";
		logger.begin(function);
		logger.debug(function, "Try to init key[{}]", key);
		UIAccess_i uiAccess_i = com.thalesgroup.scadagen.wrapper.wrapper.client.opm.access.UIAccessFactory.getInstance().get(key);
		if ( null != uiAccess_i ) {
			uiAccess_i.init();
		} else {
			logger.warn(function, "uiAccess_i IS NULL");
		}
		logger.end(function);
	}
	
}
