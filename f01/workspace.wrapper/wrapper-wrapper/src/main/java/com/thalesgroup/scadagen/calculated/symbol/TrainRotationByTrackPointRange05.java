package com.thalesgroup.scadagen.calculated.symbol;

import com.thalesgroup.scadagen.calculated.common.TrainRotationByTrackPointRange;

public class TrainRotationByTrackPointRange05 extends TrainRotationByTrackPointRange {

	public TrainRotationByTrackPointRange05 () {
		super();
		
		m_name = TrainRotationByTrackPointRange05.class.getSimpleName();

		loadCnf();
		
		loadTrackMapping();
		
    }
}
