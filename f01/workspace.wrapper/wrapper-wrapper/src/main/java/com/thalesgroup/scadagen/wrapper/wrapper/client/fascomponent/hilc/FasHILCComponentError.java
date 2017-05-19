package com.thalesgroup.scadagen.wrapper.wrapper.client.fascomponent.hilc;

import java.util.HashMap;
import java.util.Map;

import com.thalesgroup.scadagen.wrapper.wrapper.client.fascomponent.error.IFasComponentError;

public class FasHILCComponentError implements IFasComponentError {

	private static final int HILC_MAPPED_ERROR_CODE_BASE = 1000;
	
	private static final Map<Integer, ErrorCode> intToErrorCodeMap = new HashMap<Integer, ErrorCode>();
	
	static {
		for (ErrorCode err : ErrorCode.values()) {
			intToErrorCodeMap.put(err.value, err);
		}
	}
	
	// These error code are defined in HILC server code SessionManager.h
	enum ErrorCode {
		HILC_ARG_ERROR(1),
		HILC_EXISTING_SESSION_ERROR(2),
		HILC_CREATE_COND_ERROR(3),
		HILC_PREPNOTFOUND_ERROR(4), 
		HILC_MAX_SESSION_ERROR(5), 
		HILC_INIT_COND_ERROR(6);

		private int value;

		private ErrorCode(int value) {
			this.value = value;
		}

		public int getValue() {
			return this.value;
		}
	}

	@Override
	public String getName() {
		return "HILCComponent";
	}

	@Override
	public int getMappedErrorCodeValue(int errorCode) {
		ErrorCode e = intToErrorCodeMap.get(errorCode);
		if (e != null) {
			return e.value + HILC_MAPPED_ERROR_CODE_BASE;
		}
		return errorCode;
	}
}
