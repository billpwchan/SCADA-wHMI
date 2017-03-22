package com.thalesgroup.scadagen.calculated.gdg.generic;

import com.thalesgroup.scadagen.calculated.common.GDGColumn;

public class GDGColumn01 extends GDGColumn {

	public GDGColumn01 () {
		super();
		
		classname = this.getClass().getSimpleName();
		
		loadCnf();
	}
	
}
