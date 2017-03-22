package com.thalesgroup.scadagen.calculated.gdg.alarmevent;

import com.thalesgroup.scadagen.calculated.common.GDGMessage;

public class GDGMessage18 extends GDGMessage {
	
	public GDGMessage18 () {
		super();
		
		m_name = this.getClass().getSimpleName();

		loadCnf();
		
    }

}