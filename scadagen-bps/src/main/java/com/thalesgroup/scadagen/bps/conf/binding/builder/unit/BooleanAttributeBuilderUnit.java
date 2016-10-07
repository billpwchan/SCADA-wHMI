package com.thalesgroup.scadagen.bps.conf.binding.builder.unit;

import com.thalesgroup.hv.data_v1.attribute.BooleanAttributeType;
import com.thalesgroup.scadagen.bps.conf.binding.data.IData;

/**
 * builder for boolean attributes
 */
public class BooleanAttributeBuilderUnit extends AttributeBuilderUnitAbstract<BooleanAttributeType> {

    /**
     * Default constructor
     */
    public BooleanAttributeBuilderUnit() {
        //empty
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void fillValue(BooleanAttributeType attribute, IData data) {
        attribute.setValue(data.getBooleanValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<BooleanAttributeType> managedAttribute() {
        return BooleanAttributeType.class;
    }
}
