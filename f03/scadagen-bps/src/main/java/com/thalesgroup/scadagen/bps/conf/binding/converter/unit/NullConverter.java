package com.thalesgroup.scadagen.bps.conf.binding.converter.unit;

import com.thalesgroup.scadagen.binding.IdentityBinding;
import com.thalesgroup.scadagen.bps.conf.binding.converter.ConverterAbstract;
import com.thalesgroup.scadagen.bps.conf.binding.data.IData;

/**
 * Null converter implementation (does nothing)
 */
public class NullConverter extends ConverterAbstract<IdentityBinding> {

    /**
     * Constructor
     * @param binding the identity binding description
     */
    public NullConverter(IdentityBinding binding) {
        super(binding);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public IData convert(IData data) {
        //return the incoming data
        return data;
    }

}
