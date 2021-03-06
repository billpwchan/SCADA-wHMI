package com.thalesgroup.scadasoft.gwebhmi.main.client.widget;

import java.util.HashMap;
import java.util.Map;

import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.ClientLogger;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.situation.description.SymbolsPoolClient;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.situation.description.symbol.SymbolClient;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.situation.update.SymbolInsert;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.situation.view.symbol.animator.ClientAnimatorAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.situation.view.widget.SymbolWidget;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.situation.view.widget.WidgetFactory;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.situation.view.zoom.strategy.IZoomStrategy;
import com.thalesgroup.scadasoft.gwebhmi.main.client.util.ConfigurationConstantUtil;

/**
 * SCADAsoft custom WidgetFactory
 */
public class ScsWidgetFactory extends WidgetFactory {
    private static final ClientLogger s_logger = ClientLogger.getClientLogger();

    /**
     * {@inheritDoc}
     */
    @Override
    public SymbolWidget getSymbolWidget(final SymbolInsert insert, final SymbolsPoolClient symbolPool)
            throws Exception {

        s_logger.debug("Factory called for " + insert.getSymbolId() + ")");

        final String symbolId = insert.getSymbolId();
        final SymbolClient symbolClient = symbolPool.getSymbols().get(insert.getSymbolId());

        if (symbolClient == null) {
            throw new Exception("Cannot find the symbol [" + insert.getSymbolId() + "].");
        }

        final IZoomStrategy strategy = getZoomStrategyForSymbol(symbolClient, insert.getResolutionReference());

        SymbolWidget w = null;
        if (symbolId.equals(ConfigurationConstantUtil.QNAME_CAM)) {
            w = new SymbolWidget(insert, symbolClient, strategy);
        } else {
            w = new SymbolWidget(insert, symbolClient, strategy);
        }

        if ("rectangular_large_gauge_symbol".equals(insert.getSymbolId())
                || "rectangular_large_horizontal_gauge_symbol".equals(insert.getSymbolId())) {

            final Map<String, ClientAnimatorAbstract> customAnimators = new HashMap<String, ClientAnimatorAbstract>();

            customAnimators.put("rectangular_large_gauge_animation", new RectangularLargeGaugeAnimator());

            w.addCustomAnimators(customAnimators);

        }
        
        return w;
    }
    

}
