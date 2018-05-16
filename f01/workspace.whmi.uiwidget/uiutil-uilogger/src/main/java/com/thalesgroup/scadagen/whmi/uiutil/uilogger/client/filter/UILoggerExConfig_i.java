package com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.filter;

public interface UILoggerExConfig_i {
	
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
