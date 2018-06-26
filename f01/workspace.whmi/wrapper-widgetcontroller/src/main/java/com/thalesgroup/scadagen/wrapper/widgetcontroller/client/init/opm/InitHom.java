package com.thalesgroup.scadagen.wrapper.widgetcontroller.client.init.opm;

import java.util.Map;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.common.InitReady_i;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.common.Init_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.hom.UIHomFactory;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.hom.UIHomFactory_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.hom.UIHomSCADAgen;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.hom.UIHom_i;

public class InitHom implements Init_i {

	private final String className = this.getClass().getSimpleName();
	private final UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
	
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
		logger.begin(function);
		
		UIHomFactory homFactory = UIHomFactory.getInstance();
		homFactory.addFactory(className, new UIHomFactory_i() {
			
			@Override
			public UIHom_i get(String key) {
				UIHom_i uiHom_i = null;
				if ( null != key ) {

					if ( UIHomSCADAgen.class.getSimpleName().equalsIgnoreCase(key) ) {
						uiHom_i = UIHomSCADAgen.getInstance();
					}
				}
				if(null==uiHom_i) logger.warn(function, "key[{}], uiHom_i IS NULL", key);
				return uiHom_i;
			}
		});
		
		logger.end(function);
	}
	
	public void initHom(String key) {
		final String function = "initHom";
		logger.begin(function);
		logger.debug(function, "Try to init key[{}]", key);
		UIHom_i uiHom_i = UIHomFactory.getInstance().get(key);
		if ( null != uiHom_i ) {
			uiHom_i.init();
		} else {
			logger.warn(function, "uiHom_i IS NULL");
		}
		logger.end(function);
	}
	
	public String[] getKeys() {
		return new String[]{
				UIHomSCADAgen.class.getSimpleName()
		};
	}
	
}
