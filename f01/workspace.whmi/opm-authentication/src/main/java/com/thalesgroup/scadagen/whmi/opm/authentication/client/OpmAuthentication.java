package com.thalesgroup.scadagen.whmi.opm.authentication.client;

import java.util.HashMap;
import java.util.Set;

public class OpmAuthentication {
	private static OpmAuthentication instance = null;
	private OpmAuthentication () {
		init();
	}
	public static OpmAuthentication getInstance() {
		if ( null == instance ) instance = new OpmAuthentication();
		return instance;
	}
	
	private String operator = "";
	private String profile = "";
	public void setCurrentOperator(String operator) { this.operator = operator; }
	public String getCurrentOperator() { return this.operator; }
	public void setCurrentProfile(String profile) { this.profile = profile; }
	public String getCurrentProfile() { return this.profile; }
	public String[] getOperators() {
		Set<String> keySet = this.hashMapProfile.keySet();
		return keySet.toArray(new String[keySet.size()]);
	}
	public void setPassword(String operator, String password) { 
		hashMapPassword.put(operator, password);
	}
	
	public String getDefaultOperator() { return CORRECT_USER_OPERATOR; }
	public String getDefaultPassword() { return CORRECT_PASSWORD_OPERATOR; }
	
	public String getProfile(String operator) {
		String profile = null;
		if ( null != operator ) {
			profile = hashMapProfile.get(operator);
		}
		return profile;
	}
	public boolean isValidPassword(String operator, String password) {
		boolean result = false;
		if ( null != operator && null != password ) {
			String pw = this.hashMapPassword.get(operator);
			if ( null != pw ) {
				if ( 0 == pw.compareTo(password) ) {
					result = true;
				}
			}
		}
		return result;
	}
	public boolean hasRight(String action, String right, String profile) {
		boolean result = false;
		if ( null != action && null != right && null != profile ) {
			if ( 0 == action.compareTo("M") && 0 == right.compareTo("PASSWORD") ) {
				if ( 0 == profile.compareTo(ADMINISTRATOR)) {
					result = true;
				}
			}
		}
		return result;
	}
	
	
	// Temp
	private String CORRECT_USER_OPERATOR			= "OPERATOR";
	private String CORRECT_PASSWORD_OPERATOR		= "PASSWORD";
	
	private String CORRECT_USER_ADMIN				= "ADMIN";
	private String CORRECT_PASSWORD_ADMIN			= "PASSWORD";
	
//	private String EMPTY							= "";
	private String OPERATOR							= "OPERATOR";
	private String ADMINISTRATOR					= "ADMINISTRATOR";
		
	private HashMap<String, String> hashMapProfile;
	private HashMap<String, String> hashMapPassword;
	
//	class Operator {
//		public Operator (String operator, String password, String profile) {
//			setOperator(operator);
//			setPassword(password);
//			setProfile(profile);
//		}
//		private String operator;
//		private String password;
//		private String profile;
//		public String getOperator() { return operator; }
//		public void setOperator(String operator) { this.operator = operator; }
//		public String getPassword() { return password; }
//		public void setPassword(String password) { this.password = password; }
//		public String getProfile() { return profile; }
//		public void setProfile(String profile) { this.profile = profile; }
//	}
	
	private void init () {
		hashMapProfile = new HashMap<String, String>();
		hashMapPassword = new HashMap<String, String>();
		
	    hashMapProfile.put(CORRECT_USER_OPERATOR, OPERATOR);
	    hashMapProfile.put(CORRECT_USER_ADMIN, ADMINISTRATOR);
	    
	    hashMapPassword.put(CORRECT_USER_OPERATOR, CORRECT_PASSWORD_OPERATOR);
	    hashMapPassword.put(CORRECT_USER_ADMIN, CORRECT_PASSWORD_ADMIN);
	}
}
