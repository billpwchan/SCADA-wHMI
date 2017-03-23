package com.thalesgroup.scadagen.calculated.gdg.alarmevent;

import com.thalesgroup.scadagen.calculated.common.GDGMessage;

public class GDGMessage08 extends GDGMessage {
	
	public GDGMessage08 () {
		super();
		
		m_name = this.getClass().getSimpleName();

		loadCnf();
		
    }

}