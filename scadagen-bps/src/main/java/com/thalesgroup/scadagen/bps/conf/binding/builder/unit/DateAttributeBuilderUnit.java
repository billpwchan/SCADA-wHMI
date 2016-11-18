package com.thalesgroup.scadagen.bps.conf.binding.builder.unit;

import com.thalesgroup.hv.common.tools.DateUtils;
import com.thalesgroup.hv.data_v1.attribute.DateTimeAttributeType;
import com.thalesgroup.scadagen.bps.conf.binding.data.IData;

/**
 * builder for Date attributes
 */
public class DateAttributeBuilderUnit extends AttributeBuilderUnitAbstract<DateTimeAttributeType> {

    /**
     * Default constructor
     */
    public DateAttributeBuilderUnit() {
        //empty
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void fillValue(DateTimeAttributeType attribute, IData data) {
        attribute.setValue(DateUtils.convertToXMLGregorianCalendar(data.getDateValue()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<DateTimeAttributeType> managedAttribute() {
        return DateTimeAttributeType.class;
    }
}
