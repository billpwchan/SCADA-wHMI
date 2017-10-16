package com.thalesgroup.scadagen.calculated.symbol;

import com.thalesgroup.scadagen.calculated.common.TrainPositionByTrackPointRange;

public class TrainPositionByTrackPointRange08 extends TrainPositionByTrackPointRange {

	public TrainPositionByTrackPointRange08 () {
		super();
		
		m_name = TrainPositionByTrackPointRange08.class.getSimpleName();

		loadCnf();
		
		loadTrackMapping();
		
    }
}
