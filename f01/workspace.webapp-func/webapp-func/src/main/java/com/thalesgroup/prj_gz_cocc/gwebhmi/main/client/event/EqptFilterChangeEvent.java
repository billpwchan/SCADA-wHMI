package com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.event;

import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.event.HvSharedEvent;
import com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.model.EqptQuery;

public class EqptFilterChangeEvent extends HvSharedEvent<EqptFilterChangeEventHandler> {

	/** 
     * type of event handled by this class 
     */
    public static final Type<EqptFilterChangeEventHandler> TYPE = new Type<EqptFilterChangeEventHandler>();
    
    private EqptQuery query_;
    
    public EqptFilterChangeEvent(String lineId, String stationId, String subsystemId, String eqptLabel, String eqptName) {
    	query_ = new EqptQuery(lineId, stationId, subsystemId, eqptLabel, eqptName);
    }

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<EqptFilterChangeEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(EqptFilterChangeEventHandler handler) {
		if (handler != this.getSource()) {
			handler.onEqptFilterChange(this);
		}
	}

	public EqptQuery getEqptQuery() {
		return query_;
	}

}
