package com.thalesgroup.scadagen.wrapper.wrapper.client.generic.matrix.renderer;

import java.util.Map;
import java.util.Map.Entry;

import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.counter.IMxCounterRenderer;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.update.MxCounterState;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.update.MxIntersectionState;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.view.MatrixCss;

public class ScsMatrixCounterRenderer implements IMxCounterRenderer {

	private String m_cssPrefix;

    /**
     * Constructor
     * 
     * @param cssPrefix
     *            the css prefix.
     */
    public ScsMatrixCounterRenderer(final String cssPrefix) {
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
                    toReturn = toReturn + "_" + counterId;
                    break;
                }
            }
        }

        return toReturn;

    }

}
