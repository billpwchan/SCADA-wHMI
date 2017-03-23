package com.thalesgroup.scadagen.calculated.gdg.alarmevent;

import com.thalesgroup.scadagen.calculated.common.GDGMessage;

public class GDGMessage06 extends GDGMessage {
	
	public GDGMessage06 () {
		super();
		
		m_name = this.getClass().getSimpleName();

		loadCnf();
		
    }

}