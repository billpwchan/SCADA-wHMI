package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.control;

public interface SimultaneousLogin_i {

	public static final int Bit_Pos_SelfGwsIdentity_IsInvalid	= 1 << 0;
	public static final int Bit_Pos_SelfGwsArea_IsInvalid		= 1 << 1;
	public static final int Bit_Pos_SelfUsrIdentity_IsInvalid	= 1 << 2;
	public static final int Bit_Pos_Storage_IsEmpty				= 1 << 3;
	public static final int Bit_Pos_ReservedInOtherArea			= 1 << 4;
	public static final int Bit_Pos_IsByPassUsrIdentity			= 1 << 5;
	public static final int Bit_Pos_ReservedSelfGws				= 1 << 6;
	public static final int Bit_Pos_ReservedByOther				= 1 << 7;
	
	public enum BitPosDescription {
		  SelfGwsIdentity_IsInvalid("SelfGwsIdentity_IsInvalid")
		, SelfGwsArea_IsInvalid("SelfGwsArea_IsInvalid")
		, SelfUsrIdentity_IsInvalid("SelfUsrIdentity_IsInvalid")
		, Storage_IsEmpty("Storage_IsEmpty")
		, ReservedInOtherArea("ReservedInOtherArea")
		, IsByPassUsrIdentity("IsByPassUsrIdentity")
		, ReservedSelfGws("ReservedSelfGws")
		, ReservedByOther("ReservedByOther")
		;
		private final String text;
		private BitPosDescription(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}
	
	public enum ParameterAttribute {
		  OpmApi("OpmApi")
		  
		, DbAttrResrvReserveReqID("DbAttrResrvReserveReqID")
		, DbAttrResrvUnreserveReqID("DbAttrResrvUnreserveReqID")
		
		, UsrIdentityType("UsrIdentityType")
		, GwsIdentityType("GwsIdentityType")
		
		, RecordThreshold("RecordThreshold")
		
		, ByPassUsrIdentity("ByPassUsrIdentity")
		, ByPassUsrIdentityIgnoreCase("ByPassUsrIdentityIgnoreCase")
		
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
