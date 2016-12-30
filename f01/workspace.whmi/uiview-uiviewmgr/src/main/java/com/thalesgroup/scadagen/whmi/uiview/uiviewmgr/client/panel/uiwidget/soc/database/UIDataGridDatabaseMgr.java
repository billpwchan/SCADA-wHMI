package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.soc.database;

import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.soc.UIDataGridFomatterSOC;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.soc.UIDataGridFomatterSOCDetails;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.soc.UIDataGridFomatter_i;

public class UIDataGridDatabaseMgr {

	private static UIDataGridDatabaseMgr instance = null;
	public static UIDataGridDatabaseMgr getInstance() {
		if ( null == instance ) {
			instance = new UIDataGridDatabaseMgr();
		}
		return instance;
	}
	
	public UIDataGridFomatter_i getDataGrid(String key) {
		UIDataGridFomatter_i dataGrid_i = null;
		if ( key.equals(UIWidgetUtil.getClassSimpleName(UIDataGridFomatterSOC.class.getName())) ) {
			dataGrid_i = new UIDataGridFomatterSOC();
		} else if ( key.equals(UIWidgetUtil.getClassSimpleName(UIDataGridFomatterSOCDetails.class.getName())) ) {
			dataGrid_i = new UIDataGridFomatterSOCDetails();
		}
		return dataGrid_i;
	}

}
