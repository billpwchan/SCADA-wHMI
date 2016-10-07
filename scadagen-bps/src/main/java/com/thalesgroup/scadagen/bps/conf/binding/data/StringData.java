package com.thalesgroup.scadagen.bps.conf.binding.data;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

/**
 * Implementation of IData storing a string value
 */
public class StringData extends DataAbstract {

    /** string value */
    private String value_;

    /**
     * Constructor
     * @param entityId entity identifier
     * @param inputName input name
     * @param value string value
     * @param timestamp the timestamp of the data
     */
    public StringData(final String entityId,
                       final String inputName,
                       final String value,
                       final long timestamp) {
        super(entityId, inputName, timestamp);
        value_ = value;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getBooleanValue() {
        boolean toReturn = false;
        if ("true".equalsIgnoreCase(value_) || "1".equals(value_) || "on".equalsIgnoreCase(value_)) {
            toReturn = true;
        }
        return toReturn;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getIntValue() {
        try {
            return Integer.parseInt(value_);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getLongValue() {
        try {
            return Long.parseLong(value_);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float getFloatValue() {
        try {
            return Float.parseFloat(value_);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getDoubleValue() {
        try {
            return Double.parseDouble(value_);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getStringValue() {
        return value_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getDateValue() {
        try {
            return DateFormat.getDateInstance().parse(value_);
        } catch (ParseException e) {
            return new Date();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "StringData[" + getEntityId() + "][" + getInputName() + "][" + value_ + "]";
    }
    
}
