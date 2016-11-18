package com.thalesgroup.scadagen.bps.conf.binding.converter.unit;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.thalesgroup.scadagen.binding.RangeItem;
import com.thalesgroup.scadagen.binding.StringEnumerationRangeBinding;
import com.thalesgroup.scadagen.binding.StringEnumerationRangeItem;
import com.thalesgroup.scadagen.bps.conf.binding.converter.ConverterAbstract;
import com.thalesgroup.scadagen.bps.conf.binding.converter.range.RangeFactory;
import com.thalesgroup.scadagen.bps.conf.binding.converter.range.RangeWrapperAbstract;
import com.thalesgroup.scadagen.bps.conf.binding.data.IData;
import com.thalesgroup.scadagen.bps.conf.binding.data.StringData;

/**
 * Converter implementation for the target string enumertion
 */
public class StringEnumerationConverter extends ConverterAbstract<StringEnumerationRangeBinding> {

    /** values to check */
    private final Map<RangeWrapperAbstract<? extends Comparable<?>>, String> values_ = 
            new HashMap<RangeWrapperAbstract<? extends Comparable<?>>, String>();

    /** default value if does not match one */
    private String defaultValue_;

    /**
     * Constructor
     * @param binding the string enumeration description
     */
    public StringEnumerationConverter(final StringEnumerationRangeBinding binding) {
        super(binding);

        //initialize the values to check
        defaultValue_ = binding.getDefault();
        for (StringEnumerationRangeItem enumItem : binding.getItem()) {
            for (RangeItem rangeItem : enumItem.getRange()) {
                values_.put(RangeFactory.createRange(rangeItem, binding.getInput().getType()), enumItem.getValue());
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IData convert(IData data) {
        String toReturn = defaultValue_;
        for (Entry<RangeWrapperAbstract<? extends Comparable<?>>, String> entry : values_.entrySet()) {
            if (entry.getKey().contains(data)) {
                toReturn = entry.getValue();
                break;
            }
        }
        return new StringData(data.getEntityId(), getBinding().getId(), toReturn, data.getTimestamp());
    }
}
