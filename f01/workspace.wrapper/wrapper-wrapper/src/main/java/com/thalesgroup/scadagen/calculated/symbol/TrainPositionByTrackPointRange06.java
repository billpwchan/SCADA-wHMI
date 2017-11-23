package com.thalesgroup.scadagen.calculated.symbol;

import com.thalesgroup.scadagen.calculated.common.TrainPositionByTrackPointRange;

public class TrainPositionByTrackPointRange06 extends TrainPositionByTrackPointRange {

	public TrainPositionByTrackPointRange06 () {
		super();
		
		m_name = TrainPositionByTrackPointRange06.class.getSimpleName();

		loadCnf();
		
		loadTrackMapping();
		
    }
}
