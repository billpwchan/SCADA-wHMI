package com.thalesgroup.scadagen.calculated;

import com.thalesgroup.scadagen.common.calculated.GDGMessage;

public class GDGMessage03 extends GDGMessage {
	
	public GDGMessage03 () {
		
		m_name = this.getClass().getSimpleName();

		loadCnf();
		
    }

}