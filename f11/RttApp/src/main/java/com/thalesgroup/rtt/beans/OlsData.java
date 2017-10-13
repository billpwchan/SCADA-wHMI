package com.thalesgroup.rtt.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OlsData {
	private String name;
	private String value;
	private String valueType;
	private String quality;
	private String id;
	private String scstime;
	private String timestamp;
	
	@JsonProperty("Name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@JsonProperty("Value")
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	@JsonProperty("ValueType")
	public String getValueType() {
		return valueType;
	}
	public void setValueType(String valueType) {
		this.valueType = valueType;
	}
	@JsonProperty("Quality")
	public String getQuality() {
		return quality;
	}
	public void setQuality(String quality) {
		this.quality = quality;
	}
	@JsonProperty("ID")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@JsonProperty("SCSTime")
	public String getScstime() {
		return scstime;
	}
	public void setScstime(String scstime) {
		this.scstime = scstime;
	}
	@JsonProperty("Timestamp")
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
}
