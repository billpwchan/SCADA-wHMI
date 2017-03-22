package com.thalesgroup.scadagen.calculated.gdg.alarmevent;

import com.thalesgroup.scadagen.calculated.common.GDGMessage;

public class GDGMessage04 extends GDGMessage {
	
	public GDGMessage04 () {
		super();
		
		m_name = this.getClass().getSimpleName();

		loadCnf();
		
    }

}