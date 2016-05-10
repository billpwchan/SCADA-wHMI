package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.observer;

public abstract class Observer {
	protected Subject subject;
	public abstract void setSubject(Subject subject);
	public abstract void update();
}
