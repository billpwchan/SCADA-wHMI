package com.thalesgroup.scadagen.bps.conf.binding.data;

import com.thalesgroup.hv.common.tools.DateUtils;

/**
 * simplify user work by implementing some classical methods
 */
public abstract class DataAbstract implements IData {

    /** entity identifier */
    private String entityId_;
    
    /** name of the input */
    private String inputName_;
    
    /**
     * timestamp
     */
    private final long timestamp_;
    
    /**
     * Constructor
     * @param entityId entity identifier
     * @param inputName input name
     */
    public DataAbstract(final String entityId,
            final String inputName) {
        this(entityId, inputName, DateUtils.getCurrentTimestamp());
    }
    
    /**
     * Constructor
     * @param entityId entity identifier
     * @param inputName input name
     * @param timestamp the timestamp of the data
     */
    public DataAbstract(final String entityId,
            final String inputName,
            final long timestamp) {
        entityId_ = entityId;
        inputName_ = inputName;
        timestamp_ = timestamp;
        
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getEntityId() {
        return entityId_;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getInputName() {
        return inputName_;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public long getTimestamp() {
        return timestamp_;
    }

}
