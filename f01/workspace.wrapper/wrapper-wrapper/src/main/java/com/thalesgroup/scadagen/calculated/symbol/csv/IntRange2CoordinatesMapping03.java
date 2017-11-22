package com.thalesgroup.scadagen.calculated.symbol.csv;

import com.thalesgroup.scadagen.calculated.common.IntRange2CsvMapping;

public class IntRange2CoordinatesMapping03 extends IntRange2CsvMapping {

	public IntRange2CoordinatesMapping03() {
		super();
		
		m_name = this.getClass().getSimpleName();
		
		ot = OT_COORDINATES;

		loadCnf();
		
		loadCsvFile();
		
		convertCsv2Map();
    }
	
}
