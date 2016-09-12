package com.thalesgroup.scadagen.calculated;

import com.thalesgroup.scadagen.common.calculated.GDGMessage;

public class GDGMessage05 extends GDGMessage {
	
	public GDGMessage05 () {
		super();
		
		m_name = this.getClass().getSimpleName();

		loadCnf();
		
    }

}