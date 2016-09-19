package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.common;

import java.util.LinkedHashMap;

public class Control {
	String io;
	String scsEnvId;
	String dbaddress;
	String point;
	String label;
	String value;
	LinkedHashMap<String, String> valueTable;
	public Control () {}
	public Control ( Control control ) {
		this.io=control.io;
		this.dbaddress=control.dbaddress;
		this.point=control.point;
		this.label=control.label;
		this.value=control.value;
	}
	public Control ( String io, String scsEnvId, String dbaddress, String label ) {
		this.io=io;
		this.scsEnvId=scsEnvId;
		this.dbaddress=dbaddress;
		this.label=label;
	}
	public Control ( String io, String scsEnvId, String dbaddress, String point, String label, String value ) {
		this.io=io;
		this.scsEnvId=scsEnvId;
		this.dbaddress=dbaddress;
		this.point=point;
		this.label=label;
		this.value=value;
	}
	public Control ( String io, String scsEnvId, String dbaddress, String point, String label, LinkedHashMap<String, String> nameValues) {
		this.io=io;
		this.scsEnvId=scsEnvId;
		this.dbaddress=dbaddress;
		this.point=point;
		this.label=label;
		this.valueTable=nameValues;
	}
	
}