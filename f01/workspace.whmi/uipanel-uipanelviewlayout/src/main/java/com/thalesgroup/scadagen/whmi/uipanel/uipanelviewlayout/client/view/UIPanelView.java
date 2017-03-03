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
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;

public class UIPanelView {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIPanelView.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
		
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
		
		logger.begin(className, function);
		
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
//				logger.begin(className, function);
//				onMouseClick();
//				logger.end(className, function);
//			}
//		});
//		this.rootPanel.addClickHandler(new ClickHandler() {
//			
//			@Override
//			public void onClick(ClickEvent event) {
//				final String function = "addClickHandler onClick";
//				
//				logger.begin(className, function);
//				onMouseClick();
//				logger.end(className, function);
//			}
//		});
		
		this.basePanel = new DockLayoutPanel(Unit.PX);
		this.basePanel.sinkEvents(Event.ONCLICK);
		this.basePanel.addHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
            	final String function = "addClickHandler onClick";
        		
        		logger.begin(className, function);
            	onMouseClick();
            	logger.end(className, function);
            }
    	}, ClickEvent.getType());
		
		logger.end(className, function);
	}
	private void onMouseClick(){
		final String function = "onMouseClick";
		
		logger.begin(className, function);
		
		uiPanelViewEvent.setViewIdActivate(this.viewId);
		
		logger.end(className, function);
	}
	public DockLayoutPanel getMainPanel() {
		final String function = "getMainPanel";
		
		logger.beginEnd(className, function);
		
		return this.basePanel;
	}
	public int getViewId() {
		final String function = "getViewId";
		
		logger.beginEnd(className, function);
		
		return viewId;
	}
	public void setViewId(int viewId) {
		final String function = "setViewId";
		
		logger.beginEnd(className, function);
		
		this.viewId = viewId;
	}
	public boolean isActivate() {
		final String function = "isActivate";
		
		logger.beginEnd(className, function);
		
		return activate;
	}
	private void setActivateBorder(boolean activate) {
		final String function = "setActivateBorder";
		
		logger.begin(className, function);
		
		logger.info(className, function, "activate[{}]", activate);
		
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
			
			logger.info(className, function, "stylePrimaryName[{}]", stylePrimaryName);
			
			this.basePanel.setStylePrimaryName(stylePrimaryName);
		} else {
			logger.info(className, function, "stylePrimaryName IS NULL");
		}
		
		logger.end(className, function);
		
	}
	public void setActivate(boolean activate) {
		final String function = "setActivate";
		
		logger.begin(className, function);
		
		this.activate = activate;
				
		if (this.borderVisible) {
			setActivateBorder (this.activate );
		}
		logger.end(className, function);
	}
	public boolean isBorderVisible() {
		final String function = "isBorderVisible";
		
		logger.beginEnd(className, function);
		
		return borderVisible;
	}
	public void setBorderVisible(boolean borderVisible) {
		final String function = "setBorderVisible";
		
		logger.beginEnd(className, function);
		
		this.borderVisible = borderVisible;
		
	}

	public ViewLayoutAction getViewAction() {
		final String function = "getViewAction";
		
		logger.beginEnd(className, function);
		
		return viewLayoutAction;
	}
	public void setViewAction(ViewLayoutAction viewAction) {
		final String function = "setViewAction";
		
		logger.beginEnd(className, function);
		
		this.viewLayoutAction = viewAction;
		
	}
	public ViewLayoutMode getViewMode() {
		final String function = "ViewLayoutMode";
		
		logger.beginEnd(className, function);
		
		return viewLayoutType;
	}
	public void setViewType(ViewLayoutMode viewMode) {
		final String function = "setViewType";
		
		logger.begin(className, function);
		
		this.viewLayoutType = viewMode;
		
		logger.end(className, function);
	}
	public UITaskLaunch getTaskLaunch() {
		final String function = "getTaskLaunch";
		
		logger.beginEnd(className, function);
		
		return taskLaunch;
	}
	private UIWidget_i uiWidget_i = null;
	public void setTaskLaunch(UITaskLaunch taskLaunch) {
		final String function = "setTaskLaunch";
		
		logger.begin(className, function);
		
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
					logger.warn(className, function,  "panel IS NULL");
				}
				
				((UIPanelViewProvide_i)uiWidget_i).setTaskProvide(taskLaunch);
			} else {
				logger.warn(className, function, "this.taskLaunch.getTaskLaunchType()[{}] uiWidget_i IS NULL", this.taskLaunch.getTaskLaunchType());
			}

			setTitle(this.taskLaunch.getTitle());

		} else {
			
			logger.warn(className, function, "this.taskLaunch is null");
			
			this.taskLaunch = null;
		}
		
		logger.end(className, function);
	}
	
	
	private void setTitle ( String title ) {
		lblTitle.setText(title);
	}

	public void terminate() {
		final String function = "terminate";
		logger.begin(className, function);
		if ( null != uiWidget_i ) uiWidget_i.terminate();
		logger.end(className, function);
	}

}
