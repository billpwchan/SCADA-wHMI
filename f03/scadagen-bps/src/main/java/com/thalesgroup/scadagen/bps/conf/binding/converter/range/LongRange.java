package com.thalesgroup.scadagen.bps.conf.binding.converter.range;

import com.thalesgroup.scadagen.binding.RangeType;
import com.thalesgroup.scadagen.bps.conf.binding.data.IData;

/**
 * Range implementation for long
 */
public class LongRange extends RangeWrapperAbstract<Long> {

    /**
     * Constructor
     * @param from from value
     * @param to to value
     * @param type of the range (open/close)
     */
    public LongRange(long from, long to, RangeType type) {
        super(from, to, type);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(IData data) {
        return getRange().contains(data.getLongValue());
    }

    
}
