package com.thalesgroup.scadagen.bps.conf.binding.converter.range;

import com.thalesgroup.scadagen.binding.RangeType;
import com.thalesgroup.scadagen.bps.conf.binding.data.IData;

/**
 * Range implementation for integer
 */
public class IntegerRange extends RangeWrapperAbstract<Integer> {

    /**
     * Constructor
     * @param from from value
     * @param to to value
     * @param type of the range (open/close)
     */
    public IntegerRange(int from, int to, RangeType type) {
        super(from, to, type);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(IData data) {
        return getRange().contains(data.getIntValue());
    }

    
}
