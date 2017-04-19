package com.thalesgroup.scadagen.wrapper.wrapper.server.opm;

public interface OpmMgr_i {
//	public enum MODE {
//		  M("M")
//		, C("C")
//		, D("D")
//		, A("A")
//		;
//		
//		private final String text;
//		private MODE(final String text) { this.text = text; }
//		public boolean equalsName(String otherName) { return ( otherName == null ) ? false : text.equals(otherName); }
//		/* (non-Javadoc)
//		 * @see java.lang.Enum#toString()
//		 */
//		@Override
//		public String toString() { return this.text; }
//	}
	public enum ACTION {
		  M("M")
		, C("C")
		, D("D")
		, A("A")
		;
		
		private final String text;
		private ACTION(final String text) { this.text = text; }
		public boolean equalsName(String otherName) { return ( otherName == null ) ? false : text.equals(otherName); }
		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() { return this.text; }
	}
}
