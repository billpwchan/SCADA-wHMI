package com.thalesgroup.scadagen.calculated.symbol;

import com.thalesgroup.scadagen.calculated.common.TrainPositionByTrackPointRange;

public class TrainPositionByTrackPointRange12 extends TrainPositionByTrackPointRange {

	public TrainPositionByTrackPointRange12 () {
		super();
		
		m_name = TrainPositionByTrackPointRange12.class.getSimpleName();

		loadCnf();
		
		loadTrackMapping();
		
    }
}
