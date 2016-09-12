package com.thalesgroup.scadagen.calculated;

import com.thalesgroup.scadagen.common.calculated.GDGMessage;

public class GDGMessage02 extends GDGMessage {
	
	public GDGMessage02 () {
		super();
		
		m_name = this.getClass().getSimpleName();

		loadCnf();
		
    }

}