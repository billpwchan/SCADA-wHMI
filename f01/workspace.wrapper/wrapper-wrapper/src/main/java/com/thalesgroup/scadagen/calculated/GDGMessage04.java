package com.thalesgroup.scadagen.calculated;

import com.thalesgroup.scadagen.common.calculated.GDGMessage;

public class GDGMessage04 extends GDGMessage {
	
	public GDGMessage04 () {
		
		m_name = this.getClass().getSimpleName();

		loadCnf();
		
    }

}