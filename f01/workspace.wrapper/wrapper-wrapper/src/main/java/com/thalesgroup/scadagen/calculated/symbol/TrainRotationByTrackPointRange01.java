package com.thalesgroup.scadagen.calculated.symbol;

import com.thalesgroup.scadagen.calculated.common.TrainRotationByTrackPointRange;

public class TrainRotationByTrackPointRange01 extends TrainRotationByTrackPointRange {

	public TrainRotationByTrackPointRange01 () {
		super();
		
		m_name = TrainRotationByTrackPointRange01.class.getSimpleName();

		loadCnf();
		
		loadTrackMapping();
		
    }
}
