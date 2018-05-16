package com.thalesgroup.scadagen.wrapper.widgetcontroller.client.init.opm;

import java.util.Map;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.common.InitReady_i;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.common.Init_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.page.UIPageFactory;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.page.UIPageFactory_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.page.UIPageSCADAgen;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.page.UIPage_i;

public class InitPage implements Init_i {

	private final String className = this.getClass().getSimpleName();
	private final UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
	
	private InitPage() {}
	private static InitPage instance = null;
	public static InitPage getInstance() { 
		if ( null == instance ) instance = new InitPage();
		return instance;
	}
	
	@Override
	public void init(Map<String, Object> params, InitReady_i initReady) {
		// TODO Auto-generated method stub
		
	}
	
	public void initFactory() {
		final String function = "initPageFactory";
		logger.begin(function);
		
		UIPageFactory pageFactory = UIPageFactory.getInstance();
		pageFactory.addFactory(className, new UIPageFactory_i() {
			
			@Override
			public UIPage_i get(String key) {
				UIPage_i uiPage_i = null;
				if ( null != key ) {

					if ( UIPageSCADAgen.class.getSimpleName().equalsIgnoreCase(key) ) {
						uiPage_i = UIPageSCADAgen.getInstance();
					}
				}
				return uiPage_i;
			}
		});
		
		logger.end(function);
	}
	
	public void initPage(String key) {
		final String function = "initPage";
		logger.begin(function);
		logger.debug(function, "Try to init key[{}]", key);
		UIPage_i uiPage_i = UIPageFactory.getInstance().get(key);
		if ( null != uiPage_i ) {
			uiPage_i.init();
		} else {
			logger.warn(function, "uiPage_i IS NULL");
		}
		logger.end(function);
	}
	
}
