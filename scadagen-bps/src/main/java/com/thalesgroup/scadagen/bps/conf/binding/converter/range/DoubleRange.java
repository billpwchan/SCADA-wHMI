package com.thalesgroup.scadagen.bps.conf.binding.converter.range;

import com.thalesgroup.scadagen.binding.RangeType;
import com.thalesgroup.scadagen.bps.conf.binding.data.IData;

/**
 * Range implementation for double
 */
public class DoubleRange extends RangeWrapperAbstract<Double> {

    /**
     * Constructor
     * @param from from value
     * @param to to value
     * @param type of the range (open/close)
     */
    public DoubleRange(double from, double to, RangeType type) {
        super(from, to, type);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(IData data) {
        return getRange().contains(data.getDoubleValue());
    }

    
}
