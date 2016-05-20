package com.thalesgroup.prj_gz_cocc.gwebhmi.main.server.presenter.cluster;

import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.AttributeClientAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.entity.EntityClient;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.server.situation.data.grid.Cluster;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.server.situation.data.grid.Representable;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.server.situation.data.node.LayerEntryRep;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.server.situation.data.node.NodeEqpt;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.server.situation.module.synthcpt.SynthComputer;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.server.situation.symbol.SymbolDataModule;

/**
 * A specialized computer for the COCC use case
 */
public class CoccSynthComputer extends SynthComputer {

    /**
     * {@inheritDoc}
     */
    @Override
    protected void computeCluster(Cluster cluster) {
        //call first the default behavior
        super.computeCluster(cluster);
        
        //add our custom behavior
        int eqptWithDefect = 0;
        
        //loop over all the representabl in the cluster
        for (Representable wrapper : cluster.getWrappers().values())
        {
            if (wrapper instanceof LayerEntryRep) {
                final LayerEntryRep layerEntryRep = (LayerEntryRep)wrapper;
                final NodeEqpt nodeEqpt = layerEntryRep.getNodeEqpt();
                // check if there is an equipment in it.
                if(nodeEqpt != null) {
                    final EntityClient entity = nodeEqpt.getEntity();
                    //for this equipment get thre alarm synthesis information
                    final AttributeClientAbstract<Integer> alarm = entity.getAttribute("alarm");
                    if(alarm != null && alarm.getValue()>0 && alarm.isValid()) {
                        //equipment in defect are these which are with alarm value > 0
                        eqptWithDefect++;
                    }
                }
            }
        }
        //update the global counters to add the information of the equipements in defect
        cluster.getSynthesis().getCounters().get("SYNTH_GLOBAL").getCounters().put(SymbolDataModule.COUNTER_NAME_PREFIX + "EqptWithDefect", eqptWithDefect);
    }
    
//    @Override
//    protected void computeNodeAlarm(final NodeAlarm nodeAlarm, final ISynthCounters countersGLO, final ISynthCounters countersALM) {
//    	super.computeNodeAlarm(nodeAlarm, countersGLO, countersALM);
//    	
//    	//special management for counters by identifier
//        AttributeClientAbstract<String> priorityAtt = nodeAlarm.getEntity().getAttribute("priority");
//        if(priorityAtt != null) {
//            sumCounters(countersALM, priorityAtt.getValue(), 1);
//        }
//    }
}
