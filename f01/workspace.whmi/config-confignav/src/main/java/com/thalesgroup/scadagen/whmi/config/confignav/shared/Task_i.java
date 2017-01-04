package com.thalesgroup.scadagen.whmi.config.confignav.shared;

public interface Task_i {
	public enum TaskHeader {
		Key("key")
		, Setting("setting")
		;
		private final String text;
		private TaskHeader(final String text) { this.text = text; }
		public boolean equalsName(String otherName) { return ( otherName == null ) ? false : text.equals(otherName); }
		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() { return this.text; }

	}
	public enum TaskAttribute {
		Key("key")
		, Type("type")
		, Order("order")
		, Name("name")
		, Title("title")
		, Enable("enable")
		, Visible("visible")
		, LocCat("locCat")
		, FunCat("funCat")
		, ActionCat("actionCat")
		, ModeCat("modeCat")
		, UIPanel("uiPanel")
		, UIScreen("uiScreen")
		, UIPath("uiPath")
		, UICtrl("uiCtrl")
		, Css("css")
		, UIView("uiView")
		, UIOpts("uiOpts")
		, UIDict("uiDict")
		, Tooltips("tooltips")
		;
		private final String text;
		private TaskAttribute(final String text) { this.text = text; }
		public boolean equalsName(String otherName) { return ( otherName == null ) ? false : text.equals(otherName); }
		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() { return this.text; }
	}
}
