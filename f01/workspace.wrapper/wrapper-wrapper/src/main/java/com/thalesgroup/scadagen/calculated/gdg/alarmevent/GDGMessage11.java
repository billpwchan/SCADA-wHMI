package com.thalesgroup.scadagen.calculated.gdg.alarmevent;

import com.thalesgroup.scadagen.calculated.common.GDGMessage;

public class GDGMessage11 extends GDGMessage {
	
	public GDGMessage11 () {
		super();
		
		m_name = this.getClass().getSimpleName();

		loadCnf();
		
    }

}