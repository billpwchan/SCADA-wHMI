package com.thalesgroup.scadagen.calculated.gdg.alarmevent;

import com.thalesgroup.scadagen.calculated.common.GDGMessage;

public class GDGMessage20 extends GDGMessage {
	
	public GDGMessage20 () {
		super();
		
		m_name = this.getClass().getSimpleName();

		loadCnf();
		
    }

}