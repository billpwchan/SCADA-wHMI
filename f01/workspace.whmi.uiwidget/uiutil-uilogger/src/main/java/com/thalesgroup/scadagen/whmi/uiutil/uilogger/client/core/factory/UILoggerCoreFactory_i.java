package com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.core.factory;

import com.allen_sauer.gwt.log.client.Logger;

/**
 * @author t0096643
 *
 */
public interface UILoggerCoreFactory_i {
	
	public enum DefaultSupportedLoggerName {
		ConsoleLogger("ConsoleLogger")
		, DivLogger("DivLogger")
		, GWTLogger("GWTLogger")
		, LogWrapper("LogWrapper")
		, NullLogger("NullLogger")
		, RemoteLogger("RemoteLogger")
		, SystemLogger("SystemLogger")
		, WindowLogger("WindowLogger")
		;
		
		private final String text;
		private DefaultSupportedLoggerName(final String text) { this.text = text; }
		public boolean equalsName(String otherName) { return ( otherName == null ) ? false : text.equals(otherName); }
		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() { return this.text; }
	}
	
	/**
	 * Get the logger. Should be implemented by Factory owner
	 * 
	 * @param className
	 * @return return logger retrieved
	 */
	Logger getLogger(final String className);
}
