package com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client;

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
	public UIEventAction(UIEventAction uiEventAction) { 
		if ( null != uiEventAction ) {
			for ( Entry<String, Object> entry : uiEventAction.hashMap.entrySet() ) {
				String key = entry.getKey();
				Object obj = entry.getValue();
				this.hashMap.put(key, obj);
			}
		}
	}
	
	public void removeParameter(String key) { this.hashMap.remove(key); }
	public void setParameter(String key, Object value) { this.hashMap.put(key, value); }
	public Object getParameter(String key) { return this.hashMap.get(key);	}
	
	public Set<Entry<String, Object>> getParameters() { return this.hashMap.entrySet();	}
	public String[] getParameterKeys() { return this.hashMap.keySet().toArray(new String[0]); }
	
	public int getParameterKeySize() { return this.hashMap.size(); }
}