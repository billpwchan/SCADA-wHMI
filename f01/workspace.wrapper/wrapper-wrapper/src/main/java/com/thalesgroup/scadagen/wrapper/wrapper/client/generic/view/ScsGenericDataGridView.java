package com.thalesgroup.scadagen.wrapper.wrapper.client.generic.view;

import java.util.Collection;
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
			
			setPager(createSCADAgenPager(createText, buttonOperation, pageSize, fastForwardRows));
		}
		
	}
    
    public AbstractPager createSCADAgenPager(CreateText_i createText, ButtonOperation_i buttonOperation, int pageSize, int fastForwardRows) {
    	LOGGER.debug(LOG_PREFIX+ "createSCADAgenPager Begin");
    	
    	SCADAgenPager pager = new SCADAgenPager();
		
		if ( null != createText ) pager.setCreateText(createText);
		if ( null != buttonOperation ) pager.setButtonOperation(buttonOperation);
		
		LOGGER.debug(LOG_PREFIX + "createSCADAgenPager pageSize[" + pageSize + "]");
		pager.setPageSize(pageSize);
		
		LOGGER.debug(LOG_PREFIX + "createSCADAgenPager fastForwardRows[" + fastForwardRows + "]");
		pager.setFastForwardRows(fastForwardRows);
		pager.setFastBackwardRows(fastForwardRows);	
		
		LOGGER.debug(LOG_PREFIX+ "createSCADAgenPager End");
		return pager;
    }
	
	private CreateText_i createText = null;
	public void setCreateText(CreateText_i createText) { this.createText = createText; }
	public CreateText_i getCreateText() { return createText; }
	
	private ButtonOperation_i buttonOperation = null;
	public void setButtonOperation(ButtonOperation_i buttonOperation) { this.buttonOperation = buttonOperation; }
	public ButtonOperation_i getButtonOperation() { return buttonOperation; }
	
	private boolean hasSCADAgenPager = false;
	public boolean hasSCADAgenPager() { return hasSCADAgenPager; }
	public void setHasSCADAgenPager(boolean hasSCADAgenPager) { this.hasSCADAgenPager = hasSCADAgenPager; }
	
	private AbstractPager pager = null;
	public AbstractPager getPager() { return pager; }
	public void setPager(AbstractPager pager) {
		this.pager = pager;
		this.pager.setDisplay(getInnerDataGrid());
	}
	
	@Override
	protected void addInToolBar(final IsWidget widget) {
	}
	
    public void ackPage() {
    	ackPage(null, 0, 0);
    }
	
    public void ackPage(String attributeName, int start, int end) {
    	LOGGER.debug(LOG_PREFIX+ "ackPage");
        DataGrid<EntityClient> dataGrid = getInnerDataGrid();
        List<EntityClient> visibleEntityClient = dataGrid.getVisibleItems();
        AlarmAcknowledgeWithFilter(visibleEntityClient, attributeName, start, end);
    }
    
    public void ackVisible() {
    	ackVisible(null, 0, 0);
    }
    
    public void ackVisible(String attributeName, int start, int end) {
    	ackPage(attributeName, start, end);
    }
    
    public void ackVisibleSelected() {
    	LOGGER.debug(LOG_PREFIX+ "ackVisibleSelected");
        ackVisibleSelected(null, 0, 0);
    }
    public void ackVisibleSelected(String attributeName, int start, int end) {
    	LOGGER.debug(LOG_PREFIX+ "ackVisibleSelected");
        Set<EntityClient> selectedEntities = new HashSet<EntityClient>();
        List<EntityClient> visibleItems = getInnerDataGrid().getVisibleItems();
        for (EntityClient entity : visibleItems) {
            if (getInnerDataGrid().getSelectionModel().isSelected(entity)) {
                selectedEntities.add(entity);
            }
        }
        AlarmAcknowledgeWithFilter(selectedEntities, attributeName, start, end);
    }
	private void AlarmAcknowledgeWithFilter(Collection<EntityClient> entities, String attributeName, int start, int end) {
		Set<EntityClient> validEntities = new HashSet<EntityClient>();
		if ( null != attributeName  ) {
			LOGGER.debug(LOG_PREFIX+ "AlarmAcknowledgeWithFilter attributeName IS NOT NULL");
			for ( EntityClient ec : entities ) {
				int outValue = 0;
				Object obj1 = ec.getAttribute(attributeName).getValue();
				if (obj1 != null && obj1 instanceof Integer) {
					outValue = (Integer) obj1;
				} else {
					LOGGER.debug(LOG_PREFIX+ "AlarmAcknowledgeWithFilter obj1 IS NULL");
				}
				
				boolean isInRange = outValue >= start && outValue <= end;
				LOGGER.debug(LOG_PREFIX+ "AlarmAcknowledgeWithFilter start["+start+"] >= outValue["+outValue+"] <= end["+end+"] : isInRange["+isInRange+"]");
				if ( isInRange ) {
					validEntities.add(ec);
				} else {
					LOGGER.debug(LOG_PREFIX+ "AlarmAcknowledgeWithFilter Checking is FALSE");
				}
			}
			AlarmUtils.acknowledge(validEntities);
		} else {
			LOGGER.debug(LOG_PREFIX+ "AlarmAcknowledgeWithFilter attributeName IS NULL");
			AlarmUtils.acknowledge(entities);
		}
    }

}
