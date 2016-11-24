package com.thalesgroup.scadagen.calculated;

import com.thalesgroup.scadagen.common.calculated.GDGMessage;

public class GDGMessage20 extends GDGMessage {
	
	public GDGMessage20 () {
		super();
		
		m_name = this.getClass().getSimpleName();

		loadCnf();
		
    }

}