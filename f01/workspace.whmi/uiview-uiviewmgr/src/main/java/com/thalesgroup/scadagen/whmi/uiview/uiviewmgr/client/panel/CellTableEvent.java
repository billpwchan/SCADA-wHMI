package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel;

import java.util.Arrays;
import java.util.List;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.VerticalPanel;

public class CellTableEvent {

	private static class Event {
		
	    private final String time;
	    private final String building;
	    private final String system;
	    private final String equipment;
	    private final String description;

	    public Event(String time, String building
	    		, String system, String equipment, String description) {
	    	
	       this.time = time;
	       this.building = building;
	       this.system = system;
	       this.equipment = equipment;
	       this.description = description;
	    }
	 }

	public VerticalPanel GetTablePanel(int numOfRow) {
		
		
		
		String strDatetime		= "yyyy-MM-dd hh:mm:ss";
		String strStn			= "XXX";
		String strSys			= "XXX";
		String strEquipment		= "XXXX-XXX-XXXX";
		String strDescription	= "-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------";

		Event[] events = new Event[numOfRow];
		for(int i=0;i<numOfRow;++i){
			events[i]=new Event(strDatetime, strStn, strSys, strEquipment, strDescription);
		}
		List<Event> EVENTS = Arrays.asList(events);
	
		// Create a CellTable.
		CellTable<Event> table = new CellTable<Event>();
		table.setWidth("100%");
		table.setHeight("100%");
//		table.getElement().getStyle().setBackgroundColor(UIPanelScreen.RGB_PAL_BG);
//		table.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		
		// Add a text column to show the Status.
//		TextColumn<Event> statusColumn = 
//		new TextColumn<Event>() {
//		   @Override
//		   public String getValue(Event object) {
//		      return object.status;
//		   }
//		};
//		table.addColumn(statusColumn, "Status");
		
		// Add a date column to show the time.
		/*
		DateCell dateCell = new DateCell();
		Column<Event, Date> timeColumn 
		= new Column<Event, Date>(dateCell) {
		   @Override
		   public Date getValue(Event object) {
		      return object.time;
		   }
		};
		table.addColumn(timeColumn, "Time");
		*/
		TextColumn<Event> timeColumn 
		= new TextColumn<Event>() {
		   @Override
		   public String getValue(Event object) {
		      return object.time;
		   }
		};
		table.addColumn(timeColumn, "Time");
		
		// Add a text column to show the building.
		TextColumn<Event> buildingColumn 
		= new TextColumn<Event>() {
		   @Override
		   public String getValue(Event object) {
		      return object.building;
		   }
		};
		table.addColumn(buildingColumn, "Building");
		
		// Add a text column to show the System.
		TextColumn<Event> systemColumn 
		= new TextColumn<Event>() {
		   @Override
		   public String getValue(Event object) {
		      return object.system;
		   }
		};
		table.addColumn(systemColumn, "System");
		
		// Add a text column to show the Equipment.
		TextColumn<Event> equipmentColumn 
		= new TextColumn<Event>() {
		   @Override
		   public String getValue(Event object) {
		      return object.equipment;
		   }
		};
		table.addColumn(equipmentColumn, "Equipment");
		
		// Add a text column to show the Description.
		TextColumn<Event> descriptionColumn 
		= new TextColumn<Event>() {
		   @Override
		   public String getValue(Event object) {
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
		table.setRowCount(EVENTS.size(), true);
		
		// Push the data into the widget.
		table.setRowData(0, EVENTS);
		
		VerticalPanel panel = new VerticalPanel();
		panel.setWidth("100%");
		panel.setHeight("100%");
		panel.setBorderWidth(1);
		panel.add(table);
		
		return panel;
	}
}