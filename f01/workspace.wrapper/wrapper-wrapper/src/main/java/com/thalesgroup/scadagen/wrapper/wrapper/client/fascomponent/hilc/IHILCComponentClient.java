package com.thalesgroup.scadagen.wrapper.wrapper.client.fascomponent.hilc;

import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.view.HypervisorView;

public interface IHILCComponentClient extends HypervisorView {

	/**
     * Send HILC PreparationRequest (Select)
     * 
     * @param clientKey
     *            key used by client to catch the response.
     * @param errorCode
     *            the error code.
     * @param errorMessage
     *            the message associated with the error code.
     */
	void setHILCPreparationResult(String clientKey, int errorCode, String errorMessage);
	
	/**
     * Send HILC ConfirmRequest (Execute)
     * 
     * @param clientKey
     *            key used by client to catch the response.
     * @param errorCode
     *            the error code.
     * @param errorMessage
     *            the message associated with the error code.
     */
	void setHILCConfirmResult(String clientKey, int errorCode, String errorMessage);
}
