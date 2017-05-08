package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.soc;

import com.google.gwt.user.cellview.client.DataGrid;

public interface UIDataGridFormatter_i {
	
	int getNumberOfColumn();
	String getColumnType(int column);
	String getColumnHeaderString(int column);
	String getColumnLabel(int column);
	String getEmptyLabel();
	void setEmptyLabel(String label);
	int getColumnWidth(int column);
	int getColumnSort(int column);
	String getColumnType(String columnLabel);
	
	DataGrid<Equipment_i> addDataGridColumn(DataGrid<Equipment_i> dataGrid);
		
}
