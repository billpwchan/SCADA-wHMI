package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.soc;

import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ListDataProvider;

public interface UIDataGridDatabase_i {
	void addEquipment(Equipment_i contact);
	void addDataDisplay(HasData<Equipment_i> display);
	ListDataProvider<Equipment_i> getDataProvider();
	void refreshDisplays();
}
