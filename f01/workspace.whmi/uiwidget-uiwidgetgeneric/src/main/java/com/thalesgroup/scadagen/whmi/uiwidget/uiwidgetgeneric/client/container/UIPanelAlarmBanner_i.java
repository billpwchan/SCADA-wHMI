package com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.container;

public interface UIPanelAlarmBanner_i {
	public enum WidgetArrtibute {
		  alarm("alarm")
		, event("event")
		, audio("audio")
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
