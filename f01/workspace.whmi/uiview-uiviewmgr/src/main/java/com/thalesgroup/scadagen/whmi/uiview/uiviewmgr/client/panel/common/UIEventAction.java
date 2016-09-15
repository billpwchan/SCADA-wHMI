package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

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

	private HashMap<String, Object> hashMap = new HashMap<String, Object>();
	
	public UIEventAction() {}
	public UIEventAction(String key, String value) { this.hashMap.put(key, value);}
	
	public void setParameters(String key, Object value) { this.hashMap.put(key, value); }
	public Object getParameter(String key) { return this.hashMap.get(key);	}
	
	public Set<Entry<String, Object>> getParameters() { return this.hashMap.entrySet();	}
	public String[] getParameterKeys() { return this.hashMap.keySet().toArray(new String[0]); }
	
	public int getParameterKeySize() { return this.hashMap.size(); }
}
