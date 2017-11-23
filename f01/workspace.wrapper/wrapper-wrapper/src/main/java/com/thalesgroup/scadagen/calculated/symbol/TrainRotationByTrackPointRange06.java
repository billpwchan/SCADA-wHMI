package com.thalesgroup.scadagen.calculated.symbol;

import com.thalesgroup.scadagen.calculated.common.TrainRotationByTrackPointRange;

public class TrainRotationByTrackPointRange06 extends TrainRotationByTrackPointRange {

	public TrainRotationByTrackPointRange06 () {
		super();
		
		m_name = TrainRotationByTrackPointRange06.class.getSimpleName();

		loadCnf();
		
		loadTrackMapping();
		
    }
}
