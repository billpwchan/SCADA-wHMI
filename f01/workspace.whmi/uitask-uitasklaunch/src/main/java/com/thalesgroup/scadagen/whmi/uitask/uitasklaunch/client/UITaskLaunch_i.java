package com.thalesgroup.scadagen.whmi.uitask.uitasklaunch.client;

public interface UITaskLaunch_i {
	
	public final static char STR_SPLITER = '|';
	
	// TaskLaunch
	public enum TaskLaunchType {
		MENU("M")
		, PANEL("P")
		, IMAGE("S")
		, UNKNOW("U")
		;
		private final String text;
		private TaskLaunchType(final String text) { this.text = text; }
		public boolean equalsName(String otherName) { return ( otherName == null ) ? false : text.equals(otherName); }
		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() { return this.text; }
	}
	
	public enum UITaskLaunchAttribute {

		  Key("key")
		  
		, UIPath("uiPath")
		, UIScreen("uiScreen")  
		  
		, Type("type")
//		, Order("order")
		
		, Name("name")
		, EnableHTMLName("enableHTMLName")
	
		, Css("css")
		
		, Title("title")
		
		, Enable("enable")
		, Visible("visible")
		
		, Tooltips("tooltips")
		, Execute("execute")
		
		, UIConf("uiConf")
		
		, UICtrl("uiCtrl")
		, UIView("uiView")
		, UIOpts("uiOpts")
		, UIDict("uiDict")
		, UIElem("uiElem")
		, UISvId("uiSvId")	
		
		;
		private final String text;
		private UITaskLaunchAttribute(final String text) { this.text = text; }
		public boolean equalsName(String otherName) { return ( otherName == null ) ? false : text.equals(otherName); }
		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() { return this.text; }
	}
}
