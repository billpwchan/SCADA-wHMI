package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.util;

public class Database_i {
	
	public static final String STR_EMPTY                 = "";
	public static final String STR_COLON                 = ":";
	public static final String STR_UNDERSCORE            = "_";
	public static final String STR_COMMA                 = ",";
	public static final String STR_ALIAS                 = "<alias>";
	public static final String STR_UNDEFINED             = "undefined";
	
	public static final int isAlarmVectorRowIndex        = 1;
	public static final int isNeedAckAlarmVectorRowIndex = 2;
	
	public enum PointType {
		unknow("POINT_TYPE_UNKNOW")
		, aci("aci")
		, dci("dci")
		, sci("sci")
		, aio("aio")
		, dio("dio")
		, sio("sio")
		;
		
		private final String text;
		private PointType(final String text) { this.text = text; }
//		public boolean equalsName(String otherName) { return ( otherName == null ) ? false : text.equals(otherName); }
		@Override
		public String toString() { return this.text; }
	}
	
	public enum PointName {
		
		// Static 
		label                 (".label")
		
		, geographicalCat     (".geographicalCat")
		, functionalCat       (".functionalCat")

		, dalValueTableLabel  (":dal.valueTable(0:$,label)")
		, dalValueTableValue  (":dal.valueTable(0:$,value)")
		
		, hmiOrder            (".hmiOrder")
		
		, valueTableDovName   (".valueTable(0:$,dovname)")
		, valueTableLabel     (".valueTable(0:$,label)")
		, valueTableValue     (".valueTable(0:$,value)")
		
		//ExecuteStatus
		, execStatus          (".execStatus")
		
		// Dynamic
		, value               (".value")
		, validity            (".validity") // 0=invalid, 1=valid
//		, isControlable       (".isControlable")
		, computedMessage     (".computedMessage")
		
		, aalValueAlarmVector (":aal.valueAlarmVector") // (0,1)==0 = normal, (0,1)==1 = alarm
		, dalValueAlarmVector (":dal.valueAlarmVector") // (0,1)==0 = normal, (0,1)==1 = alarm
		, salValueAlarmVector (":sal.valueAlarmVector") // (0,1)==0 = normal, (0,1)==1 = alarm
		
		, afoForcedStatus     (":afo.forcedStatus") // 2=MO, AI=8, 512=SS //dfo.forcedStatus
		, dfoForcedStatus     (":dfo.forcedStatus") // 2=MO, AI=8, 512=SS //dfo.forcedStatus
		, sfoForcedStatus     (":sfo.forcedStatus") // 2=MO, AI=8, 512=SS //dfo.forcedStatus
		
		, initCondGL          (".initCondGL")
		
		// Equipment Reservation
//		, reserved(".reserved")
//		, resrvReservedID(".resrvReservedID")
//		, resrvReserveReqID(".resrvReserveReqID")
//		, resrvUnreserveReqID(".resrvUnreserveReqID")
		;
		private final String text;
		private PointName(final String text) { this.text = text; }
//		public boolean equalsName(String otherName) { return ( otherName == null ) ? false : text.equals(otherName); }
		@Override
		public String toString() { return this.text; }
	}
}
