package com.thalesgroup.scadagen.calculated.symbol;

import com.thalesgroup.scadagen.calculated.common.TrainPositionByTrackPointRange;

public class TrainPositionByTrackPointRange09 extends TrainPositionByTrackPointRange {

	public TrainPositionByTrackPointRange09 () {
		super();
		
		m_name = TrainPositionByTrackPointRange09.class.getSimpleName();

		loadCnf();
		
		loadTrackMapping();
		
    }
}
