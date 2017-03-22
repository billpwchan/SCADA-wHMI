package com.thalesgroup.scadagen.calculated.gdg.alarmevent;

import com.thalesgroup.scadagen.calculated.common.GDGMessage;

public class GDGMessage13 extends GDGMessage {
	
	public GDGMessage13 () {
		super();
		
		m_name = this.getClass().getSimpleName();

		loadCnf();
		
    }

}