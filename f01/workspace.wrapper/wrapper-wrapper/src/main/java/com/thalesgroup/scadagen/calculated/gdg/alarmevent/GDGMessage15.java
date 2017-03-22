package com.thalesgroup.scadagen.calculated.gdg.alarmevent;

import com.thalesgroup.scadagen.calculated.common.GDGMessage;

public class GDGMessage15 extends GDGMessage {
	
	public GDGMessage15 () {
		super();
		
		m_name = this.getClass().getSimpleName();

		loadCnf();
		
    }

}