package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.uidialog.container;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.uidialog.uidialog.client.UIDialog_i;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.uidialog.UIDialogMsgCtrl_i;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.uidialog.UIDialogMsgEvent.UIDialogMsgEventType;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.uidialog.container.UIDailogMsg_i.UIConfimDlgParameter;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.uidialog.container.UIDailogMsg_i.UIConfimDlgType;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.TextArea;

public class UIDialogMsg extends DialogBox implements UIDialog_i {
	
	private final String className = this.getClass().getSimpleName();
	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());

	private String element = className;
	
	private String cssPrefix = "project-"+element;
	private String imgBase = UIDailogMsg_i.STR_BASEPATH;
	private String imgPrefix = imgBase + UIDailogMsg_i.STR_FOLDER;
	
	private String focusOnBtn		= "";
	
	private String imgErr			= "/error.png";
	private String imgWar			= "/warning.png";
	private String imgOk			= "/info.png";
	private String imgYesNo			= "/question.png";
	private String imgOkCancel		= "/question.png";
	private String imgExeCancel		= "/question.png";
	private String imgConfirmCancel	= "/question.png";
	
	
	private String title = null;
	public void setTitle(String title) { this.title = title; }
	public String setTitle() { return this.title; }
	
	private String msg = null;
	public void setMsg(String msg) { this.msg = msg; }
	public String getMsg() { return this.msg; }
	
	private String opt1Label = null;
	public void setOpt1Label(String label) { this.opt1Label = label; }
	public String getOpt1Label() { return this.opt1Label; }
	
	private String opt2Label = null;
	public void setOpt2Label(String label) { this.opt2Label = label; }
	public String getOpt2Label() { return this.opt2Label; }
	
	private UIConfimDlgType confimDlgType = null;
	public void setConfimDlgType(UIConfimDlgType confimDlgType) { this.confimDlgType = confimDlgType; }
	public UIConfimDlgType getConfimDlgType() { return this.confimDlgType; }
	
	private Map<String, UIDialogMsgCtrl_i> uiDialogMsgCtrls = new HashMap<String, UIDialogMsgCtrl_i>();
	public void setResponse(String msgOpt, UIDialogMsgCtrl_i uiDialogMsgCtrl_i) {
		String function = "setResponse";
		logger.debug(function, "msgOpt[{}]", msgOpt);
		this.uiDialogMsgCtrls.put(msgOpt, uiDialogMsgCtrl_i);
	}
	private Image image				= null;
	private TextArea txtMsg			= null;
	private Button btnOk			= null;
	private Button btnCancel		= null;
	private HorizontalPanel btnBar	= null;
	
	private UINameCard uiNameCard;
	public void setUINameCard(UINameCard uiNameCard) {
		this.uiNameCard = new UINameCard(uiNameCard);
		this.uiNameCard.appendUIPanel(this);
	}
	
	public void setDialogMsg(UIConfimDlgType dlgConfirmcancel, String title, String msg) {
		String function = "setDialogMsg";
		
		logger.debug(function, "dlgConfirmcancel[{}] title[{}] msg[{}]"
				, new Object[]{dlgConfirmcancel, title, msg});
		
		this.setConfimDlgType(dlgConfirmcancel);
		this.setTitle(title);
		this.setMsg(msg);
	}
	
	public UIConfimDlgParameter getParameterKey(final String key) {
		UIConfimDlgParameter ret = null;
		UIConfimDlgParameter[] ps = UIConfimDlgParameter.values();
		for(int i = 0;i < ps.length;++i) {
			if(0==key.compareTo(ps[i].toString())) {
				ret = ps[i];
				break;
			}
		}
		return ret;
	}

	public void setParameter(final String key, final String value) {
		String function = "setParameter";
		logger.debug(function, "key[{}] value[{}]", key, value);
		UIConfimDlgParameter p = getParameterKey(key);
		switch(p) {
		case CSS_PREFIX:			cssPrefix = value;			break;
		case IMG_BASE:				imgBase = value;
									imgPrefix = imgBase + UIDailogMsg_i.STR_FOLDER;	break;		
		case IMG_PREFIX:			imgPrefix = value;			break;
		case IMG_DLG_ERR:			imgErr = value;				break;
		case IMG_DLG_WAR:			imgWar = value;				break;
		case IMG_DLG_OK:			imgOk = value;				break;
		case IMG_DLG_YESNO:			imgYesNo = value;			break;
		case IMG_DLG_OKCANCEL:		imgOkCancel = value;		break;
		case IMG_DLG_EXECANCEL:		imgExeCancel = value;		break;
		case IMG_DLG_CONFIRMCANCEL:	imgConfirmCancel = value;	break;
		
		case BTN_FOCUS:				focusOnBtn = value;			break;
		}
	}
	
	public void popUp() {
		String function = "popUp";

		int baseWidth = 500;
		int baseHeight = 200;
		
		image = new Image();

		txtMsg = new TextArea();

		btnOk = new Button();
		btnOk.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				UIDialogMsgCtrl_i uiDialogCtrl_i = uiDialogMsgCtrls.get(UIDialogMsgEventType.MSG_OPT_1.toString());
				if ( null != uiDialogCtrl_i ) { 
					uiDialogCtrl_i.response();
				}
				hide();
			}
		});
		btnCancel = new Button();
		btnCancel.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				UIDialogMsgCtrl_i uiDialogCtrl_i = uiDialogMsgCtrls.get(UIDialogMsgEventType.MSG_OPT_2.toString());
				if ( null != uiDialogCtrl_i ) { 
					uiDialogCtrl_i.response();
				}
				hide();
			}
		});
		
		btnBar = new HorizontalPanel();
		btnBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		
		btnBar.add(btnOk);
		btnBar.add(btnCancel);

		switch ( confimDlgType ) {
			case DLG_ERR:
				image.setUrl(imgPrefix+imgErr);
				btnOk.setText(null==opt1Label?UIDailogMsg_i.strOK:opt1Label);
				btnCancel.setText(null==opt2Label?UIDailogMsg_i.strEmpty:opt2Label);
				btnCancel.setVisible(false);
				break;
			case DLG_WAR:
				image.setUrl(imgPrefix+imgWar);
				btnOk.setText(null==opt1Label?UIDailogMsg_i.strOK:opt1Label);
				btnCancel.setText(null==opt2Label?UIDailogMsg_i.strEmpty:opt2Label);
				btnCancel.setVisible(false);
				break;
			case DLG_OK:
				image.setUrl(imgPrefix+imgOk);
				btnOk.setText(null==opt1Label?UIDailogMsg_i.strOK:opt1Label);
				btnCancel.setText(null==opt2Label?UIDailogMsg_i.strEmpty:opt2Label);
				btnCancel.setVisible(false);
				break;
			case DLG_YESNO:
				image.setUrl(imgPrefix+imgYesNo);
				btnOk.setText(null==opt1Label?UIDailogMsg_i.strYes:opt1Label);
				btnCancel.setText(null==opt2Label?UIDailogMsg_i.strNo:opt2Label);
				break;
			case DLG_OKCANCEL:
				image.setUrl(imgPrefix+imgOkCancel);
				btnOk.setText(null==opt1Label?UIDailogMsg_i.strOK:opt1Label);
				btnCancel.setText(null==opt2Label?UIDailogMsg_i.strCancel:opt2Label);
				break;
			case DLG_EXECANCEL:
				image.setUrl(imgPrefix+imgExeCancel);
				btnOk.setText(null==opt1Label?UIDailogMsg_i.strExecute:opt1Label);
				btnCancel.setText(null==opt2Label?UIDailogMsg_i.strCancel:opt2Label);
				break;
			case DLG_CONFIRMCANCEL:
				image.setUrl(imgPrefix+imgConfirmCancel);
				btnOk.setText(null==opt1Label?UIDailogMsg_i.strConfirm:opt1Label);
				btnCancel.setText(null==opt2Label?UIDailogMsg_i.strCancel:opt2Label);
				break;				
				
			default:
				break;
		}
		
		this.setText(title);

		txtMsg.setText(msg);
		txtMsg.setReadOnly(true);
		
		DockLayoutPanel dockLayoutPanel = new DockLayoutPanel(Unit.PX);
		dockLayoutPanel.getElement().getStyle().setWidth(baseWidth, Unit.PX);
		dockLayoutPanel.getElement().getStyle().setHeight(baseHeight, Unit.PX);	
		dockLayoutPanel.addStyleName(cssPrefix+"-panel-dialogmsg");

		btnOk.addStyleName(cssPrefix+"-button-ok");
		
		btnCancel.addStyleName(cssPrefix+"-button-cancel");

		btnBar.addStyleName(cssPrefix+"-btnbar");
		dockLayoutPanel.addSouth(btnBar, 50);
		
		image.getElement().getStyle().setPadding(25, Unit.PX);  
		dockLayoutPanel.addWest(image, 90);
		
		txtMsg.setWidth("90%");
		txtMsg.setHeight("95%");
		txtMsg.getElement().getStyle().setPadding(10, Unit.PX);
		txtMsg.addStyleName(cssPrefix+"-textarea");
		dockLayoutPanel.add(txtMsg);
		
		this.add(dockLayoutPanel);
		
		this.getElement().getStyle().setPosition(Style.Position.ABSOLUTE);
		int left = (Window.getClientWidth() / 2) - ( baseWidth / 2 ) - (UIDailogMsg_i.baseBoderX / 2);
	    int top = (Window.getClientHeight() / 2) - ( baseHeight / 2 ) - (UIDailogMsg_i.baseBoderY / 2);
	    this.setPopupPosition(left, top);
	    
	    this.show();
	    
	    if(null!=focusOnBtn) {
	    	logger.debug(function, "focusOnBtn[{}]", focusOnBtn);
	    	if (0==focusOnBtn.compareTo(UIDailogMsg_i.STR_ONE)){
	    		logger.debug(function, "btnOk.setFocus(true);");
	    		btnOk.setFocus(true);
	    	} else if(0==focusOnBtn.compareTo(UIDailogMsg_i.STR_TWO)){
	    		logger.debug(function, "btnCancel.setFocus(true);");
	    		btnCancel.setFocus(true);
	    	}
	    }
	}
	
	@Override
	public void onMouseMove(Widget sender, int x, int y) {
		super.onMouseMove(sender, x, y);
		boolean XtoMoveInvalid = false, YtoMoveInvalid = false;
		int XtoMove = 0, YtoMove = 0;
		if ( this.getPopupLeft() < 0 ) {
			XtoMove = 0;
			XtoMoveInvalid = true;
		}
		if ( this.getPopupLeft() + UIDailogMsg_i.baseWidth + UIDailogMsg_i.baseBoderX > Window.getClientWidth() ) {
			XtoMove = Window.getClientWidth() - UIDailogMsg_i.baseWidth - UIDailogMsg_i.baseBoderX;
			XtoMoveInvalid = true;
		}
		if ( this.getPopupTop() < 0 ) {
			YtoMove = 0;
			YtoMoveInvalid = true;
		}
		if ( this.getPopupTop() + UIDailogMsg_i.baseHeight + UIDailogMsg_i.baseBoderY > Window.getClientHeight() ) {
			YtoMove = Window.getClientHeight() - UIDailogMsg_i.baseHeight - UIDailogMsg_i.baseBoderY;
			YtoMoveInvalid = true;
		}
		if ( XtoMoveInvalid || YtoMoveInvalid ) {
			if ( ! XtoMoveInvalid ) XtoMove = this.getPopupLeft();
			if ( ! YtoMoveInvalid ) YtoMove = this.getPopupTop();
			this.setPopupPosition(XtoMove, YtoMove);
		}
	}
}
