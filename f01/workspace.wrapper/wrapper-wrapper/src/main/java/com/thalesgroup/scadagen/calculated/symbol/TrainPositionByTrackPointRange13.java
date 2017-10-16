package com.thalesgroup.scadagen.calculated.symbol;

import com.thalesgroup.scadagen.calculated.common.TrainPositionByTrackPointRange;

public class TrainPositionByTrackPointRange13 extends TrainPositionByTrackPointRange {

	public TrainPositionByTrackPointRange13 () {
		super();
		
		m_name = TrainPositionByTrackPointRange13.class.getSimpleName();

		loadCnf();
		
		loadTrackMapping();
		
    }
}
