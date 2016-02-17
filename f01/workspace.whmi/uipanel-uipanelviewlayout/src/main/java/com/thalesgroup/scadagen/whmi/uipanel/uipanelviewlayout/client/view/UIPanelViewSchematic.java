package com.thalesgroup.scadagen.whmi.uipanel.uipanelviewlayout.client.view;

import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uitask.uitask.client.UITask_i;
import com.thalesgroup.scadagen.whmi.uitask.uitasklaunch.client.UITaskLaunch;
import com.thalesgroup.scadagen.whmi.uitask.uitaskmgr.client.UITaskMgr;
import com.thalesgroup.scadagen.wrapper.wrapper.client.WrapperScsSituationViewPanel;

public class UIPanelViewSchematic implements UIPanelViewProvide {
	
	private static Logger logger = Logger.getLogger(UIPanelViewSchematic.class.getName());

	public static final String UNIT_PX = "px";

	public static final String IMAGE_PATH = "imgs";

	public static final int LAYOUT_BORDER = 0;
	public static final String RGB_PAL_BG = "#BEBEBE";

	public static final String RGB_RED = "rgb( 255, 0, 0)";
	public static final String RGB_GREEN = "rgb( 0, 255, 0)";
	public static final String RGB_BLUE = "rgb( 0, 0, 255)";

	LinkedList<HandlerRegistration> handlerRegistrations = new LinkedList<HandlerRegistration>();
	public void addHandlerRegistration(HandlerRegistration handlerRegistration) {
		handlerRegistrations.add(handlerRegistration);
	}
	public void removeHandlerRegistrations() {
		HandlerRegistration handlerRegistration = handlerRegistrations.poll();
		while ( null != handlerRegistration ) {
			handlerRegistration.removeHandler();
			handlerRegistration = handlerRegistrations.poll();
		}
	}
	
//	InlineLabel equipmenpLabel = null;
//	private void onClickLabel(){
//		UITaskLaunch taskLaunch = new UITaskLaunch();
//		taskLaunch.setUiPanel("UIPanelInspector");
//		taskLaunch.setTaskUiScreen(this.uiNameCard.getUiScreen());
//		taskLaunch.setUiPath(":UIGws:UIPanelScreen:UIScreenMMI");
//		this.uiNameCard.getUiEventBus().fireEvent(new UIEvent(taskLaunch));
//	}

	private UINameCard uiNameCard;
	public DockLayoutPanel getMainPanel(UINameCard uiNameCard) {
		
		logger.log(Level.FINE, "getMainPanel Begin");
		
		this.uiNameCard = new UINameCard(uiNameCard);
		this.uiNameCard.appendUIPanel(this);
		
//		HorizontalPanel hp = new HorizontalPanel();
//		hp.setWidth("100%");
//		hp.setHeight("100%");
//		hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
//		hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		
//		equipmenpLabel = new InlineLabel();
//		equipmenpLabel.setText("Schematic: ---- Click to launch the Inspector Panel");
//		hp.add(equipmenpLabel);
//		
//		equipmenpLabel.addClickHandler(new ClickHandler() {
//			
//			@Override
//			public void onClick(ClickEvent arg0) {
//				onClickLabel();
//			}
//		});
		

		WrapperScsSituationViewPanel wrapperScsSituationViewPanel = new WrapperScsSituationViewPanel("L5_33KV");
		wrapperScsSituationViewPanel.setSize("100%", "100%");
		HorizontalPanel scsViewPanel = wrapperScsSituationViewPanel.getMainPanel();
		scsViewPanel.setWidth("100%");
		scsViewPanel.setWidth("100%");
		scsViewPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		scsViewPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

		DockLayoutPanel root = new DockLayoutPanel(Unit.PX);
		root.add(scsViewPanel);
		
		logger.log(Level.FINE, "getMainPanel End");

		return root;
	}
	@Override
	public void setTaskProvide(UITask_i taskProvide) {
		
		logger.log(Level.FINE, "setTaskProvide Begin");
		
		if ( UITaskMgr.isInstanceOf(UITaskLaunch.class, taskProvide) ) {
			
			UITaskLaunch taskLaunch = (UITaskLaunch)taskProvide;
			
			if ( null != taskLaunch) {
				
				logger.log(Level.FINE, "setTaskProvide taskLaunch.getHeader()["+taskLaunch.getHeader()+"]");
				
//				this.equipmenpLabel.setText("Schematic: "+taskLaunch.getHeader()+" Click to launch the Inspector Panel");
				
			} else {
				
				logger.log(Level.FINE, "setTaskProvide taskLaunch is null");
				
			}
			
		} else {
			
			logger.log(Level.FINE, "setTaskProvide taskProvide is not TaskLaunch");
			
		}
		
		logger.log(Level.FINE, "setTaskProvide End");
	}

}
