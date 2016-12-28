package com.thalesgroup.scadagen.wrapper.wrapper.client.opm;

public interface UIWrapperRpcEvent_i {
	void CallbackEvent(String result, String detail);
	void CallbackEvent(String api, String rpcapi, String rpcReturnFunction, String resultType, String message1, String message2, String message3);
}
