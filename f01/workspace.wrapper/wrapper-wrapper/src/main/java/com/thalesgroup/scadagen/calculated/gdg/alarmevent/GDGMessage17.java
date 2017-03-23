package com.thalesgroup.scadagen.calculated.gdg.alarmevent;

import com.thalesgroup.scadagen.calculated.common.GDGMessage;

public class GDGMessage17 extends GDGMessage {
	
	public GDGMessage17 () {
		super();
		
		m_name = this.getClass().getSimpleName();

		loadCnf();
		
    }

}