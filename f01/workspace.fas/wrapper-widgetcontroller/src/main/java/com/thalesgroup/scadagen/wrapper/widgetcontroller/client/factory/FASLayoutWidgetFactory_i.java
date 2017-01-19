package com.thalesgroup.scadagen.wrapper.widgetcontroller.client.factory;

public interface FASLayoutWidgetFactory_i {
	
	public enum FASWidgetType {
			UIWidgetEntryPoint("UIWidgetEntryPoint")
		;
		private final String text;
		private FASWidgetType(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}
	
	
	public enum FASWidgetArgs {
		  uiCtrl("uiCtrl")
		, uiView("uiView")
		, uiOpts("uiOpts")
		, uiElem("uiElem")
		, uiDict("uiDict")
		;
		private final String text;
		private FASWidgetArgs(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}
    
}
