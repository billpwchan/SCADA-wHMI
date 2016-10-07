package com.thalesgroup.scadagen.bps.conf.binding.builder.unit;

import com.thalesgroup.hv.data_v1.attribute.DoubleAttributeType;
import com.thalesgroup.scadagen.bps.conf.binding.data.IData;

/**
 * builder for the double attributes
 */
public class DoubleAttributeBuilderUnit extends AttributeBuilderUnitAbstract<DoubleAttributeType> {

    /**
     * Default constructor
     */
    public DoubleAttributeBuilderUnit() {
        // empty
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void fillValue(DoubleAttributeType attribute, IData data) {
        attribute.setValue(data.getDoubleValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<DoubleAttributeType> managedAttribute() {
        return DoubleAttributeType.class;
    }
}
