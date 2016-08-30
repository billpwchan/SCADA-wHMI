package com.thalesgroup.scadasoft.gwebhmi.security.client;

public interface AppEntryPoint_WHMI_i {
	public enum ProjectName {
		COCC("COCC")
		, C1166B("C1166B")
		, SCSTraining("SCSTraining")
		;
		private final String text;
		private ProjectName(final String text) { this.text = text; }
		public boolean equalsName(String otherName) { return ( otherName == null ) ? false : text.equals(otherName); }
		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() { return this.text; }
	}
}
