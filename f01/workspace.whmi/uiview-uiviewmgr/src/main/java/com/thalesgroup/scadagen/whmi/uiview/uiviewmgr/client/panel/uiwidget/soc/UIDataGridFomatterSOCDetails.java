package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.soc;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;

public class UIDataGridFomatterSOCDetails implements UIDataGridFomatter_i {
	
	public enum Header {
		  Step("Step")
		, EquipmentLabel("Equipment Label")
		, Action("Action")
		, Status("Status")
		, Reserved("Reserved")
		, PTW("PTW")
		, LR("L/R")
		, Disable("Disable")
		;
		private final String text;
		private Header(final String text) { this.text = text; }
		public boolean equalsName(String otherName) { return ( otherName == null ) ? false : text.equals(otherName); }
		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() { return this.text; }

	}
	
	private int[] columnWidth	= new int[] {80, 80, 80, 80, 80, 80, 80, 80};
	private String[] columnTypes	= new String[] {"int", "String", "String", "String", "String", "String", "String", "boolean"};
	private String[] columnLabels	= new String[] {Header.Step.toString(), Header.EquipmentLabel.toString(), Header.Action.toString(), Header.Status.toString(), Header.Reserved.toString(), Header.PTW.toString(), Header.LR.toString(), Header.Disable.toString()};
	
	@Override
	public int getNumberOfColumn() {
		return columnLabels.length;
	}
	
	@Override
	public String getColumnLabel(int column) {
		return columnLabels[column];
	}
	
	@Override
	public String getColumnType(int column) {
		return columnTypes[column];
	}
	
	@Override
	public int getColumnWidth(int column) {
		return columnWidth[column];
	}

	@Override
	public String getEmptyLabel() {
		// TODO Auto-generated method stub
		return "Empty Label";
	}
	
