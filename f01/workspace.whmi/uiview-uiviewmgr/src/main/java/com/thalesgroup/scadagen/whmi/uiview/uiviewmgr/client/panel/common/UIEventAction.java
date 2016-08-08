package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common;

import java.util.HashMap;

import com.google.gwt.event.shared.GwtEvent;

public class UIEventAction extends GwtEvent<UIEventActionHandler> {
	
	public static Type<UIEventActionHandler> TYPE = new Type<UIEventActionHandler>();

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<UIEventActionHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(UIEventActionHandler handler) {
		handler.onAction(this);
	}

	private HashMap<String, String> hashMap = new HashMap<String, String>();
	public UIEventAction() {}
	public UIEventAction(String key, String value) { this.hashMap.put(key, value);}
	public void setAction(String key, String value) { this.hashMap.put(key, value); }
	public String getAction(String key) { return this.hashMap.get(key);	}
	public String[] getActionKeys(String key) { return this.hashMap.keySet().toArray(new String[0]);	}
}
