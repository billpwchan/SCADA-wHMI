package com.thalesgroup.scadagen.wrapper.wrapper.server;

public interface UIOpm_i {
	
	public static final String MODE = "mode";
	public static final String ACTION = "action";
	public static final String FUNCTION = "function";
	public static final String LOCATION = "location";
	
	public static final String M = "M";
	public static final String D = "D";
	public static final String A = "A";
	public static final String C = "C";
	
	boolean checkAccess(String function, String location, String action, String mode);
	
}
