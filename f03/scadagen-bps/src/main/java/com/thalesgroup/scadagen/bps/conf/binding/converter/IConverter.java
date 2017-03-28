package com.thalesgroup.scadagen.bps.conf.binding.converter;

import com.thalesgroup.scadagen.bps.conf.binding.data.IData;

/**
 * interface used to manage the conversion for a data.
 * It is with this interface that we manage cast.<br />
 * <b>Be careful :</b> A converter shall define a constructor with the supported attribute binding parameter
 * 
 */
public interface IConverter {

    /**
     * convert the data to another type
     * @param data the data to convert
     * @return the converted data
     */
    IData convert(final IData data);

}
