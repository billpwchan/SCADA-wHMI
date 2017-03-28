package com.thalesgroup.scadagen.bps.conf.binding.converter.unit;

import java.util.ArrayList;
import java.util.List;

import com.thalesgroup.scadagen.binding.BooleanBindingFromRange;
import com.thalesgroup.scadagen.binding.RangeItem;
import com.thalesgroup.scadagen.binding.RangeType;
import com.thalesgroup.scadagen.bps.conf.binding.converter.ConverterAbstract;
import com.thalesgroup.scadagen.bps.conf.binding.converter.range.RangeFactory;
import com.thalesgroup.scadagen.bps.conf.binding.converter.range.RangeWrapperAbstract;
import com.thalesgroup.scadagen.bps.conf.binding.data.BooleanData;
import com.thalesgroup.scadagen.bps.conf.binding.data.IData;

/**
 * Implementation of the boolean converter
 */
public class BooleanConverter extends ConverterAbstract<BooleanBindingFromRange> {

    /** values to test for true */
    private final List<RangeWrapperAbstract<? extends Comparable<?>>> trueValues_ = 
            new ArrayList<RangeWrapperAbstract<? extends Comparable<?>>>();

    /** values to test for false */
    private final List<RangeWrapperAbstract<? extends Comparable<?>>> falseValues_ = 
            new ArrayList<RangeWrapperAbstract<? extends Comparable<?>>>();

    /** should I test true or false */
    private final boolean testTrue_;

    /**
     * Constructor
     * @param binding the boolean binding description
     */
    public BooleanConverter(final BooleanBindingFromRange binding) {
        super(binding);

        //intialize the values to check
        boolean tempTestTrue = false;
        for (RangeItem item : binding.getFalse()) {
            falseValues_.add(RangeFactory.createRange(item, binding.getInput().getType()));
            //as we are defining false values we have to define to check false value
            tempTestTrue = false;
        }
        for (RangeItem item : binding.getTrue()) {
            trueValues_.add(RangeFactory.createRange(item, binding.getInput().getType()));
            //as we are defining true values we have to define to check true value
            tempTestTrue = true;
        }

        if (trueValues_.isEmpty() && falseValues_.isEmpty()) {
            //in case nothing is define we consider that 0 is false and other true
            final RangeItem item = new RangeItem();
            item.setFrom("0");
            item.setTo("0");
            item.setType(RangeType.CLOSE_CLOSE);
            falseValues_.add(RangeFactory.createRange(item, binding.getInput().getType()));
            tempTestTrue = false;
        }
        testTrue_ = tempTestTrue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IData convert(IData data) {
        boolean toReturn = false;
        if (testTrue_) {
            //enforce correctly default value
            toReturn = false;
            for (RangeWrapperAbstract<? extends Comparable<?>> range : trueValues_) {
                if (range.contains(data)) {
                    //its a true value
                    toReturn = true;
                    break;
                }
            }
        } else {
            //enforce correctly default value
            toReturn = true;
            for (RangeWrapperAbstract<? extends Comparable<?>> range : falseValues_) {
                if (range.contains(data)) {
                    //is a false value
                    toReturn = false;
                    break;
                }
            }
            
        }
        return new BooleanData(data.getEntityId(), getBinding().getId(), toReturn, data.getTimestamp());
    }

}
