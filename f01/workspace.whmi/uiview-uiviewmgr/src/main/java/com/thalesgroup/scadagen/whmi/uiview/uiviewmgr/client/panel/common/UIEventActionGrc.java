package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common;

import java.util.HashMap;
import java.util.Map.Entry;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIActionEventAttribute_i.ActionAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.wrapper.wrapper.client.ctl.GrcMgr;

public class UIEventActionGrc extends UIEventActionExecute_i {
	private final String className = UIWidgetUtil.getClassSimpleName(UIEventActionGrc.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	public final String strGetGrcList			= "GetGrcList";
	public final String strGetGrcState			= "GetGrcState";
	public final String strPrepareGrc			= "PrepareGrc";
	public final String strAbortGrcPreparation	= "AbortGrcPreparation";
	public final String strLaunchGrc			= "LaunchGrc";
	public final String strAbortGrc				= "AbortGrc";
	public final String strSuspendGrc			= "SuspendGrc";
	public final String strResumeGrc			= "ResumeGrc";
	
	public UIEventActionGrc ( ) {
		supportedActions = new String[] {};
	}
	
	@Override
	public void executeAction(UIEventAction action, HashMap<String, HashMap<String, Object>> override) {
		final String function = logPrefix+" executeAction";
		logger.begin(className, function);
		String strAction			= (String) action.getParameter(ActionAttribute.OperationString1.toString());
		
		if ( logger.isInfoEnabled() ) {
			for ( Entry<String, Object> entry : action.getParameters() ) {
				String key = entry.getKey();
				Object obj = entry.getValue();
				logger.info(className, function, "key[{}] obj[{}]", key, obj);
			}
		}
		
		
		GrcMgr grcMgr = GrcMgr.getInstance(className);
		
		if ( strAction.equalsIgnoreCase(strGetGrcList) ) {
			
			String strKey				= (String) action.getParameter(ActionAttribute.OperationString2.toString());
			String strScsEnvId			= (String) action.getParameter(ActionAttribute.OperationString3.toString());
			
			grcMgr.getGrcList(strKey, strScsEnvId);
			
		} else if ( strAction.equalsIgnoreCase(strGetGrcState) ) {
			
			String strKey				= (String) action.getParameter(ActionAttribute.OperationString2.toString());
			String strScsEnvId			= (String) action.getParameter(ActionAttribute.OperationString3.toString());
			String strName				= (String) action.getParameter(ActionAttribute.OperationString4.toString());
			
			grcMgr.getGrcState(strKey, strScsEnvId, strName);
			
		} else if ( strAction.equalsIgnoreCase(strPrepareGrc) ) {
			
			String strKey				= (String) action.getParameter(ActionAttribute.OperationString2.toString());
			String strScsEnvId			= (String) action.getParameter(ActionAttribute.OperationString3.toString());
			String strName				= (String) action.getParameter(ActionAttribute.OperationString4.toString());
			
			grcMgr.prepareGrc(strKey, strScsEnvId, strName);

		} else if ( strAction.equalsIgnoreCase(strAbortGrcPreparation) ) {
			
			String strKey				= (String) action.getParameter(ActionAttribute.OperationString2.toString());
			String strScsEnvId			= (String) action.getParameter(ActionAttribute.OperationString3.toString());
			String strName				= (String) action.getParameter(ActionAttribute.OperationString4.toString());
			
			grcMgr.abortGrcPreparation(strKey, strScsEnvId, strName);

		} else if ( strAction.equalsIgnoreCase(strLaunchGrc) ) {
			
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
			
			String strGrcStepsToSkips [] = strGrcStepsToSkip.split(",");
			if ( null != strGrcStepsToSkips ) {
				intGrcStepsToSkips = new int[strGrcStepsToSkips.length];
				logger.info(className, function, "intGrcStepsToSkips.length[{}]", intGrcStepsToSkips.length);
				try {
					for ( int i = 0 ; i < strGrcStepsToSkips.length ; ++i ) {
						logger.info(className, function, "intGrcStepsToSkips.length[{}]", intGrcStepsToSkips.length);
						intGrcStepsToSkips[i] = Integer.parseInt(strGrcStepsToSkips[i]);
					}
					isValid = true;
				} catch ( NumberFormatException ex ) {
					logger.warn(className, function, "intGrcStepsToSkips[{}] IS INVALID", intGrcStepsToSkips);
				}
			} else {
				logger.warn(className, function, "strGrcStepsToSkips IS NULL");
			}

			if ( null != intGrcStepsToSkips ) {
				logger.info(className, function, "intGrcStepsToSkips.length[{}]", intGrcStepsToSkips.length);
				for ( int i = 0 ; i < intGrcStepsToSkips.length ; ++i ) {
					logger.info(className, function, "intGrcStepsToSkips({})[{}]", i, intGrcStepsToSkips[i]);
				}
			} else {
				logger.warn(className, function, "intGrcStepsToSkips IS NULL");
			}
			
			if ( isValid ) {
				try {
					grcExecMode		= Short.parseShort(strGrcExecMode);
					firstStep		= Integer.parseInt(strFirstStep);
					isValid			= true;
				} catch ( NumberFormatException ex ) {
					logger.warn(className, function, "strGrcExecMode[}] strFirstStep[{}] strGrcStepsToSkip[{}] IS INVALID", new Object[]{strGrcExecMode, strFirstStep, strGrcStepsToSkip});
				}
				if ( isValid ) {
					logger.info(className, function, "launchGrc");
					grcMgr.launchGrc(strKey, strScsEnvId, strName, grcExecMode, firstStep, intGrcStepsToSkips);
				} else {
					logger.warn(className, function, "isValid IS FALSE");
				}
			} else {
				logger.warn(className, function, "isValid IS FALSE");
			}

		} else if ( strAction.equalsIgnoreCase(strAbortGrc) ) {

			String strKey				= (String) action.getParameter(ActionAttribute.OperationString2.toString());
			String strScsEnvId			= (String) action.getParameter(ActionAttribute.OperationString3.toString());
			String strName				= (String) action.getParameter(ActionAttribute.OperationString4.toString());
			
			grcMgr.abortGrc(strKey, strScsEnvId, strName);
			
		} else if ( strAction.equalsIgnoreCase(strSuspendGrc) ) {

			String strKey				= (String) action.getParameter(ActionAttribute.OperationString2.toString());
			String strScsEnvId			= (String) action.getParameter(ActionAttribute.OperationString3.toString());
			String strName				= (String) action.getParameter(ActionAttribute.OperationString4.toString());
			
			grcMgr.suspendGrc(strKey, strScsEnvId, strName);
			
		} else if ( strAction.equalsIgnoreCase(strResumeGrc) ) {

			String strKey				= (String) action.getParameter(ActionAttribute.OperationString2.toString());
			String strScsEnvId			= (String) action.getParameter(ActionAttribute.OperationString3.toString());
			String strName				= (String) action.getParameter(ActionAttribute.OperationString4.toString());
			
			grcMgr.resumeGrc(strKey, strScsEnvId, strName);
			
		}   
	}
}
