package com.thalesgroup.scadagen.calculated;

import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GDGAlarmMessage extends GDGMessage {
	
	private final Logger s_logger		= LoggerFactory.getLogger(GDGAlarmMessage.class.getName());

	@Override
	public String getComputerId() {
		return this.getClass().getName();
	}
	
	public GDGAlarmMessage () {
		super();
    	
    	String classnames [] = getComputerId().split(Pattern.quote("."));
    	classname = classnames[classnames.length-1];
    	
    	s_logger.info("{} getComputerId()[{}]", classname, getComputerId());
    	s_logger.info("{} classname[{}]", classname, classname);
    	
    }
}
