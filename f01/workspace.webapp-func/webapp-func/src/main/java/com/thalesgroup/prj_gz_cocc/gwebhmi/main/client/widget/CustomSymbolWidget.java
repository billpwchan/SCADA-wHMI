package com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.widget;

import com.google.gwt.event.dom.client.ClickHandler;
//import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.ClientLogger;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.situation.description.symbol.SymbolClient;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.situation.update.SymbolInsert;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.situation.view.widget.SymbolWidget;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.situation.view.zoom.strategy.IZoomStrategy;

public class CustomSymbolWidget extends SymbolWidget {
    //private final ClientLogger s_logger = ClientLogger.getClientLogger();

	/**
	 * Constructor.
	 * @param insert the insertion message.
	 * @param symbol the client symbol definition.
	 * @param zoomStrategy the zoom strategy.
	 */
	public CustomSymbolWidget(final SymbolInsert insert,
			final SymbolClient symbol, final IZoomStrategy zoomStrategy) {
		super(insert, symbol, zoomStrategy);
	}

	/**
	 * Adds customized click event handler to the symbol widget
	 * @param clickHandler Handler for click event
	 */
	public void addCustomEventHandler(ClickHandler clickHandler) {
		addHandlerRegistration(addClickHandler(clickHandler));
	}
}
