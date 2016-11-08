package com.thalesgroup.scadagen.bps.conf.computers;

import java.util.Map;
import java.util.Set;

import com.thalesgroup.hv.data_v1.attribute.AbstractAttributeType;

public interface IComputer {
	AbstractAttributeType compute(final Map<String, AbstractAttributeType> inputStatusByName, Map<String, String>configProperties);
	
	String getComputerId();
	
	Set<String> getInputStatuses();
}
