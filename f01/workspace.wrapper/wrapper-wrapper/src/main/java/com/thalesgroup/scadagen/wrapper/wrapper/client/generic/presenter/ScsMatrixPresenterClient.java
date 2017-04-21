package com.thalesgroup.scadagen.wrapper.wrapper.client.generic.presenter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.event.shared.EventBus;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.entity.EntityClient;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.data.AxisEntryInfo;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.enums.MatrixAxisType;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.event.MatrixAxisClickEvent;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.event.MatrixClickEvent;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.presenter.GMInitReturn;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.presenter.GenericMatrixPresenterClient;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.presenter.MatrixContext;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.renderer.IMatrixRenderer;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.update.MxIntersectionState;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.view.IGenericMatrixView;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.life.StateTransitionReturn;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.matrix.renderer.ScsMatrixRenderer;

public class ScsMatrixPresenterClient extends GenericMatrixPresenterClient {

	private static final String className = UIWidgetUtil.getClassSimpleName(ScsMatrixPresenterClient.class.getName());
	private static UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	
	private SelectionEvent selectionEvent = null;
	public SelectionEvent getSelectionEvent() { return selectionEvent; }
	public void setSelectionEvent(SelectionEvent selectionEvent) { this.selectionEvent = selectionEvent; }
	
	protected Set<HashMap<String, String>> selectionSet = new HashSet<HashMap<String, String>>();
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

    		if (!event.getSourceEvent().isControlKeyDown()) {
    			logger.debug(className, function, "controlKeyDown, clear selectionSet");
    			selectionSet.clear();
    		}
    		
    		Map<String, EntityClient> map = event.getSquare().getMxIntersectionState().getListEntities();
    		if (map != null) {
	    		for ( EntityClient ec : map.values() ) {
	            	HashMap<String, String> details = new HashMap<String, String>();
	            	for ( String attributeName : ec.attributeNames() ) {
	             		details.put(attributeName, ec.getAttribute(attributeName).getValue().toString());
	             		logger.debug(className, function, "details add attribute[{}] value[{}]", attributeName, ec.getAttribute(attributeName).getValue().toString());
	            	}
	            	selectionSet.add(details);
	            }
	    		logger.debug(className, function, "selectionSet size[{}]", selectionSet.size());
	    		
	    		selectionEvent.onSelection(selectionSet);
    		}		
    	}
    	logger.end(className, function);
    }

    @Override
    protected void sendColumnFilterEventAxis(final MatrixAxisClickEvent event) {
    	final String function = "sendColumnFilterEventAxis";
    	logger.begin(className, function);
    	
    	super.sendColumnFilterEventAxis(event);
    	
    	if (selectionEvent != null) {
    		if (!event.getSourceEvent().isControlKeyDown()) {
    			logger.debug(className, function, "controlKeyDown, clear selectionSet");
    			selectionSet.clear();
    		}
    		
    		MatrixAxisType mxAxisType = event.getAxisType();
    		AxisEntryInfo axisEntryInfo = event.getAxisEntryInfo();
    		String axisId = axisEntryInfo.getId();
    		
    		if (mxAxisType == MatrixAxisType.COLUMN) {
    			Integer columnIndex = columnIdIndexMap.get(axisId);
    			logger.debug(className, function, "MatrixAxisType=COLUMN; axisId=[{}] columnIndex=[{}]", axisId, columnIndex);
    			if (columnIndex != null) {
    				IMatrixRenderer renderer = getView().getRenderer();
    				if (renderer != null && renderer instanceof ScsMatrixRenderer) {
    					ScsMatrixRenderer scsMxRenderer = (ScsMatrixRenderer)renderer;
    					HashSet<MxIntersectionState> stateSet = scsMxRenderer.getColumnSet(columnIndex);
    					if (stateSet != null) {
    						for (MxIntersectionState state: stateSet) {
    							Map<String, EntityClient> map = state.getListEntities();
    				    		if (map != null) {
	    				    		for ( EntityClient ec : map.values() ) {
	    				            	HashMap<String, String> details = new HashMap<String, String>();
	    				            	for ( String attributeName : ec.attributeNames() ) {
	    				             		details.put(attributeName, ec.getAttribute(attributeName).getValue().toString());
	    				             		logger.debug(className, function, "details add attribute[{}] value[{}]", attributeName, ec.getAttribute(attributeName).getValue().toString());
	    				            	}
	    				            	selectionSet.add(details);
	    				            } 				    		
    				    		} else {
    				    			logger.debug(className, function, "Warning: getListEntities() return map is null");
    				    		}
    						}
    						selectionEvent.onSelection(selectionSet);
    					} else {
    						logger.debug(className, function, "Warning: scsMxRenderer.getColumnSet() columnIndex[{}] return set is null", columnIndex);
    					}
    				} else {
    					logger.debug(className, function, "Warning: renderer is null");
    				}
    			} else {
    				logger.debug(className, function, "Warning: unable to get columnIndex from id [{}]", axisId);
    			}
    			
    		} else if (mxAxisType == MatrixAxisType.ROW) {
    			Integer rowIndex = rowIdIndexMap.get(axisId);
    			logger.debug(className, function, "MatrixAxisType=ROW, axisId=[{}], rowIndex=[{}]", axisId, rowIndex);
    			if (rowIndex != null) {
    				IMatrixRenderer renderer = getView().getRenderer();
    				if (renderer != null && renderer instanceof ScsMatrixRenderer) {
    					ScsMatrixRenderer scsMxRenderer = (ScsMatrixRenderer)renderer;
    					HashSet<MxIntersectionState> stateSet = scsMxRenderer.getRowSet(rowIndex);
    					if (stateSet != null) {
    						for (MxIntersectionState state: stateSet) {
    							Map<String, EntityClient> map = state.getListEntities();
    				    		if (map != null) {
	    				    		for ( EntityClient ec : map.values() ) {
	    				            	HashMap<String, String> details = new HashMap<String, String>();
	    				            	for ( String attributeName : ec.attributeNames() ) {
	    				             		details.put(attributeName, ec.getAttribute(attributeName).getValue().toString());
	    				             		logger.debug(className, function, "details add attribute[{}] value[{}]", attributeName, ec.getAttribute(attributeName).getValue().toString());
	    				            	}
	    				            	selectionSet.add(details);
	    				            }    				    		
    				    		}  else {
    				    			logger.debug(className, function, "Warning: getListEntities() return map is null");
    				    		}
    						}
    						selectionEvent.onSelection(selectionSet);
    					} else {
    						logger.debug(className, function, "Warning: scsMxRenderer.getRowSet() rowIndex[{}] return set is null", rowIndex);
    					}
    				} else {
    					logger.debug(className, function, "Warning: renderer is null");
    				}
    			} else {
    				logger.debug(className, function, "Warning: unable to get rowIndex from id [{}]", axisId);
    			}
    		}	
    	}
    	logger.end(className, function);
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
