package com.thalesgroup.scadagen.wrapper.wrapper.client.fascomponent.error;

import java.util.HashMap;
import java.util.Map;

import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.ClientLogger;
import com.thalesgroup.scadagen.wrapper.wrapper.client.fascomponent.hilc.FasHILCComponentError;

public class FasComponentErrorManager {
	
	private static FasComponentErrorManager instance_ = null;
	private Map<String, IFasComponentError> fasErrorMap = new HashMap<String, IFasComponentError>();
	private final ClientLogger LOGGER = ClientLogger.getClientLogger();
	
	public FasComponentErrorManager() {
		init();
	}
	
	public static FasComponentErrorManager getInstance() {
		if (instance_ == null) {
			instance_ = new FasComponentErrorManager();
		}
		return instance_;
	}
	
	protected void init() {
		LOGGER.debug("FasComponentErrorMap init");
		// GWT client does not support Java SerivceLoader and Reflection
		// Manually create the IFasComponentError interface class and save into a map
		fasErrorMap.put("HILCComponent", new FasHILCComponentError());
	}
	
	public Integer getMappedErrorCode(String fasComponent, Integer errorCode) {
		IFasComponentError errorMap = fasErrorMap.get(fasComponent);
		if (errorMap != null) {
			return errorMap.getMappedErrorCodeValue(errorCode);
		}
	
		return errorCode;
	}
	
}
