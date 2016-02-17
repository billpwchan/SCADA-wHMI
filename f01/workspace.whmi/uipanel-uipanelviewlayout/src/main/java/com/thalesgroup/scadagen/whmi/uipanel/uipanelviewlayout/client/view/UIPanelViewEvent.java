package com.thalesgroup.scadagen.whmi.uipanel.uipanelviewlayout.client.view;

public interface UIPanelViewEvent {
	/**
	 * This should be called by UIPanelView only.
	 * @param viewId
	 */
	void setViewIdActivate(int viewId);
	/**
	 * This should be called by UIPanelView only.
	 * @param viewId
	 */
	void onViewIdActivateEvent(int viewId);
}
