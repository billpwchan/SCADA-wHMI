package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common;

import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.SimpleEventBus;

public class UIEventActionBus {
	private static UIEventActionBus instance = null;
	public static UIEventActionBus getInstance() {
		if ( null == instance ) instance = new UIEventActionBus();
		return instance;
	}
	private UIEventActionBus() {}
	
	private HashMap<String, SimpleEventBus> buses = new HashMap<String, SimpleEventBus>();
	public SimpleEventBus getEventBus(String key) {
		if ( ! buses.containsKey(key) ) buses.put(key, (SimpleEventBus) GWT.create(SimpleEventBus.class));
		return buses.get(key);
	}
}
