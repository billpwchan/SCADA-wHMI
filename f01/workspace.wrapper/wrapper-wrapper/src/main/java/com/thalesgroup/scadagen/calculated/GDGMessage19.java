package com.thalesgroup.scadagen.calculated;

import com.thalesgroup.scadagen.common.calculated.GDGMessage;

public class GDGMessage19 extends GDGMessage {
	
	public GDGMessage19 () {
		super();
		
		m_name = this.getClass().getSimpleName();

		loadCnf();
		
    }

}