package com.thalesgroup.prj_gz_cocc.gwebhmi.main.server.presenter.cluster;

import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.server.situation.module.generator.IClusterIdGenerator;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.server.situation.module.grid.ClusterGridVP;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.server.situation.module.synthcpt.ISynthComputer;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.server.situation.options.ClusteringOptionsRT;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.server.situation.options.GridOptionsRT;

/**
 * Coc cluster grid ViewPort to use our synthetic computer
 */
public class CoccClusterGridVP extends ClusterGridVP {

    /** the Cocc Synthetic computer to use in order to have the counter of failed equipements */
    private final CoccSynthComputer coccSynthComp_ = new CoccSynthComputer();
    
    /**
     * Constructor
     * @param options clustering options
     * @param gridOptions grid options
     * @param clusterIdGenerator cluster identifier generator
     */
    public CoccClusterGridVP(ClusteringOptionsRT options, GridOptionsRT gridOptions, IClusterIdGenerator clusterIdGenerator) {
        super(options, gridOptions, clusterIdGenerator);
        //nothing special to do here
    }
    
    /**
     * {@inheritDoc}.
     * Replace the call to get the synthetic computer in order to activate it in the runtime.
     */
    @Override
    protected ISynthComputer getSynthComputer() {
        return coccSynthComp_;
    }

}
