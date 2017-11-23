package com.thalesgroup.scadagen.calculated.symbol;

import com.thalesgroup.scadagen.calculated.common.TrainPositionByTrackPointRange;

public class TrainPositionByTrackPointRange18 extends TrainPositionByTrackPointRange {

	public TrainPositionByTrackPointRange18 () {
		super();
		
		m_name = TrainPositionByTrackPointRange18.class.getSimpleName();

		loadCnf();
		
		loadTrackMapping();
		
    }
}
