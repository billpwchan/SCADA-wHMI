package com.thalesgroup.scadagen.bps.conf.binding.converter.unit;

import com.thalesgroup.scadagen.binding.MappedVarBinding;
import com.thalesgroup.scadagen.bps.conf.binding.converter.ConverterAbstract;
import com.thalesgroup.scadagen.bps.conf.binding.data.IData;

/**
 * MappedVarConverter implementation (Do nothing)
 */
public class MappedVarConverter extends ConverterAbstract<MappedVarBinding> {

	/**
     * Constructor
     * @param binding the MappedVarBinding description
     */
	public MappedVarConverter(final MappedVarBinding binding) {
		super(binding);
	}

	/**
     * {@inheritDoc}
     */
	@Override
	public IData convert(IData data) {
		// TODO Auto-generated method stub
		return data;
	}

}
