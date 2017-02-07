package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.soc.database;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.soc.UIDataGridFomatter;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.soc.UIDataGridFomatter_i;

public class UIDataGridDatabaseMgr {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIDataGridDatabaseMgr.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);

	private static UIDataGridDatabaseMgr instance = null;
	public static UIDataGridDatabaseMgr getInstance() {
		if ( null == instance ) {
			instance = new UIDataGridDatabaseMgr();
		}
		return instance;
	}

	public UIDataGridFomatter_i getDataGrid(String strDataGrid, String[] columnTypes, String[] columnLabels, int[] columnWidth) {
		String function = "getDataGrid";
		logger.begin(className, function);
		UIDataGridFomatter_i dataGrid_i = null;
		dataGrid_i = new UIDataGridFomatter(columnTypes, columnLabels, columnWidth);
		logger.end(className, function);
		return dataGrid_i;
	}
}
