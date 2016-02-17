package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel;

public interface UIPanelPanelToolBarEvent {
	public enum UIPanelPanelToolBarEventType { AlarmSummary, EventSummary, AlarmManagement }
	void onPanelButton(UIPanelPanelToolBarEventType uiPanelPanelToolBarEventType);
}
