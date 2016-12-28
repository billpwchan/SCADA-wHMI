package com.thalesgroup.scadagen.wrapper.wrapper.client.observer;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.json.client.JSONObject;

public class Subject {
	private List<Observer> observers = new ArrayList<Observer>();
	private JSONObject state;
	public JSONObject getState() {
		return state;
	}
	public void setState(JSONObject state) {
		this.state = state;
		notifyAllObservers();
	}
	public void attach(Observer observer) {
		observers.add(observer);
	}
	public void detach(Observer observer) {
		observers.remove(observer);
	}
	public void notifyAllObservers() {
		for ( Observer observer : observers ) {
			observer.update();
		}
	}
}
