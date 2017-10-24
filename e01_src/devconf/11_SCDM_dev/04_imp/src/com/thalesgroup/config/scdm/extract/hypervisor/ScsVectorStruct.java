/**
 * 
 */
package com.thalesgroup.config.scdm.extract.hypervisor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author T0126039
 * <p>This class describe a VectorStruct (VS) as defined in SCMM (Supervision Core Meta Model)</p>
 */
public class ScsVectorStruct {

	private String vectorName; ///< The VectorStruct Name
	private String vectorSysXmlId;
	
	/**
	 * <p>This {@link Map} contains the VS columns as keys and the corresponding {@link List} of values as {@link Map} values</p>
	 */
	private SortedMap<String, List<String>> columnValuesMap;
	
	/**
	 * @param vectorName The VectorStruct name
	 */
	public ScsVectorStruct(String vectorName, String vectorSysXmlId) {
		this.vectorName = vectorName;
		this.vectorSysXmlId = vectorSysXmlId;
		this.columnValuesMap = new TreeMap<String, List<String>>();
	}
	
	/**
	 * <p>
	 * Add a value in a column of the VectorStruct<br>
	 * If the column does not exist yet, it is created as new column of the VectorStruct
	 * </p>
	 * @param columnName The column name
	 * @param value The value to add
	 */
	public void addValue(String columnName, String value) {
		// Create column if not existing yet
		if(!columnValuesMap.containsKey(columnName)) {
			columnValuesMap.put(columnName, new ArrayList<String>());
		}
		
		List<String> columnValues = columnValuesMap.get(columnName);
		columnValues.add(value);
	}
	
	/**
	 * <p>Return the values of a column</p>
	 * @param columnName The column name to return values
	 * @return The values if column exists, <code>null</code> if column does not exist
	 */
	public List<String> getValues(String columnName) {
		if(columnValuesMap.containsKey(columnName)) {
			return columnValuesMap.get(columnName);
		}
		return null;
	}
	
	/**
	 * <p>Return the column name stored at the index columnIndex</p>
	 * @param columnIndex The column index
	 * @return The column name if found, <code>null</code> if index out of bound
	 */
	public String getColumn(int columnIndex) {
		if(columnIndex < 0)
			return null;
		
		Object[] array = this.columnValuesMap.keySet().toArray();
		if(array.length > columnIndex)
			return (String) array[columnIndex];
		
		return null;
	}
	
	/**
	 * <p>Return the values corresponding to the column named "columnName"</p>
	 * @param columnName The column name
	 * @return The {@link List} of values, <code>null</code> if column does not exist
	 */
	public List<String> getColumnValues(String columnName) {
		return columnValuesMap.get(columnName);
	}
	
	/**
	 * @return The column names
	 */
	public Set<String> getColumns() {
		return columnValuesMap.keySet();
	}
	
	/**
	 * @return The VS name
	 */
	public String getName() {
		return vectorName;
	}
	
	/**
	 * @return The VS length (Max column length)
	 */
	public int getLenght() {
		int lenght = 0;
		for(String column : columnValuesMap.keySet()) {
			int size = columnValuesMap.get(column).size();
			if(size > lenght)
				lenght = size;
		}
		return lenght;
	}
	
	public String getVectorSysXmlId() {
		return vectorSysXmlId;
	}

}
