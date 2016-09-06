package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.demo;

import java.util.Arrays;
import java.util.List;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.VerticalPanel;

public class CellTableAlarm {
	
	private static class Alarm {
	    private final String status;
	    private final String time;
	    private final String building;
	    private final String system;
	    private final String equipment;
	    private final String description;

	    public Alarm(String status, String time, String building
	    		, String system, String equipment, String description) {
	       this.status = status;
	       this.time = time;
	       this.building = building;
	       this.system = system;
	       this.equipment = equipment;
	       this.description = description;
	    }
	 }
	
	public VerticalPanel GetTablePanel(int numOfRow) {
		
//		String strAcked			= "Acked";
		String strUnAck			= "Unack";
		String strDatetime		= "yyyy-MM-dd hh:mm:ss";
		String strStn			= "XXX";
		String strSys			= "XXX";
		String strEquipment		= "XXXX-XXX-XXXX";
		String strDescription	= "--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------";
		
		Alarm[] alarms = new Alarm[numOfRow];
		for(int i=0;i<numOfRow;++i){
			alarms[i]=new Alarm(strUnAck, strDatetime, strStn, strSys, strEquipment, strDescription);
		}
		List<Alarm> ALARMS = Arrays.asList(alarms);

		// Create a CellTable.
		CellTable<Alarm> table = new CellTable<Alarm>();
		table.setWidth("100%");
		table.setHeight("100%");
//		table.getElement().getStyle().setBackgroundColor(UIPanelScreen.RGB_PAL_BG);
//		table.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		
		// Add a text column to show the Status.
		TextColumn<Alarm> statusColumn = 
		new TextColumn<Alarm>() {
		   @Override
		   public String getValue(Alarm object) {
		      return object.status;
		   }
		};
		table.addColumn(statusColumn, "Status");
		
		// Add a date column to show the time.
		/*
		DateCell dateCell = new DateCell();
		Column<Alarm, Date> timeColumn 
		= new Column<Alarm, Date>(dateCell) {
		   @Override
		   public Date getValue(Alarm object) {
		      return object.time;
		   }
		};
		table.addColumn(timeColumn, "Time");
		*/
		TextColumn<Alarm> timeColumn 
		= new TextColumn<Alarm>() {
		   @Override
		   public String getValue(Alarm object) {
		      return object.time;
		   }
		};
		table.addColumn(timeColumn, "Time");
		
		// Add a text column to show the building.
		TextColumn<Alarm> buildingColumn 
		= new TextColumn<Alarm>() {
		   @Override
		   public String getValue(Alarm object) {
		      return object.building;
		   }
		};
		table.addColumn(buildingColumn, "Building");
		
		// Add a text column to show the System.
		TextColumn<Alarm> systemColumn 
		= new TextColumn<Alarm>() {
		   @Override
		   public String getValue(Alarm object) {
		      return object.system;
		   }
		};
		table.addColumn(systemColumn, "System");
		
		// Add a text column to show the Equipment.
		TextColumn<Alarm> equipmentColumn 
		= new TextColumn<Alarm>() {
		   @Override
		   public String getValue(Alarm object) {
		      return object.equipment;
		   }
		};
		table.addColumn(equipmentColumn, "Equipment");
		
		// Add a text column to show the Description.
		TextColumn<Alarm> descriptionColumn 
		= new TextColumn<Alarm>() {
		   @Override
		   public String getValue(Alarm object) {
		      return object.description;
		   }
		};
		table.addColumn(descriptionColumn, "Description");
		
		// Add a selection model to handle user selection.
//		final SingleSelectionModel<Alarm> selectionModel 
//		= new SingleSelectionModel<Alarm>();
//		table.setSelectionModel(selectionModel);
//		selectionModel.addSelectionChangeHandler(
//		new SelectionChangeEvent.Handler() {
//		   public void onSelectionChange(SelectionChangeEvent event) {
//			   Alarm selected = selectionModel.getSelectedObject();
//		      if (selected != null) {
//		         Window.alert("You selected: " + selected.status);
//		      }
//		   }
//		});
		
		// Set the total row count. This isn't strictly necessary,
		// but it affects paging calculations, so its good habit to
		// keep the row count up to date.
		table.setRowCount(ALARMS.size(), true);
		
		// Push the data into the widget.
		table.setRowData(0, ALARMS);
		
		VerticalPanel panel = new VerticalPanel();
		panel.setWidth("100%");
		panel.setHeight("100%");
		panel.setBorderWidth(1);
		panel.add(table);
		
		return panel;
	}
}