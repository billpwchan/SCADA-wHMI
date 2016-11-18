package com.thalesgroup.scadagen.bps.conf.binding.data;

import java.util.Date;

/**
 * Implementation of IData storing a boolean value
 */
public class BooleanData extends DataAbstract {

    /** boolean value */
    private boolean value_;

    /**
     * Constructor
     * @param entityId entity identifier
     * @param inputName input name
     * @param value boolean value
     * @param timestamp the timestamp of the data
     */
    public BooleanData(final String entityId,
                       final String inputName,
                       final boolean value,
                       final long timestamp) {
        super(entityId, inputName, timestamp);
        value_ = value;
    }
    
    /**
     * Constructor
     * @param entityId entity identifier
     * @param inputName input name
     * @param value boolean value
     */
    public BooleanData(final String entityId,
            final String inputName,
            final boolean value) {
        super(entityId, inputName);
        value_ = value;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getBooleanValue() {
        return value_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getIntValue() {
        return value_ ? 1 : 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getLongValue() {
        return value_ ? 1 : 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float getFloatValue() {
        return value_ ? 1 : 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getDoubleValue() {
        return value_ ? 1 : 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getStringValue() {
        return Boolean.toString(value_);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getDateValue() {
        return null;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "BooleanData[" + getEntityId() + "][" + getInputName() + "][" + value_ + "]";
    }

}
