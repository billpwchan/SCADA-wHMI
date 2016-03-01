package com.thalesgroup.scadagen.whmi.uipanel.uipanelviewlayout.client.view;

import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.thalesgroup.scadagen.whmi.uidialog.uidialogmsg.client.DialogMsgMgr;
import com.thalesgroup.scadagen.whmi.uidialog.uidialogmsg.client.UIDialogMsg;
import com.thalesgroup.scadagen.whmi.uidialog.uidialogmsg.client.UIDialogMsg.ConfimDlgType;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uitask.uitask.client.UITask_i;
import com.thalesgroup.scadagen.whmi.uitask.uitasklaunch.client.UITaskLaunch;
import com.thalesgroup.scadagen.whmi.uitask.uitaskmgr.client.UITaskMgr;
import com.thalesgroup.scadagen.whmi.uiview.uiview.client.UIView_i;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.UIViewMgr;

public class UIPanelViewPanel implements UIPanelViewProvide {
	
	private static Logger logger = Logger.getLogger(UIPanelViewPanel.class.getName());
	
	public static final String UNIT_PX = "px";

	public static final String IMAGE_PATH = "imgs";

	public static final int LAYOUT_BORDER = 0;
	public static final String RGB_PAL_BG = "#BEBEBE";

	public static final String RGB_RED = "rgb( 255, 0, 0)";
	public static final String RGB_GREEN = "rgb( 0, 255, 0)";
	public static final String RGB_BLUE = "rgb( 0, 0, 255)";

	LinkedList<HandlerRegistration> handlerRegistrations = new LinkedList<HandlerRegistration>();
	@Override
	public
	void addHandlerRegistration(HandlerRegistration handlerRegistration) {
		handlerRegistrations.add(handlerRegistration);
	}
	@Override
	public
	void removeHandlerRegistrations() {
		HandlerRegistration handlerRegistration = handlerRegistrations.poll();
		while ( null != handlerRegistration ) {
			handlerRegistration.removeHandler();
			handlerRegistration = handlerRegistrations.poll();
		}
	}
	InlineLabel equipmenpLabel = null;

	private void onClickLabel(){
		
		DialogMsgMgr dialogMsgMgr = DialogMsgMgr.getInstance();
		UIDialogMsg uiDialgogMsg = (UIDialogMsg) dialogMsgMgr.getDialog("UIDialogMsg");
		uiDialgogMsg.setUINameCard(this.uiNameCard);
//		UIDialogMsg uiDialgogMsg = new UIDialogMsg(this.uiNameCard);
		uiDialgogMsg.setDialogMsg(ConfimDlgType.DLG_OK, "Message Dialog",
				"This is the popup dialog message box", null, null);
		uiDialgogMsg.popUp();
	}
	
	private UINameCard uiNameCard = null;

	private DockLayoutPanel root = null;
	@Override
	public DockLayoutPanel getMainPanel(UINameCard uiNameCard) {
		logger.log(Level.FINE, "getMainPanel Begin");
		
		this.uiNameCard = new UINameCard(uiNameCard);
		this.uiNameCard.appendUIPanel(this);
		
		HorizontalPanel hp = new HorizontalPanel();
		hp.setWidth("100%");
		hp.setHeight("100%");
		hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		
		equipmenpLabel = new InlineLabel();
		equipmenpLabel.setText("Panel: ---- Click to launch the Message Dialog");
		hp.add(equipmenpLabel);
		
		equipmenpLabel.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent arg0) {
				onClickLabel();
			}
		});

		root = new DockLayoutPanel(Unit.PX);
		root.add(hp);
		
		logger.log(Level.FINE, "getMainPanel End");

		return root;
	}
	
	@Override
	public void setTaskProvide(UITask_i taskProvide) {
		
		logger.log(Level.FINE, "setTaskProvide Begin");
		
		if ( UITaskMgr.isInstanceOf(UITaskLaunch.class, taskProvide) ) {
			
			UITaskLaunch taskLaunch = (UITaskLaunch)taskProvide;
			
			if ( null != taskLaunch) {
				
				logger.log(Level.FINE, "setTaskProvide taskLaunch.getHeader()["+taskLaunch.getUiPanel()+"]");
				
				this.equipmenpLabel.setText("Panel: "+taskLaunch.getUiPanel()+" should be launch...");
				
				logger.log(Level.FINE, "setTaskProvide root clear()");
				
				this.root.clear();

				UIViewMgr viewFactoryMgr = UIViewMgr.getInstance();
				
				logger.log(Level.FINE, "setTaskProvide viewFactoryMgr.getPanel["+taskLaunch.getUiPanel()+"]");
				
				UIView_i uiViewProvide = viewFactoryMgr.getPanel(taskLaunch.getUiPanel());
				
				logger.log(Level.FINE, "setTaskProvide uiViewProvide.getMainPanel["+uiNameCard.getUiPath()+"]");
				
				DockLayoutPanel dockLayoutPanel = uiViewProvide.getMainPanel(uiNameCard);
				
				logger.log(Level.FINE, "setTaskProvide root.add");
				
				this.root.add(dockLayoutPanel);
				
			} else {
				
				logger.log(Level.FINE, "setTaskProvide taskLaunch is null");
				
			}
			
		} else {
			
			logger.log(Level.FINE, "setTaskProvide taskProvide is not TaskLaunch");
			
		}
		
		logger.log(Level.FINE, "setTaskProvide End");
	}
}
