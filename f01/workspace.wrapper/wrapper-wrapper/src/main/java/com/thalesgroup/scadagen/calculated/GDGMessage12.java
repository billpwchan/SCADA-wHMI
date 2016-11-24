package com.thalesgroup.scadagen.calculated;

import com.thalesgroup.scadagen.common.calculated.GDGMessage;

public class GDGMessage12 extends GDGMessage {
	
	public GDGMessage12 () {
		super();
		
		m_name = this.getClass().getSimpleName();

		loadCnf();
		
    }

}