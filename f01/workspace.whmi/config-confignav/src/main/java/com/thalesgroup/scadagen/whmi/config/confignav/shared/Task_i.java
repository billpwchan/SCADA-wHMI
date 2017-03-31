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
		, UIPanel("uiPanel")
		, UIScreen("uiScreen")
		, UIPath("uiPath")
		, UICtrl("uiCtrl")
		, Css("css")
		
		, UIView("uiView")
		, UIOpts("uiOpts")
		, UIDict("uiDict")
		, UIElem("uiElem")
		
		, Tooltips("tooltips")

		, opm("opm")
		, opmOperation("opmOperation")
		, opmName("opmName")

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
