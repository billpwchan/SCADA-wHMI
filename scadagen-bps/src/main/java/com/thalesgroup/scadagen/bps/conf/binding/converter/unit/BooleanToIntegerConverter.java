package com.thalesgroup.scadagen.bps.conf.binding.converter.unit;

import java.util.HashMap;
import java.util.Map;

import com.thalesgroup.scadagen.binding.BooleanToIntegerBinding;
import com.thalesgroup.scadagen.binding.BooleanToIntegerItem;
import com.thalesgroup.scadagen.binding.BooleanValueType;
import com.thalesgroup.scadagen.bps.conf.binding.converter.ConverterAbstract;
import com.thalesgroup.scadagen.bps.conf.binding.data.IData;
import com.thalesgroup.scadagen.bps.conf.binding.data.IntegerData;

/**
 * Implementation of the boolean to integer converter
 */
public class BooleanToIntegerConverter extends ConverterAbstract<BooleanToIntegerBinding> {

	/** values to check */
    private Map<Boolean, Integer> map = new HashMap<Boolean, Integer>();

	/** default value if does not match one */
    private Integer defaultValue_;
    
    /**
     * Constructor
     * @param binding the BooleanToIntegerBinding description
     */
	public BooleanToIntegerConverter(final BooleanToIntegerBinding binding) {
		super(binding);
		
		defaultValue_ = binding.getDefault();
		
		for (BooleanToIntegerItem item: binding.getItem()) {
			BooleanValueType b = item.getBooleanValue();
			try {
				if (b.isValue()) {
					map.put(Boolean.TRUE, Integer.parseInt(item.getValue()));
				} else {
					map.put(Boolean.FALSE, Integer.parseInt(item.getValue()));
				}
			} catch (NumberFormatException e) {
				
			}
		}
	}

	/**
     * {@inheritDoc}
     */
    @Override
    public IData convert(IData data) {
        int toReturn = defaultValue_;
        
        Integer n = map.get(data.getBooleanValue());
    	if (n != null) {
    		toReturn = n;
    	}

        return new IntegerData(data.getEntityId(), getBinding().getId(), toReturn, data.getTimestamp());
    }
}
