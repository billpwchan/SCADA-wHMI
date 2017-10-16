package com.thalesgroup.scadagen.calculated.symbol;

import com.thalesgroup.scadagen.calculated.common.TrainPositionByTrackPointRange;

public class TrainPositionByTrackPointRange07 extends TrainPositionByTrackPointRange {

	public TrainPositionByTrackPointRange07 () {
		super();
		
		m_name = TrainPositionByTrackPointRange07.class.getSimpleName();

		loadCnf();
		
		loadTrackMapping();
		
    }
}
