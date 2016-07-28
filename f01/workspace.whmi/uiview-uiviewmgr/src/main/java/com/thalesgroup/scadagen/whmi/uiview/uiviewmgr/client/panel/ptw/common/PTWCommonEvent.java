package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.ptw.common;

import com.google.gwt.event.shared.GwtEvent;

public class PTWCommonEvent extends GwtEvent<PTWCommonEventHandler> {
	
	public static Type<PTWCommonEventHandler> TYPE = new Type<PTWCommonEventHandler>();

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<PTWCommonEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(PTWCommonEventHandler handler) {
		handler.onOperation(this);
	}
	
	public PTWCommonEvent (String operation, String operationDetails) {
		this.operation = operation;
		this.operationDetails = operationDetails;
	}
	
	// Filter: Equipment (Set) or NAN (Unset)
	// Selection: Equipment (Select) or NAN (DeSelect)
	
	private String operation = null;
	private String operationDetails = null;
	
	public String getOperation() { return operation; }
	public void setOperation(String operation) { this.operation = operation; }
	
	public String getOperationDetails() { return operationDetails; }
	public void setOperationDetails(String operationDetails) { this.operationDetails = operationDetails; }
}
