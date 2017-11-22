package com.thalesgroup.scadagen.wrapper.wrapper.client.opm;

/**
 * Factory Class for the UIOpm
 * 
 * @author syau
 *
 */
public interface UIOpmFactory {
	
	/**
	 * Return the UIOpm_i instance by the input key
	 * 
	 * @param key Key to mapping the UIOpm Instance
	 * @return UIOpm_i related to the 
	 */
	UIOpm_i getOpm(String key);
}
