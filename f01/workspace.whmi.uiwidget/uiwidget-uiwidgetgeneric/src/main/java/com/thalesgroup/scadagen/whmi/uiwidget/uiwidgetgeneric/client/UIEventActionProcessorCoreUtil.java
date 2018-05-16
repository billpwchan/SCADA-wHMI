package com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.UIActionEventTargetAttribute;

public class UIEventActionProcessorCoreUtil {

	private final UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
	
	public String getParameter(final String prefix, final UIEventAction uiEventAction, final String parameter) {
		final String function = prefix+" getParameter";
		logger.begin(function);
		logger.debug(function, "parameter[{}]", parameter);
		String parameterValue = null;
		if ( null != uiEventAction ) {
			parameterValue = (String) uiEventAction.getParameter(parameter);
		} else {
			logger.warn(function, "uiEventAction IS NULL");
		}
		logger.debug(function, "parameter[{}] parameterValue[{}]", parameter, parameterValue);
		logger.end(function);
		return parameterValue;
	}
	
	public String getOperationElement(final String prefix, final UIEventAction uiEventAction) {
		final String function = prefix+" getOperationElement";
		logger.begin(function);
		String operationElement = getParameter(prefix, uiEventAction, UIActionEventTargetAttribute.OperationElement.toString());
		logger.end(function);
		return operationElement;
	}
	
	public void dumpUIEventAction(final String prefix, final UIEventAction uiEventAction) {
		final String function = prefix+" dumpUIEventAction";
		logger.begin(function);
		if ( null != uiEventAction ) {
			for ( String key : uiEventAction.getParameterKeys() ) {
				Object object = uiEventAction.getParameter(key);
				if ( object instanceof String ) {
					String string = (String)object;
					logger.trace(function, "string[{}]", string);
				} else {
					logger.trace(function, "object[{}]", object);
				}
			}
		} else {
			logger.warn(function, "uiEventAction IS NULL", prefix);
		}
		logger.end(function);
	}
}
