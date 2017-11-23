package com.thalesgroup.scadagen.calculated.symbol;

import com.thalesgroup.scadagen.calculated.common.TrainPositionByTrackPointRange;

public class TrainPositionByTrackPointRange15 extends TrainPositionByTrackPointRange {

	public TrainPositionByTrackPointRange15 () {
		super();
		
		m_name = TrainPositionByTrackPointRange15.class.getSimpleName();

		loadCnf();
		
		loadTrackMapping();
		
    }
}
