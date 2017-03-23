package com.thalesgroup.scadagen.calculated.gdg.alarmevent;

import com.thalesgroup.scadagen.calculated.common.GDGMessage;

public class GDGMessage16 extends GDGMessage {
	
	public GDGMessage16 () {
		super();
		
		m_name = this.getClass().getSimpleName();

		loadCnf();
		
    }

}