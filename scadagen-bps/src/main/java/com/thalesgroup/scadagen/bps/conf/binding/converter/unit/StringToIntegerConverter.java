package com.thalesgroup.scadagen.bps.conf.binding.converter.unit;

import java.util.HashMap;
import java.util.Map;

import com.thalesgroup.scadagen.binding.StringToIntegerBinding;
import com.thalesgroup.scadagen.binding.StringToIntegerItem;
import com.thalesgroup.scadagen.binding.StringValueType;
import com.thalesgroup.scadagen.bps.conf.binding.converter.ConverterAbstract;
import com.thalesgroup.scadagen.bps.conf.binding.data.IData;
import com.thalesgroup.scadagen.bps.conf.binding.data.IntegerData;

/**
 * StringToIntegerConverter implementation
 */
public class StringToIntegerConverter extends ConverterAbstract<StringToIntegerBinding> {

	/** values to check */
    private Map<String, Integer> map = new HashMap<String, Integer>();

	/** default value if does not match one */
    private Integer defaultValue_;

    /**
     * Constructor
     * @param binding the StringToIntegerBinding description
     */
	public StringToIntegerConverter(final StringToIntegerBinding binding) {
		super(binding);

		defaultValue_ = binding.getDefault();
		
		for (StringToIntegerItem item: binding.getItem()) {
			for (StringValueType s: item.getStringValue()) {
				try {
					map.put(s.getValue(), Integer.parseInt(item.getValue()));			
				} catch (NumberFormatException e) {
					
				}
			}
		}
	}

	/**
     * {@inheritDoc}
     */
	@Override
	public IData convert(IData data) {
		int toReturn = defaultValue_;
        
        Integer n = map.get(data.getStringValue());
    	if (n != null) {
    		toReturn = n;
    	}

        return new IntegerData(data.getEntityId(), getBinding().getId(), toReturn, data.getTimestamp());
	}

}
