package com.thalesgroup.scadagen.calculated.symbol.csv;

import com.thalesgroup.scadagen.calculated.common.IntRange2CsvMapping;

public class IntRange2DoubleMapping01 extends IntRange2CsvMapping {

	public IntRange2DoubleMapping01() {
		super();
		
		m_name = this.getClass().getSimpleName();
		
		ot = OT_DOUBLE;

		loadCnf();
		
		loadCsvFile();
		
		convertCsv2Map();
    }
	
}
