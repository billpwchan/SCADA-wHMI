package com.thalesgroup.scadagen.calculated.symbol;

import com.thalesgroup.scadagen.calculated.common.TrainRotationByTrackPointRange;

public class TrainRotationByTrackPointRange18 extends TrainRotationByTrackPointRange {

	public TrainRotationByTrackPointRange18 () {
		super();
		
		m_name = TrainRotationByTrackPointRange18.class.getSimpleName();

		loadCnf();
		
		loadTrackMapping();
		
    }
}
