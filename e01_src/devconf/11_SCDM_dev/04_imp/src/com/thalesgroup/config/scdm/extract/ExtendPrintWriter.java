package com.thalesgroup.config.scdm.extract;

import java.io.PrintWriter;
import java.io.Writer;

/**
 * 
 *
 */
public class ExtendPrintWriter extends PrintWriter {

	/**
	 * table space
	 */
	final public String TS = "\t";
	
	public ExtendPrintWriter(Writer out) {
		super(out);
	}
	
	public void printts(String val, int nbTS) {
		print(appendTsBefore(val, nbTS));
	}
	
	public void printtsln(String val, int nbTS) {
		println(appendTsBefore(val, nbTS));
	}

	public void printStringValue(String val) {
		print(getStringValue(val));
	}
	
	public void printlnStringValue(String val) {
		println(getStringValue(val));
	}
	
	public void printtsStringValue(String val, int nbTS) {
		print(appendTsBefore(getStringValue(val),nbTS));
	}
	
	public void printtslnStringValue(String val, int nbTS) {
		println(appendTsBefore(getStringValue(val),nbTS));
	}
	
	private String getStringValue(String val) {
		return "\"" + val + "\"";
	}
	
	private String appendTsBefore(String val, int nbTS) {
		String ts = "";
		for(int i=0; i<nbTS; i++) {
			ts += TS;
		}
		return ts + val;
	}
}
