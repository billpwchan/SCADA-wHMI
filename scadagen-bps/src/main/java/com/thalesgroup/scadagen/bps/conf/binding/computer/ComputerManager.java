package com.thalesgroup.scadagen.bps.conf.binding.computer;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thalesgroup.hv.data.tools.helper.IDataHelper;
import com.thalesgroup.hv.data_v1.entity.AbstractEntityStatusesType;
import com.thalesgroup.scadagen.binding.MultiInputBinding;
import com.thalesgroup.scadagen.bps.conf.binding.data.IData;

/**
 * Manage the storage and call of computers
 */
public class ComputerManager {

    /** logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(ComputerManager.class); 
    
    /** index of computers */
    private final Map<MultiInputBinding, IComputer> computers_ = 
            new HashMap<MultiInputBinding, IComputer>();
    
    /**
     * Default constructor
     */
    public ComputerManager() {
        //empty
    }
    
    /**
     * add a computer in the index
     * @param binding binding to analyse to get the computer
     */
    @SuppressWarnings("unchecked")
    public void add(final MultiInputBinding binding) {
        try {
            computers_.put(binding, ((Class<IComputer>) IComputer.class.getClassLoader().
                        loadClass(binding.getComputer())).newInstance());
        } catch (Exception e) {
            LOGGER.warn("Unable to instanciate [{}], default constructor not available", binding.getComputer());
        }
    }
    
    /**
     * compute the data using the matching computer
     * @param binding the binding to use
     * @param data the data to merge
     * @return the merged data (could be null if not result wanted)
     */
    public IData compute(final IDataHelper dataHelper, final AbstractEntityStatusesType entity, final MultiInputBinding binding, final Map<String, IData> data) {
        final IComputer computer = computers_.get(binding);
        return (computer != null) ? computer.compute(dataHelper, entity, binding, data) : null;
    }
}
