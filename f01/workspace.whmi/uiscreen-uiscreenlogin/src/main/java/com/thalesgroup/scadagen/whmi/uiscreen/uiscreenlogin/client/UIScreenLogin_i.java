package com.thalesgroup.scadagen.whmi.uiscreen.uiscreenlogin.client;

public interface UIScreenLogin_i {
	public enum Attribute {
		name("name")
		, profile("profile")
		, password("password")
		, login("login")
		, changepassword("changepassword")
		, cancel("cancel")
		;
		
		private final String text;
		private Attribute(final String text) { this.text = text; }
		public boolean equalsName(String otherName) { return ( otherName == null ) ? false : text.equals(otherName); }
		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() { return this.text; }
	}
}
