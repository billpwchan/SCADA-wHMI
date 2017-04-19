package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.panel.reserve;

public interface EquipmentReserve_i {
	public enum AttributeName {
		  ResrvReserveReqIDAttribute("resrvReserveReqIDAttribute")
		, ResrvUnreserveReqIDAttribute("resrvUnreserveReqIDAttribute")
//		, ResrvReservedIDAttribute("resrvReservedIDAttribute")
		;
		private final String text;
		private AttributeName(final String text) { this.text = text; }
//		public boolean equalsName(String otherName) { return ( otherName == null ) ? false : text.equals(otherName); }
		@Override
		public String toString() { return this.text; }
	}
}
