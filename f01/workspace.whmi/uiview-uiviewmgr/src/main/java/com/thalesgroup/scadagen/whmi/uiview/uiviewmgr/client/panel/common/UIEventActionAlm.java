package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common;

import java.util.Map;
import java.util.Map.Entry;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.ActionAttribute;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionAlm_i.UIEventActionAlmAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionExecute_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.alm.AlmMgr;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.OpmMgr;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i;

public class UIEventActionAlm extends UIEventActionExecute_i {
	private final String className = UIWidgetUtil.getClassSimpleName(UIEventActionAlm.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private final String STR_GET_OPERATOR		= "%GETOPERATOR%";
	private final String STR_GET_PROFILE		= "%GETPROFILE%";
	private final String STR_GET_IPADDRESS		= "%GETIPADDRESS%";
	private final String STR_GET_HOSTNAME		= "%GETHOSTNAME%";
	private final String STR_GET_ENV			= "%GETENV%";
	private final String STR_GET_ALIAS			= "%GETALIAS%";
	
	public UIEventActionAlm ( ) {
		supportedActions = new String[] {
				  UIEventActionAlmAction.NotifyExternalAlarm.toString()
				, UIEventActionAlmAction.NotifyExternalEvent.toString()
				};
	}
	
	@Override
	public boolean executeAction(UIEventAction action, Map<String, Map<String, Object>> override) {
		final String function = logPrefix+" executeAction";
		logger.begin(className, function);
		
		boolean bContinue = true;
		
		String strAction			= (String) action.getParameter(ActionAttribute.OperationString1.toString());
		String strKey				= (String) action.getParameter(ActionAttribute.OperationString2.toString());
		String strScsEnvId			= (String) action.getParameter(ActionAttribute.OperationString3.toString());
		String strConfigFileName	= (String) action.getParameter(ActionAttribute.OperationString4.toString());
		String strClassId			= (String) action.getParameter(ActionAttribute.OperationString5.toString());
		String strPointAlias		= (String) action.getParameter(ActionAttribute.OperationString6.toString());
		String strObjectId			= (String) action.getParameter(ActionAttribute.OperationString7.toString());
		String strExtSourceId		= (String) action.getParameter(ActionAttribute.OperationString8.toString());

		if ( logger.isDebugEnabled() ) {
			for ( Entry<String, Object> entry : action.getParameters() ) {
				String key = entry.getKey();
				Object obj = entry.getValue();
				logger.debug(className, function, "key[{}] obj[{}]", key, obj);
			}
		}
		
		boolean isValid = false;
		
		int classId		= -1;
		int objectId	= -1;
		int extSourceId	= -1;
		
		try {
			classId			= Integer.parseInt(strClassId);
			objectId		= Integer.parseInt(strObjectId);
			extSourceId		= Integer.parseInt(strExtSourceId);
			isValid = true;
		} catch ( NumberFormatException ex ) {
			logger.warn(className, function, "strClassId[}] strObjectId[{}] strExtSourceId[{}] IS INVALID", new Object[]{strClassId, strObjectId, strExtSourceId});
		}
		if ( isValid ) {
			AlmMgr almMgr = AlmMgr.getInstance(className); 
			
			if ( strAction.equals(UIEventActionAlmAction.NotifyExternalAlarm.toString()) ) {
				
				String strIsAlarm			= (String) action.getParameter(ActionAttribute.OperationString9.toString());
				String strMessage			= (String) action.getParameter(ActionAttribute.OperationString10.toString());
				String strOpmApi			= (String) action.getParameter(ActionAttribute.OperationString11.toString());
				
				String strOpmOptEnv			= (String) action.getParameter(ActionAttribute.OperationString12.toString());
				String strOpmOptAlias		= (String) action.getParameter(ActionAttribute.OperationString13.toString());
				
				boolean isAlarm = false;
				if ( Boolean.parseBoolean(strIsAlarm) ) isAlarm = true;
				
				strMessage = replaceOpmKeyword(strOpmApi, strMessage);
				logger.debug(className, function, "strMessage[{}]", strMessage);
				
				strScsEnvId = replaceOpmKeyword(strOpmApi, strScsEnvId);
				if ( 0 == strScsEnvId.compareTo(STR_GET_ENV) ) { strScsEnvId = strOpmOptEnv; }
				logger.debug(className, function, "strScsEnvId[{}]", strScsEnvId);
				
				strPointAlias = replaceOpmKeyword(strOpmApi, strPointAlias);
				if ( 0 == strPointAlias.compareTo(STR_GET_ALIAS) ) { strPointAlias = strOpmOptAlias; }
				logger.debug(className, function, "strPointAlias[{}]", strPointAlias);
				
				almMgr.notifyExternalAlarm(strKey, strScsEnvId
						, strConfigFileName, classId, strPointAlias
		    			, objectId, extSourceId, isAlarm, strMessage
		    			);
			} else if ( strAction.equals(UIEventActionAlmAction.NotifyExternalEvent.toString()) ) {
				
				String strMessage			= (String) action.getParameter(ActionAttribute.OperationString9.toString());
				String strOpmApi			= (String) action.getParameter(ActionAttribute.OperationString10.toString());
				
				String strOpmOptEnv			= (String) action.getParameter(ActionAttribute.OperationString11.toString());
				String strOpmOptAlias		= (String) action.getParameter(ActionAttribute.OperationString12.toString());
				
				strMessage = replaceOpmKeyword(strOpmApi, strMessage);
				logger.debug(className, function, "strMessage[{}]", strMessage);
				
				strScsEnvId = replaceOpmKeyword(strOpmApi, strScsEnvId);
				if ( 0 == strScsEnvId.compareTo(STR_GET_ENV) ) { strScsEnvId = strOpmOptEnv; }
				logger.debug(className, function, "strScsEnvId[{}]", strScsEnvId);
				
				strPointAlias = replaceOpmKeyword(strOpmApi, strPointAlias);
				if ( 0 == strPointAlias.compareTo(STR_GET_ALIAS) ) { strPointAlias = strOpmOptAlias; }
				logger.debug(className, function, "strPointAlias[{}]", strPointAlias);
				
				almMgr.notifyExternalEvent(strKey, strScsEnvId
		    			, strConfigFileName, classId, strPointAlias
		    			, objectId, extSourceId, strMessage
		    			);
			}
		}
		
		logger.end(className, function);
		return bContinue;
	}
	
	public String replaceOpmKeyword(String strOpmApi, String strMessage) {
		final String function = "replaceOpmKeyword";
		logger.begin(className, function);
		logger.debug(className, function, "strOpmApi[{}] strMessage[{}]", strOpmApi, strMessage);
		
		OpmMgr opmMgr = OpmMgr.getInstance();
		UIOpm_i uiOpm_i = opmMgr.getOpm(strOpmApi);
		if ( null != uiOpm_i ) {
			logger.debug(className, function, "call opm_i login");
			
			String strOperator = uiOpm_i.getCurrentOperator();
			if ( null != strOperator ) {
				logger.debug(className, function, logPrefix+"STR_GET_OPERATOR[{}] strProfile[{}]", STR_GET_OPERATOR, strOperator);
				strMessage = UIWidgetUtil.replaceKeyword(strMessage, STR_GET_OPERATOR, strOperator);
				logger.debug(className, function, logPrefix+"strMessage[{}]", strMessage);
			} else {
				logger.warn(className, function, logPrefix+"strOperator IS NULL");
			}

			String strProfile = uiOpm_i.getCurrentProfile();
			if ( null != strProfile ) {
				logger.debug(className, function, logPrefix+"STR_GET_PROFILE[{}] strProfile[{}]", STR_GET_PROFILE, strProfile);
				strMessage = UIWidgetUtil.replaceKeyword(strMessage, STR_GET_PROFILE, strProfile);
				logger.debug(className, function, logPrefix+"strMessage[{}]", strMessage);
			} else {
				logger.warn(className, function, logPrefix+"strProfile IS NULL");
			}
			
			String strIPAddress = uiOpm_i.getCurrentIPAddress();
			if ( null != strIPAddress ) {
				logger.debug(className, function, logPrefix+"STR_GET_IPADDRESS[{}] strWorkstation[{}]", STR_GET_IPADDRESS, strIPAddress);
				strMessage = UIWidgetUtil.replaceKeyword(strMessage, STR_GET_IPADDRESS, strIPAddress);
				logger.debug(className, function, logPrefix+"strMessage[{}]", strMessage);
			} else {
				logger.warn(className, function, logPrefix+"strIPAddress IS NULL");
			}

			String strHostName = uiOpm_i.getCurrentHostName();
			if ( null != strHostName ) {
				logger.debug(className, function, logPrefix+"STR_GET_HOSTNAME[{}] strHostName[{}]", STR_GET_HOSTNAME, strHostName);
				strMessage = UIWidgetUtil.replaceKeyword(strMessage, STR_GET_HOSTNAME, strHostName);
				logger.debug(className, function, logPrefix+"strMessage[{}]", strMessage);
			} else {
				logger.warn(className, function, logPrefix+"strHostName IS NULL");
			}
			
			String strEnv = uiOpm_i.getCurrentEnv();
			if ( null != strEnv ) {
				logger.debug(className, function, logPrefix+"STR_GET_ENV[{}] strEnv[{}]", STR_GET_ENV, strEnv);
				strMessage = UIWidgetUtil.replaceKeyword(strMessage, STR_GET_ENV, strEnv);
				logger.debug(className, function, logPrefix+"strMessage[{}]", strMessage);
			} else {
				logger.warn(className, function, logPrefix+"strEnv IS NULL");
			}

			String strAlias = uiOpm_i.getCurrentAlias();
			if ( null != strAlias ) {
				logger.debug(className, function, logPrefix+"STR_GET_ALIAS[{}] strAlias[{}]", STR_GET_ALIAS, strAlias);
				strMessage = UIWidgetUtil.replaceKeyword(strMessage, STR_GET_ALIAS, strAlias);
				logger.debug(className, function, logPrefix+"strMessage[{}]", strMessage);
			} else {
				logger.warn(className, function, logPrefix+"strAlias IS NULL");
			}

		} else {
			logger.warn(className, function, logPrefix+"opmapi[{}] instance IS NULL", strOpmApi);
		}
		logger.end(className, function);
		return strMessage;
	}

}
