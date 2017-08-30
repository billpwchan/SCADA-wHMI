package com.thalesgroup.scadagen.wrapper.wrapper.client.opm;

public interface UIOpmSCADAgen_i {
	
	public final static String dictionariesCacheName = "UIJson";
	public final static String fileName = "hom.json";
	public final static String fileNameLevel = "homlevel.json";
	
	public final static String byPassValueskey = "ByPassValues";
	public final static String dbAttributekey = "DBAttribute";
	
	public final static String homActionsArrayKey = "HOMActions";
	
	public final static String homLevelDefaultValueKey = "HOMLevelDefaultValue";
	public final static String homLevelsArrayKey = "HOMLevels";
	
	public final static String homLevelsArrayKeyKeyName = "Key";
	public final static String homLevelsArrayKeyValueName = "Value";
	
	public final static String homIdentityTypeKeyName = "HOMIdentityType";
	public final static String homIdentityTypeDefaultValue = "Profile";
	
	public enum HOMIdentityType {
		  Profile("Profile")
		, Operator("Operator")
		, IpAddress("IpAddress")
		, HostName("HostName")
		;
		private String value;
		private HOMIdentityType(String value) {
			this.value = value;
		}
		public String getValue() { return this.value; }
	}
}
