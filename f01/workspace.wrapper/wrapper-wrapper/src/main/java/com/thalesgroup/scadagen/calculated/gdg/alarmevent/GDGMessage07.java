package com.thalesgroup.scadagen.calculated.gdg.alarmevent;

import com.thalesgroup.scadagen.calculated.common.GDGMessage;

public class GDGMessage07 extends GDGMessage {
	
	public GDGMessage07 () {
		super();
		
		m_name = this.getClass().getSimpleName();

		loadCnf();
		
    }

}