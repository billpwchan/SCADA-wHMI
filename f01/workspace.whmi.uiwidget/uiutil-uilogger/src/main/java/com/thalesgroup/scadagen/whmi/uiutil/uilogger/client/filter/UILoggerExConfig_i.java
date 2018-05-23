package com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.filter;

/**
 * @author t0096643
 *
 */
public interface UILoggerExConfig_i {
	
	public enum AttributeMsg {
		PREFIX("PREFIX")
		, MSG("MSG")
		, OCB("OCB")
		, BEGIN("BEGIN")
		, END("END")
		, BEGINEND("BEGINEND")
		, NULL("NULL")
		;
		
		private final String text;
		private AttributeMsg(final String text) { this.text = text; }
		public boolean equalsName(String otherName) { return ( otherName == null ) ? false : text.equals(otherName); }
		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() { return this.text; }
	}
	
	public enum AttributeLevel {
		TRACE("TRACE")
		, DEBUG("DEBUG")
		, INFO("INFO")
		, WARN("WARN")
		, ERROR("ERROR")
		, FATAL("FATAL")
		, OFF("OFF")
		;
		
		private final String text;
		private AttributeLevel(final String text) { this.text = text; }
		public boolean equalsName(String otherName) { return ( otherName == null ) ? false : text.equals(otherName); }
		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() { return this.text; }
	}
}
