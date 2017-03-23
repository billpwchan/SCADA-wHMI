package com.thalesgroup.scadagen.calculated.gdg.alarmevent;

import com.thalesgroup.scadagen.calculated.common.GDGMessage;

public class GDGMessage02 extends GDGMessage {
	
	public GDGMessage02 () {
		super();
		
		m_name = this.getClass().getSimpleName();

		loadCnf();
		
    }

}