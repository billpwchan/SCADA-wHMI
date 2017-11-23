package com.thalesgroup.scadagen.calculated.symbol;

import com.thalesgroup.scadagen.calculated.common.TrainRotationByTrackPointRange;

public class TrainRotationByTrackPointRange10 extends TrainRotationByTrackPointRange {

	public TrainRotationByTrackPointRange10 () {
		super();
		
		m_name = TrainRotationByTrackPointRange10.class.getSimpleName();

		loadCnf();
		
		loadTrackMapping();
		
    }
}
