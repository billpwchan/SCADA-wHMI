package com.thalesgroup.scadagen.calculated;

import com.thalesgroup.scadagen.common.calculated.GDGMessage;

public class GDGMessage06 extends GDGMessage {
	
	public GDGMessage06 () {
		
		m_name = this.getClass().getSimpleName();

		loadCnf();
		
    }

}