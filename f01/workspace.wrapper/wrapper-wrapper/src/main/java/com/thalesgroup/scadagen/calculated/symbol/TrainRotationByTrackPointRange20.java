package com.thalesgroup.scadagen.calculated.symbol;

import com.thalesgroup.scadagen.calculated.common.TrainRotationByTrackPointRange;

public class TrainRotationByTrackPointRange20 extends TrainRotationByTrackPointRange {

	public TrainRotationByTrackPointRange20 () {
		super();
		
		m_name = TrainRotationByTrackPointRange20.class.getSimpleName();

		loadCnf();
		
		loadTrackMapping();
		
    }
}
