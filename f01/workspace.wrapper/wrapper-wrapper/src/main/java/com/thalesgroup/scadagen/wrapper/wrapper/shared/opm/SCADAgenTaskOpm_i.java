package com.thalesgroup.scadagen.wrapper.wrapper.shared.opm;

public interface SCADAgenTaskOpm_i {
	
	public final static String setSpliter = "\\|";
	public final static String valSpliter = ":";
	
	public enum AttributeValue {
		  OR("OR")
		, AND("AND")
		;
		private final String text;
		private AttributeValue(final String text) { this.text = text; }
		public boolean equalsName(String otherName) { return ( otherName == null ) ? false : text.equals(otherName); }
		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() { return this.text; }

	}
}
