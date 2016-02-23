package com.thalesgroup.scadagen.whmi.uipanel.uipanelviewlayout.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uipanel.uipanelmgr.client.UIPanelMgr;
import com.thalesgroup.scadagen.whmi.uipanel.uipanelviewlayout.client.view.UIPanelView;
import com.thalesgroup.scadagen.whmi.uipanel.uipanelviewlayout.client.view.UIPanelViewEvent;
import com.thalesgroup.scadagen.whmi.uipanel.uipanelviewlayout.client.viewtoolbar.UIPanelImageToolBar;
import com.thalesgroup.scadagen.whmi.uipanel.uipanelviewlayout.client.viewtoolbar.UIPanelImageToolBarEvent;
import com.thalesgroup.scadagen.whmi.uitask.uitasklaunch.client.UITaskLaunch;

public class UIPanelViewLayout implements UIPanelViewEvent, ViewLayoutMgrEvent, UIPanelImageToolBarEvent {
	
	private static Logger logger = Logger.getLogger(UIPanelViewLayout.class.getName());
		
	public static final String UNIT_PX		= "px";
	public static final int LAYOUT_BORDER	= 0;
	
	public static final String RGB_RED		= "rgb( 255, 0, 0)";
	public static final String RGB_GREEN	= "rgb( 0, 255, 0)";
	public static final String RGB_BLUE		= "rgb( 0, 0, 255)";
	
	public static final String RGB_BTN_SEL 	= "rgb(246, 230, 139)";
	public static final String RGB_BTN_BG	= "#F1F1F1";
	public static final String IMG_NONE		= "none";
	
	public static final String RGB_PAL_BG	= "#BEBEBE";
	
	public static final String IMAGE_PATH	= "imgs";

	private ViewLayoutMgr viewLayoutMgr;
	
	private UIPanelView[] uiPanelViews;
	
	private DockLayoutPanel basePanel;
	
	private DockLayoutPanel upperMainPanel;
	
	private UIPanelImageToolBar uiPanelImageToolBar = null;
	private HorizontalPanel imageToolBar = null;
	private VerticalPanel bottomToolBar = null;
	
	/**
	 * @param viewId
	 * @return
	 */
	private UIPanelView createView(int viewId)
	{
		logger.log(Level.FINE, "getView Begin");
		
		UIPanelView v = new UIPanelView(this, this.uiNameCard, viewId);
		v.setViewId(viewId);
		v.getPanel().getElement().getStyle().setBorderColor(RGB_BTN_SEL);
		v.getPanel().setWidth("100%");
		v.getPanel().setHeight("100%");
		v.getPanel().getElement().getStyle().setBackgroundColor(RGB_PAL_BG);
//		v.getPanel().add(new InlineLabel("View ID: "+viewId));
		
		logger.log(Level.FINE, "getView End");
		
		return v;
	}
	
	/**
	 * @param size
	 * @return
	 */
	private UIPanelView[] createViews(int size) 
	{
		logger.log(Level.FINE, "getViews Begin");
		
		UIPanelView[] vs = new UIPanelView[size];
		for(int i=0;i<size;++i) {
			vs[i] = createView(i);
		}
		
		logger.log(Level.FINE, "getViews End");
		
		return vs;
	}
	
	private UINameCard uiNameCard;
	public DockLayoutPanel getMainPanel(UINameCard uiNameCard) 
	{ 
		logger.log(Level.FINE, "getMainPanel Begin...");
		
		this.uiNameCard = new UINameCard(uiNameCard);
		this.uiNameCard.appendUIPanel(this);
		
		this.viewLayoutMgr = new ViewLayoutMgr(this, this.uiNameCard);

		basePanel = new DockLayoutPanel(Unit.PX);
		basePanel.setWidth("100%");
		basePanel.setHeight("100%");
		
		logger.log(Level.FINE, "getMainPanel End.");
				
		return basePanel; 
	}
	

