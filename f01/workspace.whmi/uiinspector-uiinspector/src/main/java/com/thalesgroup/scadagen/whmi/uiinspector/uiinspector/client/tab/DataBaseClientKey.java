package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.tab;

import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.tab.DataBaseClientKey_i.API;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.tab.DataBaseClientKey_i.ClientKeyIndex;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.tab.DataBaseClientKey_i.Stability;

public class DataBaseClientKey {

	private API api = null;
	private String widget = null;
	private Stability stability = null;
	private String address = null;
	
	public void setAPI(API api) { this.api = api; }
	public void setWidget(String widget) { this.widget = widget; }
	public void setStability(Stability method) { this.stability = method; }
	public void setAdress(String address) { this.address = address; }
	
	public boolean isStatic()	{ return stability.toString().equalsIgnoreCase(Stability.STATIC.toString()); }
	public boolean isDynaimc()	{ return stability.toString().equalsIgnoreCase(Stability.DYNAMIC.toString()); }
	
	public String getClientKey () { return toClientKey(api, widget, stability, address); }
	
	public DataBaseClientKey() {
	}
	
	public DataBaseClientKey(String key) {
		setClientKey(key);
	}
	
	public DataBaseClientKey(DataBaseClientKey clientKey) {
		api			= clientKey.api;
		widget		= clientKey.widget;
		stability	= clientKey.stability;
		address		= clientKey.address;
	}
	
	public DataBaseClientKey(API api, String widget, Stability method, String address) {
		this.api		= api;
		this.widget		= widget;
		this.stability	= method;
		this.address	= address;
	}
	
	public void setClientKey(String clientKey) {
		String [] tokens = clientKey.split("_");
		api			= extractApi(tokens);
		widget		= tokens[ClientKeyIndex.Widget.getValue()];
		stability	= extractStability(tokens);
		address		= tokens[ClientKeyIndex.Address.getValue()];
	}
	
	@Override
	public boolean equals(Object obj) {
		if ( null == obj ) return false;
		if ( ! ( obj instanceof DataBaseClientKey ) ) return false;
//		if ( ! ClientKey.class.isAssignableFrom(obj.getClass())) return false;
		final DataBaseClientKey other = (DataBaseClientKey) obj;
		if ( null == this.api 		? null != other.api			: this.api.equals(other.api) ) return false;
		if ( null == this.widget 	? null != other.widget 		: this.widget.equalsIgnoreCase(other.widget) ) return false;
		if ( null == this.stability ? null != other.stability 	: this.stability.equals(other.stability) ) return false;
		if ( null == this.address 	? null != other.address		: this.address.equalsIgnoreCase(other.address) ) return false;
		return true;
	}
	
	@Override
	public String toString() {
		return toClientKey();
	}
	
	public String toClientKey() {
		return toClientKey(api, widget, stability, address);
	}
	
	public String toClientKey (API api, String widget, Stability stability, String address) {
		return api.toString() + "_" + widget + "_" + stability.toString() + "_" + address;
	}
	
	public String[] extactToken(String clienKey) {
		String [] tokens = clienKey.split("_");
		return tokens;
	}
	
	public API extractApi(String [] tokens) {
		String strApi = tokens[ClientKeyIndex.API.getValue()];
		return toApi(strApi);
	}
	
	public Stability extractStability(String [] tokens) {
		String strStability = tokens[ClientKeyIndex.Stability.getValue()];
		return toStability(strStability);
	}
	
	public API getApi() {
		return api;
	}
	public API toApi(String key) {
		API api = null;
		for ( API apiValue : API.values() ) {
			String strApi = apiValue.toString();
			if ( key.equalsIgnoreCase(strApi) ) {
				api = apiValue;
				break;
			}
		}
		return api;
	}
	
	public Stability getStability() {
		return stability;
	}
	public Stability toStability(String key) {
		Stability stability = null;
		for ( Stability stabilityValue : Stability.values() ) {
			String strStability = stabilityValue.toString();
			if ( key.equalsIgnoreCase(strStability) ) {
				stability = stabilityValue;
				break;
			}
		}
		return stability;
	}
	
}
