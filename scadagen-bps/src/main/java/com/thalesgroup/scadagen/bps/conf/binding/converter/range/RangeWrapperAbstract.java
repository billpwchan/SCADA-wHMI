package com.thalesgroup.scadagen.bps.conf.binding.converter.range;

import com.google.common.collect.Range;
import com.google.common.collect.Ranges;
import com.thalesgroup.scadagen.binding.RangeType;
import com.thalesgroup.scadagen.bps.conf.binding.data.IData;

/**
 * Abstract class to simplify the implementation of a range wrapper
 * to be able to use range on {@link IData  }  
 * @param <T>
 */
public abstract class RangeWrapperAbstract<T extends Comparable<T>> {
    
    /** range which stores the data */
    private final Range<T> range_;

    /**
     * Constructor
     * @param range range to use
     */
    public RangeWrapperAbstract(Range<T> range) {
        this.range_ = range;
    }
    
    /**
     * Constructor
     * @param from the minimum allowed value
     * @param to the maximum allowed value
     * @param type range type to use
     */
    public RangeWrapperAbstract(T from, T to, RangeType type) {
        switch (type) {
            case CLOSE_CLOSE:
                range_ = Ranges.closed(from, to);
                break;
            case CLOSE_OPEN:
                range_ = Ranges.closedOpen(from, to);
                break;
            case OPEN_OPEN:
                range_ = Ranges.open(from, to);
                break;
            case OPEN_CLOSE:
                range_ = Ranges.openClosed(from, to);
                break;
            default:
                throw new IllegalArgumentException(type + " is not supported");
        }
    }
    
    /**
     * is the data value contained in the range
     * @param data the data to check
     * @return true if in the range
     */
    public abstract boolean contains(IData data);
    
    /**
     * get the internal range
     * @return the internal range
     */
    protected Range<T> getRange() {
        return range_;
    }

}
