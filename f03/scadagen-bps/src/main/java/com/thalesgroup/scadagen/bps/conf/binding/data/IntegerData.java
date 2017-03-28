package com.thalesgroup.scadagen.bps.conf.binding.data;

import java.util.Date;

/**
 * Implementation of IData storing an integer value
 */
public class IntegerData extends DataAbstract {

    /** integer value */
    private int value_;

    /**
     * Constructor
     * @param entityId entity identifier
     * @param inputName input name
     * @param value integer value
     * @param timestamp the timestamp of the data
     */
    public IntegerData(final String entityId,
                       final String inputName,
                       final int value,
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
        return value_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getLongValue() {
        return value_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float getFloatValue() {
        return value_;
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
        return Integer.toString(value_);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getDateValue() {
        return new Date(value_);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "IntegerData[" + getEntityId() + "][" + getInputName() + "][" + value_ + "]";
    }

}
