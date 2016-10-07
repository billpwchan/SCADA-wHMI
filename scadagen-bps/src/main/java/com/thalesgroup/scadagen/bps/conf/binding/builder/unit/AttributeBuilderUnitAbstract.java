package com.thalesgroup.scadagen.bps.conf.binding.builder.unit;

import com.thalesgroup.hv.data_v1.attribute.AbstractAttributeType;
import com.thalesgroup.scadagen.bps.conf.binding.builder.IAttributeBuilderUnit;
import com.thalesgroup.scadagen.bps.conf.binding.data.IData;

/**
 * Default class which handles the timestamp and validity
 * @param <T> type of the supported attribute
 */
public abstract class AttributeBuilderUnitAbstract<T extends AbstractAttributeType> implements IAttributeBuilderUnit<T> {

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public void fill(AbstractAttributeType attribute, IData data) {
        attribute.setTimestamp(data.getTimestamp());
        attribute.setValid(true);
        if (managedAttribute().isInstance(attribute)) {
            fillValue((T) attribute, data);
        }
    }
    
    /**
     * add the right value in the attribute
     * @param atttribute attribute to set
     * @param data data to read
     */
    protected abstract void fillValue(T atttribute, IData data);
}
