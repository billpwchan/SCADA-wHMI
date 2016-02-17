package com.thalesgroup.scadagen.whmi.uipanel.uipanelviewlayout.client.viewtoolbar;

public interface UIPanelImageToolBarEvent {
	public enum UIPanelImageBarEventType {
		ZoomIn, ZoomOut, Zoom, Locator, VDouble, HDouble
	}
	/**
	 * This should be called by the UIPanelImageToolBar only.
	 * @param uiPanelImageBarEventType
	 */
	void onImageButtonEvent(UIPanelImageBarEventType uiPanelImageBarEventType);
}
