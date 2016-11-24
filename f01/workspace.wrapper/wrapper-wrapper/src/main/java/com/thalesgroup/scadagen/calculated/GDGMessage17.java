package com.thalesgroup.scadagen.calculated;

import com.thalesgroup.scadagen.common.calculated.GDGMessage;

public class GDGMessage17 extends GDGMessage {
	
	public GDGMessage17 () {
		super();
		
		m_name = this.getClass().getSimpleName();

		loadCnf();
		
    }

}