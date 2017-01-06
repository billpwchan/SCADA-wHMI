package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.soc;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;

public class UIDataGridFomatter implements UIDataGridFomatter_i {
	
//	private String[] columnTypes	= new String[]{"String", "String", "String"};
//	private String[] columnLabels	= new String[]{"SOCCard", "ScsEnvID", "Alias"};
//	private int[] columnWidth		= new int[]{50, 50, 50};
	
	private String[] columnTypes	= null;
	private String[] columnLabels	= null;
	private int[] columnWidth		= null;
	
	public UIDataGridFomatter (String[] columnTypes, String[] columnLabels, int[] columnWidth) {
		this.columnTypes = columnTypes;
		this.columnLabels = columnLabels;
		this.columnWidth = columnWidth;
	}
	
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
		return "Empty Label";
	}
	
	@Override
	public DataGrid<Equipment_i> addDataGridColumn(DataGrid<Equipment_i> dataGrid) {
	   
	    for ( int i = 0 ; i < columnLabels.length ; ++i ) {
	    	final String columnType = getColumnType(i);
	    	final String columnLabel = getColumnLabel(i);
	    	final int columnWidth = getColumnWidth(i);
	    	
			if ( columnType.equals("String") ) {
			    Column<Equipment_i, String> stringColumn = new Column<Equipment_i, String>(new TextCell()) {
				      @Override
				      public String getValue(Equipment_i object) {
				        return object.getStringValue(columnLabel);
				      }
				    };
//				    column.setSortable(true);
//				    sortHandler.setComparator(column, new Comparator<Equipment_i>() {
//				      @Override
//				      public int compare(Equipment_i o1, Equipment_i o2) {
//				        return o1.getValue().compareTo(o2.getValue());
//				      }
//				    });
				    dataGrid.addColumn(stringColumn, columnLabel);
				    dataGrid.setColumnWidth(stringColumn, columnWidth, Unit.PX);
			} else if ( columnType.equals("Number") ) {
			    Column<Equipment_i, Number> numberColumn = new Column<Equipment_i, Number>(new NumberCell()) {
			      @Override
			      public Number getValue(Equipment_i object) {
			        return object.getNumberValue(columnLabel);
			      }
			    };
//				    numberColumn.setSortable(true);
//				    sortHandler.setComparator(numberColumn, new Comparator<Equipment_i>() {
//				      @Override
//				      public int compare(Equipment_i o1, Equipment_i o2) {
//				        return o1.getValue().compareTo(o2.getValue());
//				      }
//				    });
			    dataGrid.addColumn(numberColumn, columnLabel);
			    dataGrid.setColumnWidth(numberColumn, columnWidth, Unit.PX);
			} else if ( columnType.equals("Boolean") ) {
			    Column<Equipment_i, Boolean> booleanColumn =
			        new Column<Equipment_i, Boolean>(new CheckboxCell(true, false)) {
			          @Override
			          public Boolean getValue(Equipment_i object) {
			            // Get the value from the selection model.
			        	  return object.getBooleanValue(columnLabel);
//			            return selectionModel.isSelected(object);
			          }
			        };
			    dataGrid.addColumn(booleanColumn, columnLabel);
			    dataGrid.setColumnWidth(booleanColumn, columnWidth, Unit.PX);
			}
	    }
	    
	    return dataGrid;
	}
}
