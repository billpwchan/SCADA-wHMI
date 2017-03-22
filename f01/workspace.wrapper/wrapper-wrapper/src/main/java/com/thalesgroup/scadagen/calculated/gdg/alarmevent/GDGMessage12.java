package com.thalesgroup.scadagen.calculated.gdg.alarmevent;

import com.thalesgroup.scadagen.calculated.common.GDGMessage;

public class GDGMessage12 extends GDGMessage {
	
	public GDGMessage12 () {
		super();
		
		m_name = this.getClass().getSimpleName();

		loadCnf();
		
    }

}