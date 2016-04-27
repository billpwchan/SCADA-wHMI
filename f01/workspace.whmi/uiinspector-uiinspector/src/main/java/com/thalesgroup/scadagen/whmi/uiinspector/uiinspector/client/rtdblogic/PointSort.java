package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.rtdblogic;

public class PointSort extends Point implements Comparable<PointSort> {

	public PointSort(String address, String[] keys) {
		super(address, keys);
	}

	@Override
	public int compareTo(PointSort o) {
		
		int comparedOrder = 0;
		String strComparedOrder = o.getValue("hmiOrder");
		if ( null != strComparedOrder ) {
			comparedOrder = Integer.parseInt(strComparedOrder);
		}
		
		int order = 0;
		String strOrder = getValue("hmiOrder");
		if ( null != strOrder ) {
			comparedOrder = Integer.parseInt(strOrder);
		}
		
		if (order > comparedOrder) {
			return 1;
		} else if (order == comparedOrder) {
			return 0;
		} else {
			return -1;
		}
	}
 
	public String toString() {
		return getAddress();
	}
	
}
