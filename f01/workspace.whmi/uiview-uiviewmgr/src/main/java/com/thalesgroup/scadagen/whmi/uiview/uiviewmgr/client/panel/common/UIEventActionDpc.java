package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common;

import java.util.Map;
import java.util.Map.Entry;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.ActionAttribute;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionDpc_i.UIEventActionDpcAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionExecute_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.dpc.DCP_i.TaggingStatus;
import com.thalesgroup.scadagen.wrapper.wrapper.client.dpc.DpcMgr;

public class UIEventActionDpc extends UIEventActionExecute_i {
	private final String className = this.getClass().getSimpleName();
	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
	
	public UIEventActionDpc ( ) {
		supportedActions = new String[] {
				UIEventActionDpcAction.SendChangeValueStatus.toString()
				, UIEventActionDpcAction.SendChangeEqpTag.toString()
				};
	}
	
	@Override
	public boolean executeAction(UIEventAction action, Map<String, Map<String, Object>> override) {
		final String function = logPrefix+" executeAction";
		logger.begin(function);
		
		boolean bContinue = true;
		
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
				logger.info(function, "key[{}] obj[{}]", key, obj);
			}
		}
		
		if ( strAction.equals(UIEventActionDpcAction.SendChangeValueStatus.toString()) ) {

			int intStatus = 0;
			boolean isValid = false;
			try {
				intStatus = Integer.parseInt(strStatus);
				isValid = true;
			} catch ( NumberFormatException ex ) {
				logger.warn(function, "strStatus NumberFormatException", strStatus);
			}
			
			if ( isValid ) {

				String key = UIEventActionDpcAction.SendChangeValueStatus.toString() + "_" + className + "_"+ "alarminhibit" + "_" + intStatus + "_" + strAddress;
				
				logger.info(function, "key[{}]", key);
				
				DpcMgr dpcMgr = DpcMgr.getInstance(instance);
				dpcMgr.sendChangeVarStatus(key, strEnvName, strAddress, intStatus);
			} else {
				logger.warn(function, "command details IS INVALID");
			}
		} else if ( strAction.equals(UIEventActionDpcAction.SendChangeEqpTag.toString()) ) {

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

			String key = UIEventActionDpcAction.SendChangeEqpTag.toString() + "_" + className + "_"+ "dpctag" + "_" + taggingStatus.toString() + "_" + strAddress;
					
			logger.info(function, "key[{}]", key);
				
			DpcMgr dpcMgr = DpcMgr.getInstance(instance);
			dpcMgr.sendChangeEqpTag(key, strEnvName, strAddress, taggingStatus, strTaggingLabel1, strTaggingLabel2);

		}
		
		logger.end(function);
		return bContinue;
	}
}
