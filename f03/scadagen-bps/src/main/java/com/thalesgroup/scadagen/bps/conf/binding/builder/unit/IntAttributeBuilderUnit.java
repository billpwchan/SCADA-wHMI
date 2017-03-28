package com.thalesgroup.scadagen.bps.conf.binding.builder.unit;

import com.thalesgroup.hv.data_v1.attribute.IntAttributeType;
import com.thalesgroup.scadagen.bps.conf.binding.data.IData;

/**
 * builder for integer attributes
 */
public class IntAttributeBuilderUnit extends AttributeBuilderUnitAbstract<IntAttributeType> {

    /**
     * Default constructor
     */
    public IntAttributeBuilderUnit() {
        // empty
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void fillValue(IntAttributeType attribute, IData data) {
        attribute.setValue(data.getIntValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<IntAttributeType> managedAttribute() {
        return IntAttributeType.class;
    }
}
