package com.thalesgroup.scadagen.calculated.symbol;

import com.thalesgroup.scadagen.calculated.common.TrainPositionByTrackPointRange;

public class TrainPositionByTrackPointRange04 extends TrainPositionByTrackPointRange {

	public TrainPositionByTrackPointRange04 () {
		super();
		
		m_name = TrainPositionByTrackPointRange04.class.getSimpleName();

		loadCnf();
		
		loadTrackMapping();
		
    }
}
