package com.thalesgroup.scadagen.wrapper.widgetcontroller.client.init;

import java.util.Map;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.common.InitReady_i;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.common.Init_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.hom.UIHomFactory;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.hom.UIHomFactory_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.hom.UIHomSCADAgen;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.hom.UIHom_i;

public class InitHom implements Init_i {

	private final String className = UIWidgetUtil.getClassSimpleName(InitHom.class.getName());
	private final UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private InitHom() {}
	private static InitHom instance = null;
	public static InitHom getInstance() { 
		if ( null == instance ) instance = new InitHom();
		return instance;
	}
	
	@Override
	public void init(Map<String, Object> params, InitReady_i initReady) {
		// TODO Auto-generated method stub
		
	}
	
	public void initFactory() {
		final String function = "initHomFactory";
		logger.begin(className, function);
		
		UIHomFactory homFactory = UIHomFactory.getInstance();
		homFactory.addFactory(className, new UIHomFactory_i() {
			
			@Override
			public UIHom_i get(String key) {
				UIHom_i uiHom_i = null;
				if ( null != key ) {
					
					String uiHomSCADAgenName = UIWidgetUtil.getClassSimpleName(UIHomSCADAgen.class.getName());
					
					if ( key.equalsIgnoreCase(uiHomSCADAgenName) ) {
						uiHom_i = UIHomSCADAgen.getInstance();
					}
				}
				return uiHom_i;
			}
		});
		
		logger.end(className, function);
	}
	
	public void initHom(String key) {
		final String function = "initHom";
		logger.begin(className, function);
		logger.debug(className, function, "Try to init key[{}]", key);
		UIHom_i uiHom_i = UIHomFactory.getInstance().get(key);
		if ( null != uiHom_i ) {
			uiHom_i.init();
		} else {
			logger.warn(className, function, "uiHom_i IS NULL");
		}
		logger.end(className, function);
	}
	
}
