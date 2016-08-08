package com.thalesgroup.scadagen.whmi.uipanel.uipanelviewlayout.client.view;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uipanel.uipanelviewlayout.client.ViewLayoutMgrEvent.ViewLayoutAction;
import com.thalesgroup.scadagen.whmi.uipanel.uipanelviewlayout.client.ViewLayoutMgrEvent.ViewLayoutMode;
import com.thalesgroup.scadagen.whmi.uitask.uitasklaunch.client.UITaskLaunch;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;

public class UIPanelView {
	
	private Logger logger = Logger.getLogger(UIPanelView.class.getName());
		
	private int viewId;
	private boolean activate;
	private boolean borderVisible;
	private ViewLayoutMode viewLayoutType;
	private ViewLayoutAction viewLayoutAction;
	private UIPanelViewEvent uiPanelViewEvent;
	private UITaskLaunch taskLaunch;
	private DockLayoutPanel panel = null;
	
	private InlineLabel lblTitle;
	
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
		
		logger.log(Level.FINE, "setActivateBorder activate["+activate+"]");
		
		String stylePrimaryName = null;
		
		if ( ViewLayoutAction.VDoubleLayout == getViewAction() ) {
			stylePrimaryName = "project-gwt-panel-view-vp";
		} else if ( ViewLayoutAction.HDoubleLayout == getViewAction() ) {
			stylePrimaryName = "project-gwt-panel-view-hp";
		}
		
		if ( null != stylePrimaryName ) {
			if ( activate ) {
				stylePrimaryName += "-activate";
			} else {
				stylePrimaryName += "-deactivate";
			}
		}

		if ( null != stylePrimaryName ) {
			
			logger.log(Level.FINE, "setActivateBorder stylePrimaryName["+stylePrimaryName+"]");
			
			this.panel.setStylePrimaryName(stylePrimaryName);
		} else {
			logger.log(Level.FINE, "setActivateBorder stylePrimaryName IS NULL");
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
	public ViewLayoutAction getViewAction() {
		
		logger.log(Level.FINE, "ViewLayoutAction Begin/End");
		
		return viewLayoutAction;
	}
	public void setViewAction(ViewLayoutAction viewAction) {
		
		logger.log(Level.FINE, "setViewType Begin");
		
		this.viewLayoutAction = viewAction;
		
		logger.log(Level.FINE, "setViewType End");
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
			panel.clear();
			
			UIPanelViewFactoryMgr uiPanelViewFactoryMgr = new UIPanelViewFactoryMgr();
			UIWidget_i uiWidget_i = null;
			ComplexPanel dockLayoutPanel = null;
			
			switch ( this.taskLaunch.getTaskLaunchType() ) {
			case PANEL:
				uiWidget_i = uiPanelViewFactoryMgr.getPanel(UIPanelViewFactoryMgr.UIPanelViewPanel, uiNameCard);
				dockLayoutPanel = uiWidget_i.getMainPanel();				
				break;
			case IMAGE:
				uiWidget_i = uiPanelViewFactoryMgr.getPanel(UIPanelViewFactoryMgr.UIPanelViewSchematic, uiNameCard);
				dockLayoutPanel = uiWidget_i.getMainPanel();
				break;
			default:
				uiWidget_i = uiPanelViewFactoryMgr.getPanel(UIPanelViewFactoryMgr.UIPanelViewEmpty, uiNameCard);
				dockLayoutPanel = uiWidget_i.getMainPanel();				
				break;			
			}

			HorizontalPanel hp = new HorizontalPanel();
			hp.addStyleName("project-gwt-panel-panelview-titlebar");
			hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			
			lblTitle = new InlineLabel("Title");
			lblTitle.addStyleName("project-gwt-inlinelabel-panelview-title");
			hp.add(lblTitle);
			
			panel.addNorth(hp, 40);
			
			panel.addStyleName("project-gwt-panel-panelview-container");
			panel.add(dockLayoutPanel);
			
			((UIPanelViewProvide_i)uiWidget_i).setTaskProvide(taskLaunch);
			
			setTitle(this.taskLaunch.getTitle());

		} else {
			
			logger.log(Level.FINE, "setTaskLaunch this.taskLaunch is null");
			
			this.taskLaunch = null;
		}
		
		logger.log(Level.FINE, "setTaskLaunch End");
	}
	
	
	private void setTitle ( String title ) {
		lblTitle.setText(title);
	}


}
