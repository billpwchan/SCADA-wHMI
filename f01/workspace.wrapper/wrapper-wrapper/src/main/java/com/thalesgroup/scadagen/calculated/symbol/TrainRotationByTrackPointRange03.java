package com.thalesgroup.scadagen.calculated.symbol;

import com.thalesgroup.scadagen.calculated.common.TrainRotationByTrackPointRange;

public class TrainRotationByTrackPointRange03 extends TrainRotationByTrackPointRange {

	public TrainRotationByTrackPointRange03 () {
		super();
		
		m_name = TrainRotationByTrackPointRange03.class.getSimpleName();

		loadCnf();
		
		loadTrackMapping();
		
    }
}
