package com.thalesgroup.scadagen.bps.conf.binding.converter;

import com.thalesgroup.scadagen.binding.AttributeBinding;

/**
 * abstract class used to manage the conversion for a data.
 * If user start with this abstract class they should not forget the constructor
 * @param <T> the type of the supported attribute binding
 */
public abstract class ConverterAbstract<T extends AttributeBinding> implements IConverter {

    /** stored binding */
    private final T binding_;

    /**
     * Constructor
     * @param binding binding to support
     */
    protected ConverterAbstract(T binding) {
        binding_ = binding;
    }
    
    /**
     * get the binding
     * @return the binding
     */
    protected T getBinding() {
        return binding_;
    }

}
