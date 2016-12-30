package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.soc;

import java.util.List;

import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ListDataProvider;

public class UIDataGridDatabase implements UIDataGridDatabase_i {

//	private static HashMap<String, UIDataGridDatabase> instances = new HashMap<String, UIDataGridDatabase>();
//
//	public static UIDataGridDatabase getInstance(String key) {
//		if ( ! instances.containsKey(key) ) { instances.put(key, new UIDataGridDatabase()); }
//		UIDataGridDatabase instance = instances.get(key);
//		return instance;
//	}
//	private UIDataGridDatabase() {}
	
//	public static UIDataGridDatabase getInstance(String key) {
//		if ( null == instance ) {
//			instance = new UIDataGridDatabase();
//		}
//		return instance;
//	}

	/**
	 * The provider that holds the list of contacts in the database.
	 */
	private ListDataProvider<Equipment_i> dataProvider = new ListDataProvider<Equipment_i>();

	/**
	 * Add a new contact.
	 * 
	 * @param equipment
	 *            the contact to add.
	 */
	@Override
	public void addEquipment(Equipment_i equipment) {
		List<Equipment_i> equipments = dataProvider.getList();
		// Remove the contact first so we don't add a duplicate.
		equipments.remove(equipment);
		equipments.add(equipment);
	}

	/**
	 * Add a display to the database. The current range of interest of the
	 * display will be populated with data.
	 * 
	 * @param display a {@Link HasData}.
	 */
	@Override
	public void addDataDisplay(HasData<Equipment_i> display) {
		dataProvider.addDataDisplay(display);
	}

	@Override
	public ListDataProvider<Equipment_i> getDataProvider() {
		return dataProvider;
	}

	/**
	 * Refresh all displays.
	 */
	@Override
	public void refreshDisplays() {
		dataProvider.refresh();
	}
}
