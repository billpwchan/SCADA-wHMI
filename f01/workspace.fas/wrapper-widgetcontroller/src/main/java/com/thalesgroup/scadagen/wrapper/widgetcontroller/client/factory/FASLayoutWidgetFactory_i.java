package com.thalesgroup.scadagen.wrapper.widgetcontroller.client.factory;

public interface FASLayoutWidgetFactory_i {
	
	public enum FASWidget {
		  // PTW(DPC) Summary
		  PTW_DPC_PANEL("PTW_DPC_PANEL")
		  // Login Summary
		, LOGIN_PANEL("LOGIN_PANEL")
		  // Change Password Summary
		, CHANGE_PASSWORD_PANEL("CHANGE_PASSWORD_PANEL")
		  // SOC Summary
		, SOC_PANEL("SOC_PANEL")
		;
		private final String text;
		private FASWidget(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}
    
}
