package com.thalesgroup.scadagen.bps.computers;

import java.util.HashSet;
import java.util.Set;

import com.thalesgroup.scadagen.bps.conf.computers.IComputer;

public abstract class BpsStatusComputer implements IComputer {
	protected Set<String> statusSet_ = new HashSet<String>();
	protected String name_ = "";

	@Override
	public String getComputerId() {
		return name_;
	}

	@Override
	public Set<String> getInputStatuses() {
		return statusSet_;
	}

}
