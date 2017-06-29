package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget;

public interface UIWidgetSimultaneousLoginControl_i {
	
	public static final String loginValidProcedure					= "login_valid_procedure";
	public static final String loginInvalidThresHoldReachProcedure	= "login_invalid_thresthold_reach_procedure";
	public static final String loginInvalidSelfIdentityProcedure	= "login_invalid_self_identity_procedure";
	
	public static final String strSimultaneousLoginRequest	= "simultaneous_login_request";
	public static final String strSimultaneousLogoutRequest	= "simultaneous_logout_request";
	
	public static final String strLoginRequest	= "loginrequest";
	public static final String strLogoutRequest	= "logoutrequest";
	public static final String strValidateLogin	= "validatelogin";
	
	public enum ParameterName {
		  OpmApi("OpmApi")
		, OpmIdentityType("OpmIdentityType")
		
		, ColumnNameArea("ColumnNameArea")  
		, ColumnNameAlias("ColumnNameAlias")
		, ColumnNameServiceOwnerID("ColumnNameServiceOwnerID")
		
		, ColumnNameGwsIdentity("ColumnNameGwsIdentity")
		, ColumnNameResrReservedID("ColumnNameResrReservedID")
		
		, DbAttrNameResrvReserveReqID("DbAttrNameResrvReserveReqID")
		, DbAttrNameResrvUnreserveReqID("DbAttrNameResrvUnreserveReqID")

		, InitDelayTime("InitDelayTime")
		, DataDelayTime("DataDelayTime")
		
		;
		private final String text;
		private ParameterName(final String text) { this.text = text; }
		public boolean equalsName(String otherName) { return ( otherName == null ) ? false : text.equals(otherName); }
		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() { return this.text; }
		public static String[] toStrings() {
			ParameterName[] enums = values();
		    String[] strings = new String[enums.length];
		    for (int i = 0; i < enums.length; i++) {
		    	strings[i] = enums[i].toString();
		    }
		    return strings;
		}
	}
	
	public enum UserIdendifyType {
		 Profile("Profile")
		, Operator("Operator")
		;
		private final String text;
		private UserIdendifyType(final String text) { this.text = text; }
		public boolean equalsName(String otherName) { return ( otherName == null ) ? false : text.equals(otherName); }
		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() { return this.text; }
		public static String[] toStrings() {
			UserIdendifyType[] enums = values();
		    String[] strings = new String[enums.length];
		    for (int i = 0; i < enums.length; i++) {
		    	strings[i] = enums[i].toString();
		    }
		    return strings;
		}
	}
	
	public enum GwsIdendifyType {
		  HostName("HostName")
		, IpAddress("IpAddress")
		;
		private final String text;
		private GwsIdendifyType(final String text) { this.text = text; }
		public boolean equalsName(String otherName) { return ( otherName == null ) ? false : text.equals(otherName); }
		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() { return this.text; }
		public static String[] toStrings() {
			GwsIdendifyType[] enums = values();
		    String[] strings = new String[enums.length];
		    for (int i = 0; i < enums.length; i++) {
		    	strings[i] = enums[i].toString();
		    }
		    return strings;
		}
	}
	
	public enum SimultaneousLoginEvent {
		  RowUpdated("RowUpdated")
		  
		, PagerValueChanged_PageStart("PagerValueChanged_PageStart")
		, PagerValueChanged_pageStart("PagerValueChanged_pageStart")
		, PagerValueChanged_EndIndex("PagerValueChanged_EndIndex")
		;
		private final String text;
		private SimultaneousLoginEvent(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}	
}
