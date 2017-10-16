package com.thalesgroup.scadagen.calculated.symbol;

import com.thalesgroup.scadagen.calculated.common.TrainRotationByTrackPointRange;

public class TrainRotationByTrackPointRange12 extends TrainRotationByTrackPointRange {

	public TrainRotationByTrackPointRange12 () {
		super();
		
		m_name = TrainRotationByTrackPointRange12.class.getSimpleName();

		loadCnf();
		
		loadTrackMapping();
		
    }
}
