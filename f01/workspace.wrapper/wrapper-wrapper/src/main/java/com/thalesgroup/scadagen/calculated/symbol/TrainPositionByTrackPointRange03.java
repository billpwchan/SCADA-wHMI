package com.thalesgroup.scadagen.calculated.symbol;

import com.thalesgroup.scadagen.calculated.common.TrainPositionByTrackPointRange;

public class TrainPositionByTrackPointRange03 extends TrainPositionByTrackPointRange {

	public TrainPositionByTrackPointRange03 () {
		super();
		
		m_name = TrainPositionByTrackPointRange03.class.getSimpleName();

		loadCnf();
		
		loadTrackMapping();
		
    }
}
