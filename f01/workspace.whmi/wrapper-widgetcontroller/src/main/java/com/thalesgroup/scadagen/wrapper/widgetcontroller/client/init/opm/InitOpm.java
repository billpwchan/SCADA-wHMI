package com.thalesgroup.scadagen.wrapper.widgetcontroller.client.init.opm;

import java.util.Map;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.common.InitReady_i;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.common.Init_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.OpmMgr;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpmFactory;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpmSCADAgen;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i;

public class InitOpm implements Init_i {
	
	private final String className = this.getClass().getSimpleName();
	private final UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
	
	private InitOpm() {}
	private static InitOpm instance = null;
	public static InitOpm getInstance() { 
		if ( null == instance ) instance = new InitOpm();
		return instance;
	}
	
	@Override
	public void init(Map<String, Object> params, InitReady_i initReady) {
		// TODO Auto-generated method stub
		
	}
	
	public void initFactory() {
		final String function = "initOpmFactory";
		logger.begin(function);
		
		OpmMgr opmMgr = OpmMgr.getInstance();
		opmMgr.addUIOpmFactory(className, new UIOpmFactory() {
			
			@Override
			public UIOpm_i getOpm(String key) {
				UIOpm_i uiOpm_i = null;
				if ( null != key ) {

					if ( UIOpmSCADAgen.class.getSimpleName().equalsIgnoreCase(key) ) {
						uiOpm_i = UIOpmSCADAgen.getInstance();
					}
				}
				return uiOpm_i;
			}
		});
		
		logger.end(function);
	}
	
	public void initOpm(String opmkey) {
		final String function = "initOpm";
		logger.begin(function);
		logger.debug(function, "Try to init opm[{}]", opmkey);
		UIOpm_i uiOpm_i = OpmMgr.getInstance().getOpm(opmkey);
		if ( null != uiOpm_i ) {
			uiOpm_i.init();
		} else {
			logger.warn(function, "uiOpm_i IS NULL");
		}
		logger.end(function);
	}
}
