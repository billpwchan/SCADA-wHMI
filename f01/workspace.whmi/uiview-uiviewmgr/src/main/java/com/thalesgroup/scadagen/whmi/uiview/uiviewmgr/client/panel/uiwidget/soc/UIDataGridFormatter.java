package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.soc;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.DataGrid;

public class UIDataGridFormatter implements UIDataGridFormatter_i {
	
//	private String[] columnTypes	= new String[]{"String", "String", "String"};
//	private String[] columnLabels	= new String[]{"SOCCard", "ScsEnvID", "Alias"};
//	private int[] columnWidth		= new int[]{50, 50, 50};
	
	private String[] columnTypes	= null;
	private String[] columnHeaderStrings	= null;
	private String[] columnLabels	= null;
	private int[] columnWidth		= null;
	private int[] columnSort		= null;
	private String strDataGrid		= null;
	
	private Map<String, String> columnLabelTypeMap = new HashMap<String, String>();
	
	public UIDataGridFormatter (String strDataGrid, String[] columnTypes, String[] columnHeaderStrings, String[] columnLabels, int[] columnWidth, int[] columnSort) {
		this.strDataGrid = strDataGrid;
		this.columnTypes = columnTypes;
		this.columnHeaderStrings = columnHeaderStrings;
		this.columnLabels = columnLabels;
		this.columnWidth = columnWidth;
		this.columnSort = columnSort;
	}
	
	@Override
	public int getNumberOfColumn() {
		return columnLabels.length;
	}
	
	@Override
	public String getColumnHeaderString(int column) {
		return columnHeaderStrings[column];
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
	public int getColumnSort(int column) {
		if (columnSort == null || column >= columnSort.length) {
			return 0;
		}
		return columnSort[column];
	}

	@Override
	public String getEmptyLabel() {
		return "Empty Label";
	}
	
	@Override
	public String getColumnType(String columnLabel) {
		return columnLabelTypeMap.get(columnLabel);
	}
	
	@Override
	public DataGrid<Equipment_i> addDataGridColumn(DataGrid<Equipment_i> dataGrid) {
	   
		// Attach a column sort handler to the ListDataProvider to sort the list.
	    ListHandler<Equipment_i> sortHandler =
	        new ListHandler<Equipment_i>(UIDataGridDatabase.getInstance(strDataGrid).getDataProvider().getList());

	    for ( int i = 0 ; i < columnLabels.length ; ++i ) {
	    	String columnType = getColumnType(i);
	    	
	    	String headerString = getColumnHeaderString(i);
	    	String columnLabel = getColumnLabel(i);
	    	
	    	int columnWidth = getColumnWidth(i);
	    	int enableSort = getColumnSort(i);

	    	columnLabelTypeMap.put(columnLabel, columnType);
	    	newColumn(sortHandler, dataGrid, columnType, headerString, columnLabel, columnWidth, enableSort);
	    }
	    
	    dataGrid.addColumnSortHandler(sortHandler);
	    
	    return dataGrid;
	}
	
	private void newColumn(
			ListHandler<Equipment_i> sortHandler
			, DataGrid<Equipment_i> dataGrid
			, final String columnType
			, final String headerString
			, final String columnLabel
			, final int columnWidth
			, final int enableSort) {
		
		if ( columnType.equals("String") ) {
			Column<Equipment_i, String> stringColumn = new Column<Equipment_i, String>(new TextCell()) {
				@Override
				public String getValue(Equipment_i object) {
					return object.getStringValue(columnLabel);
				}
			};
			    
			if (enableSort==1) {
				stringColumn.setSortable(true);
			} else {
				stringColumn.setSortable(false);
			}
	    
			sortHandler.setComparator(stringColumn, new Comparator<Equipment_i>() {
		    	@Override
		    	public int compare(Equipment_i o1, Equipment_i o2) {
		    		return o1.getValue(columnLabel).compareTo(o2.getValue(columnLabel));
		    	}
		    });
			    
			dataGrid.addColumn(stringColumn, headerString);
			dataGrid.setColumnWidth(stringColumn, columnWidth, Unit.PX);				    
		} else if ( columnType.equals("Number") ) {
			Column<Equipment_i, Number> numberColumn = new Column<Equipment_i, Number>(new NumberCell()) {
				@Override
				public Number getValue(Equipment_i object) {
					return object.getNumberValue(columnLabel);
				}
			};
		    
			if (enableSort==1) {
				numberColumn.setSortable(true);
			} else {
				numberColumn.setSortable(false);
			}

			sortHandler.setComparator(numberColumn, new Comparator<Equipment_i>() {
				@Override
				public int compare(Equipment_i o1, Equipment_i o2) {
					return o1.getValue(columnLabel).compareTo(o2.getValue(columnLabel));
				}
			});
			
			dataGrid.addColumn(numberColumn, headerString);
			dataGrid.setColumnWidth(numberColumn, columnWidth, Unit.PX);
		} else if ( columnType.equals("Boolean") ) {
			Column<Equipment_i, Boolean> booleanColumn = new Column<Equipment_i, Boolean>(new CheckboxCell(true, false)) {
				@Override
				public Boolean getValue(Equipment_i object) {
		    		// Get the value from the selection model.
		    		return object.getBooleanValue(columnLabel);
//		            return selectionModel.isSelected(object);
		    	}
			};
			dataGrid.addColumn(booleanColumn, headerString);
			dataGrid.setColumnWidth(booleanColumn, columnWidth, Unit.PX);
		}
	}
}
