package com.thalesgroup.scadagen.whmi.config.config.shared;

import java.util.ArrayList;
import java.util.HashMap;

public class Configs implements Config_i, java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 515123616962324773L;
	
	public Configs() {}
	public Configs ( ArrayList<Config_i> configs) { this.configs = new ArrayList<Config_i>(configs); }

	private HashMap<String, String> headMap = new HashMap<String, String>();
	public Object getHeader(String key) { return this.headMap.get(key); }
	public void setHeader(String key, String value) { this.headMap.put(key, value); }

	private ArrayList<Config_i> configs = new ArrayList<Config_i>();
	public int getObjectSize() { return this.configs.size(); }
	public void setConfig(Config_i config) { this.configs.add(config); }
	public Config_i getConfig(int i){ return this.configs.get(i); }

}
