package com.thalesgroup.scadagen.calculated;

import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GDGEventMessage extends GDGMessage {
	
	private static final Logger s_logger		= LoggerFactory.getLogger(GDGEventMessage.class.getName());
	
	@Override
	public String getComputerId() {
		return this.getClass().getName();
	}

	public GDGEventMessage () {
		super();
    	
    	String classnames [] = getComputerId().split(Pattern.quote("."));
    	propertiesname = classnames[classnames.length-1];
    	
    	s_logger.info(propertiesname+" getComputerId()["+getComputerId()+"]");
    	s_logger.info(propertiesname+" propertiesname["+propertiesname+"]");
    	
    }
}
