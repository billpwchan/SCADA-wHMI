package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.soc;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;

public class UIDataGridFomatterSOC implements UIDataGridFomatter_i {
	
	public enum Header {
		  SOCCard("SOCCard")
		, Alias("Alias")
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

	private int[] columnWidth		= new int[]{50, 50};
	private String[] columnTypes	= new String[]{"String", "String"};
	private String[] columnLabels	= new String[]{Header.SOCCard.toString(), Header.Alias.toString()};
	
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
		// TODO Auto-generated method stub
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
	    // SOC Card
	    Column<Equipment_i, String> socCardColumn = new Column<Equipment_i, String>(new TextCell()) {
	      @Override
	      public String getValue(Equipment_i object) {
	        return object.getStringValue(Header.SOCCard.toString());
	      }
	    };
//	    equipmentLabelColumn.setSortable(true);
//	    sortHandler.setComparator(addressColumn, new Comparator<Equipment>() {
//	      @Override
//	      public int compare(Equipment o1, Equipment o2) {
//	        return o1.getAddress().compareTo(o2.getAddress());
//	      }
//	    });
	    dataGrid.addColumn(socCardColumn, getColumnLabel(counter));
	    dataGrid.setColumnWidth(socCardColumn, getColumnWidth(counter), Unit.PX);
	    
	    
	    counter++;
	    // SOC Card
	    Column<Equipment_i, String> aliasColumn = new Column<Equipment_i, String>(new TextCell()) {
	      @Override
	      public String getValue(Equipment_i object) {
	        return object.getStringValue(Header.Alias.toString());
	      }
	    };
//	    equipmentLabelColumn.setSortable(true);
//	    sortHandler.setComparator(addressColumn, new Comparator<Equipment>() {
//	      @Override
//	      public int compare(Equipment o1, Equipment o2) {
//	        return o1.getAddress().compareTo(o2.getAddress());
//	      }
//	    });
	    dataGrid.addColumn(aliasColumn, getColumnLabel(counter));
	    dataGrid.setColumnWidth(aliasColumn, getColumnWidth(counter), Unit.PX);

	    return dataGrid;
	}


	
}
