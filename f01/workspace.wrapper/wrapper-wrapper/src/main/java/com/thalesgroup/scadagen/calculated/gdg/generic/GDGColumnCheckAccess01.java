package com.thalesgroup.scadagen.calculated.gdg.generic;

import com.thalesgroup.scadagen.calculated.common.GDGColumnCheckAccess;

public class GDGColumnCheckAccess01 extends GDGColumnCheckAccess {

	public GDGColumnCheckAccess01 () {
		super();
		
		m_name = this.getClass().getSimpleName();
		
		loadCnf();
	}
	
}
