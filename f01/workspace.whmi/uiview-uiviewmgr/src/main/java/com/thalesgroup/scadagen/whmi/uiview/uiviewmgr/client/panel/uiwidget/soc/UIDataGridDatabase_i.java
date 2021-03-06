package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.soc;

import java.util.Map;

import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ListDataProvider;

public interface UIDataGridDatabase_i {
	void addEquipment(Equipment_i contact);
	void addDataDisplay(HasData<Equipment_i> display);
	void updateEquipmentElement(int row, Equipment_i contact);
	ListDataProvider<Equipment_i> getDataProvider();
	void refreshDisplays();
	int getColumnCount();
	String [] getColumnLabels();
	String [] getColumnTypes();
	int [] getColumnTranslation();
	void changeColumnFilter(Map<String,String> filterMap);
}
