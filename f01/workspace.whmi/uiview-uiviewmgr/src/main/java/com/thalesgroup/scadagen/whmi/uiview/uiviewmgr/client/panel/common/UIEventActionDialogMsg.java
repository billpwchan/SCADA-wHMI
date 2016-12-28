package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common;

import com.thalesgroup.scadagen.whmi.uidialog.uidialogmgr.client.UIDialogMgr;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIActionEventAttribute_i.ActionAttribute;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.uidialog.UIDialogMsgCtrlUIEventActionSet;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.uidialog.UIDialogMsgCtrl_i;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.uidialog.UIDialogMsgEvent.UIDialogMsgEventType;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.uidialog.container.UIDialogMsg;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.uidialog.container.UIDialogMsg.UIConfimDlgType;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.wrapper.wrapper.client.util.Translation;

public class UIEventActionDialogMsg extends UIEventActionExecute_i {
	private final String className = UIWidgetUtil.getClassSimpleName(UIEventActionDialogMsg.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private final String strUIDialogMsg = "UIDialogMsg";
	
	public UIEventActionDialogMsg ( ) {
		supportedActions = new String[] {strUIDialogMsg};
	}
	
	public void executeAction(UIEventAction uiEventAction) {
		final String function = logPrefix+" executeAction";
		
		logger.begin(className, function);

		if ( uiEventAction == null ) {
			logger.warn(className, function, "uiEventAction IS NULL");
			return;
		}
		
		String strUIDialogBoxAction		= (String) uiEventAction.getParameter(ActionAttribute.OperationString1.toString());
		String strUIConfirmDlgType		= (String) uiEventAction.getParameter(ActionAttribute.OperationString2.toString());
		String strTitle					= (String) uiEventAction.getParameter(ActionAttribute.OperationString3.toString());
		String strMessage				= (String) uiEventAction.getParameter(ActionAttribute.OperationString4.toString());
		String strMsgOpt1ActionSetKey	= (String) uiEventAction.getParameter(ActionAttribute.OperationString5.toString());
		String strMsgOpt2ActionSetKey	= (String) uiEventAction.getParameter(ActionAttribute.OperationString6.toString());
		
		if ( strUIDialogBoxAction == null ) {
			logger.warn(className, function, logPrefix+"strUIDialogBoxAction IS NULL");
			return;
		}
		
		if ( strUIConfirmDlgType == null ) {
			logger.warn(className, function, logPrefix+"strUIConfirmDlgType IS NULL");
			return;
		}
		
		if ( strTitle == null ) {
			logger.warn(className, function, logPrefix+"strTitle IS NULL");
			return;
		}
		
		if ( strMessage == null ) {
			logger.warn(className, function, logPrefix+"strMessage IS NULL");
			return;
		}
		
		if ( strUIDialogBoxAction.equals(strUIDialogMsg) ) {
			
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
			
			String title	= Translation.getDBMessage(strTitle);
			String message	= Translation.getDBMessage(strMessage);
			
			UIDialogMgr uiDialogMsgMgr = UIDialogMgr.getInstance();
			UIDialogMsg uiDialgogMsg = (UIDialogMsg) uiDialogMsgMgr.getDialog(strUIDialogMsg);
			if ( null != uiDialgogMsg ) {
				uiDialgogMsg.setUINameCard(uiNameCard);
				uiDialgogMsg.setDialogMsg(uiConfimDlgType, title, message);
				
				if ( strMsgOpt1ActionSetKey != null && ! strMsgOpt1ActionSetKey.trim().isEmpty() ) {
					UIDialogMsgCtrl_i action1 = new UIDialogMsgCtrlUIEventActionSet(uiEventActionProcessor_i, strMsgOpt1ActionSetKey);
					uiDialgogMsg.setResponse(UIDialogMsgEventType.MSG_OPT_1.toString(), action1);
				} else {
					 logger.warn(className, function, logPrefix+"strMsgOpt1ActionSetKey[{}] IS INVALID", strMsgOpt1ActionSetKey);
				}

				if ( strMsgOpt2ActionSetKey != null && ! strMsgOpt2ActionSetKey.trim().isEmpty() ) {
					UIDialogMsgCtrl_i action2 = new UIDialogMsgCtrlUIEventActionSet(uiEventActionProcessor_i, strMsgOpt2ActionSetKey);
					uiDialgogMsg.setResponse(UIDialogMsgEventType.MSG_OPT_2.toString(), action2);
				} else {
					 logger.warn(className, function, logPrefix+"strMsgOpt2ActionSetKey[{}] IS INVALID", strMsgOpt2ActionSetKey);
				}

				uiDialgogMsg.popUp();
			} else {
				logger.warn(className, function, logPrefix+"strUIDialogMsg[{}] uiDialogMsg IS NULL", strUIDialogMsg);
			}

		}
		
		logger.end(className, function);
	}

}
