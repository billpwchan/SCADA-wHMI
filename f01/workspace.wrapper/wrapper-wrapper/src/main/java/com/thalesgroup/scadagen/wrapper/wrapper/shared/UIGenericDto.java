package com.thalesgroup.scadagen.wrapper.wrapper.shared;

import java.io.Serializable;

public class UIGenericDto implements UIGenericDto_i, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String data = null;
	@Override
	public String getData() { return data; }
	@Override
	public void setData(String data) { this.data = data; }

}
