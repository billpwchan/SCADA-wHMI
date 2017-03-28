package com.thalesgroup.scadagen.bps.conf.binding.data;

import java.util.Date;

/**
 * Implementation of IData storing a double value
 */
public class DoubleData extends DataAbstract {

    /** double value */
    private double value_;

    /**
     * Constructor
     * @param entityId entity identifier
     * @param inputName input name
     * @param value double value
     * @param timestamp the timestamp of the data
     */
    public DoubleData(final String entityId,
                       final String inputName,
                       final double value,
                       final long timestamp) {
        super(entityId, inputName, timestamp);
        value_ = value;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getBooleanValue() {
        return (value_ != 0) ? true : false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getIntValue() {
        return (int) value_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getLongValue() {
        return (long) value_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float getFloatValue() {
        return (float) value_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getDoubleValue() {
        return value_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getStringValue() {
        return Double.toString(value_);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getDateValue() {
        return new Date(getLongValue());
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "DoubleData[" + getEntityId() + "][" + getInputName() + "][" + value_ + "]";
    }

}
