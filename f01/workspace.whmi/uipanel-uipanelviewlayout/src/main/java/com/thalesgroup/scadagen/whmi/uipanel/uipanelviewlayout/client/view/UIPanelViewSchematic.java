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
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uitask.uitask.client.UITask_i;
import com.thalesgroup.scadagen.whmi.uitask.uitasklaunch.client.UITaskLaunch;
import com.thalesgroup.scadagen.whmi.uitask.uitaskmgr.client.UITaskMgr;
import com.thalesgroup.scadagen.wrapper.wrapper.client.WrapperScsSituationViewPanel;
import com.thalesgroup.scadagen.wrapper.wrapper.client.WrapperScsSituationViewPanelEvent;

public class UIPanelViewSchematic implements UIPanelViewProvide {
	
	private static Logger logger = Logger.getLogger(UIPanelViewSchematic.class.getName());
	
	private final String UIPathUIScreenMMI 	= ":UIGws:UIPanelScreen:UIScreenMMI";

	public static final String UNIT_PX = "px";

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

	private DockLayoutPanel root = null;
	
	private UINameCard uiNameCard;
	public DockLayoutPanel getMainPanel(UINameCard uiNameCard) {
		
		logger.log(Level.FINE, "getMainPanel Begin");
		
		this.uiNameCard = new UINameCard(uiNameCard);
		this.uiNameCard.appendUIPanel(this);

		HorizontalPanel hp = new HorizontalPanel();
		hp.setWidth("100%");
		hp.setHeight("100%");
		hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

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
				
				String header = taskLaunch.getHeader();
				String uiPanel = taskLaunch.getUiPanel();
				
				logger.log(Level.SEVERE, "setTaskProvide header["+header+"] uiPanel["+uiPanel+"]");
				
				root.clear();

				WrapperScsSituationViewPanel wrapperScsSituationViewPanel = new WrapperScsSituationViewPanel(uiPanel);
				wrapperScsSituationViewPanel.setSize("100%", "100%");
				wrapperScsSituationViewPanel.setWrapperScsSituationViewPanelEvent(new WrapperScsSituationViewPanelEvent() {
					@Override
					public void triggerSymbolWidget(String hv_id) {
						showInspectorPanel(hv_id);
					}
				});
				
				
				root.add(wrapperScsSituationViewPanel.getMainPanel());
				
//				HorizontalPanel scsViewPanel = wrapperScsSituationViewPanel.getMainPanel();
//				scsViewPanel.setWidth("100%");
//				scsViewPanel.setWidth("100%");
//				scsViewPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
//				scsViewPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
//				
//				root.add(scsViewPanel);

//				HorizontalPanel hp = new HorizontalPanel();
//				hp.setWidth("100%");
//				hp.setHeight("100%");
//				hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
//				hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
//					
//				InlineLabel equipmenpLabel = new InlineLabel();
//				equipmenpLabel.setText("Schematic: "+header);
//				hp.add(equipmenpLabel);
//								
//				root.add(hp);

			} else {
				
				logger.log(Level.FINE, "setTaskProvide taskLaunch is null");
				
			}
			
		} else {
			
			logger.log(Level.FINE, "setTaskProvide taskProvide is not TaskLaunch");
			
		}
		
		logger.log(Level.FINE, "setTaskProvide End");
	}

	private void showInspectorPanel (String hv_id) {
		UITaskLaunch taskLaunch = new UITaskLaunch();
		taskLaunch.setUiPanel("UIPanelInspector");
		taskLaunch.setTaskUiScreen(this.uiNameCard.getUiScreen());
		taskLaunch.setUiPath(UIPathUIScreenMMI);
		taskLaunch.setOption(new String[]{hv_id});
		this.uiNameCard.getUiEventBus().fireEvent(new UIEvent(taskLaunch));
	}

}
