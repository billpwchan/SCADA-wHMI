package com.thalesgroup.scadagen.calculated;

import com.thalesgroup.scadagen.common.calculated.GDGMessage;

public class GDGMessage09 extends GDGMessage {
	
	public GDGMessage09 () {
		super();
		
		m_name = this.getClass().getSimpleName();

		loadCnf();
		
    }

}