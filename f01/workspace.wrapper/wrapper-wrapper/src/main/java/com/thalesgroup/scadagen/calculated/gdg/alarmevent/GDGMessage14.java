package com.thalesgroup.scadagen.calculated.gdg.alarmevent;

import com.thalesgroup.scadagen.calculated.common.GDGMessage;

public class GDGMessage14 extends GDGMessage {
	
	public GDGMessage14 () {
		super();
		
		m_name = this.getClass().getSimpleName();

		loadCnf();
		
    }

}