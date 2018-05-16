package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common;

import java.util.Map;

import com.thalesgroup.scadagen.whmi.translation.translationmgr.client.TranslationMgr;
import com.thalesgroup.scadagen.whmi.uidialog.uidialogmgr.client.UIDialogMgr;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.ActionAttribute;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionDialogMsg_i.UIEventActionDialogMsgAction;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.uidialog.UIDialogMsgCtrlUIEventActionSet;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.uidialog.UIDialogMsgCtrl_i;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.uidialog.UIDialogMsgEvent.UIDialogMsgEventType;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.uidialog.container.UIDailogMsg_i.UIConfimDlgParameter;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.uidialog.container.UIDailogMsg_i.UIConfimDlgType;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.uidialog.container.UIDialogMsg;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionExecute_i;

public class UIEventActionDialogMsg extends UIEventActionExecute_i {
	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
		
	public UIEventActionDialogMsg ( ) {
		supportedActions = new String[] {
				UIEventActionDialogMsgAction.UIDialogMsg.toString()
				};
	}
	
	@Override
	public boolean executeAction(UIEventAction uiEventAction, Map<String, Map<String, Object>> override) {
		final String function = logPrefix+" executeAction";
		
		boolean bContinue = true;
		
		logger.begin(function);

		if ( uiEventAction == null ) {
			logger.warn(function, "uiEventAction IS NULL");
			return bContinue;
		}
		
		String strUIDialogBoxAction		= (String) uiEventAction.getParameter(ActionAttribute.OperationString1.toString());
		String strUIConfirmDlgType		= (String) uiEventAction.getParameter(ActionAttribute.OperationString2.toString());
		String strTitle					= (String) uiEventAction.getParameter(ActionAttribute.OperationString3.toString());
		String strMessage				= (String) uiEventAction.getParameter(ActionAttribute.OperationString4.toString());
		String strMsgOpt1ActionSetKey	= (String) uiEventAction.getParameter(ActionAttribute.OperationString5.toString());
		String strMsgOpt2ActionSetKey	= (String) uiEventAction.getParameter(ActionAttribute.OperationString6.toString());
		String strMsgOpt1Label			= (String) uiEventAction.getParameter(ActionAttribute.OperationString7.toString());
		String strMsgOpt2Label			= (String) uiEventAction.getParameter(ActionAttribute.OperationString8.toString());
		
		String strCssPrefix				= (String) uiEventAction.getParameter(ActionAttribute.OperationString9.toString());
		String strImgPrefix				= (String) uiEventAction.getParameter(ActionAttribute.OperationString10.toString());
		
		String strFocusOnBtn			= (String) uiEventAction.getParameter(ActionAttribute.OperationString11.toString());
		
		if ( strUIDialogBoxAction == null ) {
			logger.warn(function, logPrefix+"strUIDialogBoxAction IS NULL");
			return bContinue;
		}
		
		if ( strUIConfirmDlgType == null ) {
			logger.warn(function, logPrefix+"strUIConfirmDlgType IS NULL");
			return bContinue;
		}
		
		if ( strTitle == null ) {
			logger.warn(function, logPrefix+"strTitle IS NULL");
			return bContinue;
		}
		
		if ( strMessage == null ) {
			logger.warn(function, logPrefix+"strMessage IS NULL");
			return bContinue;
		}
		
		if ( strUIDialogBoxAction.equals(UIEventActionDialogMsgAction.UIDialogMsg.toString()) ) {
			
			UIConfimDlgType uiConfimDlgType = UIConfimDlgType.DLG_OK;
			if ( UIConfimDlgType.DLG_ERR.toString().equals(strUIConfirmDlgType) ) {
				uiConfimDlgType = UIConfimDlgType.DLG_ERR;
			} else if ( UIConfimDlgType.DLG_WAR.toString().equals(strUIConfirmDlgType) ) {
				uiConfimDlgType = UIConfimDlgType.DLG_WAR;
			} else if ( UIConfimDlgType.DLG_OK.toString().equals(strUIConfirmDlgType) ) {
				uiConfimDlgType = UIConfimDlgType.DLG_OK;
			} else if ( UIConfimDlgType.DLG_YESNO.toString().equals(strUIConfirmDlgType) ) {
				uiConfimDlgType = UIConfimDlgType.DLG_YESNO;
			} else if ( UIConfimDlgType.DLG_OKCANCEL.toString().equals(strUIConfirmDlgType) ) {
				uiConfimDlgType = UIConfimDlgType.DLG_OKCANCEL;
			} else if ( UIConfimDlgType.DLG_EXECANCEL.toString().equals(strUIConfirmDlgType) ) {
				uiConfimDlgType = UIConfimDlgType.DLG_EXECANCEL;
			} else if ( UIConfimDlgType.DLG_CONFIRMCANCEL.toString().equals(strUIConfirmDlgType) ) {
				uiConfimDlgType = UIConfimDlgType.DLG_CONFIRMCANCEL;
			}
			
			logger.debug(function, logPrefix+"BF strTitle[{}]", strTitle);
			logger.debug(function, logPrefix+"BF strMessage[{}]", strMessage);
			logger.debug(function, logPrefix+"BF strMsgOpt1Label[{}]", strMsgOpt1Label);
			logger.debug(function, logPrefix+"BF strMsgOpt2Label[{}]", strMsgOpt2Label);
			
			strTitle	= TranslationMgr.getInstance().getTranslation(strTitle);
			strMessage	= TranslationMgr.getInstance().getTranslation(strMessage);
			
			if ( null != strMsgOpt1Label ) strMsgOpt1Label = TranslationMgr.getInstance().getTranslation(strMsgOpt1Label);
			if ( null != strMsgOpt2Label ) strMsgOpt2Label = TranslationMgr.getInstance().getTranslation(strMsgOpt2Label);
			
			logger.debug(function, logPrefix+"AF strTitle[{}]", strTitle);
			logger.debug(function, logPrefix+"AF strMessage[{}]", strMessage);
			logger.debug(function, logPrefix+"AF strMsgOpt1Label[{}]", strMsgOpt1Label);
			logger.debug(function, logPrefix+"AF strMsgOpt2Label[{}]", strMsgOpt2Label);
			
			UIDialogMsg uiDialgogMsg = (UIDialogMsg) UIDialogMgr.getInstance().getDialog(UIEventActionDialogMsgAction.UIDialogMsg.toString());
			if ( null != uiDialgogMsg ) {
				uiDialgogMsg.setUINameCard(uiNameCard);
				uiDialgogMsg.setDialogMsg(uiConfimDlgType, strTitle, strMessage);
				
				if ( null != strMsgOpt1Label ) uiDialgogMsg.setOpt1Label(strMsgOpt1Label);
				if ( null != strMsgOpt2Label ) uiDialgogMsg.setOpt2Label(strMsgOpt2Label);
				
				if ( strMsgOpt1ActionSetKey != null && ! strMsgOpt1ActionSetKey.trim().isEmpty() ) {
					UIDialogMsgCtrl_i action1 = new UIDialogMsgCtrlUIEventActionSet(uiEventActionProcessorCore_i, strMsgOpt1ActionSetKey, override);
					uiDialgogMsg.setResponse(UIDialogMsgEventType.MSG_OPT_1.toString(), action1);
				} else {
					logger.warn(function, logPrefix+"strMsgOpt1ActionSetKey[{}] IS INVALID", strMsgOpt1ActionSetKey);
				}

				if ( strMsgOpt2ActionSetKey != null && ! strMsgOpt2ActionSetKey.trim().isEmpty() ) {
					UIDialogMsgCtrl_i action2 = new UIDialogMsgCtrlUIEventActionSet(uiEventActionProcessorCore_i, strMsgOpt2ActionSetKey, override);
					uiDialgogMsg.setResponse(UIDialogMsgEventType.MSG_OPT_2.toString(), action2);
				} else {
					logger.warn(function, logPrefix+"strMsgOpt2ActionSetKey[{}] IS INVALID", strMsgOpt2ActionSetKey);
				}
				
				if(null!=strCssPrefix) {
					logger.debug(function, logPrefix+"strCssPrefix[{}]", strCssPrefix);
					uiDialgogMsg.setParameter(UIConfimDlgParameter.CSS_PREFIX.toString(), strCssPrefix);	
				} else {
					logger.warn(function, logPrefix+"strCssPrefix IS NULL");
				}
				
				if(null!=strImgPrefix) {
					logger.debug(function, logPrefix+"strImgPrefix[{}]", strImgPrefix);
					uiDialgogMsg.setParameter(UIConfimDlgParameter.IMG_PREFIX.toString(), strImgPrefix);
				} else {
					logger.warn(function, logPrefix+"strImgPrefix IS NULL");
				}
				
				if(null!=strFocusOnBtn) {
					logger.debug(function, logPrefix+"strFocusOnBtn[{}]", strFocusOnBtn);
					uiDialgogMsg.setParameter(UIConfimDlgParameter.BTN_FOCUS.toString(), strFocusOnBtn);
				} else {
					logger.warn(function, logPrefix+"strFocusOnBtn IS NULL");
				}
				
				uiDialgogMsg.popUp();
			} else {
				logger.warn(function, logPrefix+"strUIDialogMsg[{}] uiDialogMsg IS NULL", UIEventActionDialogMsgAction.UIDialogMsg.toString());
			}

		}
		
		logger.end(function);
		return bContinue;
	}

}
