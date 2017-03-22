package com.thalesgroup.scadagen.calculated.gdg.alarmevent;

import com.thalesgroup.scadagen.calculated.common.GDGMessage;

public class GDGMessage10 extends GDGMessage {
	
	public GDGMessage10 () {
		super();
		
		m_name = this.getClass().getSimpleName();

		loadCnf();
		
    }

}