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
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.entity.EntityClient;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.datagrid.conf.GDGClientConfiguration;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.datagrid.view.GenericDataGridView;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.util.AlarmUtils;

/**
 * Generic Data Grid view.
 */
public class ScsGenericDataGridView extends GenericDataGridView {

	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
    
    private int pageSize = 0;
    private int fastForwardRows = 0;

    /**
     * {@inheritDoc}
     */
    @Override
	public void init(final GDGClientConfiguration clientConfiguration)  {
		super.init(clientConfiguration);
		String f = "init";
		
    	if ( null != clientConfiguration ) {
    		pageSize = clientConfiguration.getPageSize();
    		fastForwardRows = clientConfiguration.getFastForwardRows();
    	}
		
		if (!clientConfiguration.isDisplayPager()) {
			logger.debug(f, "hide pager");
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
			logger.debug(f, "isDisplayPager IS TRUE");
			setPager(createSCADAgenPager(createText, buttonOperation, pageSize, fastForwardRows));
		}
	}
    
    public AbstractPager createSCADAgenPager(CreateText_i createText, ButtonOperation_i buttonOperation, int pageSize, int fastForwardRows) {
    	String f = "createSCADAgenPager";
    	logger.begin(f);
    	
    	SCADAgenPager pager = new SCADAgenPager();
		
		if ( null != createText ) pager.setCreateText(createText);
		if ( null != buttonOperation ) pager.setButtonOperation(buttonOperation);
		
		logger.debug(f, "pageSize[{}]", pageSize);
		pager.setPageSize(pageSize);
		
		logger.debug(f, "fastForwardRows[{}]", fastForwardRows);
		pager.setFastForwardRows(fastForwardRows);
		pager.setFastBackwardRows(fastForwardRows);	
		
		logger.end(f);
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
    	String f = "ackPage";
    	logger.debug(f);
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
    	String f = "ackVisibleSelected";
    	logger.debug(f, "ackVisibleSelected");
        ackVisibleSelected(null, 0, 0);
    }
    public void ackVisibleSelected(String attributeName, int start, int end) {
    	String f = "ackVisibleSelected";
    	logger.debug(f, "ackVisibleSelected");
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
		String f = "AlarmAcknowledgeWithFilter";
		Set<EntityClient> validEntities = new HashSet<EntityClient>();
		if ( null != attributeName  ) {
			logger.debug(f, "attributeName IS NOT NULL");
			for ( EntityClient ec : entities ) {
				int outValue = 0;
				Object obj1 = ec.getAttribute(attributeName).getValue();
				if (obj1 != null && obj1 instanceof Integer) {
					outValue = (Integer) obj1;
				} else {
					logger.debug(f, "obj1 IS NULL");
				}
				
				boolean isInRange = outValue >= start && outValue <= end;
				logger.debug(f, "start[{}] >= outValue[{}] <= end[{}] : isInRange[{}]", new Object[]{start, outValue, end, isInRange});
				if ( isInRange ) {
					validEntities.add(ec);
				} else {
					logger.debug(f, "Checking is FALSE");
				}
			}
			AlarmUtils.acknowledge(validEntities);
		} else {
			logger.debug(f, "attributeName IS NULL");
			AlarmUtils.acknowledge(entities);
		}
    }

}
