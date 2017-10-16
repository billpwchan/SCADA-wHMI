package com.thalesgroup.scadagen.calculated.symbol;

import com.thalesgroup.scadagen.calculated.common.TrainRotationByTrackPointRange;

public class TrainRotationByTrackPointRange15 extends TrainRotationByTrackPointRange {

	public TrainRotationByTrackPointRange15 () {
		super();
		
		m_name = TrainRotationByTrackPointRange15.class.getSimpleName();

		loadCnf();
		
		loadTrackMapping();
		
    }
}
