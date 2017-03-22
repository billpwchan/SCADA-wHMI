package com.thalesgroup.scadagen.calculated.gdg.alarmevent;

import com.thalesgroup.scadagen.calculated.common.GDGMessage;

public class GDGMessage03 extends GDGMessage {
	
	public GDGMessage03 () {
		super();
		
		m_name = this.getClass().getSimpleName();

		loadCnf();
		
    }

}