package com.thalesgroup.scadagen.calculated;

import com.thalesgroup.scadagen.common.calculated.GDGMessage;

public class GDGMessage07 extends GDGMessage {
	
	public GDGMessage07 () {
		
		m_name = this.getClass().getSimpleName();

		loadCnf();
		
    }

}