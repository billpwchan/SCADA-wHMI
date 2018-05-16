package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common;

import java.util.Map;
import java.util.Map.Entry;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.ActionAttribute;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionGrc_i.UIEventActionGrcAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionExecute_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.ctl.GrcMgr;

public class UIEventActionGrc extends UIEventActionExecute_i {
	private final String className = this.getClass().getSimpleName();
	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
	
	public UIEventActionGrc ( ) {
		supportedActions = new String[] {
				  UIEventActionGrcAction.GetGrcList.toString()
				, UIEventActionGrcAction.GetGrcState.toString()
				, UIEventActionGrcAction.PrepareGrc.toString()
				, UIEventActionGrcAction.AbortGrcPreparation.toString()
				, UIEventActionGrcAction.LaunchGrc.toString()
				, UIEventActionGrcAction.AbortGrc.toString()
				, UIEventActionGrcAction.SuspendGrc.toString()
				, UIEventActionGrcAction.ResumeGrc.toString()
		};
	}
	
	@Override
	public boolean executeAction(UIEventAction action, Map<String, Map<String, Object>> override) {
		final String function = logPrefix+" executeAction";
		logger.begin(function);
		
		boolean bContinue = true;
		
		String strAction			= (String) action.getParameter(ActionAttribute.OperationString1.toString());
		
		if ( logger.isInfoEnabled() ) {
			for ( Entry<String, Object> entry : action.getParameters() ) {
				String key = entry.getKey();
				Object obj = entry.getValue();
				logger.info(function, "key[{}] obj[{}]", key, obj);
			}
		}
		
		GrcMgr grcMgr = GrcMgr.getInstance(className);
		
		if ( strAction.equalsIgnoreCase(UIEventActionGrcAction.GetGrcList.toString()) ) {
			
			String strKey				= (String) action.getParameter(ActionAttribute.OperationString2.toString());
			String strScsEnvId			= (String) action.getParameter(ActionAttribute.OperationString3.toString());
			
			grcMgr.getGrcList(strKey, strScsEnvId);
			
		} else if ( strAction.equalsIgnoreCase(UIEventActionGrcAction.GetGrcState.toString()) ) {
			
			String strKey				= (String) action.getParameter(ActionAttribute.OperationString2.toString());
			String strScsEnvId			= (String) action.getParameter(ActionAttribute.OperationString3.toString());
			String strName				= (String) action.getParameter(ActionAttribute.OperationString4.toString());
			
			grcMgr.getGrcState(strKey, strScsEnvId, strName);
			
		} else if ( strAction.equalsIgnoreCase(UIEventActionGrcAction.PrepareGrc.toString()) ) {
			
			String strKey				= (String) action.getParameter(ActionAttribute.OperationString2.toString());
			String strScsEnvId			= (String) action.getParameter(ActionAttribute.OperationString3.toString());
			String strName				= (String) action.getParameter(ActionAttribute.OperationString4.toString());
			
			grcMgr.prepareGrc(strKey, strScsEnvId, strName);

		} else if ( strAction.equalsIgnoreCase(UIEventActionGrcAction.AbortGrcPreparation.toString()) ) {
			
			String strKey				= (String) action.getParameter(ActionAttribute.OperationString2.toString());
			String strScsEnvId			= (String) action.getParameter(ActionAttribute.OperationString3.toString());
			String strName				= (String) action.getParameter(ActionAttribute.OperationString4.toString());
			
			grcMgr.abortGrcPreparation(strKey, strScsEnvId, strName);

		} else if ( strAction.equalsIgnoreCase(UIEventActionGrcAction.LaunchGrc.toString()) ) {
			
			String strKey				= (String) action.getParameter(ActionAttribute.OperationString2.toString());
			String strScsEnvId			= (String) action.getParameter(ActionAttribute.OperationString3.toString());
			String strName				= (String) action.getParameter(ActionAttribute.OperationString4.toString());
			
			String strGrcExecMode		= (String) action.getParameter(ActionAttribute.OperationString5.toString());
			String strFirstStep			= (String) action.getParameter(ActionAttribute.OperationString6.toString());
			String strGrcStepsToSkip 	= (String) action.getParameter(ActionAttribute.OperationString7.toString());
			
			boolean isValid = false;
			
			short grcExecMode		= -1;
			int firstStep			= -1;
			int intGrcStepsToSkips [] = null;
			
			logger.info(function, "strGrcStepsToSkip[{}]", strGrcStepsToSkip);
			
			intGrcStepsToSkips = UIWidgetUtil.getIntArray(strGrcStepsToSkip, ",");

			if ( null != intGrcStepsToSkips ) {
				isValid = true;
				
				logger.info(function, "intGrcStepsToSkips.length[{}]", intGrcStepsToSkips.length);
				for ( int i = 0 ; i < intGrcStepsToSkips.length ; ++i ) {
					logger.info(function, "intGrcStepsToSkips({})[{}]", i, intGrcStepsToSkips[i]);
				}
			} else {
				logger.warn(function, "intGrcStepsToSkips IS NULL");
			}
			
			if ( isValid ) {
				try {
					grcExecMode		= Short.parseShort(strGrcExecMode);
					firstStep		= Integer.parseInt(strFirstStep);
					isValid			= true;
				} catch ( NumberFormatException ex ) {
					logger.warn(function, "strGrcExecMode[}] strFirstStep[{}] strGrcStepsToSkip[{}] IS INVALID", new Object[]{strGrcExecMode, strFirstStep, strGrcStepsToSkip});
				}
				if ( isValid ) {
					logger.info(function, "launchGrc");
					grcMgr.launchGrc(strKey, strScsEnvId, strName, grcExecMode, firstStep, intGrcStepsToSkips);
				} else {
					logger.warn(function, "isValid IS FALSE");
				}
			} else {
				logger.warn(function, "isValid IS FALSE");
			}

		} else if ( strAction.equalsIgnoreCase(UIEventActionGrcAction.AbortGrc.toString()) ) {

			String strKey				= (String) action.getParameter(ActionAttribute.OperationString2.toString());
			String strScsEnvId			= (String) action.getParameter(ActionAttribute.OperationString3.toString());
			String strName				= (String) action.getParameter(ActionAttribute.OperationString4.toString());
			
			grcMgr.abortGrc(strKey, strScsEnvId, strName);
			
		} else if ( strAction.equalsIgnoreCase(UIEventActionGrcAction.SuspendGrc.toString()) ) {

			String strKey				= (String) action.getParameter(ActionAttribute.OperationString2.toString());
			String strScsEnvId			= (String) action.getParameter(ActionAttribute.OperationString3.toString());
			String strName				= (String) action.getParameter(ActionAttribute.OperationString4.toString());
			
			grcMgr.suspendGrc(strKey, strScsEnvId, strName);
			
		} else if ( strAction.equalsIgnoreCase(UIEventActionGrcAction.ResumeGrc.toString()) ) {

			String strKey				= (String) action.getParameter(ActionAttribute.OperationString2.toString());
			String strScsEnvId			= (String) action.getParameter(ActionAttribute.OperationString3.toString());
			String strName				= (String) action.getParameter(ActionAttribute.OperationString4.toString());
			
			grcMgr.resumeGrc(strKey, strScsEnvId, strName);
			
		}
		
		logger.end(function);
		return bContinue;
	}
}
