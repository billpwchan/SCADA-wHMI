package com.thalesgroup.scadagen.calculated.symbol;

import com.thalesgroup.scadagen.calculated.common.TrainRotationByTrackPointRange;

public class TrainRotationByTrackPointRange07 extends TrainRotationByTrackPointRange {

	public TrainRotationByTrackPointRange07 () {
		super();
		
		m_name = TrainRotationByTrackPointRange07.class.getSimpleName();

		loadCnf();
		
		loadTrackMapping();
		
    }
}
