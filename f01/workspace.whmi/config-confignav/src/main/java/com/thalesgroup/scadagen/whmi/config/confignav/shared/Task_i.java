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
		
		, Name("name")
		, EnableHTMLName("enableHTMLName")
		
		, Title("title")
		, Enable("enable")
		, Visible("visible")
		, UIScreen("uiScreen")
		, UIPath("uiPath")
		, UICtrl("uiCtrl")
		, Css("css")
		
		, UIConf("uiConf")
		
		, UIView("uiView")
		, UIOpts("uiOpts")
		, UIDict("uiDict")
		, UIElem("uiElem")
		, UISvId("uiSvId")
		
		, Tooltips("tooltips")

		, Opm("opm")
		, OpmOperation("opmOperation")
		, OpmName("opmName")

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
