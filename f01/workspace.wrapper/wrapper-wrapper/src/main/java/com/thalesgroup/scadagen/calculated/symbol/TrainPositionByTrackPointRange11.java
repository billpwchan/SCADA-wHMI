package com.thalesgroup.scadagen.calculated.symbol;

import com.thalesgroup.scadagen.calculated.common.TrainPositionByTrackPointRange;

public class TrainPositionByTrackPointRange11 extends TrainPositionByTrackPointRange {

	public TrainPositionByTrackPointRange11 () {
		super();
		
		m_name = TrainPositionByTrackPointRange11.class.getSimpleName();

		loadCnf();
		
		loadTrackMapping();
		
    }
}
