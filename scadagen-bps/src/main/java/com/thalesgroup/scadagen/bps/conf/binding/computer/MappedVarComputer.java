package com.thalesgroup.scadagen.bps.conf.binding.computer;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thalesgroup.hv.data.exception.EntityManipulationException;
import com.thalesgroup.hv.data.tools.helper.IDataHelper;
import com.thalesgroup.hv.data_v1.entity.AbstractEntityStatusesType;
import com.thalesgroup.scadagen.binding.AttributeBinding;
import com.thalesgroup.scadagen.binding.MappedVarBinding;
import com.thalesgroup.scadagen.bps.conf.binding.data.FloatData;
import com.thalesgroup.scadagen.bps.conf.binding.data.IData;
import com.thalesgroup.scadagen.bps.conf.binding.data.IntegerData;
import com.thalesgroup.scadagen.bps.conf.binding.data.StringData;

public class MappedVarComputer implements IComputer {
	
	protected static final Logger LOGGER = LoggerFactory.getLogger(MappedVarComputer.class);

	public MappedVarComputer() {
	}

	@Override
	public IData compute(IDataHelper dataHelper, AbstractEntityStatusesType entity, AttributeBinding binding, Map<String, IData> dataMap) {
		MappedVarBinding mappedBinding = (MappedVarBinding) binding;
		String mappedName = mappedBinding.getInputMapping().getMappedName();
		String mappedValue = mappedBinding.getInputMapping().getMappedValue();
		String mappedValueType = mappedBinding.getInputMapping().getMappedValueType();
		
		String valueStr = null;
		String valueTypeStr = null;

		try {
			valueStr = (String)dataHelper.getAttributeValue(entity, mappedValue);
			valueTypeStr = (String)dataHelper.getAttributeValue(entity, mappedValueType);
		} catch (EntityManipulationException e) {
			LOGGER.error("Unable to compute value from mapping. {}", e);
		}
		
		IData toReturn = null;
		if (valueTypeStr.compareToIgnoreCase("integer") == 0) {
			toReturn = new IntegerData(entity.getId(), mappedName, Integer.parseInt(valueStr), System.currentTimeMillis());
		} else if (valueTypeStr.compareToIgnoreCase("float") == 0) {
			toReturn = new FloatData(entity.getId(), mappedName, Float.parseFloat(valueStr), System.currentTimeMillis());
		} else if (valueTypeStr.compareToIgnoreCase("hexstring") == 0 ||
				valueTypeStr.compareToIgnoreCase("asciistring") == 0) {
			toReturn = new StringData(entity.getId(), mappedName, valueStr, System.currentTimeMillis());
		}
		
		return toReturn;
	}

}
