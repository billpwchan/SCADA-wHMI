package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common;

import java.util.HashMap;
import java.util.Map.Entry;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIActionEventAttribute_i.ActionAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.wrapper.wrapper.client.dpc.DCP_i.TaggingStatus;
import com.thalesgroup.scadagen.wrapper.wrapper.client.dpc.DpcMgr;

public class UIEventActionDpc extends UIEventActionExecute_i {
	private final String className = UIWidgetUtil.getClassSimpleName(UIEventActionDpc.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);

	private final String strSendChangeValueStatus	= "SendChangeValueStatus";
	private final String strSendChangeEqpTag		= "SendChangeEqpTag";
	
	public UIEventActionDpc ( ) {
		supportedActions = new String[] {strSendChangeValueStatus, strSendChangeEqpTag};
	}
	
	@Override
	public void executeAction(UIEventAction action, HashMap<String, HashMap<String, Object>> override) {
		final String function = logPrefix+" executeAction";
		logger.begin(className, function);
		
		String strAction			= (String) action.getParameter(ActionAttribute.OperationString1.toString());
		String strEnvName			= (String) action.getParameter(ActionAttribute.OperationString2.toString());
		String strAddress			= (String) action.getParameter(ActionAttribute.OperationString3.toString());
		String strStatus			= (String) action.getParameter(ActionAttribute.OperationString4.toString());
		String strTaggingLabel1		= (String) action.getParameter(ActionAttribute.OperationString5.toString());
		String strTaggingLabel2		= (String) action.getParameter(ActionAttribute.OperationString6.toString());
		if ( logger.isInfoEnabled() ) {
			for ( Entry<String, Object> entry : action.getParameters() ) {
				String key = entry.getKey();
				Object obj = entry.getValue();
				logger.info(className, function, "key[{}] obj[{}]", key, obj);
			}
		}
		
		if ( strAction.equals(strSendChangeValueStatus) ) {

			int intStatus = 0;
			boolean isValid = false;
			try {
				intStatus = Integer.parseInt(strStatus);
				isValid = true;
			} catch ( NumberFormatException ex ) {
				logger.warn(className, function, "strStatus NumberFormatException", strStatus);
			}
			
			if ( isValid ) {

				String key = strSendChangeValueStatus + "_" + className + "_"+ "alarminhibit" + "_" + intStatus + "_" + strAddress;
				
				logger.info(className, function, "key[{}]", key);
				
				DpcMgr dpcMgr = DpcMgr.getInstance(instance);
				dpcMgr.sendChangeVarStatus(key, strEnvName, strAddress, intStatus);
			} else {
				logger.warn(className, function, "command details IS INVALID");
			}
		} else if ( strAction.equals(strSendChangeEqpTag) ) {

			TaggingStatus taggingStatus = TaggingStatus.NO_TAGGING;
			if ( strStatus.equalsIgnoreCase(TaggingStatus.NO_TAGGING.toString()) ) {
				taggingStatus = TaggingStatus.NO_TAGGING;
			} else if ( strStatus.equalsIgnoreCase(TaggingStatus.ALL_TAGGING.toString()) ) {
				taggingStatus = TaggingStatus.ALL_TAGGING;
			} else if ( strStatus.equalsIgnoreCase(TaggingStatus.TAGGING_1.toString()) ) {
				taggingStatus = TaggingStatus.TAGGING_1;
			} else if ( strStatus.equalsIgnoreCase(TaggingStatus.TAGGING_2.toString()) ) {
				taggingStatus = TaggingStatus.TAGGING_2;
			}

			String key = strSendChangeEqpTag + "_" + className + "_"+ "dpctag" + "_" + taggingStatus.toString() + "_" + strAddress;
					
			logger.info(className, function, "key[{}]", key);
				
			DpcMgr dpcMgr = DpcMgr.getInstance(instance);
			dpcMgr.sendChangeEqpTag(key, strEnvName, strAddress, taggingStatus, strTaggingLabel1, strTaggingLabel2);

		}
		logger.end(className, function);
	}
}
