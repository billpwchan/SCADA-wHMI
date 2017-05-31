package com.thalesgroup.scadagen.wrapper.widgetcontroller.client.scadagen;

import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.common.InitProcess_i;

public interface Loader_i {
	
	void setParameter(String key, String value);
	
	void iniDefaultParameterName();
	
	InitProcess_i getLoader();
}
