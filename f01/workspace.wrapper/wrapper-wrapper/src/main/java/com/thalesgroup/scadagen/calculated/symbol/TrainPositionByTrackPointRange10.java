package com.thalesgroup.scadagen.calculated.symbol;

import com.thalesgroup.scadagen.calculated.common.TrainPositionByTrackPointRange;

public class TrainPositionByTrackPointRange10 extends TrainPositionByTrackPointRange {

	public TrainPositionByTrackPointRange10 () {
		super();
		
		m_name = TrainPositionByTrackPointRange10.class.getSimpleName();

		loadCnf();
		
		loadTrackMapping();
		
    }
}
