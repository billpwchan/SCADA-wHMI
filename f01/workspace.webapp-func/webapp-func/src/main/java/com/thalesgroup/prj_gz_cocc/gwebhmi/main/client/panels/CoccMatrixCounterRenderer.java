package com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.panels;

import java.util.Map;
import java.util.Map.Entry;

import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.counter.IMxCounterRenderer;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.update.MxCounterState;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.update.MxIntersectionState;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.view.MatrixCss;

public class CoccMatrixCounterRenderer implements IMxCounterRenderer {

    private String m_cssPrefix;

    /**
     * Constructor
     * 
     */
    public CoccMatrixCounterRenderer(final String cssPrefix) {
        m_cssPrefix = cssPrefix;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSquareCssCounter(final MxIntersectionState mxIntersectionState) {
        String toReturn = MatrixCss.CSS_SQUARE_INNER_DEFAULT;

        final MxCounterState counterState = mxIntersectionState.getCountersState();
        final Map<String, Integer> countersValue = counterState != null ? counterState.getCountersValue() : null;
        if (countersValue != null) {
            toReturn = m_cssPrefix;
            for (final Entry<String, Integer> entry : countersValue.entrySet()) {

                final String counterId = entry.getKey();
                final Integer counterValue = entry.getValue();
                
                final boolean isCounterPositive = counterValue != null && counterValue.intValue() > 0;

                if (isCounterPositive) {
                    toReturn =  toReturn + "_" + counterId;
                    break;
                }
            }
        }

        return toReturn;

    }

}
