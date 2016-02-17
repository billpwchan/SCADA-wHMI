
package com.thalesgroup.scadasoft.gwebhmi.main.client;

import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.entry.base.MwtEntryPointApp;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.entry.event.AppContextReadyEvent;
import com.thalesgroup.scadasoft.gwebhmi.main.client.layout.AppPanel;

/**
 * Hypervisor showcase's entry point : everything starts here !
 */
public class AppEntryPoint extends MwtEntryPointApp {

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
        // Create layout
        final AppPanel appPanel = new AppPanel();
        RootLayoutPanel.get().add(appPanel);
    }
}
