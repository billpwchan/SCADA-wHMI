package com.thalesgroup.scadagen.calculated;

import com.thalesgroup.scadagen.common.calculated.GDGMessage;

public class GDGMessage16 extends GDGMessage {
	
	public GDGMessage16 () {
		super();
		
		m_name = this.getClass().getSimpleName();

		loadCnf();
		
    }

}