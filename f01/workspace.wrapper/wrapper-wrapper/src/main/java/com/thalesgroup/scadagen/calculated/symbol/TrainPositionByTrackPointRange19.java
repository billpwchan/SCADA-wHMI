package com.thalesgroup.scadagen.calculated.symbol;

import com.thalesgroup.scadagen.calculated.common.TrainPositionByTrackPointRange;

public class TrainPositionByTrackPointRange19 extends TrainPositionByTrackPointRange {

	public TrainPositionByTrackPointRange19 () {
		super();
		
		m_name = TrainPositionByTrackPointRange19.class.getSimpleName();

		loadCnf();
		
		loadTrackMapping();
		
    }
}
