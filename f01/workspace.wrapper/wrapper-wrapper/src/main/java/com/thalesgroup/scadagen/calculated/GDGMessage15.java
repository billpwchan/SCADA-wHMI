package com.thalesgroup.scadagen.calculated;

import com.thalesgroup.scadagen.common.calculated.GDGMessage;

public class GDGMessage15 extends GDGMessage {
	
	public GDGMessage15 () {
		super();
		
		m_name = this.getClass().getSimpleName();

		loadCnf();
		
    }

}