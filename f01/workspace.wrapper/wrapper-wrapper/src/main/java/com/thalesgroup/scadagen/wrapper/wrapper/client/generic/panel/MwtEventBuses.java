package com.thalesgroup.scadagen.wrapper.wrapper.client.generic.panel;

import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.event.MwtEventBus;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.AppUtils;

public class MwtEventBuses {
	
	public static String AppUtils_EVENT_BUS = "AppUtils.EVENT_BUS";
	
	private static MwtEventBuses instance = null;
	public static MwtEventBuses getInstance() { 
		if ( null == instance ) instance = new MwtEventBuses();
		return instance;
	}
	
	private MwtEventBuses () {
		hashMap.put(AppUtils_EVENT_BUS, AppUtils.EVENT_BUS);
	}
	
	private HashMap<String, EventBus> hashMap = new HashMap<String, EventBus>();
	public EventBus getEventBus(String key) {
		if ( ! hashMap.containsKey(key) ) hashMap.put(key, (EventBus) GWT.create(MwtEventBus.class));
		return hashMap.get(key);
	}

}
