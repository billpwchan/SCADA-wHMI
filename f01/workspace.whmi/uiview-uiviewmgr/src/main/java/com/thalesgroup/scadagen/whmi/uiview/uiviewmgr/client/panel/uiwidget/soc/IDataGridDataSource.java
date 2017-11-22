package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.soc;

import java.util.Map;

public interface IDataGridDataSource {
	void init(String strDataGrid, String[] scsEnvIds, String strDataGridOptsXMLFile, UIDataGridDatabase uiDataGridDatabase);

	void connect();
	
	void loadData(String scsEnvId, String dbaddress);
	
	void disconnect();

	void resetColumnData(String columnLabel, String columnType);
	
	void reloadColumnData(String[] columnLabels, String[] columnTypes, boolean[] enableTranslations);
	
	void changeColumnFilter(Map<String,String>filterMap);
}
