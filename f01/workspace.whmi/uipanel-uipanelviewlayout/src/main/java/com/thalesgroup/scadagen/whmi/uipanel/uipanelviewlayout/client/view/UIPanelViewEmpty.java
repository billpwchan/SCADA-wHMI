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
import com.google.gwt.user.client.ui.InlineLabel;
import com.thalesgroup.scadagen.whmi.uitask.uitask.client.UITask_i;
import com.thalesgroup.scadagen.whmi.uitask.uitasklaunch.client.UITaskLaunch;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;

public class UIPanelViewEmpty extends UIWidget_i implements UIPanelViewProvide_i {
	
	private Logger logger = Logger.getLogger(UIPanelViewEmpty.class.getName());
	
	public static final String UNIT_PX		= "px";

	public static final String IMAGE_PATH	= "imgs";
	
	InlineLabel equipmenpLabel = null;
	
	@Override
	public void init() {
		
		logger.log(Level.FINE, "init Begin");
		
		HorizontalPanel hp = new HorizontalPanel();
		hp.setWidth("100%");
		hp.setHeight("100%");
		hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		
		equipmenpLabel = new InlineLabel();
		equipmenpLabel.setText("Panel: This is the empty panel");
		hp.add(equipmenpLabel);
		
//		equipmenpLabel.addClickHandler(new ClickHandler() {
//			
//			@Override
//			public void onClick(ClickEvent arg0) {
//				onClickLabel();
//			}
//		});

		rootPanel = new DockLayoutPanel(Unit.PX);
		rootPanel.add(hp);
		
		logger.log(Level.FINE, "init End");
	}

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
	
	@Override
	public void setTaskProvide(UITask_i taskProvide) {
		
		logger.log(Level.FINE, "setTaskProvide Begin");
		
		if ( null != taskProvide ) {
			if ( taskProvide instanceof UITaskLaunch ) {
				
				UITaskLaunch taskLaunch = (UITaskLaunch)taskProvide;
				
				if ( null != taskLaunch) {
					logger.log(Level.FINE, "setTaskProvide taskLaunch.getHeader()["+taskLaunch.getHeader()+"]");
					this.equipmenpLabel.setText("Empty: "+taskLaunch.getHeader()+" can't be found!");
				}
			}			
		} else {
				logger.log(Level.FINE, "setTaskProvide taskProvide is not TaskLaunch");
		}

		logger.log(Level.FINE, "setTaskProvide End");
	}
}
