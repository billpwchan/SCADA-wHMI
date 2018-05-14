package com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.UIActionEventTargetAttribute;

public class UIEventActionProcessorCoreUtil {
	
	private final String className = this.getClass().getSimpleName();
	private final UILogger logger = UILoggerFactory.getInstance().getLogger(this.getClass().getName());
	
	public String getParameter(final String prefix, final UIEventAction uiEventAction, final String parameter) {
		final String function = prefix+" getParameter";
		logger.begin(className, function);
		logger.debug(className, function, "parameter[{}]", parameter);
		String parameterValue = null;
		if ( null != uiEventAction ) {
			parameterValue = (String) uiEventAction.getParameter(parameter);
		} else {
			logger.warn(className, function, "uiEventAction IS NULL");
		}
		logger.debug(className, function, "parameter[{}] parameterValue[{}]", parameter, parameterValue);
		logger.end(className, function);
		return parameterValue;
	}
	
	public String getOperationElement(final String prefix, final UIEventAction uiEventAction) {
		final String function = prefix+" getOperationElement";
		logger.begin(className, function);
		String operationElement = getParameter(prefix, uiEventAction, UIActionEventTargetAttribute.OperationElement.toString());
		logger.end(className, function);
		return operationElement;
	}
	
	public void dumpUIEventAction(final String prefix, final UIEventAction uiEventAction) {
		final String function = prefix+" dumpUIEventAction";
		logger.begin(className, function);
		if ( null != uiEventAction ) {
			for ( String key : uiEventAction.getParameterKeys() ) {
				Object object = uiEventAction.getParameter(key);
				if ( object instanceof String ) {
					String string = (String)object;
					logger.trace(className, function, "string[{}]", string);
				} else {
					logger.trace(className, function, "object[{}]", object);
				}
			}
		} else {
			logger.warn(className, function, "uiEventAction IS NULL", prefix);
		}
		logger.end(className, function);
	}
}
