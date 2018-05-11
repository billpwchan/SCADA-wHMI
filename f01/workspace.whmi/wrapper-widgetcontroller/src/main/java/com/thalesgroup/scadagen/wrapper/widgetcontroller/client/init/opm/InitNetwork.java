package com.thalesgroup.scadagen.wrapper.widgetcontroller.client.init.opm;

import java.util.Map;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.common.InitReady_i;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.common.Init_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.network.UINetworkFactory;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.network.UINetworkFactory_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.network.UINetworkSCADAgen;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.network.UINetwork_i;

public class InitNetwork implements Init_i {

	private final String className = this.getClass().getSimpleName();
	private final UILogger logger = UILoggerFactory.getInstance().getLogger(this.getClass().getName());
	
	private InitNetwork() {}
	private static InitNetwork instance = null;
	public static InitNetwork getInstance() { 
		if ( null == instance ) instance = new InitNetwork();
		return instance;
	}
	
	@Override
	public void init(Map<String, Object> params, InitReady_i initReady) {
		// TODO Auto-generated method stub
		
	}
	
	public void initFactory() {
		final String function = "initNetworkFactory";
		logger.begin(className, function);
		
		UINetworkFactory networkFactory = UINetworkFactory.getInstance();
		networkFactory.addFactory(className, new UINetworkFactory_i() {
			
			@Override
			public UINetwork_i get(String key) {
				UINetwork_i uiNetwork_i = null;
				if ( null != key ) {

					if ( UINetworkSCADAgen.class.getSimpleName().equalsIgnoreCase(key) ) {
						uiNetwork_i = UINetworkSCADAgen.getInstance();
					}
				}
				return uiNetwork_i;
			}
		});
		
		logger.end(className, function);
	}
	
	public void initNetwork(String key) {
		final String function = "initNetwork";
		logger.begin(className, function);
		logger.debug(className, function, "Try to init key[{}]", key);
		UINetwork_i uiNetwork_i = UINetworkFactory.getInstance().get(key);
		if ( null != uiNetwork_i ) {
			uiNetwork_i.init();
		} else {
			logger.warn(className, function, "uiNetwork_i IS NULL");
		}
		logger.end(className, function);
	}
	
}
