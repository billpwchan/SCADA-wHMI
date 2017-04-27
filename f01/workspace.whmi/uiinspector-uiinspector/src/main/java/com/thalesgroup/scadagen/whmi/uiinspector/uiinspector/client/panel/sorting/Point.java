package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.panel.sorting;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;

public class Point {
	
	private final String className = UIWidgetUtil.getClassSimpleName(Point.class.getName());
	private final UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
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
			logger.debug(className, function, "alias[{}] attribute[{}] attributeValue[{}]", new Object[]{alias, attribute, attributeValue});
	}
}