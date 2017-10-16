package com.thalesgroup.scadagen.calculated.symbol;

import com.thalesgroup.scadagen.calculated.common.TrainRotationByTrackPointRange;

public class TrainRotationByTrackPointRange04 extends TrainRotationByTrackPointRange {

	public TrainRotationByTrackPointRange04 () {
		super();
		
		m_name = TrainRotationByTrackPointRange04.class.getSimpleName();

		loadCnf();
		
		loadTrackMapping();
		
    }
}
