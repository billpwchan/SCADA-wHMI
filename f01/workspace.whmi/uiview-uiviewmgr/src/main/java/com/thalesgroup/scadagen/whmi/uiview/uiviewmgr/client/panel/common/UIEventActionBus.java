package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common;

import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.SimpleEventBus;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;

public class UIEventActionBus {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIEventActionBus.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private static UIEventActionBus instance = null;
	public static UIEventActionBus getInstance() {
		if ( null == instance ) instance = new UIEventActionBus();
		return instance;
	}
	private UIEventActionBus() {}
	
	private HashMap<String, SimpleEventBus> buses = new HashMap<String, SimpleEventBus>();
	public SimpleEventBus getEventBus(String key) {
		final String function = "getEventBus";
		logger.begin(className, function);
		logger.debug(className, function, "key[{}]", key);
		if ( ! buses.containsKey(key) ) buses.put(key, (SimpleEventBus) GWT.create(SimpleEventBus.class));
		logger.end(className, function);
		return buses.get(key);
	}
}
