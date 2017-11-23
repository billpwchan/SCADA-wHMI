package com.thalesgroup.scadagen.calculated.symbol;

import com.thalesgroup.scadagen.calculated.common.TrainRotationByTrackPointRange;

public class TrainRotationByTrackPointRange08 extends TrainRotationByTrackPointRange {

	public TrainRotationByTrackPointRange08 () {
		super();
		
		m_name = TrainRotationByTrackPointRange08.class.getSimpleName();

		loadCnf();
		
		loadTrackMapping();
		
    }
}
