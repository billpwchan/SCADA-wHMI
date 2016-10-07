package com.thalesgroup.scadagen.bps.conf.binding.computer;

import java.util.Map;

import com.thalesgroup.hv.data.tools.helper.IDataHelper;
import com.thalesgroup.hv.data_v1.entity.AbstractEntityStatusesType;
import com.thalesgroup.scadagen.binding.AttributeBinding;
import com.thalesgroup.scadagen.bps.conf.binding.data.IData;

/**
 * interface for the input computers
 */
public interface IComputer {

    /**
     * compute one input from others
     * @param data input to read
     * @return the computed input
     */
    IData compute(IDataHelper dataHelper, AbstractEntityStatusesType entity, AttributeBinding binding, Map<String, IData> data);
}
