package com.thalesgroup.scadagen.bps.conf.binding.converter.unit;

import com.thalesgroup.scadagen.binding.LinearBinding;
import com.thalesgroup.scadagen.bps.conf.binding.converter.ConverterAbstract;
import com.thalesgroup.scadagen.bps.conf.binding.data.DoubleData;
import com.thalesgroup.scadagen.bps.conf.binding.data.FloatData;
import com.thalesgroup.scadagen.bps.conf.binding.data.IData;
import com.thalesgroup.scadagen.bps.conf.binding.data.IntegerData;
import com.thalesgroup.scadagen.bps.conf.binding.data.LongData;

/**
 * Implementation of the linear regression converter
 */
public class LinearRegressionConverter extends ConverterAbstract<LinearBinding> {

    /**
     * Constructor
     * @param binding the boolean binding description
     */
    public LinearRegressionConverter(final LinearBinding binding) {
        super(binding);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IData convert(final IData data) {
        IData toReturn = data;
        switch (getBinding().getInput().getType()) {
            case INT:
                //integer use case
                toReturn = new IntegerData(data.getEntityId(), data.getInputName(), 
                        (int) (getBinding().getScale()) * data.getIntValue() + ((int) getBinding().getOffset()), 
                        data.getTimestamp());
                break;
            case LONG:
                //long use case
                toReturn =  new LongData(data.getEntityId(), data.getInputName(), 
                        (long) (getBinding().getScale()) * data.getLongValue() + (long) (getBinding().getOffset()), 
                        data.getTimestamp());
                break;
            case FLOAT:
                //float use case
                toReturn =  new FloatData(data.getEntityId(), data.getInputName(), 
                        (float) (getBinding().getScale()) * data.getFloatValue() + (float) (getBinding().getOffset()), 
                        data.getTimestamp());
                break;
            case DOUBLE:
                //double use case
                toReturn =  new DoubleData(data.getEntityId(), data.getInputName(), 
                        getBinding().getScale() * data.getDoubleValue() + getBinding().getOffset(), 
                        data.getTimestamp());
                break;
            default:
                toReturn =  data;
        }
        return toReturn;
    }

}
