package com.thalesgroup.scadagen.calculated;

import com.thalesgroup.scadagen.common.calculated.GDGMessage;

public class GDGMessage13 extends GDGMessage {
	
	public GDGMessage13 () {
		super();
		
		m_name = this.getClass().getSimpleName();

		loadCnf();
		
    }

}