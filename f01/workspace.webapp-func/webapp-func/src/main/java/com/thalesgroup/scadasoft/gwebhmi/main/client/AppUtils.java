package com.thalesgroup.scadasoft.gwebhmi.main.client;

import com.google.gwt.event.shared.EventBus;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.event.MwtEventBus;

/**
 * This class holds constants shared by several application components.
 */
public final class AppUtils {

    /**
     * In order to prevent instanciation
     */
    private AppUtils() {
    }

    /**
     * Application event bus (waiting for MWT Event Bus mechanism)
     */
    public static final EventBus EVENT_BUS = new MwtEventBus();

    /****************************** LAYOUT ***********************************/

    /**
     * The size of SplitLayoutPanel's vertical and horizontal draggers.
     */
    public static final int SPLIT_DRAGGER_SIZE_PX = 5;
}
