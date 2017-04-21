package com.thalesgroup.scadagen.wrapper.wrapper.client.generic.matrix;

public interface ScsMatrixPanel_i {
	public enum ParameterName {
		  MwtEventBusName("MwtEventBusName")
		, MwtEventBusScope("MwtEventBusScope")
		, MatrixConfigId("MatrixConfigId")
		, MatrixViewStyle("MatrixViewStyle")
		, SelectionMode("SelectionMode")
		, CoeffAttributeName("CoeffAttributeName")
		, DisplayCounterTooltip("DisplayCounterTooltip")
		, CounterCssPrefix("CounterCssPrefix")
		, EventBus("EventBus")
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
