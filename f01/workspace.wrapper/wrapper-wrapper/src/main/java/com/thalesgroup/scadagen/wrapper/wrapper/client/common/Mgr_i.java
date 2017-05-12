package com.thalesgroup.scadagen.wrapper.wrapper.client.common;

import com.thalesgroup.scadagen.wrapper.wrapper.client.observer.Subject;

public interface Mgr_i {
	
	void setSubject(String key, Subject subject);
	
	void removeSubject(String key);
}
