package com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client;

public interface UIPanelGeneric_i {
	
	public enum UIPanelGenericElement {
		  header("header")
		, option("option")
		;
		private final String text;
		private UIPanelGenericElement(final String text) { this.text = text; }
		public boolean equalsName(String otherName) { return ( otherName == null ) ? false : text.equals(otherName); }
		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() { return this.text; }
	}
	
	public enum RootAttribute {
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
		private RootAttribute(final String text) { this.text = text; }
		public boolean equalsName(String otherName) { return ( otherName == null ) ? false : text.equals(otherName); }
		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() { return this.text; }
	}
	
	public enum PanelAttribute {
		  VerticalPanel("VerticalPanel")
		, HorizontalPanel("HorizontalPanel")
		, DockLayoutPanel("DockLayoutPanel")
		, AbsolutePanel("AbsolutePanel")
		;
		private final String text;
		private PanelAttribute(final String text) { this.text = text; }
		public boolean equalsName(String otherName) { return ( otherName == null ) ? false : text.equals(otherName); }
		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() { return this.text; }
	}
	
	public enum HorizontalAlignmentAttribute {
		ALIGN_LEFT("ALIGN_LEFT")
		, ALIGN_CENTER("ALIGN_CENTER")
		, ALIGN_RIGHT("ALIGN_RIGHT")
		;
		private final String text;
		private HorizontalAlignmentAttribute(final String text) { this.text = text; }
		public boolean equalsName(String otherName) { return ( otherName == null ) ? false : text.equals(otherName); }
		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() { return this.text; }
	}
	
	public enum VerticalAlignmentAttribute {
		ALIGN_TOP("ALIGN_TOP")
		, ALIGN_MIDDLE("ALIGN_MIDDLE")
		, ALIGN_BOTTOM("ALIGN_BOTTOM")
		;
		private final String text;
		private VerticalAlignmentAttribute(final String text) { this.text = text; }
		public boolean equalsName(String otherName) { return ( otherName == null ) ? false : text.equals(otherName); }
		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() { return this.text; }
	}
	
	public enum WidgetAttribute {
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
		private WidgetAttribute(final String text) { this.text = text; }
		public boolean equalsName(String otherName) { return ( otherName == null ) ? false : text.equals(otherName); }
		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() { return this.text; }
	}
	
	public enum TypeAttribute {
		  predefine("predefine")
		, configuration("configuration")
		, layoutconfiguration("layoutconfiguration")
		;
		private final String text;
		private TypeAttribute(final String text) { this.text = text; }
		public boolean equalsName(String otherName) { return ( otherName == null ) ? false : text.equals(otherName); }
		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() { return this.text; }
	}
	
	public enum DirectionAttribute {
		  North("North")
		, East("East")
		, South("South")
		, West("West")
		, Center("Center")
		;
		private final String text;
		private DirectionAttribute(final String text) { this.text = text; }
		public boolean equalsName(String otherName) { return ( otherName == null ) ? false : text.equals(otherName); }
		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() { return this.text; }
	}
}
