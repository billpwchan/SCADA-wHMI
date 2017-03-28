package com.thalesgroup.scadagen.bps.conf.binding.builder.unit;

import com.thalesgroup.hv.data_v1.attribute.LongAttributeType;
import com.thalesgroup.scadagen.bps.conf.binding.data.IData;

/**
 * builder for long attributes
 */
public class LongAttributeBuilderUnit extends AttributeBuilderUnitAbstract<LongAttributeType> {

    /**
     * Default constructor
     */
    public LongAttributeBuilderUnit() {
        // empty
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void fillValue(LongAttributeType attribute, IData data) {
        attribute.setValue(data.getIntValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<LongAttributeType> managedAttribute() {
        return LongAttributeType.class;
    }
}
