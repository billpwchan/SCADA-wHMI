package com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.container;

public interface UIPanelPanelToolBarEvent {
	public enum UIPanelPanelToolBarEventType { AlarmSummary, EventSummary, AlarmManagement }
	void onPanelButton(UIPanelPanelToolBarEventType uiPanelPanelToolBarEventType);
}
