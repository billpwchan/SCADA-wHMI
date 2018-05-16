package com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client;

import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.SimpleEventBus;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;

public class UIEventActionBus {

	private final UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
	
	private static UIEventActionBus instance = null;
	public static UIEventActionBus getInstance() {
		if ( null == instance ) instance = new UIEventActionBus();
		return instance;
	}
	private UIEventActionBus() {}
	
	private HashMap<String, SimpleEventBus> buses = new HashMap<String, SimpleEventBus>();
	public SimpleEventBus getEventBus(String key) {
		final String function = "getEventBus";
		logger.begin(function);
		logger.debug(function, "key[{}]", key);
		if ( ! buses.containsKey(key) ) buses.put(key, (SimpleEventBus) GWT.create(SimpleEventBus.class));
		logger.end(function);
		return buses.get(key);
	}
	public void resetEventBus(String key) {
		final String function = "resetEventBus";
		logger.begin(function);
		logger.debug(function, "key[{}]", key);
		if ( buses.containsKey(key) ) {
			buses.remove(key);
		}
		logger.end(function);
	}
	public void cleanEventBuses() {
		final String function = "cleanEventBuses";
		logger.begin(function);
		buses.clear();
		logger.end(function);
	}
}
