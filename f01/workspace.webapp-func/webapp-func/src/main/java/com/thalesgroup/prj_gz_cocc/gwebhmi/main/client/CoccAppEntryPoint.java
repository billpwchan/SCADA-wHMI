
package com.thalesgroup.prj_gz_cocc.gwebhmi.main.client;

import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.entry.base.MwtEntryPointApp;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.entry.event.AppContextReadyEvent;
import com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.layout.CoccAppPanel;

/**
 * Hypervisor showcase's entry point : everything starts here !
 */
public class CoccAppEntryPoint extends MwtEntryPointApp {

	/**
     * Constructor.
     */
    public CoccAppEntryPoint() {
        super();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void onContextReadyEventAfter(final AppContextReadyEvent event) {
        // Create layout
        final CoccAppPanel appPanel = new CoccAppPanel();
        RootLayoutPanel.get().add(appPanel);
    }

}
