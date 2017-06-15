package com.thalesgroup.scadagen.wrapper.wrapper.client.generic.presenter.event;

import java.util.HashMap;
import java.util.Set;

public interface SelectionEvent {
	void onSelection(Set<HashMap<String, String>> entities);
}
