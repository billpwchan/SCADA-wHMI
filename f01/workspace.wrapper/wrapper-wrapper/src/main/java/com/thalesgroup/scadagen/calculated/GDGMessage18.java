package com.thalesgroup.scadagen.calculated;

import com.thalesgroup.scadagen.common.calculated.GDGMessage;

public class GDGMessage18 extends GDGMessage {
	
	public GDGMessage18 () {
		super();
		
		m_name = this.getClass().getSimpleName();

		loadCnf();
		
    }

}