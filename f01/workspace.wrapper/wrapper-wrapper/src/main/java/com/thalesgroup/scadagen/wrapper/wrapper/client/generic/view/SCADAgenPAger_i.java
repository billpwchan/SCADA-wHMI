package com.thalesgroup.scadagen.wrapper.wrapper.client.generic.view;

public interface SCADAgenPAger_i {
	public enum ParameterName {
		  HasPreviousPage("HasPreviousPage")
		, HasNextPage("HasNextPage")
		, HasFastForwardPage("HasFastForwardPage")
		;
		private final String text;
		private ParameterName(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
		public static String[] toStrings() {
			ParameterName[] enums = values();
		    String[] strings = new String[enums.length];
		    for (int i = 0; i < enums.length; i++) {
		    	strings[i] = enums[i].name();
		    }
		    return strings;
		}
	}
}
