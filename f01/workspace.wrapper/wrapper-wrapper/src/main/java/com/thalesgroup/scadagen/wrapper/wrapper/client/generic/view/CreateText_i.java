package com.thalesgroup.scadagen.wrapper.wrapper.client.generic.view;

public interface CreateText_i {
	String CreateText(int pageStart, int endIndex, boolean exact, int dataSize);
	void pageStart(int pageStart);
	void endIndex(int endIndex);
	void exact(boolean exact, int dataSize);
}
