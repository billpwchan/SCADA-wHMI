package com.thalesgroup.scadagen.bps.conf.binding.data;

import java.util.Date;

/**
 * Implementation of IData storing a float value
 */
public class FloatData extends DataAbstract {

    /** float value */
    private float value_;

    /**
     * Constructor
     * @param entityId entity identifier
     * @param inputName input name
     * @param value float value
     * @param timestamp the timestamp of the data
     */
    public FloatData(final String entityId,
                       final String inputName,
                       final float value,
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
        return Float.toString(value_);
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
        return "FloatData[" + getEntityId() + "][" + getInputName() + "][" + value_ + "]";
    }

}
