package com.thalesgroup.scadagen.calculated.symbol;

import com.thalesgroup.scadagen.calculated.common.TrainRotationByTrackPointRange;

public class TrainRotationByTrackPointRange16 extends TrainRotationByTrackPointRange {

	public TrainRotationByTrackPointRange16 () {
		super();
		
		m_name = TrainRotationByTrackPointRange16.class.getSimpleName();

		loadCnf();
		
		loadTrackMapping();
		
    }
}
