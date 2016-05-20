package com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface EqptFilterChangeEventHandler extends EventHandler{
	/**
     * Called when equipment filter is submitted
     * @param event the current {@link EqptFilterChangeEvent}.
     */
    void onEqptFilterChange(EqptFilterChangeEvent event);
}
