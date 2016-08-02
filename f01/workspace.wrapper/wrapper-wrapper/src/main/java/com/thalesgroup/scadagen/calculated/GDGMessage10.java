package com.thalesgroup.scadagen.calculated;

import com.thalesgroup.scadagen.common.calculated.GDGMessage;

public class GDGMessage10 extends GDGMessage {
	
	public GDGMessage10 () {
		
		m_name = this.getClass().getSimpleName();

		loadCnf();
		
    }

}