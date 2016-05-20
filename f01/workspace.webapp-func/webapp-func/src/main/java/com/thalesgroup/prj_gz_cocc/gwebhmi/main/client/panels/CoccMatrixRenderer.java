package com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.panels;

import java.util.Map;
import java.util.Map.Entry;

import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.dictionary.Dictionary;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.renderer.IMatrixRenderer;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.tools.MxTooltipTools;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.update.MxCounterState;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.update.MxIntersectionState;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.update.MxItemStyleClient;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.update.MxItemStyleServer;


public class CoccMatrixRenderer implements IMatrixRenderer {

    /** Counter Renderer */
    private final CoccMatrixCounterRenderer counterRenderer_;
    
    /**
     * Constructor
     */
    public CoccMatrixRenderer() {
        super();
        counterRenderer_ = new CoccMatrixCounterRenderer("scsmx");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MxItemStyleClient computeRendererStyle(final MxIntersectionState mxIntersectionState) {
        
        final MxItemStyleServer styleServer = mxIntersectionState.getStyleCssServer();
        
        final MxItemStyleClient styleClient = new MxItemStyleClient();
        
        styleClient.setCoefficientStyle(styleServer.getCoefficientStyle());
        styleClient.setCoefficientText(styleServer.getCoefficientText());
        styleClient.setSquareInnerStyle(counterRenderer_.getSquareCssCounter(mxIntersectionState));
        styleClient.setSquareOuterStyle(styleServer.getOuterSquareStyle());
        
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
        
        return styleClient;
    }

}
