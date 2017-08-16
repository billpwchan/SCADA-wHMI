package com.thalesgroup.scadagen.wrapper.wrapper.scadasoft.gwebhmi.main.client.widget;

import java.util.HashMap;
import java.util.Map;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.ClientLogger;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.situation.description.SymbolsPoolClient;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.situation.description.symbol.SymbolClient;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.situation.update.SymbolInsert;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.situation.view.symbol.animator.ClientAnimatorAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.situation.view.widget.SymbolWidget;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.situation.view.widget.WidgetFactory;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.situation.view.zoom.strategy.IZoomStrategy;
import com.thalesgroup.scadagen.wrapper.wrapper.client.WrapperScsSituationViewPanelEvent;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.util.ConfigurationConstantUtil;

/**
 * SCADAsoft custom WidgetFactory
 */
public class ScsWidgetFactory extends WidgetFactory {
    private static final ClientLogger s_logger = ClientLogger.getClientLogger();
    private static Logger logger = Logger.getLogger(ScsWidgetFactory.class.getName());
	private WrapperScsSituationViewPanelEvent wrapperScsSituationViewPanelEvent = null;
	
	public ScsWidgetFactory(WrapperScsSituationViewPanelEvent wrapperScsSituationViewPanelEvent) {
		super();
		this.wrapperScsSituationViewPanelEvent = wrapperScsSituationViewPanelEvent;
	}
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
		
        // SCADAgen Symbol Click
        w.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				logger.log(Level.FINE, "getSymbolWidget onClick Begin");
				
				SymbolWidget symbolWidget = (SymbolWidget)event.getSource();
				
				logger.log(Level.FINE, "getSymbolWidget getSource()");
				
				if ( null != symbolWidget ) {
					
					logger.log(Level.FINE, "getSymbolWidget symbolWidget is not null");
					
					String hv_id = symbolWidget.getEntityId();
					
					String hv_type = symbolWidget.getEntityClassName();
					
					logger.log(Level.FINE, "getSymbolWidget symbolWidget is hv_id["+hv_id+"]  hv_type["+hv_type+"]");
					
					if ( null != wrapperScsSituationViewPanelEvent ) {
						
						int mouseX = event.getClientX();
						int mouseY = event.getClientY();
						
						Map<String, String> options = new HashMap<String, String>();
						
						options.put("hv_id", hv_id);
						options.put("hv_type", hv_type);
						options.put("mouseX", Integer.toString(mouseX));
						options.put("mouseY", Integer.toString(mouseY));
						
						wrapperScsSituationViewPanelEvent.triggerSymbolWidget(options);
						
					}
					
				} else {
					
					logger.log(Level.FINE, "getSymbolWidget symbolWidget is NULL");
				}
				
				logger.log(Level.FINE, "getSymbolWidget onClick End");
				
			}
		});
        
        logger.log(Level.FINE, "getSymbolWidget End");

        return w;
    }

}
