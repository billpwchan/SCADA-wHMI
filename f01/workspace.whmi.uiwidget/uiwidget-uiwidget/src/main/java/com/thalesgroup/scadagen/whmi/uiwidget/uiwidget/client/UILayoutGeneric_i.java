package com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client;

public interface UILayoutGeneric_i {
	
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
		, rootPanel("rootPanel")
		, rootCSS("rootCSS")
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

	public enum WidgetAttribute {
		  type("type")
		, widget("widget")
		, direction("direction")
		, width("width")
		, cellwidth("cellwidth")
		, cellheight("cellheight")
		, left("left")
		, top("top")
		, csscontainer("csscontainer")
		, uiView("uiView")
		, eventbusname("eventbusname")
		, eventbusscope("eventbusscope")
		, element("element")
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
	
	public enum OptionAttribute {
		option1("option1")
		, option2("option2")
		, option3("option3")
		, option4("option4")
		, option5("option5")
		, option6("option6")
		, option7("option7")
		, option8("option8")
		, option9("option9")
		, option10("option10")
		, option11("option11")
		, option12("option12")
		;
		private final String text;
		private OptionAttribute(final String text) { this.text = text; }
		public boolean equalsName(String otherName) { return ( otherName == null ) ? false : text.equals(otherName); }
		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() { return this.text; }
	}
	
	public enum ActionAttribute {
		initAction1("initAction1")
		, initAction2("initAction2")
		, initAction3("initAction3")
		, initAction4("initAction4")
		, initAction5("initAction5")
		, initAction6("initAction6")
		;
		private final String text;
		private ActionAttribute(final String text) { this.text = text; }
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
