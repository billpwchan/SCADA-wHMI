package com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetcontainer.client.container;

public interface UIPanelAccessBar_i {
	public enum WidgetArrtibute {
		  splith("splith")
		, splitv("splitv")
		, previous("previous")
		, next("next")
		, stationoperation("stationoperation")
		, dss("dss")
		, print("print")
		, help("help")
		, logout("logout")
		;
		private final String text;
		private WidgetArrtibute(final String text) { this.text = text; }
		public boolean equalsName(String otherName) { return ( otherName == null ) ? false : text.equals(otherName); }
		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() { return this.text; }
	}
	
}
