package com.thalesgroup.scadagen.bps.conf.binding.converter.range;

import java.util.Date;

import com.google.common.collect.Range;
import com.thalesgroup.scadagen.binding.RangeType;
import com.thalesgroup.scadagen.bps.conf.binding.data.IData;

/**
 * Range implementation for date
 */
public class DateRange extends RangeWrapperAbstract<Date> {

    /**
     * Constructor
     * @param from from value
     * @param to to value
     * @param type of the range (open/close)
     */
    public DateRange(Date from, Date to, RangeType type) {
        super(from, to, type);
    }
    /**
     * Constructor
     * @param range range of integers
     */
    public DateRange(Range<Date> range) {
        super(range);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(IData data) {
        return getRange().contains(data.getDateValue());
    }

    
}
