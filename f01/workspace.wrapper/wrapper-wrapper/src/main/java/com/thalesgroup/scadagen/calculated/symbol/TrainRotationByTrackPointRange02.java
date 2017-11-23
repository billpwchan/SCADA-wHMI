package com.thalesgroup.scadagen.calculated.symbol;

import com.thalesgroup.scadagen.calculated.common.TrainRotationByTrackPointRange;

public class TrainRotationByTrackPointRange02 extends TrainRotationByTrackPointRange {

	public TrainRotationByTrackPointRange02 () {
		super();
		
		m_name = TrainRotationByTrackPointRange02.class.getSimpleName();

		loadCnf();
		
		loadTrackMapping();
		
    }
}
