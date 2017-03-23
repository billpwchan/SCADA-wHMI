package com.thalesgroup.scadagen.calculated.gdg.generic;

import com.thalesgroup.scadagen.calculated.common.GDGColumn;

public class GDGColumn02 extends GDGColumn {

	public GDGColumn02 () {
		super();
		
		classname = this.getClass().getSimpleName();
		
		loadCnf();
	}
	
}
