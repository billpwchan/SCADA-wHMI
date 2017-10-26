package com.thalesgroup.scadagen.calculated.symbol.csv;

import com.thalesgroup.scadagen.calculated.common.IntRange2CsvMapping;

public class IntRange2DoubleMapping06 extends IntRange2CsvMapping {

	public IntRange2DoubleMapping06() {
		super();
		
		m_name = this.getClass().getSimpleName();
		
		ot = OT_DOUBLE;

		loadCnf();
		
		loadCsvFile();
		
		convertCsv2Map();
    }
	
}
