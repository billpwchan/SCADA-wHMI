package com.thalesgroup.scadagen.bps.conf.binding.converter.range;

import com.thalesgroup.hv.common.HypervisorConversionException;
import com.thalesgroup.hv.data.tools.bean.Converter;
import com.thalesgroup.scadagen.binding.RangeItem;
import com.thalesgroup.scadagen.binding.Type;

/**
 * Create the right range linked to the configuration
 */
public class RangeFactory {
    
    /**
     * create a range from the configuration
     * @param item configuration item to define the range values
     * @param sourceType the type of the expected received data (to correctly cast the input)
     * @return the range
     */
    public static RangeWrapperAbstract<? extends Comparable<?>> createRange(final RangeItem item, Type sourceType) {
        RangeWrapperAbstract<? extends Comparable<?>> toReturn = null;
        try {
            switch (sourceType) {
                case INT:
                    //create an integer range
                    toReturn = new IntegerRange(Converter.convertStringTo(Integer.class, item.getFrom()),
                            Converter.convertStringTo(Integer.class, item.getTo()),
                            item.getType());
                    break;
                case LONG:
                    //create a long range
                    toReturn = new LongRange(Converter.convertStringTo(Long.class, item.getFrom()),
                            Converter.convertStringTo(Long.class, item.getTo()),
                            item.getType());
                    break;
                case FLOAT:
                    //create a float range
                    toReturn = new FloatRange(Converter.convertStringTo(Float.class, item.getFrom()),
                            Converter.convertStringTo(Float.class, item.getTo()),
                            item.getType());
                    break;
                case DOUBLE:
                    //create a double range
                    toReturn = new DoubleRange(Converter.convertStringTo(Double.class, item.getFrom()),
                            Converter.convertStringTo(Double.class, item.getTo()),
                            item.getType());
                    break;
                case STRING:
                    //create a string range
                    toReturn = new StringRange(item.getFrom(), item.getTo(), item.getType());
                    break;
                default:
                    throw new IllegalArgumentException(sourceType + " is not supported");
            }

        } catch (HypervisorConversionException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        return toReturn;
    }

}
