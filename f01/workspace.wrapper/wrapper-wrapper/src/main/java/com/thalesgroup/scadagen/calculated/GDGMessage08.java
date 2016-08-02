package com.thalesgroup.scadagen.calculated;

import com.thalesgroup.scadagen.common.calculated.GDGMessage;

public class GDGMessage08 extends GDGMessage {
	
	public GDGMessage08 () {
		
		m_name = this.getClass().getSimpleName();

		loadCnf();
		
    }

}