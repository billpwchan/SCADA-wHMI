package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.tab;

public interface UIInspectorHeader_i {
	
	public enum AttributeName {
		
		  ShortLabelAttribute("ShortLabelAttribute")
		, LocationAttribute("LocationAttribute")
		
		, IsControlableAttribute("IsControlableAttribute")
		
		, ResrvReservedIDAttribute("ResrvReservedIDAttribute")
		, ResrvReserveReqIDAttribute("ResrvReserveReqIDAttribute")
		, ResrvUnreserveReqIDAttribute("ResrvUnreserveReqIDAttribute")
		
		, HdvFlagAttribute("HdvFlagAttribute")
		;
		private final String text;
		private AttributeName(final String text) { this.text = text; }
//		public boolean equalsName(String otherName) { return ( otherName == null ) ? false : text.equals(otherName); }
		@Override
		public String toString() { return this.text; }
	}
}
