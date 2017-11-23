package com.thalesgroup.scadagen.calculated.symbol;

import com.thalesgroup.scadagen.calculated.common.TrainPositionByTrackPointRange;

public class TrainPositionByTrackPointRange02 extends TrainPositionByTrackPointRange {

	public TrainPositionByTrackPointRange02 () {
		super();
		
		m_name = TrainPositionByTrackPointRange02.class.getSimpleName();

		loadCnf();
		
		loadTrackMapping();
		
    }
}
