package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.panel.sorting;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;

public class Point {

	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
	
	String alias;
	String aliasWithAttribute;
	
	String attribute;
	int attributeValue;
	
	public Point(String alias, String hmiOrderAttribute) {
		final String function = "Point";
		
		this.alias=alias;
		this.attribute=hmiOrderAttribute;
		
		this.aliasWithAttribute = this.alias + this.attribute;
		
		if ( logger.isDebugEnabled() )
			logger.debug(function, "alias[{}] attribute[{}] attributeValue[{}]", new Object[]{alias, attribute, attributeValue});
	}
}
