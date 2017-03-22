package com.thalesgroup.scadagen.calculated.gdg.alarmevent;

import com.thalesgroup.scadagen.calculated.common.GDGMessage;

public class GDGMessage05 extends GDGMessage {
	
	public GDGMessage05 () {
		super();
		
		m_name = this.getClass().getSimpleName();

		loadCnf();
		
    }

}