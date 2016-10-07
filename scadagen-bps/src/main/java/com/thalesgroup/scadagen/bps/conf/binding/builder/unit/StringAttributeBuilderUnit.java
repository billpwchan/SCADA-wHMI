package com.thalesgroup.scadagen.bps.conf.binding.builder.unit;

import com.thalesgroup.hv.data_v1.attribute.StringAttributeType;
import com.thalesgroup.scadagen.bps.conf.binding.data.IData;

/**
 * builder for string attributes
 */
public class StringAttributeBuilderUnit extends AttributeBuilderUnitAbstract<StringAttributeType> {

    /**
     * Default constructor
     */
    public StringAttributeBuilderUnit() {
        //empty
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void fillValue(StringAttributeType attribute, IData data) {
        attribute.setValue(data.getStringValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<StringAttributeType> managedAttribute() {
        return StringAttributeType.class;
    }
}
