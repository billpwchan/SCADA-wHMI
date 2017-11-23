package com.thalesgroup.scadagen.calculated.symbol;

import com.thalesgroup.scadagen.calculated.common.TrainRotationByTrackPointRange;

public class TrainRotationByTrackPointRange09 extends TrainRotationByTrackPointRange {

	public TrainRotationByTrackPointRange09 () {
		super();
		
		m_name = TrainRotationByTrackPointRange09.class.getSimpleName();

		loadCnf();
		
		loadTrackMapping();
		
    }
}
