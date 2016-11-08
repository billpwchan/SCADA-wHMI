package com.thalesgroup.scadagen.bps.conf.binding.computer;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thalesgroup.hv.data.tools.helper.IDataHelper;
import com.thalesgroup.hv.data_v1.entity.AbstractEntityStatusesType;
import com.thalesgroup.scadagen.binding.AttributeBinding;
import com.thalesgroup.scadagen.bps.conf.binding.data.DateData;
import com.thalesgroup.scadagen.bps.conf.binding.data.IData;

public class ExtSysVarDateComputer implements IBindingComputer {

	protected static final Logger LOGGER = LoggerFactory.getLogger(ExtSysVarDateComputer.class);
	
	@Override
	public IData compute(IDataHelper dataHelper, AbstractEntityStatusesType entity, AttributeBinding binding,
			Map<String, IData> dataMap) {

		IData toReturn = null;

		// Check "evarSec" "evarMs" are available
		if (dataMap.get("evarSec") != null && dataMap.get("evarMs") != null) {
			
			IData sec = dataMap.get("evarSec");
			int secVal = sec.getIntValue();
			LOGGER.trace("evarSec [{}]", secVal);
			
			IData microsec = dataMap.get("evarMs");
			int msVal = microsec.getIntValue();
			LOGGER.trace("evarMs [{}]", msVal);
			
			long millisec = (long)secVal * (long)1000;
			if (msVal > 0) {
				millisec += ((long)msVal/(long)1000);
			}
			LOGGER.trace("millisec [{}]", millisec);
			
			Date d = new Date(millisec);
			LOGGER.debug("date = [{}], timestamp in millisec [{}]", d.toString(), d.getTime()); 

			toReturn = new DateData(entity.getId(), "date", d, sec.getTimestamp());
		}
			
		return toReturn;
	}

	@Override
	public String getComputedDataType() {
		return "DATE";
	}

}
