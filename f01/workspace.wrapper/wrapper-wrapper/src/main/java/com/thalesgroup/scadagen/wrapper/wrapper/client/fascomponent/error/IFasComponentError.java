package com.thalesgroup.scadagen.wrapper.wrapper.client.fascomponent.error;

public interface IFasComponentError {
	// This name has to match the name used in connector IJSComponent implementation class
	String getName();
	
	// If no mapping is found, the input errorCode is returned
	int getMappedErrorCodeValue(int errorCode);
}
