package com.thalesgroup.scadagen.calculated.gdg.alarmevent;

import com.thalesgroup.scadagen.calculated.common.GDGMessage;

public class GDGMessage01 extends GDGMessage {
	
	public GDGMessage01 () {
		super();
		
		m_name = this.getClass().getSimpleName();

		loadCnf();
		
    }

}
