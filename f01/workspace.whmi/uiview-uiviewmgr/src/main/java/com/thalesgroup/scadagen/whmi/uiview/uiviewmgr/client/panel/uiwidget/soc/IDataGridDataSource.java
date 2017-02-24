package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.soc;

public interface IDataGridDataSource {
	void init(String[] scsEnvIds, String strDataGridOptsXMLFile, UIDataGridDatabase uiDataGridDatabase);

	void connect();
	
	void loadData(String scsEnvId, String dbaddress);
	
	void disconnect();

	void resetColumnData(String columnLabel, String columnType);
	
	void reloadColumnData(String columnLabel, String columnType, boolean enableTranslation);
}
