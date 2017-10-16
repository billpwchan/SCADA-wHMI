package com.thalesgroup.scadagen.calculated.symbol;

import com.thalesgroup.scadagen.calculated.common.TrainRotationByTrackPointRange;

public class TrainRotationByTrackPointRange11 extends TrainRotationByTrackPointRange {

	public TrainRotationByTrackPointRange11 () {
		super();
		
		m_name = TrainRotationByTrackPointRange11.class.getSimpleName();

		loadCnf();
		
		loadTrackMapping();
		
    }
}
