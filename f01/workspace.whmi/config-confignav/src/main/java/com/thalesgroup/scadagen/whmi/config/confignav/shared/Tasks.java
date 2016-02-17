package com.thalesgroup.scadagen.whmi.config.confignav.shared;

import java.util.ArrayList;

public class Tasks extends ArrayList<Task> implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7923135168434815894L;
	
	private int parentLevel;
	private String parentHeader;
	
	public Tasks() {}

	public int getParentLevel() { return parentLevel; }
	public void setParentLevel(int parentLevel) { this.parentLevel = parentLevel; }
	public String getParentHeader() { return parentHeader; }
	public void setParentHeader(String parentHeader) { this.parentHeader = parentHeader; }
}
