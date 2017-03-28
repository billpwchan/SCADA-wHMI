package com.thalesgroup.scadagen.bps.conf.binding.builder.unit;

import com.thalesgroup.hv.data_v1.attribute.FloatAttributeType;
import com.thalesgroup.scadagen.bps.conf.binding.data.IData;

/**
 * builder for the float attributes
 */
public class FloatAttributeBuilderUnit extends AttributeBuilderUnitAbstract<FloatAttributeType> {

    /**
     * Default constructor
     */
    public FloatAttributeBuilderUnit() {
        // empty
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void fillValue(FloatAttributeType attribute, IData data) {
        attribute.setValue(data.getFloatValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<FloatAttributeType> managedAttribute() {
        return FloatAttributeType.class;
    }
}
