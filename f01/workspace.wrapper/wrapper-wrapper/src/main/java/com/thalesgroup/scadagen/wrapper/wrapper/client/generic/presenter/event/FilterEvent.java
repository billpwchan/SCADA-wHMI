package com.thalesgroup.scadagen.wrapper.wrapper.client.generic.presenter.event;

import java.util.ArrayList;

public interface FilterEvent {
	void onFilterChange(ArrayList<String> columns);
}
