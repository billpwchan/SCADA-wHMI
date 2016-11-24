package com.thalesgroup.scadagen.calculated;

import com.thalesgroup.scadagen.common.calculated.GDGMessage;

public class GDGMessage11 extends GDGMessage {
	
	public GDGMessage11 () {
		super();
		
		m_name = this.getClass().getSimpleName();

		loadCnf();
		
    }

}