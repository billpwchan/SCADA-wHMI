package com.thalesgroup.scadagen.wrapper.wrapper.scadasoft.gwebhmi.main.client.widget;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.situation.description.SymbolsPoolClient;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.situation.description.symbol.SymbolClient;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.situation.update.SymbolInsert;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.situation.view.symbol.animator.ClientAnimatorAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.situation.view.widget.SymbolWidget;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.situation.view.widget.WidgetFactory;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.situation.view.zoom.strategy.IZoomStrategy;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.panel.SCADAgenSituationViewPanelEvent;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.util.ConfigurationConstantUtil;

/**
 * SCADAsoft custom WidgetFactory
 */
public class ScsWidgetFactory extends WidgetFactory {

	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());

	private SCADAgenSituationViewPanelEvent wrapperScsSituationViewPanelEvent = null;
	
	public ScsWidgetFactory(SCADAgenSituationViewPanelEvent wrapperScsSituationViewPanelEvent) {
		super();
		this.wrapperScsSituationViewPanelEvent = wrapperScsSituationViewPanelEvent;
	}
    /**
     * {@inheritDoc}
     */
    @Override
    public SymbolWidget getSymbolWidget(final SymbolInsert insert, final SymbolsPoolClient symbolPool)
            throws Exception {
    	final String f = "getSymbolWidget";
    	logger.begin(f);

        logger.debug(f, "Factory called for " + insert.getSymbolId() + ")");

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
				
				String f2 = f + " onClick";
				
				logger.begin(f2);
				
				SymbolWidget symbolWidget = (SymbolWidget)event.getSource();
				
				logger.debug(f2, "getSource()");
				
				if ( null != symbolWidget ) {
					
					logger.debug(f2, "symbolWidget is not null");
					
					String hv_id = symbolWidget.getEntityId();
					
					String hv_type = symbolWidget.getEntityClassName();
					
					logger.debug(f2, "symbolWidget is hv_id["+hv_id+"]  hv_type["+hv_type+"]");
					
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
					
					logger.debug(f2, "symbolWidget is NULL");
				}
				
				logger.end(f2);
				
			}
		});
        
        logger.end(f);

        return w;
    }

}
