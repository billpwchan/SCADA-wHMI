package com.thalesgroup.scadagen.bps.conf.binding.builder;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thalesgroup.hv.common.HypervisorException;
import com.thalesgroup.hv.data.tools.helper.IDataHelper;
import com.thalesgroup.hv.data_v1.attribute.AbstractAttributeType;
import com.thalesgroup.hv.data_v1.entity.AbstractEntityStatusesType;
import com.thalesgroup.scadagen.bps.conf.binding.builder.unit.BooleanAttributeBuilderUnit;
import com.thalesgroup.scadagen.bps.conf.binding.builder.unit.DateAttributeBuilderUnit;
import com.thalesgroup.scadagen.bps.conf.binding.builder.unit.DoubleAttributeBuilderUnit;
import com.thalesgroup.scadagen.bps.conf.binding.builder.unit.FloatAttributeBuilderUnit;
import com.thalesgroup.scadagen.bps.conf.binding.builder.unit.IntAttributeBuilderUnit;
import com.thalesgroup.scadagen.bps.conf.binding.builder.unit.LongAttributeBuilderUnit;
import com.thalesgroup.scadagen.bps.conf.binding.builder.unit.StringAttributeBuilderUnit;
import com.thalesgroup.scadagen.bps.conf.binding.data.IData;

/**
 * Build ann hypervisor attribute from an hypervisor description and data
 */
public class AttributeBuilder {

    /** logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(AttributeBuilder.class);
    
    /** data helper to manipulate hypervisor model */
    private final IDataHelper dataHelper_;

    /** index to know which builder to use regarding the attribute */
    private final Map<Class<? extends AbstractAttributeType>, IAttributeBuilderUnit<? extends AbstractAttributeType>> 
    index_ = new HashMap<Class<? extends AbstractAttributeType>, IAttributeBuilderUnit<? extends AbstractAttributeType>>();
    
    /**
     * Constructor with default attribute builder units
     * @param dataHelper data helper to manipulate hypervisor model
     */
    public AttributeBuilder(final IDataHelper dataHelper) {
        dataHelper_ = dataHelper;
        initIndex();
    }
    
    /**
     * Constructor with default attribute builder units
     * @param dataHelper data helper to manipulate hypervisor model
     * @param extraUnits extra attribute builder to manage customization (can also override default behavior)
     */
    public AttributeBuilder(final IDataHelper dataHelper, 
        Collection<Class<? extends IAttributeBuilderUnit<? extends AbstractAttributeType>>> extraUnits) {
        this(dataHelper);
        for (Class<? extends IAttributeBuilderUnit<? extends AbstractAttributeType>> extraUnit : extraUnits) {
            addUnit(extraUnit);
        }
    }
    
    /**
     * default unit index
     */
    private void initIndex() {
        addUnit(BooleanAttributeBuilderUnit.class);
        addUnit(DateAttributeBuilderUnit.class);
        addUnit(DoubleAttributeBuilderUnit.class);
        addUnit(FloatAttributeBuilderUnit.class);
        addUnit(IntAttributeBuilderUnit.class);
        addUnit(LongAttributeBuilderUnit.class);
        addUnit(StringAttributeBuilderUnit.class);
    }
    
    /**
     * add a unit in the index list
     * @param unitClass unit class to add
     */
    private void addUnit(Class<? extends IAttributeBuilderUnit<? extends AbstractAttributeType>> unitClass) {
        try {
            final IAttributeBuilderUnit<? extends AbstractAttributeType> instance = unitClass.newInstance();
            index_.put(instance.managedAttribute(), instance);
        } catch (Exception e) {
            LOGGER.warn("Unable to add builder unit class [{]}], default constructor not available", unitClass);
        }
    }
    
    /**
     * create an attribute from a description and data
     * @param entityType the expected entity type
     * @param attributeName the attribute name to create (automatically find the right type)
     * @param data the data to use to fill the attribute
     * @return the created attribute (could be null in case of error)
     */
    @SuppressWarnings("unchecked")
    public final AbstractAttributeType createAttribute(final Class<? extends AbstractEntityStatusesType> entityType,
                                 final String attributeName,
                                 final IData data) {
        //attribute to return
        AbstractAttributeType attribute = null;
        try {
            //find the type of the attribute
            final Class<? extends AbstractAttributeType> attributeType = (Class<? extends AbstractAttributeType>)
                dataHelper_.getMemberType(entityType, attributeName);
            //unit to use
            IAttributeBuilderUnit<? extends AbstractAttributeType> unit = null;
            //temp variable to follow inheritance
            Class<? extends AbstractAttributeType> temp = attributeType;
            do {
                //get the unit for this class
                unit = index_.get(temp);
                //check if the super class can be used (in case the unit was not found)
                final Class<?> superClass = temp.getSuperclass();
                if (AbstractAttributeType.class.isAssignableFrom(superClass)) {
                    temp = (Class<? extends AbstractAttributeType>) superClass;
                } else {
                    //not an attribute, don't try to look further, the attribute is not managed
                    break;
                }
                //exit the loop if we find a unit
            } while(unit == null);
            if (unit != null) {
                //a unit is found, create and fill the attribute
                attribute = dataHelper_.getBeanEditor().createBean(attributeType);
                LOGGER.trace("trying to fill attribute [{}] with input [{}]", attributeName, data);
                unit.fill(attribute, data);
            } else {
                LOGGER.warn("Unable to find an attribute builder unit for attribute type [{}]", attributeType);
            }
        } catch (HypervisorException e) {
            LOGGER.error(e.getMessage());
        }
        return attribute;
    }
}
