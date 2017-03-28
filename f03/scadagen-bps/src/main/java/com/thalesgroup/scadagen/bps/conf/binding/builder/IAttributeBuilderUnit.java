package com.thalesgroup.scadagen.bps.conf.binding.builder;

import com.thalesgroup.hv.data_v1.attribute.AbstractAttributeType;
import com.thalesgroup.scadagen.bps.conf.binding.data.IData;

/**
 * interface for the attribute builder
 * @param <T> supported attribute type
 */
public interface IAttributeBuilderUnit<T extends AbstractAttributeType> {

    /**
    * read the data and fill it in an attribute
    * @param attribute the attribute to fill
    * @param data data to read
    */
    void fill(final AbstractAttributeType attribute, final IData data);
    
    /**
     * the supported attribute type for this unit
     * @return the manage attribute type by this builder
     */
    Class<T> managedAttribute();
    
}
