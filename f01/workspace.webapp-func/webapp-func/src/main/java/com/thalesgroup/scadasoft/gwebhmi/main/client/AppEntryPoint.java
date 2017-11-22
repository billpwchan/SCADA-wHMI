
package com.thalesgroup.scadasoft.gwebhmi.main.client;

import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.ClientLogger;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.entry.base.MwtEntryPointApp;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.entry.event.AppContextReadyEvent;
import com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.layout.CoccAppPanel;

/**
 * Hypervisor showcase's entry point : everything starts here !
 */
public class AppEntryPoint extends MwtEntryPointApp {
	
	/** logger */
    private static final ClientLogger LOGGER = ClientLogger.getClientLogger();
    private static final String LOG_PREFIX = "[AppEntryPoint] ";

	/**
     * Constructor.
     */
    public AppEntryPoint() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onContextReadyEventAfter(final AppContextReadyEvent event) {
    	
    	LOGGER.debug(LOG_PREFIX+" onContextReadyEventAfter...");
    	
    	final CoccAppPanel appPanel = new CoccAppPanel();
        RootLayoutPanel.get().add(appPanel);
    }
}
