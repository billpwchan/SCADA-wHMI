package com.thalesgroup.scadagen.calculated.gdg.alarmevent;

import com.thalesgroup.scadagen.calculated.common.GDGMessage;

public class GDGMessage09 extends GDGMessage {
	
	public GDGMessage09 () {
		super();
		
		m_name = this.getClass().getSimpleName();

		loadCnf();
		
    }

}