	/* (non-Javadoc)
	 * @see com.thalesgroup.scadagen.whmi.uipanel.uipanelviewlayout.client.ViewLayoutMgrEvent#setLayout(com.thalesgroup.scadagen.whmi.uipanel.uipanelviewlayout.client.ViewLayoutMgrEvent.ViewLayoutMode, com.thalesgroup.scadagen.whmi.uipanel.uipanelviewlayout.client.ViewLayoutMgrEvent.ViewLayoutAction)
	 */
	@Override
	public void setLayout(ViewLayoutMode viewLayoutMode, ViewLayoutAction viewLayoutAction)
	{
		logger.log(Level.FINE, "setLayout Begin");
		
		logger.log(Level.FINE, "setLayout ViewLayoutMode["+viewLayoutMode+"]");
		logger.log(Level.FINE, "setLayout viewLayoutAction["+viewLayoutAction+"]");
		
		basePanel.clear();
		
		UIPanelMgr uiPanelFactoryMgr = UIPanelMgr.getInstance();
		
		upperMainPanel = uiPanelFactoryMgr.getMainPanel("UIPanelEmpty", uiNameCard);
		
		boolean borderVisible = false;
		
		if ( ViewLayoutMode.Image == viewLayoutMode ) {
			
			logger.log(Level.FINE, "setLayout ViewLayoutMode.Image");
						
			uiPanelImageToolBar = new UIPanelImageToolBar();
			imageToolBar = uiPanelImageToolBar.getMainPanel(this, this.uiNameCard);
	
			imageToolBar.setVisible(true);
			
			bottomToolBar = new VerticalPanel();
			bottomToolBar.setBorderWidth(LAYOUT_BORDER);
		    bottomToolBar.setWidth("100%");
		    bottomToolBar.setHeight("100%");
		    bottomToolBar.getElement().getStyle().setBackgroundColor(RGB_PAL_BG);
		    bottomToolBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		    bottomToolBar.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		    bottomToolBar.add(imageToolBar);
			
			basePanel.addSouth(bottomToolBar, 50);
			
//			basePanel.addSouth(imageToolBar, 50);
			
			
			borderVisible = true;
			
		} else if ( ViewLayoutMode.Panel == viewLayoutMode ) {
			
			logger.log(Level.FINE, "setLayout ViewLayoutMode.Panel");
			
			bottomToolBar = null;
			uiPanelImageToolBar = null;
			imageToolBar = null;
		}
		
		basePanel.add(upperMainPanel);
		
		switch (viewLayoutAction) 
		{
		case VDoubleLayout:
		{
			logger.log(Level.FINE, "setLayout ViewLayoutAction.VDoubleLayout");
			
			upperMainPanel.clear();
			
			uiPanelViews = createViews(2);
			
			uiPanelViews[0].setViewType(viewLayoutMode);
			uiPanelViews[0].setBorderVisible(borderVisible);
			uiPanelViews[1].setViewType(viewLayoutMode);
			uiPanelViews[1].setBorderVisible(borderVisible);
			
			VerticalPanel vp = new VerticalPanel();
		    vp.setWidth("100%");
		    vp.setHeight("100%");
			vp.setBorderWidth(LAYOUT_BORDER);
			DockLayoutPanel v0 = uiPanelViews[0].getPanel();
			vp.add(v0);
			
			DockLayoutPanel v1 = uiPanelViews[1].getPanel();
			vp.add(v1);
			
			vp.setCellHeight(v0, "50%");
			vp.setCellHeight(v1, "50%");
			
			upperMainPanel.add(vp);
			
			onViewIdActivateEvent(0); 
		}
			break;
		case HDoubleLayout:
		{
			logger.log(Level.FINE, "setLayout ViewLayoutAction.HDoubleLayout");
			
			upperMainPanel.clear();
			
			uiPanelViews = createViews(2);
			
			uiPanelViews[0].setViewType(viewLayoutMode);
			uiPanelViews[0].setBorderVisible(borderVisible);
			uiPanelViews[1].setViewType(viewLayoutMode);
			uiPanelViews[1].setBorderVisible(borderVisible);
			
			HorizontalPanel hp = new HorizontalPanel();
		    hp.setWidth("100%");
		    hp.setHeight("100%");
			hp.setBorderWidth(LAYOUT_BORDER);
			
			DockLayoutPanel v0 = uiPanelViews[0].getPanel();
			hp.add(v0);
			
			DockLayoutPanel v1 = uiPanelViews[1].getPanel();
			hp.add(v1);
			
			hp.setCellWidth(v0, "50%");
			hp.setCellWidth(v1, "50%");
			
			upperMainPanel.add(hp);
			
			onViewIdActivateEvent(0); 
		}
			break;
		case SingleLayout:
			logger.log(Level.FINE, "setLayout ViewLayoutAction.SingleLayout");
		default:
		{
			logger.log(Level.FINE, "setLayout default ViewLayoutAction.SingleLayout");
			borderVisible = false;
			
			upperMainPanel.clear();
			
			uiPanelViews = createViews(1);
			
			uiPanelViews[0].setViewType(viewLayoutMode);
			uiPanelViews[0].setBorderVisible(borderVisible);
			
			upperMainPanel.add(uiPanelViews[0].getPanel());
			
			onViewIdActivateEvent(0); 
		}	
			break;
			
		}
		logger.log(Level.FINE, "setLayout End");

	}

