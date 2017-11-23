package com.thalesgroup.scadagen.calculated.symbol;

import com.thalesgroup.scadagen.calculated.common.TrainPositionByTrackPointRange;

public class TrainPositionByTrackPointRange01 extends TrainPositionByTrackPointRange {

	public TrainPositionByTrackPointRange01 () {
		super();
		
		m_name = TrainPositionByTrackPointRange01.class.getSimpleName();

		loadCnf();
		
		loadTrackMapping();
		
    }
}
