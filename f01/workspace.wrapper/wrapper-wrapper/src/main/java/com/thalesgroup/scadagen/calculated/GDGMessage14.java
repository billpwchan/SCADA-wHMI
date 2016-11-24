package com.thalesgroup.scadagen.calculated;

import com.thalesgroup.scadagen.common.calculated.GDGMessage;

public class GDGMessage14 extends GDGMessage {
	
	public GDGMessage14 () {
		super();
		
		m_name = this.getClass().getSimpleName();

		loadCnf();
		
    }

}