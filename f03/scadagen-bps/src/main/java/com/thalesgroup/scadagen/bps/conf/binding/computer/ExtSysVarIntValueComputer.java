package com.thalesgroup.scadagen.bps.conf.binding.computer;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thalesgroup.hv.data.tools.helper.IDataHelper;
import com.thalesgroup.hv.data_v1.entity.AbstractEntityStatusesType;
import com.thalesgroup.scadagen.binding.AttributeBinding;
import com.thalesgroup.scadagen.bps.conf.binding.data.IData;
import com.thalesgroup.scadagen.bps.conf.binding.data.IntegerData;

public class ExtSysVarIntValueComputer implements IBindingComputer {

	protected static final Logger LOGGER = LoggerFactory.getLogger(ExtSysVarIntValueComputer.class);
	
	@Override
	public IData compute(IDataHelper dataHelper, AbstractEntityStatusesType entity, AttributeBinding binding,
			Map<String, IData> dataMap) {
		LOGGER.debug("start compute entity [{}] target [{}]", entity.getId(), binding.getId());
		IData toReturn = null;

		// Check "evarValue" "evarType" are available
		if (dataMap.get("evarValue") != null && dataMap.get("evarType") != null) {
			int value = 0;
			IData data = dataMap.get("evarValue");
			String strVal = data.getStringValue();
			LOGGER.trace("evarValue [{}]", strVal);
			
			IData type = dataMap.get("evarType");
			String strType = type.getStringValue();
			LOGGER.trace("evarType [{}]", strType);
			
			if (strType.compareTo("integer") == 0) {
				value = Integer.parseInt(strVal);
			} else if (strType.compareTo("float") == 0) {
				value = (int) Float.parseFloat(strVal);
			} else if (strType.compareTo("hexstring") ==0) {
				value = Integer.parseInt(strVal, 16);
			} else if (strType.compareTo("asciistring") == 0) {
				LOGGER.error("Error converting from asciistring type to integer value for entity [{}] evarValue", entity.getId());
				value = 0;
			}
			toReturn = new IntegerData(entity.getId(), "evarValue", value, data.getTimestamp());
		}
			
		return toReturn;
	}

	@Override
	public String getComputedDataType() {
		return "INT";
	}

}
