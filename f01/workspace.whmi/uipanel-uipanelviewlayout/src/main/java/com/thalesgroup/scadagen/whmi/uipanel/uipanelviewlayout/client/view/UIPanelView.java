package com.thalesgroup.scadagen.whmi.uipanel.uipanelviewlayout.client.view;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Panel;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uipanel.uipanelviewlayout.client.ViewLayoutMgrEvent.ViewLayoutAction;
import com.thalesgroup.scadagen.whmi.uipanel.uipanelviewlayout.client.ViewLayoutMgrEvent.ViewLayoutMode;
import com.thalesgroup.scadagen.whmi.uitask.uitasklaunch.client.UITaskLaunch;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;

public class UIPanelView {

	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
		
	private int viewId;
	private boolean activate;
	private boolean borderVisible;
	private ViewLayoutMode viewLayoutType;
	private ViewLayoutAction viewLayoutAction;
	private UIPanelViewEvent uiPanelViewEvent;
	private UITaskLaunch taskLaunch;
//	private FocusPanel rootPanel = null;
	private DockLayoutPanel basePanel = null;
	
	private InlineLabel lblTitle;
	
	private UINameCard uiNameCard = null;
	public UIPanelView(UIPanelViewEvent uiPanelViewEvent, UINameCard uiNameCard, int viewId) {
		final String function = "UIPanelView";
		
		logger.begin(function);
		
		this.uiPanelViewEvent = uiPanelViewEvent;
		
		this.uiNameCard = new UINameCard(uiNameCard);
		this.uiNameCard.appendUIPanel(this);
		this.uiNameCard.appendUIPath(Integer.toString(viewId));
		
//		this.rootPanel = new FocusPanel();
//		this.rootPanel.addStyleName("project-gwt-panel-full");
//		this.rootPanel.addMouseDownHandler(new MouseDownHandler() {
//			
//			@Override
//			public void onMouseDown(MouseDownEvent event) {
//				final String function = "addMouseDownHandler onClick";
//				
//				logger.begin(function);
//				onMouseClick();
//				logger.end(function);
//			}
//		});
//		this.rootPanel.addClickHandler(new ClickHandler() {
//			
//			@Override
//			public void onClick(ClickEvent event) {
//				final String function = "addClickHandler onClick";
//				
//				logger.begin(function);
//				onMouseClick();
//				logger.end(function);
//			}
//		});
		
		this.basePanel = new DockLayoutPanel(Unit.PX);
		this.basePanel.sinkEvents(Event.ONCLICK);
		this.basePanel.addHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
            	final String function = "addClickHandler onClick";
        		
        		logger.begin(function);
            	onMouseClick();
            	logger.end(function);
            }
    	}, ClickEvent.getType());
		
		logger.end(function);
	}
	private void onMouseClick(){
		final String function = "onMouseClick";
		
		logger.begin(function);
		
		uiPanelViewEvent.setViewIdActivate(this.viewId);
		
		logger.end(function);
	}
	public DockLayoutPanel getMainPanel() {
		final String function = "getMainPanel";
		
		logger.beginEnd(function);
		
		return this.basePanel;
	}
	public int getViewId() {
		final String function = "getViewId";
		
		logger.beginEnd(function);
		
		return viewId;
	}
	public void setViewId(int viewId) {
		final String function = "setViewId";
		
		logger.beginEnd(function);
		
		this.viewId = viewId;
	}
	public boolean isActivate() {
		final String function = "isActivate";
		
		logger.beginEnd(function);
		
		return activate;
	}
	private void setActivateBorder(boolean activate) {
		final String function = "setActivateBorder";
		
		logger.begin(function);
		
		logger.info(function, "activate[{}]", activate);
		
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
			
			logger.info(function, "stylePrimaryName[{}]", stylePrimaryName);
			
			this.basePanel.setStylePrimaryName(stylePrimaryName);
		} else {
			logger.info(function, "stylePrimaryName IS NULL");
		}
		
		logger.end(function);
		
	}
	public void setActivate(boolean activate) {
		final String function = "setActivate";
		
		logger.begin(function);
		
		this.activate = activate;
				
		if (this.borderVisible) {
			setActivateBorder (this.activate );
		}
		logger.end(function);
	}
	public boolean isBorderVisible() {
		final String function = "isBorderVisible";
		
		logger.beginEnd(function);
		
		return borderVisible;
	}
	public void setBorderVisible(boolean borderVisible) {
		final String function = "setBorderVisible";
		
		logger.beginEnd(function);
		
		this.borderVisible = borderVisible;
		
	}

	public ViewLayoutAction getViewAction() {
		final String function = "getViewAction";
		
		logger.beginEnd(function);
		
		return viewLayoutAction;
	}
	public void setViewAction(ViewLayoutAction viewAction) {
		final String function = "setViewAction";
		
		logger.beginEnd(function);
		
		this.viewLayoutAction = viewAction;
		
	}
	public ViewLayoutMode getViewMode() {
		final String function = "ViewLayoutMode";
		
		logger.beginEnd(function);
		
		return viewLayoutType;
	}
	public void setViewType(ViewLayoutMode viewMode) {
		final String function = "setViewType";
		
		logger.begin(function);
		
		this.viewLayoutType = viewMode;
		
		logger.end(function);
	}
	public UITaskLaunch getTaskLaunch() {
		final String function = "getTaskLaunch";
		
		logger.beginEnd(function);
		
		return taskLaunch;
	}
	private UIWidget_i uiWidget_i = null;
	public void setTaskLaunch(UITaskLaunch taskLaunch) {
		final String function = "setTaskLaunch";
		
		logger.begin(function);
		
		if ( null != taskLaunch ) {
			
			if ( null != uiWidget_i ) uiWidget_i.terminate();
		
			this.taskLaunch = new UITaskLaunch(taskLaunch);
			basePanel.clear();
			
			// Title
			HorizontalPanel hp = new HorizontalPanel();
			hp.addStyleName("project-gwt-panel-panelview-titlebar");
			hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			
			lblTitle = new InlineLabel("Title");
			lblTitle.addStyleName("project-gwt-inlinelabel-panelview-title");
			hp.add(lblTitle);
			
			((DockLayoutPanel)basePanel).addNorth(hp, 40);
			
			UIPanelViewFactoryMgr uiPanelViewFactoryMgr = new UIPanelViewFactoryMgr();
			
			Panel panel = null;
			
			switch ( this.taskLaunch.getTaskLaunchType() ) {
			case PANEL:
				uiWidget_i = uiPanelViewFactoryMgr.getMainPanel(UIPanelViewFactoryMgr.UIPanelViewPanel, uiNameCard);			
				break;
			case IMAGE:
				uiWidget_i = uiPanelViewFactoryMgr.getMainPanel(UIPanelViewFactoryMgr.UIPanelViewSchematic, uiNameCard);
				break;
			default:
				uiWidget_i = uiPanelViewFactoryMgr.getMainPanel(UIPanelViewFactoryMgr.UIPanelViewEmpty, uiNameCard);			
				break;			
			}
			if ( null != uiWidget_i ) {
				panel = uiWidget_i.getMainPanel();
				
				if ( null != panel ) {
					basePanel.addStyleName("project-gwt-panel-panelview-container");
					basePanel.add(panel);
				} else {
					logger.warn(function,  "panel IS NULL");
				}
				
				((UIPanelViewProvide_i)uiWidget_i).setTaskProvide(taskLaunch);
			} else {
				logger.warn(function, "this.taskLaunch.getTaskLaunchType()[{}] uiWidget_i IS NULL", this.taskLaunch.getTaskLaunchType());
			}

			setTitle(this.taskLaunch.getTitle());

		} else {
			
			logger.warn(function, "this.taskLaunch is null");
			
			this.taskLaunch = null;
		}
		
		logger.end(function);
	}
	
	
	private void setTitle ( String title ) {
		lblTitle.setText(title);
	}

	public void terminate() {
		final String function = "terminate";
		logger.begin(function);
		if ( null != uiWidget_i ) uiWidget_i.terminate();
		logger.end(function);
	}

}
