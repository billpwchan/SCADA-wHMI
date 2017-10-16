package com.thalesgroup.scadagen.calculated.symbol;

import com.thalesgroup.scadagen.calculated.common.TrainPositionByTrackPointRange;

public class TrainPositionByTrackPointRange05 extends TrainPositionByTrackPointRange {

	public TrainPositionByTrackPointRange05 () {
		super();
		
		m_name = TrainPositionByTrackPointRange05.class.getSimpleName();

		loadCnf();
		
		loadTrackMapping();
		
    }
}
