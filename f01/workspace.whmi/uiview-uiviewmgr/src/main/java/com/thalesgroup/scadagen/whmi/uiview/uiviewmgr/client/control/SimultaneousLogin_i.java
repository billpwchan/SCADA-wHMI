package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.control;

public interface SimultaneousLogin_i {
	
	public enum ParameterAttribute {
		  OpmApi("OpmApi")
		  
		, DbAttrResrvReserveReqID("DbAttrResrvReserveReqID")
		, DbAttrResrvUnreserveReqID("DbAttrResrvUnreserveReqID")
		, RecordThreshold("RecordThreshold")
		
		, UsrIdentityType("UsrIdentityType")
		, GwsIdentityType("GwsIdentityType")
		;
		private final String text;
		private ParameterAttribute(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
		public static String[] toStrings() {
			ParameterAttribute[] enums = values();
		    String[] strings = new String[enums.length];
		    for (int i = 0; i < enums.length; i++) {
		    	strings[i] = enums[i].name();
		    }
		    return strings;
		}
	}
	
	public enum StorageAttribute {
		
		  Area("Area")
		, ScsEnvId("ScsEnvId")
		, Alias("Alias")
		
		, ResrReservedID("ResrReservedID")
		;
		private final String text;
		private StorageAttribute(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
		public static String[] toStrings() {
			StorageAttribute[] enums = values();
		    String[] strings = new String[enums.length];
		    for (int i = 0; i < enums.length; i++) {
		    	strings[i] = enums[i].name();
		    }
		    return strings;
		}
	}
}
