package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common;

import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.event.MwtEventBus;

public class UIEventMWT {
	private static UIEventMWT instance = null;
	public static UIEventMWT getInstance() {
		if ( null == instance ) instance = new UIEventMWT();
		return instance;
	}
	private UIEventMWT() {}
	
	private HashMap<String, MwtEventBus> buses = new HashMap<String, MwtEventBus>();
	public MwtEventBus getEventBus(String key) {
		if ( ! buses.containsKey(key) ) buses.put(key, (MwtEventBus) GWT.create(MwtEventBus.class));
		return buses.get(key);
	}
}
