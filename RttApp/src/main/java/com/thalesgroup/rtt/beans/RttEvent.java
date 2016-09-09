package com.thalesgroup.rtt.beans;

import org.springframework.context.ApplicationEvent;


public class RttEvent extends ApplicationEvent
{
  
	private static final long serialVersionUID = 1L;
	
	private int eventType;
	
	private String subId;
	
	private String field;
	
	private String hvId;

	private String value;
	
    private String time;
	
	public RttEvent(Object source, int eventType, String subId, String field, String hvId, String time, String value) {
		super(source);
		this.eventType = eventType;
		this.subId = subId;
		this.field = field;
		this.hvId = hvId;
		this.time = time;
		this.value = value;
	}

 
	public String getValue()
	{
		return this.value;
	}
  
	public void setValue(String value)
	{
		this.value = value;
	}


	public String getTime() {
		return time;
	}


	public void setTime(String time) {
		this.time = time;
	}


	public String getSubId() {
		return subId;
	}


	public void setSubId(String subId) {
		this.subId = subId;
	}


	public String getField() {
		return field;
	}


	public void setField(String field) {
		this.field = field;
	}


	public String getHvId() {
		return hvId;
	}


	public void setHvId(String hvId) {
		this.hvId = hvId;
	}

	public int getEventType() {
		return eventType;
	}


	public void setEventType(int eventType) {
		this.eventType = eventType;
	}
	
}
