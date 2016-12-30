package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.soc;

import com.google.gwt.user.cellview.client.DataGrid;

public interface UIDataGridFomatter_i {
	
	int getNumberOfColumn();
	String getColumnType(int column);
	String getColumnLabel(int column);
	String getEmptyLabel();
	int getColumnWidth(int column);
	
	DataGrid<Equipment_i> addDataGridColumn(DataGrid<Equipment_i> dataGrid);
		
}