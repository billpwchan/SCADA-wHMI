
package com.thalesgroup.scadasoft.gwebhmi.main.client;

import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.entry.base.MwtEntryPointApp;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.entry.event.AppContextReadyEvent;
import com.thalesgroup.scadagen.whmi.uiroot.uiroot.client.UIGws;

/**
 * Hypervisor showcase's entry point : everything starts here !
 */
public class AppEntryPoint_WHMI extends MwtEntryPointApp {

	/**
     * Constructor.
     */
    public AppEntryPoint_WHMI() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onContextReadyEventAfter(final AppContextReadyEvent event) {

		UIGws uiGws = new UIGws();
		RootLayoutPanel.get().add(uiGws.getMainPanel());

    }
    
}
