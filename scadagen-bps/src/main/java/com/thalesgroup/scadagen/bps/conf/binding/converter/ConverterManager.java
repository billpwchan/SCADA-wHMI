package com.thalesgroup.scadagen.bps.conf.binding.converter;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thalesgroup.scadagen.binding.AttributeBinding;
import com.thalesgroup.scadagen.bps.conf.binding.converter.unit.BooleanConverter;
import com.thalesgroup.scadagen.bps.conf.binding.converter.unit.BooleanToIntegerConverter;
import com.thalesgroup.scadagen.bps.conf.binding.converter.unit.IntEnumerationConverter;
import com.thalesgroup.scadagen.bps.conf.binding.converter.unit.LinearRegressionConverter;
import com.thalesgroup.scadagen.bps.conf.binding.converter.unit.MappedVarConverter;
import com.thalesgroup.scadagen.bps.conf.binding.converter.unit.NullConverter;
import com.thalesgroup.scadagen.bps.conf.binding.converter.unit.StringEnumerationConverter;
import com.thalesgroup.scadagen.bps.conf.binding.converter.unit.StringToIntegerConverter;
import com.thalesgroup.scadagen.bps.conf.binding.data.IData;

/**
 * Manager of the converters to find the right converter for a data 
 */
public class ConverterManager {

    /** logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(ConverterManager.class);
    
    /** index of active converters */
    private final ConcurrentHashMap<AttributeBinding, IConverter> converters_
        = new ConcurrentHashMap<AttributeBinding, IConverter>();
    
    /** index of available converters */
    private final Map<Class<? extends AttributeBinding>, Constructor<? extends IConverter>> 
    constructors_ = new HashMap<Class<? extends AttributeBinding>, Constructor<? extends IConverter>>();
    
    /**
     * Default constructor.
     * Initialize the basic converters
     */
    public ConverterManager() {
        //emtpy
        initIndex();
    }
    
    /**
     * Constructor
     * @param converters extra converters for customization.
     */
    public ConverterManager(Collection<Class<? extends IConverter>> converters) {
        //emtpy
        initIndex();
        for (Class<? extends IConverter> converter : converters) {
            addConverter(converter);
        }
    }
    
    /**
     * initialize the default converters
     */
    private void initIndex() {
        addConverter(NullConverter.class);
        addConverter(BooleanConverter.class);
        addConverter(IntEnumerationConverter.class);
        addConverter(StringEnumerationConverter.class);
        addConverter(LinearRegressionConverter.class);
        addConverter(BooleanToIntegerConverter.class);
        addConverter(StringToIntegerConverter.class);
        addConverter(MappedVarConverter.class);
    }
    
    /**
     * add a converter in the list of available ones
     * @param clazz the class of the converter
     */
    @SuppressWarnings("unchecked")
    private void addConverter(Class<? extends IConverter> clazz) {
        final Constructor<? extends IConverter>[] constructors = 
                (Constructor<? extends IConverter>[]) clazz.getConstructors();
        final Constructor<? extends IConverter> constructor = constructors[0];
        constructors_.put((Class<? extends AttributeBinding>) constructor.getParameterTypes()[0], constructor);
    }

    /**
     * add an instance of converter based on the attribute binding description
     * @param binding attribute binding description
     */
    @SuppressWarnings("unchecked")
    public void add(final AttributeBinding binding) {
        try {
            //find the correct converter based on binding class
            Constructor<? extends IConverter> constructor = null;
            Class<? extends AttributeBinding> temp = binding.getClass();
            do {
                constructor = constructors_.get(temp);
                final Class<?> superClass = temp.getSuperclass();
                if (AttributeBinding.class.isAssignableFrom(superClass)) {
                    temp = (Class<? extends AttributeBinding>) superClass;
                } else {
                    break;
                }
            } while(constructor == null);
            if (constructor != null) {
                converters_.put(binding, constructor.newInstance(binding));
            }
        } catch (Exception e) {
            LOGGER.error("Unable to find a suitable converter for attribute binding type [" + binding.getClass() + "]", e);
        }
    }
    
    /**
     * convert the data using the suitable converter
     * @param binding the binding description linked to the data
     * @param data the data to convert
     * @return the converted data
     */
    public IData convert(final AttributeBinding binding, IData data) {
        return converters_.get(binding).convert(data);
    }
}
