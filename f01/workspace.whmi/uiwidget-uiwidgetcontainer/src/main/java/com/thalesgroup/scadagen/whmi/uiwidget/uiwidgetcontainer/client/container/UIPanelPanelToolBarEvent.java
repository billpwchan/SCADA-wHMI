package com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetcontainer.client.container;

public interface UIPanelPanelToolBarEvent {
	public enum UIPanelPanelToolBarEventType { AlarmSummary, EventSummary, AlarmManagement }
	void onPanelButton(UIPanelPanelToolBarEventType uiPanelPanelToolBarEventType);
}