	/* (non-Javadoc)
	 * @see com.thalesgroup.mmi.viewlayout.client.view.UIPanelViewEvent#setViewIdActivate(int)
	 */
	@Override
	public void setViewIdActivate(int viewId) 
	{
		logger.log(Level.FINE, "setViewIdActivate Begin");
		
		this.viewLayoutMgr.setViewIdActivate(viewId);
		
		onViewIdActivateEvent(viewId);
		
		logger.log(Level.FINE, "setViewIdActivate End");
	}

	/* (non-Javadoc)
	 * @see com.thalesgroup.mmi.viewlayout.client.ViewLayoutMgrEvent#setTaskLaunch(com.thalesgroup.mmi.tasklaunch.client.TaskLaunch, int)
	 */
	@Override
	public void setTaskLaunch(UITaskLaunch taskLaunch, int viewId) 
	{
		logger.log(Level.FINE, "setTaskLaunch Begin");
		
		if ( null != this.uiPanelViews ) 
		{
			if ( viewId >= 0 && viewId < this.uiPanelViews.length ) {
				this.uiPanelViews[viewId].setTaskLaunch(taskLaunch);
			} else {
				logger.log(Level.FINE, "setTaskLaunch is INVALID viewId["+viewId+"] set to this.uiPanelViews.length["+this.uiPanelViews.length+"]");
			}
		}
		else 
		{
			logger.log(Level.FINE, "setTaskLaunch this.uiPanelViews is null");
		}
		
		
		logger.log(Level.FINE, "setTaskLaunch End");
	}

	/* (non-Javadoc)
	 * @see com.thalesgroup.mmi.viewlayout.client.view.UIPanelViewEvent#onViewIdActivateEvent(int)
	 */
	@Override
	public void onViewIdActivateEvent(int viewId) 
	{
		logger.log(Level.FINE, "onViewIdActivateEvent Begin");
		
		for(UIPanelView uiPanelView: uiPanelViews)
		{
			if ( uiPanelView.getViewId() != viewId ) 
			{
				uiPanelView.setActivate(false);
			} 
			else 
			{
				uiPanelView.setActivate(true);
			}
		}
		
		logger.log(Level.FINE, "onViewIdActivateEvent End");
	}
	
	/* (non-Javadoc)
	 * @see com.thalesgroup.mmi.viewlayout.client.ViewLayoutMgrEvent#setSplitButton()
	 */
	public void setSplitButton() {
		logger.log(Level.FINE, "setSplitButton Begin");
		
		uiPanelImageToolBar.setButton(UIPanelImageBarEventType.HDouble, viewLayoutMgr.getViewAction()==ViewLayoutAction.HDoubleLayout);
		
		uiPanelImageToolBar.setButton(UIPanelImageBarEventType.VDouble, viewLayoutMgr.getViewAction()==ViewLayoutAction.VDoubleLayout);
		
		logger.log(Level.FINE, "setSplitButton End");
	};

	/* (non-Javadoc)
	 * @see com.thalesgroup.mmi.viewlayout.client.viewtoolbar.UIPanelImageToolBarEvent#onImageButtonEvent(com.thalesgroup.mmi.viewlayout.client.viewtoolbar.UIPanelImageToolBarEvent.UIPanelImageBarEventType)
	 */
	@Override
	public void onImageButtonEvent(UIPanelImageBarEventType uiPanelImageBarEventType) {
		
		logger.log(Level.FINE, "onImageButtonEvent Begin");

		switch (uiPanelImageBarEventType) 
		{
		case ZoomIn:
			break;
		case ZoomOut:
			break;
		case Zoom:
			break;
		case Locator:
			break;
		case VDouble:
		{
			logger.log(Level.FINE, "onImageButtonEvent VDouble");
			
			viewLayoutMgr.setSplitScreen(ViewLayoutAction.VDoubleLayout);
		}
			break;
		case HDouble:
		{
			logger.log(Level.FINE, "onImageButtonEvent HDouble");
			
			viewLayoutMgr.setSplitScreen(ViewLayoutAction.HDoubleLayout);
		}
			break;
		default:
			logger.log(Level.FINE, "onButtonEvent Invald UIPanelImageBarEventType");
			break;
		}
		
		logger.log(Level.FINE, "onImageButtonEvent End");
	}

	@Override
	public void setActivateView(int viewIdActivate) {
		onViewIdActivateEvent(viewIdActivate);
	}
	
	
}
