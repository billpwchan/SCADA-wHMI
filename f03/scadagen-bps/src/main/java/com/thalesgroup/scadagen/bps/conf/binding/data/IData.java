package com.thalesgroup.scadagen.bps.conf.binding.data;

import java.util.Date;

/**
 * Interface to get the value of the data
 */
public interface IData {

    /**
     * get the entity identifier linked to this data.
     * This is the hypervisor identifier.
     * @return the entity identifier
     */
    String getEntityId();
    
    /**
     * get the input name. This allows to find which hypervisor attributes shall
     * be updated upon this data update.
     * @return the input name
     */
    String getInputName();
    
    /**
     * get the data value as a boolean
     * @return the boolean value of the data
     */
    boolean getBooleanValue();
    
    /**
     * get the data value as an integer
     * @return the integer value of the data
     */
    int getIntValue();
    
    /**
     * get the data value as a long
     * @return the long value of the data
     */
    long getLongValue();
    
    /**
     * get the data value as a float
     * @return the float value of the data
     */
    float getFloatValue();
    
    /**
     * get the data value as a double
     * @return the double value of the data
     */
    double getDoubleValue();

    /**
     * get the data value as a string
     * @return the string value of the data
     */
    String getStringValue();

    /**
     * get the data value as a date
     * @return the date value of the data
     */
    public Date getDateValue();
    
    /**
     * get the timestamp of the data
     * @return the timestamp of the data
     */
    public long getTimestamp();
    
}
