package com.thalesgroup.scadagen.wrapper.wrapper.client.generic.view;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.ClientLogger;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.entity.EntityClient;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.datagrid.conf.GDGClientConfiguration;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.datagrid.view.GenericDataGridView;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.util.AlarmUtils;

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
	
	@Override
	protected void addInToolBar(final IsWidget widget) {
	}
	
    public void ackPage() {
        DataGrid<EntityClient> dataGrid = getInnerDataGrid();
        List<EntityClient> visibleEntityClient = dataGrid.getVisibleItems();
        AlarmUtils.acknowledge(visibleEntityClient);
    }
    
    public void ackVisible() {
    	ackPage();
    }
    
    public void ackVisibleSelected() {
        Set<EntityClient> selectedEntities = new HashSet<EntityClient>();
        List<EntityClient> visibleItems = getInnerDataGrid().getVisibleItems();
        for (EntityClient entity : visibleItems) {
            if (getInnerDataGrid().getSelectionModel().isSelected(entity)) {
                selectedEntities.add(entity);
            }
        }
        AlarmUtils.acknowledge(selectedEntities);
    }

}
