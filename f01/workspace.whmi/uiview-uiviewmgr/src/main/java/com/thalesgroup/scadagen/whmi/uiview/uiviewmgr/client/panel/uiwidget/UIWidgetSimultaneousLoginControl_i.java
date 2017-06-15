package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget;

public interface UIWidgetSimultaneousLoginControl_i {
	
	public enum ParameterName {
		  OpmApi("OpmApi")
		, OpmIdentityType("OpmIdentityType")
		  
		, ColumnNameAlias("ColumnNameAlias")
		, ColumnNameServiceOwnerID("ColumnNameServiceOwnerID")
		
		, ColumnNameIdentity("ColumnNameIdentity")
		, ColumnNameResrReservedID("ColumnNameResrReservedID")
		
		, DbAttrNameResrvReserveReqID("DbAttrNameResrvReserveReqID")
		, DbAttrNameResrvUnreserveReqID("DbAttrNameResrvUnreserveReqID")
		
		, RecordThreshold("RecordThreshold")
		
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
	
	public enum OpmIdendifyType {
		  HostName("HostName")
		, IpAddress("IpAddress")
		, Profile("Profile")
		;
		private final String text;
		private OpmIdendifyType(final String text) { this.text = text; }
		public boolean equalsName(String otherName) { return ( otherName == null ) ? false : text.equals(otherName); }
		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() { return this.text; }
		public static String[] toStrings() {
			OpmIdendifyType[] enums = values();
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
