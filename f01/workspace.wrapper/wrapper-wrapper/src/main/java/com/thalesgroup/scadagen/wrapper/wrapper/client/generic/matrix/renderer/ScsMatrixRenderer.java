package com.thalesgroup.scadagen.wrapper.wrapper.client.generic.matrix.renderer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.entity.EntityClient;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.dictionary.Dictionary;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.renderer.IMatrixRenderer;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.tools.MxTooltipTools;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.update.LocationKey;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.update.MxCounterState;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.update.MxIntersectionState;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.update.MxItemStyleClient;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.update.MxItemStyleServer;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;

public class ScsMatrixRenderer implements IMatrixRenderer {
	
	private static final String className = UIWidgetUtil.getClassSimpleName(ScsMatrixRenderer.class.getName());
	private static UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private static final String defaultCounterCssPrefix = "scsmx";

	protected HashMap<LocationKey, MxIntersectionState> locationToMxIntersectionStateMap = new HashMap<LocationKey, MxIntersectionState>();
	protected Integer startRowIndex = -1;
	protected Integer endRowIndex = -1;
	protected Integer startColumnIndex = -1;
	protected Integer endColumnIndex = -1;

	/** Counter Renderer */
    protected final ScsMatrixCounterRenderer counterRenderer_;
    
    protected final String coeffAttributeName_;
    
    protected final Boolean displayCounterTooltip_;

    /**
     * Constructor
     */
    public ScsMatrixRenderer() {
        counterRenderer_ = new ScsMatrixCounterRenderer(defaultCounterCssPrefix);
        coeffAttributeName_ = null;
        displayCounterTooltip_ = false;
    }
    
    public ScsMatrixRenderer(String counterCssPrefix, String coeffAttributeName, Boolean displayCounterTooltip) {
    	counterRenderer_ = new ScsMatrixCounterRenderer(counterCssPrefix);
    	coeffAttributeName_ = coeffAttributeName;
    	displayCounterTooltip_ = displayCounterTooltip;
    }
	
	@Override
	public MxItemStyleClient computeRendererStyle(MxIntersectionState mxIntersectionState) {
		final String function = "computeRendererStyle";
		logger.begin(className, function);
		
		logger.debug(className, function, "location key row[{}] column[{}]", mxIntersectionState.getLocationKey().getRow(), mxIntersectionState.getLocationKey().getColumn());
		LocationKey location = mxIntersectionState.getLocationKey();
		locationToMxIntersectionStateMap.put(location, mxIntersectionState);
		
		Integer row = mxIntersectionState.getLocationKey().getRow();
		if (row < startRowIndex) {
			startRowIndex = row;
		}
		if (row > endRowIndex) {
			endRowIndex = row;
		}
		
		Integer col = mxIntersectionState.getLocationKey().getColumn();
		if (col < startColumnIndex) {
			startColumnIndex = col;
		}
		if (col > endColumnIndex) {
			endColumnIndex = col;
		}
		
		logger.debug(className, function, "location name [{}]", mxIntersectionState.getLocationName());

		if (mxIntersectionState.getCountersState() != null && mxIntersectionState.getCountersState().getCountersValue() != null) {
			for (String counterKey : mxIntersectionState.getCountersState().getCountersValue().keySet()) {
				logger.debug(className, function, "counter[{}] value[{}]", counterKey, mxIntersectionState.getCountersState().getCountersValue().get(counterKey));
			}
		} else {
			logger.debug(className, function, "getCountersState() or getCountersValue() return null");
		}

		if (mxIntersectionState.getEntityToSelect() != null && mxIntersectionState.getEntityToSelect().getEntityClient() != null) {
			for (String attributeName : mxIntersectionState.getEntityToSelect().getEntityClient().attributeNames()) {
				logger.debug(className, function, "getEntityToSelect attribute[{}] value[{}]", attributeName, mxIntersectionState.getEntityToSelect().getEntityClient().getAttribute(attributeName));
			}
		} else {
			logger.debug(className, function, "getEntityToSelect() or getEntityToSelect().getEntityClient() return null");
		}

		if (mxIntersectionState.getListEntities() != null) {
			for (String hvid : mxIntersectionState.getListEntities().keySet()) {
				EntityClient ec = mxIntersectionState.getListEntities().get(hvid);
				if (ec != null) {
					logger.debug(className, function, "getListEntities hvid [{}]", hvid);
					for (String attributeName : ec.attributeNames()) {
						logger.debug(className, function, "getListEntities attribute[{}] value[{}]", attributeName, mxIntersectionState.getEntityToSelect().getEntityClient().getAttribute(attributeName));
					}
				}
			}
		} else {
			logger.debug(className, function, "mxIntersectionState.getListEntities() return null");
		}
		
		logger.debug(className, function, "getNbEntityToDisplay[{}]", mxIntersectionState.getNbEntityToDisplay());
		
		final MxItemStyleServer styleServer = mxIntersectionState.getStyleCssServer();

        final MxItemStyleClient styleClient = new MxItemStyleClient();

        styleClient.setCoefficientStyle(styleServer.getCoefficientStyle());
        styleClient.setCoefficientText(ScsMatrixCoeffTools.getCoefficientText(mxIntersectionState, coeffAttributeName_));
        styleClient.setSquareInnerStyle(counterRenderer_.getSquareCssCounter(mxIntersectionState));
        styleClient.setSquareOuterStyle(styleServer.getOuterSquareStyle());

        if (displayCounterTooltip_) {
	        // put counter in tooltip
	        final MxCounterState counterState = mxIntersectionState.getCountersState();
	        final Map<String, Integer> countersValue = counterState != null ? counterState.getCountersValue() : null;
	        if (countersValue != null) {
	            String txt = "Counter:\n";
	            for (final Entry<String, Integer> entry : countersValue.entrySet()) {
	                final String counterId = entry.getKey();
	                final Integer counterValue = entry.getValue();
	                txt = txt + "  " + Dictionary.getWording(counterId) + ": " + counterValue + "\n";
	            }
	            styleClient.setSquareTooltip(txt);
	        } else {
	            styleClient.setSquareTooltip(MxTooltipTools.computeSquareTooltipStd(mxIntersectionState));
	        }
        }
        
        logger.end(className, function);

        return styleClient;
	}
	
	public HashSet<MxIntersectionState> getRowSet(Integer row) {
		HashSet<MxIntersectionState> set = new HashSet<MxIntersectionState>();
		
		for (int col=startColumnIndex; col<=endColumnIndex; col++) {
			LocationKey key = new LocationKey(row, col);
			MxIntersectionState state = locationToMxIntersectionStateMap.get(key);
			if (state != null && state.getNbEntityToDisplay() > 0) {
				set.add(state);
			}
		}
		
		return set;
	}
	
	public HashSet<MxIntersectionState> getColumnSet(Integer column) {
		HashSet<MxIntersectionState> set = new HashSet<MxIntersectionState>();
		
		for (int row=startRowIndex; row<=endRowIndex; row++) {
			LocationKey key = new LocationKey(row, column);
			MxIntersectionState state = locationToMxIntersectionStateMap.get(key);
			if (state != null && state.getNbEntityToDisplay() > 0) {
				set.add(state);
			}
		}
		
		return set;
	}
	
	public MxIntersectionState getMxIntersectionState(LocationKey key) {
		return locationToMxIntersectionStateMap.get(key);
	}

}
