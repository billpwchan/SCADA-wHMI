package com.thalesgroup.scadagen.wrapper.wrapper.client.db.util;

import java.util.Objects;

import com.thalesgroup.scadagen.wrapper.wrapper.client.db.util.DataBaseClientKey_i.API;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.util.DataBaseClientKey_i.ClientKeyIndex;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.util.DataBaseClientKey_i.Stability;

public class DataBaseClientKey {
	
	private String sperator = "_";

	private API api = null;
	private String widget = null;
	private Stability stability = null;
	private int screen = 0;
	private String env = null;
	private String address = null;
	
	public void setAPI(API api) { this.api = api; }
	public void setWidget(String widget) { this.widget = widget; }
	public void setStability(Stability method) { this.stability = method; }
	public void setScreen(int screen) { this.screen = screen; }
	public void setEnv(String env) { this.env = env; }
	public void setAdress(String address) { this.address = address; }
	
	public boolean isStatic()	{ return stability.toString().equalsIgnoreCase(Stability.STATIC.toString()); }
	public boolean isDynaimc()	{ return stability.toString().equalsIgnoreCase(Stability.DYNAMIC.toString()); }
	
	public String getClientKey () { return toClientKey(api, widget, stability, screen, env, address); }
	
	public DataBaseClientKey() {
	}
	
	public DataBaseClientKey(String spliter) {
		setSpliter(spliter);
	}
	
	public DataBaseClientKey(String spliter, String key) {
		setSpliter(spliter);
		setClientKey(key);
	}
	
	public DataBaseClientKey(DataBaseClientKey clientKey) {
		api			= clientKey.api;
		widget		= clientKey.widget;
		stability	= clientKey.stability;
		screen		= clientKey.screen;
		env			= clientKey.env;
		address		= clientKey.address;
	}

	public DataBaseClientKey(API api, String widget, Stability method, int screen, String env, String address) {
		this.api		= api;
		this.widget		= widget;
		this.stability	= method;
		this.screen		= screen;
		this.env		= env;
		this.address	= address;
	}
	
	public void setSpliter(String sperator) {
		this.sperator = sperator;
	}
	
	public void setClientKey(String clientKey) {
		String [] tokens = clientKey.split(sperator);
		api			= extractApi(tokens);
		widget		= extractWidget(tokens);
		stability	= extractStability(tokens);
		screen		= extractScreen(tokens);
		env			= extractEnv(tokens);
		address		= tokens[ClientKeyIndex.Address.getValue()];
	}
	
	@Override
	public boolean equals(Object obj) {
		if ( null == obj ) return false;
		if ( ! ( obj instanceof DataBaseClientKey ) ) return false;
//		if ( ! ClientKey.class.isAssignableFrom(obj.getClass())) return false;
		final DataBaseClientKey other = (DataBaseClientKey) obj;
		if ( null == this.api 		? null != other.api			: ! this.api.equals(other.api) ) return false;
		if ( null == this.widget 	? null != other.widget 		: ! this.widget.equalsIgnoreCase(other.widget) ) return false;
		if ( null == this.stability ? null != other.stability 	: ! this.stability.equals(other.stability) ) return false;
		if ( this.screen != other.screen ) return false;
		if ( null == this.env 		? null != other.env		: ! this.env.equalsIgnoreCase(other.env) ) return false;
		if ( null == this.address 	? null != other.address		: ! this.address.equalsIgnoreCase(other.address) ) return false;
		return true;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.api, this.widget, this.stability, this.screen, this.env , this.address);
	}
	
	@Override
	public String toString() {
		return toClientKey();
	}
	
	public String toClientKey() {
		return toClientKey(api, widget, stability, screen, env, address);
	}
	
	public String toClientKey (API api, String widget, Stability stability, int screen, String env, String address) {
		return api.toString() + sperator + widget + sperator + stability.toString() + sperator + screen + sperator + env + sperator + address;
	}
	
	public String[] extactToken(String clienKey) {
		String [] tokens = clienKey.split(sperator);
		return tokens;
	}
	
	public API extractApi(String [] tokens) {
		String strApi = tokens[ClientKeyIndex.API.getValue()];
		return toApi(strApi);
	}
	
	public String extractWidget(String [] tokens) {
		String strWidget = tokens[ClientKeyIndex.Widget.getValue()];
		return strWidget;
	}
	
	public Stability extractStability(String [] tokens) {
		String strStability = tokens[ClientKeyIndex.Stability.getValue()];
		return toStability(strStability);
	}
	
	public int extractScreen(String [] tokens) {
		String strScreen = tokens[ClientKeyIndex.Screen.getValue()];
		return Integer.parseInt(strScreen);
	}
	
	public String extractEnv(String [] tokens) {
		String strEnv = tokens[ClientKeyIndex.Env.getValue()];
		return strEnv;
	}
	
	public String extractAddress(String [] tokens) {
		int indexAddress = ClientKeyIndex.Address.getValue();
		int addressTokenLength = tokens.length - indexAddress;
		String [] addressToken = new String[addressTokenLength];
		for ( int i = 0, x = indexAddress ; x < tokens.length ; ++i ) 
			addressToken[i] = tokens[x];
		return stringJoin(addressToken, sperator);
	}
	
	public String stringJoin(String[] strings, String sperator ) {
		if ( null == strings || 0 == strings.length ) return null;
		StringBuilder sb = new StringBuilder(256);
		sb.append(strings[0]);
		for ( int i = 1 ; i < strings.length ; ++i ) sb.append(strings[i]);
		return sb.toString();
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
	
	public int getScreen() { 
		return this.screen; 
	}
	public void getScreen(int screen) { 
		this.screen = screen; 
	}
	
}
