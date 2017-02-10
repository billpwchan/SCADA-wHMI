package com.thalesgroup.scadagen.wrapper.wrapper.client.generic.view;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.gwt.user.cellview.client.AbstractPager;
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
    
    private int pageSize = 0;
    private int fastForwardRows = 0;

    /**
     * {@inheritDoc}
     */
    @Override
	public void init(final GDGClientConfiguration clientConfiguration)  {
		super.init(clientConfiguration);
		
    	if ( null != clientConfiguration ) {
    		pageSize = clientConfiguration.getPageSize();
    		fastForwardRows = clientConfiguration.getFastForwardRows();
    	}
		
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
		
		if ( hasSCADAgenPager ) {
			
			LOGGER.debug(LOG_PREFIX+ "isDisplayPager IS TRUE");
			
			pager = new SCADAgenPager();
			
			if ( null != createText ) pager.setCreateText(createText);
			if ( null != buttonOperation ) pager.setButtonOperation(buttonOperation);
			
			LOGGER.debug(LOG_PREFIX + "pageSize[" + pageSize + "]");
			pager.setPageSize(pageSize);
			
			LOGGER.debug(LOG_PREFIX + "fastForwardRows[" + fastForwardRows + "]");
			pager.setFastForwardRows(fastForwardRows);			

			DataGrid<EntityClient> dataGrid = getInnerDataGrid();
			pager.setDisplay(dataGrid);
		}
		
	}
	
	private CreateText_i createText = null;
	public void setCreateText(CreateText_i createText) { this.createText = createText; }
	
	private ButtonOperation_i buttonOperation = null;
	public void setButtonOperation(ButtonOperation_i buttonOperation) { this.buttonOperation = buttonOperation; }
	
	private boolean hasSCADAgenPager = false;
	public boolean hasSCADAgenPager() { return hasSCADAgenPager; }
	public void setHasSCADAgenPager(boolean hasSCADAgenPager) { this.hasSCADAgenPager = hasSCADAgenPager; }
	
	private SCADAgenPager pager = null;
	public AbstractPager getPager() {
		return pager;
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
