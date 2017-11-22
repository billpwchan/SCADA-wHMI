package com.thalesgroup.scadagen.wrapper.wrapper.client.generic.presenter.event;

import java.util.Map;

public interface CounterEvent {
	void onCounterChange(final Map<String, Integer> countersValue);
}
