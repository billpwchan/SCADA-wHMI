package com.thalesgroup.scadagen.calculated.symbol;

import com.thalesgroup.scadagen.calculated.common.TrainPositionByTrackPointRange;

public class TrainPositionByTrackPointRange20 extends TrainPositionByTrackPointRange {

	public TrainPositionByTrackPointRange20 () {
		super();
		
		m_name = TrainPositionByTrackPointRange20.class.getSimpleName();

		loadCnf();
		
		loadTrackMapping();
		
    }
}
