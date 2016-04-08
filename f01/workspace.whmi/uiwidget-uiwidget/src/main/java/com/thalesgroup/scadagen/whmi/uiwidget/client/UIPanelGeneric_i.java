package com.thalesgroup.scadagen.whmi.uiwidget.client;

public interface UIPanelGeneric_i {
	
	public enum UIScreenGenericElement {
		  header("header")
		, option("option")
		;
		private final String text;
		private UIScreenGenericElement(final String text) { this.text = text; }
		public boolean equalsName(String otherName) { return ( otherName == null ) ? false : text.equals(otherName); }
		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() { return this.text; }
	}
	
	public enum UIScreenGenericRootAttribute {
		  rows("rows")
		, cols("cols")
		, outerPanel("outerpanel")
		, innerPanel("innerpanel")
		, outerCSS("outercss")
		, innerCSS("innercss")
		, outerhorizontalalignment("outerhorizontalalignment")
		, outerverticalalignment("outerverticalalignment")
		, innerhorizontalalignment("innerhorizontalalignment")
		, innerverticalalignment("innerverticalalignment")
		;
		private final String text;
		private UIScreenGenericRootAttribute(final String text) { this.text = text; }
		public boolean equalsName(String otherName) { return ( otherName == null ) ? false : text.equals(otherName); }
		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() { return this.text; }
	}
	
	public enum UIScreenGenericPanelAttribute {
		  VerticalPanel("VerticalPanel")
		, HorizontalPanel("HorizontalPanel")
		, DockLayoutPanel("DockLayoutPanel")
		, AbsolutePanel("AbsolutePanel")
		;
		private final String text;
		private UIScreenGenericPanelAttribute(final String text) { this.text = text; }
		public boolean equalsName(String otherName) { return ( otherName == null ) ? false : text.equals(otherName); }
		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() { return this.text; }
	}
	
	public enum UIScreenGenericHorizontalAlignmentAttribute {
		ALIGN_LEFT("ALIGN_LEFT")
		, ALIGN_CENTER("ALIGN_CENTER")
		, ALIGN_RIGHT("ALIGN_RIGHT")
		;
		private final String text;
		private UIScreenGenericHorizontalAlignmentAttribute(final String text) { this.text = text; }
		public boolean equalsName(String otherName) { return ( otherName == null ) ? false : text.equals(otherName); }
		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() { return this.text; }
	}
	
	public enum UIScreenGenericVerticalAlignmentAttribute {
		ALIGN_TOP("ALIGN_TOP")
		, ALIGN_MIDDLE("ALIGN_MIDDLE")
		, ALIGN_BOTTOM("ALIGN_BOTTOM")
		;
		private final String text;
		private UIScreenGenericVerticalAlignmentAttribute(final String text) { this.text = text; }
		public boolean equalsName(String otherName) { return ( otherName == null ) ? false : text.equals(otherName); }
		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() { return this.text; }
	}
	
	public enum UIScreenGenericWidgetAttribute {
		  type("type")
		, widget("widget")
		, horizontalalignment("horizontalalignment")
		, verticalalignment("verticalalignment")
		, direction("direction")
		, width("width")
		, cellwidth("cellwidth")
		, cellheight("cellheight")
		, left("left")
		, top("top")
		;
		private final String text;
		private UIScreenGenericWidgetAttribute(final String text) { this.text = text; }
		public boolean equalsName(String otherName) { return ( otherName == null ) ? false : text.equals(otherName); }
		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() { return this.text; }
	}
	
	public enum UIScreenGenericTypeAttribute {
		  predefine("predefine")
		, configuration("configuration")
		;
		private final String text;
		private UIScreenGenericTypeAttribute(final String text) { this.text = text; }
		public boolean equalsName(String otherName) { return ( otherName == null ) ? false : text.equals(otherName); }
		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() { return this.text; }
	}
	
	public enum UIScreenGenericDirectionAttribute {
		  North("North")
		, East("East")
		, South("Sourth")
		, West("West")
		, Center("Center")
		;
		private final String text;
		private UIScreenGenericDirectionAttribute(final String text) { this.text = text; }
		public boolean equalsName(String otherName) { return ( otherName == null ) ? false : text.equals(otherName); }
		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() { return this.text; }
	}
}
