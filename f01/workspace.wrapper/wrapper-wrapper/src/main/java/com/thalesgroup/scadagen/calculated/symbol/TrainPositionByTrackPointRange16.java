package com.thalesgroup.scadagen.calculated.symbol;

import com.thalesgroup.scadagen.calculated.common.TrainPositionByTrackPointRange;

public class TrainPositionByTrackPointRange16 extends TrainPositionByTrackPointRange {

	public TrainPositionByTrackPointRange16 () {
		super();
		
		m_name = TrainPositionByTrackPointRange16.class.getSimpleName();

		loadCnf();
		
		loadTrackMapping();
		
    }
}
