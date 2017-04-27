package com.thalesgroup.scadagen.wrapper.wrapper.client.generic.presenter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.event.shared.EventBus;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.entity.EntityClient;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.data.AxisEntryInfo;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.event.MatrixAxisClickEvent;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.event.MatrixClickEvent;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.presenter.GMInitReturn;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.presenter.GenericMatrixPresenterClient;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.presenter.MatrixContext;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.update.LocationKey;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.update.MxIntersectionState;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.view.IGenericMatrixView;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.life.StateTransitionReturn;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.matrix.ScsMatrixMultipleSelectionManager;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.matrix.renderer.ScsMatrixRenderer;

public class ScsMatrixPresenterClient extends GenericMatrixPresenterClient {

	private static final String className = UIWidgetUtil.getClassSimpleName(ScsMatrixPresenterClient.class.getName());
	private static UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	
	private SelectionEvent selectionEvent = null;
	public SelectionEvent getSelectionEvent() { return selectionEvent; }
	public void setSelectionEvent(SelectionEvent selectionEvent) { this.selectionEvent = selectionEvent; }
	
	protected HashMap<String, Integer> columnIdIndexMap = new HashMap<String, Integer>();
	protected HashMap<String, Integer> rowIdIndexMap = new HashMap<String, Integer>();
	
    /**
     * Constructor
     * @param context Initialization context
     * @param view the generic matrix view instance.
     * @param eventBus the {@link EventBus}.
     */
    public ScsMatrixPresenterClient(final MatrixContext context, final IGenericMatrixView view, final EventBus eventBus) {
        super(context, view, eventBus);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void onInitialize(final StateTransitionReturn result) {
    	final String function = "onInitialize";
        super.onInitialize(result);

        if (result instanceof GMInitReturn) {
            // initialize the view from the view description
            final GMInitReturn initReturn = (GMInitReturn) result;
            
            if (initReturn.getViewData() != null) {
            	List<AxisEntryInfo> columnsInfo = initReturn.getViewData().getColumns();
            	for (int columnIndex=0; columnIndex<columnsInfo.size(); columnIndex++) {
            		columnIdIndexMap.put(columnsInfo.get(columnIndex).getId(), columnIndex);
            	}
            	
            	List<AxisEntryInfo> rowsInfo = initReturn.getViewData().getRows();
            	for (int rowIndex=0; rowIndex<rowsInfo.size(); rowIndex++) {
            		rowIdIndexMap.put(rowsInfo.get(rowIndex).getId(), rowIndex);
            	}
            }
        } else {
            logger.error(className, function, "Error while initializing view : " + result);
        }
    }
    
    @Override
    protected void sendColumnFilterEventSquare(final MatrixClickEvent event) {
    	final String function = "sendColumnFilterEventSquare";
    	logger.begin(className, function);
    	
    	super.sendColumnFilterEventSquare(event);
    	
    	if (selectionEvent != null) {
    		sendSelectionEvent();
    	}
    	logger.end(className, function);
    }

    @Override
    protected void sendColumnFilterEventAxis(final MatrixAxisClickEvent event) {
    	final String function = "sendColumnFilterEventAxis";
    	logger.begin(className, function);
    	
    	super.sendColumnFilterEventAxis(event);
    	
    	if (selectionEvent != null) {
    		sendSelectionEvent();
    	}
    	logger.end(className, function);
    }
    
    protected void sendSelectionEvent() {
    	final String function = "sendSelectionEvent";
    	
    	ScsMatrixRenderer scsMxRenderer = null;
    	ScsMatrixMultipleSelectionManager selectionManager = null;
    		
		if (getView() != null && getView().getRenderer() != null && getView().getRenderer() instanceof ScsMatrixRenderer) {
			scsMxRenderer = (ScsMatrixRenderer)getView().getRenderer();
		} else {
			logger.warn(className, function, "Warning: renderer is null or renderer type not instanceof ScsMatrixRenderer");
			return;
		}

    	
    	if (getSelectionManager() != null && getSelectionManager() instanceof ScsMatrixMultipleSelectionManager) {
    		selectionManager = (ScsMatrixMultipleSelectionManager) getSelectionManager();
    	} else {
			logger.warn(className, function, "Warning: selectionManager is null or renderer type not instanceof ScsMatrixMultipleSelectionManager");
			return;
		}
    	
    	// Create selection set for selection event
    	Set<HashMap<String, String>> selectionSet = new HashSet<HashMap<String, String>>();
    	Set<LocationKey> currentSelectedLocationKeySet = selectionManager.getCurrentSelectedLocationKeySet();
    	if (currentSelectedLocationKeySet == null) {
    		logger.debug(className, function, "currentSelectionLocationKeySet is null");
    		return;
    	}
    	
    	logger.debug(className, function, "currentSelectedLocationKeySet size=[{}]", currentSelectedLocationKeySet.size());
		for (LocationKey key: currentSelectedLocationKeySet) {
			MxIntersectionState state = scsMxRenderer.getMxIntersectionState(key);
			logger.debug(className, function, "getMxIntersectionState with location [{}]", key);
			if (state != null) {
				Map<String, EntityClient> map = state.getListEntities();
	    		if (map != null) {
		    		for ( EntityClient ec : map.values() ) {  				    			
		            	HashMap<String, String> details = new HashMap<String, String>();
		            	for ( String attributeName : ec.attributeNames() ) {
		             		details.put(attributeName, ec.getAttribute(attributeName).getValue().toString());
		             		logger.debug(className, function, "details add attribute[{}] value[{}]", attributeName, ec.getAttribute(attributeName).getValue().toString());
		            	}        	
		            	selectionSet.add(details);
		            	logger.debug(className, function, "selectionSet size=[{}]", selectionSet.size());
		    		}
		    	}  else {
	    			logger.debug(className, function, "Warning: getListEntities() return map is null");
	    		}
			}
		}
		
		if (selectionEvent != null) {
			if (selectionSet.size() > 0) {
				logger.debug(className, function, "send selectionEvent.onSelection(selectionSet) size=[{}]", selectionSet.size());
				selectionEvent.onSelection(selectionSet);
			} else {
				logger.debug(className, function, "send selectionEvent.onSelection(null)");
				selectionEvent.onSelection(null);
			}
		}
    }
    
    public Map<String, Integer> getColumnIdIndexMap() {
    	return columnIdIndexMap;
    }
    
    public Map<String, Integer> getRowIdIndexMap() {
    	return rowIdIndexMap;
    }
    
    public Integer getColumnIndex(String columnId) {
    	return columnIdIndexMap.get(columnId);
    }
    
    public Integer getRowIndex(String rowId) {
    	return rowIdIndexMap.get(rowId);
    }
}