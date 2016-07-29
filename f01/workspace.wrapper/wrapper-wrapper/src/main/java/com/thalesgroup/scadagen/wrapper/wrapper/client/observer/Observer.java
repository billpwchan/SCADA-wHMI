package com.thalesgroup.scadagen.wrapper.wrapper.client.observer;

public abstract class Observer {
	protected Subject subject;
	public abstract void setSubject(Subject subject);
	public abstract void update();
}
