package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common;

import java.util.Map.Entry;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIActionEventAttribute_i.WidgetExecuteAttribute;
import com.thalesgroup.scadagen.wrapper.wrapper.client.dpc.DCP_i.ValidityStatus;
import com.thalesgroup.scadagen.wrapper.wrapper.client.dpc.DpcMgr;

public class UIEventActionDpc {
	private final String className = UIWidgetUtil.getClassSimpleName(UIEventActionDpc.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	private String logPrefix = "";
	private String instance = "";
	
	public UIEventActionDpc ( String logPrefix, String instance ) {
		this.logPrefix = logPrefix;
		this.instance = instance;
	}
	
	public void execute(UIEventAction action) {
		final String function = logPrefix+" execute";
		logger.begin(className, function);
		
		String strAction			= (String) action.getParameter(WidgetExecuteAttribute.OperationString1.toString());
		String strEnvName			= (String) action.getParameter(WidgetExecuteAttribute.OperationString2.toString());
		String strAddress			= (String) action.getParameter(WidgetExecuteAttribute.OperationString3.toString());
		String strValidityStatus	= (String) action.getParameter(WidgetExecuteAttribute.OperationString4.toString());
		if ( logger.isInfoEnabled() ) {
			for ( Entry<String, Object> entry : action.getParameters() ) {
				String key = entry.getKey();
				Object obj = entry.getValue();
				logger.info(className, function, "key[{}] obj[{}]", key, obj);
			}
		}
		
		if ( strAction.equals("SendDpcAlarmInhibit") ) {
			ValidityStatus validityStatus = ValidityStatus.NO_ALARM_INHIBIT_VAR;
			if ( null != strValidityStatus && strValidityStatus.equals(ValidityStatus.ALARM_INHIBIT_VAR.toString()) ) {
				validityStatus = ValidityStatus.ALARM_INHIBIT_VAR;
			}

			String key = "changeEqpStatus" + "_" + className + "_"+ "alarminhibit" + "_" + validityStatus.toString() + "_" + strAddress;
				
			logger.info(className, function, "key[{}]", key);
			
			DpcMgr dpcMgr = DpcMgr.getInstance(instance);
			dpcMgr.sendChangeVarStatus(key, strEnvName, strAddress, validityStatus);
		}

		logger.end(className, function);
	}
}
