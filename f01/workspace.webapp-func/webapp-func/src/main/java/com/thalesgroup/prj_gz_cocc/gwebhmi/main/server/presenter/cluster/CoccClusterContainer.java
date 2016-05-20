package com.thalesgroup.prj_gz_cocc.gwebhmi.main.server.presenter.cluster;

import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.situation.enums.ClusterGridMode;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.server.situation.module.clusterc.ClusterContainer;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.server.situation.module.generator.IClusterIdGenerator;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.server.situation.module.grid.IBaseGrid;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.server.situation.options.ClusteringOptionsRT;

/**
 * Specific cluster container for the COCC project
 */
public class CoccClusterContainer extends ClusterContainer {

    /**
     * Constructor
     * @param options cluster options
     * @param clusterIdGenerator generator for cluster identifiers
     */
    public CoccClusterContainer(ClusteringOptionsRT options, IClusterIdGenerator clusterIdGenerator) {
        super(options, clusterIdGenerator);
        //nothing special to do
    }
    
    /**
     * {@inheritDoc}
     * We are overriding this method to add our custom behavior for the 
     * cluster viewport use case in order to add the custom counter
     * based on the alarm synthesis
     */
    @Override
    protected IBaseGrid createNewGrid(ClusterGridMode clusterGridMode) {
        IBaseGrid gridNew = null;

        if (ClusterGridMode.CLUSTER_VIEWPORT.equals(clusterGridMode))
        {
            //using the cocc cluster grid
          gridNew = new CoccClusterGridVP(getOptions(), createGridOptions(getBoundsViewPort()), getClusterIdGenerator());
        } else {
            //fallback to the default behavior
            gridNew = super.createNewGrid(clusterGridMode);
        }
        return gridNew;
    }

}
