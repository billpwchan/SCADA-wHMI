package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common;

public class Database_i {
	public static final int isAlarmVectorIndex = 1;
	public static final int isNeedAckAlarmVectorIndex = 2;
	
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
		label(".label")
		, shortLabel(".shortLabel")
		, geographicalCat(".geographicalCat")
		, aalValueTable(":aal.valueTable")
		, dalValueTable(":dal.valueTable")
		, salValueTable(":sal.valueTable")
		
		, hmiOrder(".hmiOrder")
		, valueTable(".valueTable")
		
		//ExecuteStatus
		, execStatus(".execStatus")
		
		// Dynamic
		, value(".value")
		, validity(".validity") // 0=invalid, 1=valid
		, isControlable(".isControlable")
		
		, aalValueAlarmVector(":aal.valueAlarmVector") // (0,1)==0 = normal, (0,1)==1 = alarm
		, dalValueAlarmVector(":dal.valueAlarmVector") // (0,1)==0 = normal, (0,1)==1 = alarm
		, salValueAlarmVector(":sal.valueAlarmVector") // (0,1)==0 = normal, (0,1)==1 = alarm
		
		, afoForcedStatus(":afo.forcedStatus") // 2=MO, AI=8, 512=SS //dfo.forcedStatus
		, dfoForcedStatus(":dfo.forcedStatus") // 2=MO, AI=8, 512=SS //dfo.forcedStatus
		, sfoForcedStatus(":sfo.forcedStatus") // 2=MO, AI=8, 512=SS //dfo.forcedStatus
		
		// Equipment Reservation
		, reserved(".reserved")
		, resrvReservedID(".resrvReservedID")
		, resrvReserveReqID(".resrvReserveReqID")
		, resrvUnreserveReqID(".resrvUnreserveReqID")
		;
		private final String text;
		private PointName(final String text) { this.text = text; }
//		public boolean equalsName(String otherName) { return ( otherName == null ) ? false : text.equals(otherName); }
		@Override
		public String toString() { return this.text; }
	}
}