	@Override
	public DataGrid<Equipment_i> addDataGridColumn(DataGrid<Equipment_i> dataGrid) {
	    int counter = 0;
	    // Step.
	    Column<Equipment_i, Number> stepColumn = new Column<Equipment_i, Number>(new NumberCell()) {
	      @Override
	      public Number getValue(Equipment_i object) {
	        return object.getNumberValue(Header.Step.toString());
	      }
	    };
//	    ageColumn.setSortable(true);
//	    sortHandler.setComparator(stepColumn, new Comparator<Equipment>() {
//	      @Override
//	      public int compare(Equipment o1, Equipment o2) {
//	        return o1.getBirthday().compareTo(o2.getBirthday());
//	      }
//	    });
	    dataGrid.addColumn(stepColumn, getColumnLabel(counter));
	    dataGrid.setColumnWidth(stepColumn, getColumnWidth(counter), Unit.PX);
	    

	    counter++;
	    // Equipment Label
	    Column<Equipment_i, String> equipmentLabelColumn = new Column<Equipment_i, String>(new TextCell()) {
	      @Override
	      public String getValue(Equipment_i object) {
	        return object.getStringValue(Header.EquipmentLabel.toString());
	      }
	    };
//	    equipmentLabelColumn.setSortable(true);
//	    sortHandler.setComparator(addressColumn, new Comparator<Equipment>() {
//	      @Override
//	      public int compare(Equipment o1, Equipment o2) {
//	        return o1.getAddress().compareTo(o2.getAddress());
//	      }
//	    });
	    dataGrid.addColumn(equipmentLabelColumn, getColumnLabel(counter));
	    dataGrid.setColumnWidth(equipmentLabelColumn, getColumnWidth(counter), Unit.PX);
	    
	    
	    counter++;
	    // Action
	    Column<Equipment_i, String> actionColumn = new Column<Equipment_i, String>(new TextCell()) {
	      @Override
	      public String getValue(Equipment_i object) {
	        return object.getStringValue(Header.Action.toString());
	      }
	    };
//	    actionColumn.setSortable(true);
//	    sortHandler.setComparator(addressColumn, new Comparator<Equipment>() {
//	      @Override
//	      public int compare(Equipment o1, Equipment o2) {
//	        return o1.getAddress().compareTo(o2.getAddress());
//	      }
//	    });
	    dataGrid.addColumn(actionColumn, getColumnLabel(counter));
	    dataGrid.setColumnWidth(actionColumn, getColumnWidth(counter), Unit.PX);
	    
	    
	    counter++;
	    // Status
	    Column<Equipment_i, String> statusColumn = new Column<Equipment_i, String>(new TextCell()) {
	      @Override
	      public String getValue(Equipment_i object) {
	        return object.getStringValue(Header.Status.toString());
	      }
	    };
//	    statusColumn.setSortable(true);
//	    sortHandler.setComparator(addressColumn, new Comparator<Equipment>() {
//	      @Override
//	      public int compare(Equipment o1, Equipment o2) {
//	        return o1.getAddress().compareTo(o2.getAddress());
//	      }
//	    });
	    dataGrid.addColumn(statusColumn, getColumnLabel(counter));
	    dataGrid.setColumnWidth(statusColumn, getColumnWidth(counter), Unit.PX);
	    
	    
	    counter++;
	    // Reserved
	    Column<Equipment_i, String> reservedColumn = new Column<Equipment_i, String>(new TextCell()) {
	      @Override
	      public String getValue(Equipment_i object) {
	        return object.getStringValue(Header.Reserved.toString());
	      }
	    };
//	    reservedColumn.setSortable(true);
//	    sortHandler.setComparator(reservedColumn, new Comparator<Equipment>() {
//	      @Override
//	      public int compare(Equipment o1, Equipment o2) {
//	        return o1.getAddress().compareTo(o2.getAddress());
//	      }
//	    });
	    dataGrid.addColumn(reservedColumn, getColumnLabel(counter));
	    dataGrid.setColumnWidth(reservedColumn, getColumnWidth(counter), Unit.PX);
	    
	    
	    counter++;
	    // PTW
	    Column<Equipment_i, String> ptwColumn = new Column<Equipment_i, String>(new TextCell()) {
	      @Override
	      public String getValue(Equipment_i object) {
	        return object.getStringValue(Header.PTW.toString());
	      }
	    };
//	    reservedColumn.setSortable(true);
//	    sortHandler.setComparator(ptwColumn, new Comparator<Equipment>() {
//	      @Override
//	      public int compare(Equipment o1, Equipment o2) {
//	        return o1.getAddress().compareTo(o2.getAddress());
//	      }
//	    });
	    dataGrid.addColumn(ptwColumn, getColumnLabel(counter));
	    dataGrid.setColumnWidth(ptwColumn, getColumnWidth(counter), Unit.PX);
	    
	    
	    counter++;
	    // L/R
	    Column<Equipment_i, String> lrColumn = new Column<Equipment_i, String>(new TextCell()) {
	      @Override
	      public String getValue(Equipment_i object) {
	        return object.getStringValue(Header.LR.toString());
	      }
	    };
//	    lrColumn.setSortable(true);
//	    sortHandler.setComparator(lrColumn, new Comparator<Equipment>() {
//	      @Override
//	      public int compare(Equipment o1, Equipment o2) {
//	        return o1.getAddress().compareTo(o2.getAddress());
//	      }
//	    });
	    dataGrid.addColumn(lrColumn, getColumnLabel(counter));
	    dataGrid.setColumnWidth(lrColumn, getColumnWidth(counter), Unit.PX);
	    
	    
	    counter++;
	    // Checkbox column. This table will uses a checkbox column for selection.
	    // Alternatively, you can call dataGrid.setSelectionEnabled(true) to enable
	    // mouse selection.
	    Column<Equipment_i, Boolean> disableColumn =
	        new Column<Equipment_i, Boolean>(new CheckboxCell(true, false)) {
	          @Override
	          public Boolean getValue(Equipment_i object) {
	            // Get the value from the selection model.
	        	  return object.getBooleanValue(Header.Disable.toString());
//	            return selectionModel.isSelected(object);
	          }
	        };
	    dataGrid.addColumn(disableColumn, getColumnLabel(counter));
	    dataGrid.setColumnWidth(disableColumn, getColumnWidth(counter), Unit.PX);
	    
	    return dataGrid;
	}
}
