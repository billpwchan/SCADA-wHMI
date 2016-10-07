package com.thalesgroup.scadagen.bps.conf.binding.data;

import java.util.Date;

/**
 * Implementation of IData storing a long value
 */
public class LongData extends DataAbstract {

    /** long value */
    private long value_;

    /**
     * Constructor
     * @param entityId entity identifier
     * @param inputName input name
     * @param value long value
     * @param timestamp the timestamp of the data
     */
    public LongData(final String entityId,
                       final String inputName,
                       final long value,
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
        return Long.toString(value_);
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
        return "LongData[" + getEntityId() + "][" + getInputName() + "][" + value_ + "]";
    }

}
