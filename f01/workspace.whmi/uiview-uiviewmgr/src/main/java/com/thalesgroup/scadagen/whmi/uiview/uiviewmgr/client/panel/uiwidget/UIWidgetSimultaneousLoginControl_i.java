package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget;

public interface UIWidgetSimultaneousLoginControl_i {
	
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
		
		, RecordThreshold("RecordThreshold")
		
		, DataDelayTime("DataDelayTime")
		, WritingDelayTime("WritingDelayTime")
		, CheckingDelayTime("CheckingDelayTime")
		
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
