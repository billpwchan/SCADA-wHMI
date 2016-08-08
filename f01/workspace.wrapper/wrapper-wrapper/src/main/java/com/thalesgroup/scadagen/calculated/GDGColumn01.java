package com.thalesgroup.scadagen.calculated;

import com.thalesgroup.scadagen.common.calculated.GDGColumn;

public class GDGColumn01 extends GDGColumn {

	public GDGColumn01 () {
		super();
		
		classname = this.getClass().getSimpleName();
		
		loadCnf();
	}
	
}
