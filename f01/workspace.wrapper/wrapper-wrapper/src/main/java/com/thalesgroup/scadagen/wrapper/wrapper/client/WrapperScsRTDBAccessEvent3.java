package com.thalesgroup.scadagen.wrapper.wrapper.client;

import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.dbm.ScsRTDBComponentAccess.ScsClassAttInfo;

public interface WrapperScsRTDBAccessEvent3 {
	void setReadResult(String key, String[] values, int errorCode, String errorMessage);
	void setGetChildrenResult(String clientKey, String[] instances, int errorCode, String errorMessage);
	void setGetAttributesDescriptionResult(String clientKey, ScsClassAttInfo[] attributesInfo, int errorCode, String errorMessage);
}
