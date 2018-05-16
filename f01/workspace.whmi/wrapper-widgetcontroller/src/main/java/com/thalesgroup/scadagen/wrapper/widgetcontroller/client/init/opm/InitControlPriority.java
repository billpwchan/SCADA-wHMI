package com.thalesgroup.scadagen.wrapper.widgetcontroller.client.init.opm;

import java.util.Map;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.common.InitReady_i;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.common.Init_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.controlpriority.UIControlPriorityFactory;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.controlpriority.UIControlPriorityFactory_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.controlpriority.UIControlPrioritySCADAgen;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.controlpriority.UIControlPriority_i;

public class InitControlPriority implements Init_i {

	private final String className = this.getClass().getSimpleName();
	private final UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
	
	private InitControlPriority() {}
	private static InitControlPriority instance = null;
	public static InitControlPriority getInstance() { 
		if ( null == instance ) instance = new InitControlPriority();
		return instance;
	}
	
	@Override
	public void init(Map<String, Object> params, InitReady_i initReady) {
		// TODO Auto-generated method stub
		
	}
	
	public void initFactory() {
		final String function = "initControlPriorityFactory";
		logger.begin(function);
		
		UIControlPriorityFactory controlPriorityFactory = UIControlPriorityFactory.getInstance();
		controlPriorityFactory.addFactory(className, new UIControlPriorityFactory_i() {
			
			@Override
			public UIControlPriority_i get(String key) {
				UIControlPriority_i uiControlPriority_i = null;
				if ( null != key ) {
					
					if ( UIControlPrioritySCADAgen.class.getSimpleName().equalsIgnoreCase(key) ) {
						uiControlPriority_i = UIControlPrioritySCADAgen.getInstance();
					}
				}
				return uiControlPriority_i;
			}
		});
		
		logger.end(function);
	}
	
	public void initControlPriority(String key) {
		final String function = "initControlPriority";
		logger.begin(function);
		logger.debug(function, "Try to init key[{}]", key);
		UIControlPriority_i uiControlPriority_i = UIControlPriorityFactory.getInstance().get(key);
		if ( null != uiControlPriority_i ) {
			uiControlPriority_i.init();
		} else {
			logger.warn(function, "uiControlPriority_i IS NULL");
		}
		logger.end(function);
	}
	
}
