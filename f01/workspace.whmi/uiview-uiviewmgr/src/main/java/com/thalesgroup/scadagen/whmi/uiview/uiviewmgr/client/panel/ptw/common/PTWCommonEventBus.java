package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.ptw.common;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.SimpleEventBus;

public class PTWCommonEventBus {
	private static SimpleEventBus instance = null;
	public static SimpleEventBus getInstance() {
//		if ( null == instance ) instance = new SimpleEventBus();
		if ( null == instance ) instance = GWT.create(SimpleEventBus.class);
		return instance;
	}
	private PTWCommonEventBus() {}
}
