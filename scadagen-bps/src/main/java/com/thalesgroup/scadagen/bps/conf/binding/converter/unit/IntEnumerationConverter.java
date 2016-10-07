package com.thalesgroup.scadagen.bps.conf.binding.converter.unit;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.thalesgroup.scadagen.binding.IntegerEnumerationRangeBinding;
import com.thalesgroup.scadagen.binding.IntegerEnumerationRangeItem;
import com.thalesgroup.scadagen.binding.RangeItem;
import com.thalesgroup.scadagen.bps.conf.binding.converter.ConverterAbstract;
import com.thalesgroup.scadagen.bps.conf.binding.converter.range.RangeFactory;
import com.thalesgroup.scadagen.bps.conf.binding.converter.range.RangeWrapperAbstract;
import com.thalesgroup.scadagen.bps.conf.binding.data.IData;
import com.thalesgroup.scadagen.bps.conf.binding.data.IntegerData;

/**
 * Converter from range to enumeration
 */
public class IntEnumerationConverter extends ConverterAbstract<IntegerEnumerationRangeBinding> {

    /** values to check */
    private final Map<RangeWrapperAbstract<? extends Comparable<?>>, Integer> values_ = 
            new HashMap<RangeWrapperAbstract<? extends Comparable<?>>, Integer>();

    /** default value if does not match one */
    private Integer defaultValue_;

    /**
     * Constructor
     * @param binding the int enumeration description
     */
    public IntEnumerationConverter(final IntegerEnumerationRangeBinding binding) {
        super(binding);

        defaultValue_ = binding.getDefault();
        //initialize the values to check
        for (IntegerEnumerationRangeItem enumItem : binding.getItem()) {
            for (RangeItem rangeItem : enumItem.getRange()) {
                values_.put(RangeFactory.createRange(rangeItem, binding.getInput().getType()), enumItem.getValue());
            }
        }
    }

    @Override
    public IData convert(IData data) {
        Integer toReturn = defaultValue_;
        for (Entry<RangeWrapperAbstract<? extends Comparable<?>>, Integer> entry : values_.entrySet()) {
            if (entry.getKey().contains(data)) {
                toReturn = entry.getValue();
                break;
            }
        }
        return new IntegerData(data.getEntityId(), getBinding().getId(), toReturn, data.getTimestamp());
    }

}
