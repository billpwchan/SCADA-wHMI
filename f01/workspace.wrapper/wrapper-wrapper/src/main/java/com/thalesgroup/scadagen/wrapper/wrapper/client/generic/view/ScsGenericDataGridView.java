package com.thalesgroup.scadagen.wrapper.wrapper.client.generic.view;

import java.util.Iterator;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.ClientLogger;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.datagrid.conf.GDGClientConfiguration;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.datagrid.view.GenericDataGridView;

/**
 * Generic Data Grid view.
 */
public class ScsGenericDataGridView extends GenericDataGridView {

	/** logger */
    private static final ClientLogger LOGGER = ClientLogger.getClientLogger();
    private static final String LOG_PREFIX = "[ScsGenericDataGridView] ";
    
	public void init(final GDGClientConfiguration clientConfiguration)  {
		super.init(clientConfiguration);
		if (!clientConfiguration.isDisplayPager()) {
			LOGGER.debug(LOG_PREFIX+ "hide pager");
			if (this.getWidget() instanceof SimpleLayoutPanel) {
				SimpleLayoutPanel w = (SimpleLayoutPanel) this.getWidget();
				if (w.getWidget() instanceof DockLayoutPanel) {
					DockLayoutPanel dl = (DockLayoutPanel) w.getWidget();
				
					Iterator<Widget> it = dl.iterator();
					while (it.hasNext()) {
						if (it.next() instanceof HTMLPanel) {
							it.remove();
							break;
						}
					}
					
				}
			}
		}
	}

}
