package com.thalesgroup.scadagen.wrapper.wrapper.client.generic.event;

import com.google.gwt.event.shared.EventHandler;

/**
 * Handler interface for the {@link OpenSituationViewEvent}.
 *
 */
public interface OpenSituationViewEventHandler extends EventHandler {

    /**
     * Called when alarm selection changes
     * 
     * @param event
     *            the current {@link OpenSituationViewEvent}.
     */
    void onSelectionChange(OpenSituationViewEvent event);

}