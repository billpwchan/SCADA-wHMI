package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.soc.database;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.soc.UIDataGridFormatter;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.soc.UIDataGridFormatter_i;

public class UIDataGridDatabaseMgr {
	
	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());

	private static UIDataGridDatabaseMgr instance = null;
	public static UIDataGridDatabaseMgr getInstance() {
		if ( null == instance ) {
			instance = new UIDataGridDatabaseMgr();
		}
		return instance;
	}

	public UIDataGridFormatter_i getDataGrid(String strDataGrid, String[] columnTypes, String[] columnHeaderStrings, String[] columnLabels, int[] columnWidth, int[] columnSort) {
		String function = "getDataGrid";
		logger.begin(function);
		UIDataGridFormatter_i dataGrid_i = null;
		dataGrid_i = new UIDataGridFormatter(strDataGrid, columnTypes, columnHeaderStrings, columnLabels, columnWidth, columnSort);
		logger.end(function);
		return dataGrid_i;
	}
}
