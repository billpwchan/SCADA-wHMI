package com.thalesgroup.scadagen.whmi.uipanel.uipanelviewlayout.client.view;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uipanel.uipanelviewlayout.client.ViewLayoutMgrEvent.ViewLayoutMode;
import com.thalesgroup.scadagen.whmi.uitask.uitasklaunch.client.UITaskLaunch;

public class UIPanelView {
	
	private static Logger logger = Logger.getLogger(UIPanelView.class.getName());
	
	private static final int LAYOUT_BORDER_ACTIVATE = 2;
	private static final int LAYOUT_BORDER_DEACTIVATE = 0;
	
	private int viewId;
	private boolean activate;
	private boolean borderVisible;
	private ViewLayoutMode viewLayoutType;
	private UIPanelViewEvent uiPanelViewEvent;
	private UITaskLaunch taskLaunch;
	private DockLayoutPanel panel = null;
	
	private UINameCard uiNameCard = null;
	public UIPanelView(UIPanelViewEvent uiPanelViewEvent, UINameCard uiNameCard, int viewId) {
		
		logger.log(Level.FINE, "UIPanelView Begin");
		
		this.uiPanelViewEvent = uiPanelViewEvent;
		
		this.uiNameCard = new UINameCard(uiNameCard);
		this.uiNameCard.appendUIPanel(this);
		this.uiNameCard.appendUIPath(Integer.toString(viewId));
		this.panel = new DockLayoutPanel(Unit.PX);
		
		this.panel.sinkEvents(Event.ONCLICK);
		this.panel.addHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
            	onMouseClick(event);
            }
    	}, ClickEvent.getType());
		
		logger.log(Level.FINE, "UIPanelView End");
	}
	private void onMouseClick(ClickEvent event){
		
		logger.log(Level.FINE, "onMouseClick Begin");
		
		uiPanelViewEvent.setViewIdActivate(this.viewId);
				
//		triggerTitleChange(this.taskLaunch);
				
		logger.log(Level.FINE, "onMouseClick End");
	}
	public DockLayoutPanel getMainPanel() {
		
		logger.log(Level.FINE, "getMainPanel Begin/End");
		
		return this.panel;
	}
	public int getViewId() {
		
		logger.log(Level.FINE, "getViewId Begin/End");
		
		return viewId;
	}
	public void setViewId(int viewId) {
		
		logger.log(Level.FINE, "setViewId Begin/End");
		
		this.viewId = viewId;
	}
	public boolean isActivate() {
		
		logger.log(Level.FINE, "isActivate Begin/End");
		
		return activate;
	}
	private void setActivateBorder(boolean activate) {
		
		logger.log(Level.FINE, "setActivateBorder Begin");
		
		if ( activate ) {
//			this.panel.setWidth("100%");
			this.panel.getElement().getStyle().setProperty("border","yellow solid "+LAYOUT_BORDER_ACTIVATE+"px");
//			int width = this.panel.getOffsetWidth();
//			width -= LAYOUT_BORDER_ACTIVATE;
//			this.panel.setWidth(width+"px");
//			this.panel.setHeight("100%");
		} else {
			
			this.panel.getElement().getStyle().setProperty("border","yellow solid "+LAYOUT_BORDER_DEACTIVATE+"px");
//			this.panel.setWidth("100%");
//			this.panel.setHeight("100%");
		}
		
		logger.log(Level.FINE, "setActivateBorder End");
		
	}
	public void setActivate(boolean activate) {
		
		logger.log(Level.FINE, "setActivate Begin");
		
		this.activate = activate;
				
		if (this.borderVisible) {
			setActivateBorder (this.activate );
		}
		logger.log(Level.FINE, "setActivate End");
	}
	public boolean isBorderVisible() {
		
		logger.log(Level.FINE, "isBorderVisible Begin/End");
		
		return borderVisible;
	}
	public void setBorderVisible(boolean borderVisible) {
		
		logger.log(Level.FINE, "setBorderVisible Begin");
		
		this.borderVisible = borderVisible;
		
		logger.log(Level.FINE, "setBorderVisible End");
	}
	public DockLayoutPanel getPanel() {
		
		logger.log(Level.FINE, "getPanel Begin/End");
		
		return panel;
	}
	public void setPanel(DockLayoutPanel panel) {
		
		logger.log(Level.FINE, "setPanel Begin/End");
		
		this.panel = panel;
	}
	public ViewLayoutMode getViewMode() {
		
		logger.log(Level.FINE, "ViewLayoutMode Begin/End");
		
		return viewLayoutType;
	}
	public void setViewType(ViewLayoutMode viewMode) {
		
		logger.log(Level.FINE, "setViewType Begin");
		
		this.viewLayoutType = viewMode;
		
		logger.log(Level.FINE, "setViewType End");
	}
	public UITaskLaunch getTaskLaunch() {
		
		logger.log(Level.FINE, "getTaskLaunch Begin/End");
		
		return taskLaunch;
	}
	public void setTaskLaunch(UITaskLaunch taskLaunch) {
		
		logger.log(Level.FINE, "setTaskLaunch Begin");
		
		if ( null != taskLaunch ) {
		
			this.taskLaunch = new UITaskLaunch(taskLaunch);
			this.panel.clear();
			
			UIPanelViewFactoryMgr uiPanelViewFactoryMgr = new UIPanelViewFactoryMgr();
			UIPanelViewProvide uiPanelViewProvide = null;
			DockLayoutPanel dockLayoutPanel = null;
			
			switch ( this.taskLaunch.getTaskLaunchType() ) {
			case PANEL:
				uiPanelViewProvide = uiPanelViewFactoryMgr.getPanel(UIPanelViewFactoryMgr.UIPanelViewPanel);
				dockLayoutPanel = uiPanelViewProvide.getMainPanel(uiNameCard);				
				break;
			case IMAGE:
				uiPanelViewProvide = uiPanelViewFactoryMgr.getPanel(UIPanelViewFactoryMgr.UIPanelViewSchematic);
				dockLayoutPanel = uiPanelViewProvide.getMainPanel(uiNameCard);
				break;
			default:
				uiPanelViewProvide = uiPanelViewFactoryMgr.getPanel(UIPanelViewFactoryMgr.UIPanelViewEmpty);
				dockLayoutPanel = uiPanelViewProvide.getMainPanel(uiNameCard);				
				break;			
			}

			dockLayoutPanel.setWidth("100%");
			dockLayoutPanel.setHeight("100%");
			this.panel.add(dockLayoutPanel);
			
			uiPanelViewProvide.setTaskProvide(taskLaunch);

		} else {
			
			logger.log(Level.FINE, "setTaskLaunch this.taskLaunch is null");
			
			this.taskLaunch = null;
		}
		
		logger.log(Level.FINE, "setTaskLaunch End");
	}


}
