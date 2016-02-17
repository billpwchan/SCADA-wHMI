package com.thalesgroup.scadagen.wrapper.wrapper.scadasoft.gwebhmi.main.client.event;

import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.event.HvSharedEvent;

/**
 * Publish this event in the bus to open a situation view.
 */
public class OpenSituationViewEvent extends HvSharedEvent<OpenSituationViewEventHandler> {

    /**
     * type of event handled by this class
     */
    public static final Type<OpenSituationViewEventHandler> TYPE = new Type<OpenSituationViewEventHandler>();
    /**
     * Situation view configuration id
     */
    private String situationViewId_;

    /**
     * @param situationViewId
     *            the configuration id
     */
    public OpenSituationViewEvent(String situationViewId) {
        situationViewId_ = situationViewId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<OpenSituationViewEventHandler> getAssociatedType() {
        return TYPE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void dispatch(OpenSituationViewEventHandler handler) {
        if (handler != this.getSource()) {
            handler.onSelectionChange(this);
        }
    }

    /**
     * @return the configuration id
     */
    public String getSituationViewId() {
        return situationViewId_;
    }

}
