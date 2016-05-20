package com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.widget;

import com.google.gwt.event.dom.client.ClickHandler;
//import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.ClientLogger;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.situation.description.SymbolsPoolClient;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.situation.description.symbol.SymbolClient;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.situation.update.SymbolInsert;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.situation.view.widget.SymbolWidget;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.situation.view.widget.WidgetFactory;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.situation.view.zoom.factory.ZoomFactory;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.situation.view.zoom.strategy.IZoomStrategy;
import com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.panels.CoccSituationViewPanel;

public class CustomWidgetFactory extends WidgetFactory {
	/** Logger */
	//private final ClientLogger s_logger = ClientLogger.getClientLogger();

	/** Situation View Panel */
	private CoccSituationViewPanel viewPanel_;

    /**
     * Constructor
     * @param zoomFactory Zoom factory
     */
	public CustomWidgetFactory(final ZoomFactory zoomFactory) {
		super(zoomFactory);
	}

	/**
	 * Constructor
	 */
	public CustomWidgetFactory() {
		super();
	}

	/**
	 * Constructor
	 * @param viewPanel Situation view panel
	 */
	public CustomWidgetFactory(CoccSituationViewPanel viewPanel) {
		this();
		viewPanel_ = viewPanel;
	}

	@Override
    public SymbolWidget getSymbolWidget(final SymbolInsert insert, final SymbolsPoolClient symbolPool) throws Exception {
		final SymbolClient symbolClient = symbolPool.getSymbols().get(insert.getSymbolId());
		
		if (symbolClient == null) {
			throw new Exception("Cannot find the symbol [" + insert.getSymbolId() + "].");
		}
		
		final IZoomStrategy strategy = getZoomStrategyForSymbol(symbolClient, insert.getResolutionReference());
		
		// Symbol Widget to Return
		SymbolWidget widget;

		if(insert != null && insert.getSelectionInfo() != null &&
				insert.getSelectionInfo().getEntityClassName().endsWith("cctv.CameraStatusesType")) {
			// Camera Found, Overwrite Widget with Custom Camera Symbol Widget
			CustomSymbolWidget csw = new CustomSymbolWidget(insert, symbolClient, strategy);
			csw.addCustomEventHandler(getClickHandler());
			widget = (SymbolWidget)csw;
		} else {
			// Not Camera, Default Behaviour
			widget = super.getSymbolWidget(insert, symbolPool);
		}
    	return widget;
    }

	/**
	 * Returns the click handler defined by View Panel
	 * @return Handler for the click event 
	 */
	public ClickHandler getClickHandler() {
		return viewPanel_.getClickHandler();
	}
}
