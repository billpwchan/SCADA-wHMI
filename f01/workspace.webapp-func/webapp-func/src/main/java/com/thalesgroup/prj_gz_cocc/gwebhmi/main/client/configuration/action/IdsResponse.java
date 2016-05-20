package com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.configuration.action;

import java.util.Set;

import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.ret.IConfigResponse;

@SuppressWarnings("serial")
public class IdsResponse implements IConfigResponse {

    /** the data holder, a set */
    private Set<String> ids_;

    /**
     * Empty constructor for serialization
     */
    public IdsResponse() {
        //empty
    }
    /**
     * Get set of Identifiers
     * @return set of Identifiers
     */
    public Set<String> getIds() {
        return ids_;
    }

    /**
     * Set the set of Identifiers
     * @param ids set of Identifiers
     */
    public void setIds(final Set<String> ids) {
        ids_ = ids;
    }
    
}
