package com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client;

public interface UIWidgetGeneric_i {
	
	public enum RootAttribute {
		  rows("rows")
		, cols("cols")
		, rootPanel("rootPanel")
		, rootCss("rootCss")
		, rootContainerCss("rootContainerCss")
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
	
	public enum RootWidgetType {
		  HorizontalPanel("HorizontalPanel")
		, VerticalPanel("VerticalPanel")
		, FlexTable("FlexTable")
		, AbsolutePanel("AbsolutePanel")
		;
		
		private final String text;
		private RootWidgetType(final String text) { this.text = text; }
		public boolean equalsName(String otherName) { return ( otherName == null ) ? false : text.equals(otherName); }
		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() { return this.text; }
	}
	
	public enum WidgetAttribute {
		  widget("widget")
		, label("label")
		, icon("icon")
		, tooltip("tooltip")
		, css("css")
		, element("element")
		, format("format")
		, media("media")
		, readonly("readonly")
		, maxlength("maxlength")
		, visibleitemcount("visibleitemcount")
		, enable("enable")
		
		, labelDown("labelDown")
		, labelDisable("labelDisable")
		
		, tooltipDown("tooltipDown")
		, tooltipDisable("strTooltipDisable")
		
		, iconDown("iconDown")
		, iconDisable("iconDisable")
		
		, iconDivWidth("iconDivWidth")
		, iconDivHeight("iconDivHeight")
		, iconImgWidth("iconImgWidth")
		, iconImgHeight("iconImgHeight")
		
		, cssUp("cssUp")
		, cssDown("cssDown")
		, cssDisable("cssDisable")
		
		, cssContainer("cssContainer")
		
		, enableDown("enableDown")
		, enableDisable("enableDisable")
		
		, left("left")
		, top("top")
		
		, menuType("menuType")
		, menuLevel("menuLevel")
		
		, groupName("groupName")
		
		, debugId("debugId")
		
		, widgetStatus("widgetStatus")
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
	
	public enum WidgetType {
		  InlineLabel("InlineLabel")
		, TextBox("TextBox")
		, PasswordTextBox("PasswordTextBox")
		, ListBox("ListBox")
		, Button("Button")
		, Image("Image")
		, ImageButton("ImageButton")
		, ImageToggleButton("ImageToggleButton")
		, UIMenu("UIMenu")
		, HTML("HTML")
		, RadioButton("RadioButton")
		, WidgetFactory("WidgetFactory")
		;
		
		private final String text;
		private WidgetType(final String text) { this.text = text; }
		public boolean equalsName(String otherName) { return ( null == otherName ) ? false : text.equals(otherName); }
		public boolean startsWith(String prefix) { return ( null == prefix ) ? false : text.startsWith(prefix); }
		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() { return this.text; }
	}

	public enum WidgetStatus {
		  Up("Up")
		, Down("Down")
		, Disable("Disable")
		, Visible("Visible")
		, Invisible("Invisible")
		;
		
		private final String text;
		private WidgetStatus(final String text) { this.text = text; }
		public boolean equalsName(String otherName) { return ( otherName == null ) ? false : text.equals(otherName); }
		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() { return this.text; }
	}
	
	public enum WidgetMedia {
		  DateTimeFormat("DateTimeFormat")
		;
		private final String text;
		private WidgetMedia(final String text) { this.text = text; }
		public boolean equalsName(String otherName) { return ( otherName == null ) ? false : text.equals(otherName); }
		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() { return this.text; }
	}
	
}
