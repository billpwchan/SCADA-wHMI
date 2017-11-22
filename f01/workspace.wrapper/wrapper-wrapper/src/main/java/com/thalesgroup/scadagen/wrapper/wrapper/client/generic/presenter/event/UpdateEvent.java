package com.thalesgroup.scadagen.wrapper.wrapper.client.generic.presenter.event;

import java.util.Map;
import java.util.Set;

public interface UpdateEvent {
	void onUpdate(Set<Map<String, String>> entities);
}