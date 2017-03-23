package com.thalesgroup.scadagen.calculated.gdg.alarmevent;

import com.thalesgroup.scadagen.calculated.common.GDGMessage;

public class GDGMessage19 extends GDGMessage {
	
	public GDGMessage19 () {
		super();
		
		m_name = this.getClass().getSimpleName();

		loadCnf();
		
    }

}