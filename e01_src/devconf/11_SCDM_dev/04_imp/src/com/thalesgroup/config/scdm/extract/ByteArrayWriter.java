package com.thalesgroup.config.scdm.extract;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.thalesis.config.utils.Logger;

public class ByteArrayWriter extends ByteArrayOutputStream {

	/**
	 * table space
	 */
	final public String TS = "\t";
	
	static String lineSeparator = System.getProperty ("line.separator" );
	
	public ByteArrayWriter() {
		super();
	}
	
	public void print(String val) {
		try {
			write(val.getBytes());
		} catch (IOException e) {
			Logger.USER.error(
					"ByteArrayWriter : " + Logger.exStackTraceToString(e) );
		}
	}
	
	public void println(String val) {
	   print(val) ;
	   println() ;
	}
	
	public void println() {
		print(lineSeparator) ;
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
