package com.thalesgroup.scadagen.calculated;

import com.thalesgroup.scadagen.common.calculated.GDGMessage;

public class GDGMessage01 extends GDGMessage {
	
	public GDGMessage01 () {
		super();
		
		m_name = this.getClass().getSimpleName();

		loadCnf();
		
    }

}
