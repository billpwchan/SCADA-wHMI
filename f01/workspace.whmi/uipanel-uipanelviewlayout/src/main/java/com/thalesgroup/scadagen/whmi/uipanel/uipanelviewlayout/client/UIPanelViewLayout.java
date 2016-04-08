package com.thalesgroup.scadagen.whmi.uipanel.uipanelviewlayout.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uipanel.uipanel.client.UIPanel_i;
import com.thalesgroup.scadagen.whmi.uipanel.uipanelmgr.client.UIPanelMgr;
import com.thalesgroup.scadagen.whmi.uipanel.uipanelviewlayout.client.view.UIPanelView;
import com.thalesgroup.scadagen.whmi.uipanel.uipanelviewlayout.client.view.UIPanelViewEvent;
import com.thalesgroup.scadagen.whmi.uitask.uitasklaunch.client.UITaskLaunch;

public class UIPanelViewLayout implements UIPanel_i, UIPanelViewEvent, ViewLayoutMgrEvent {
	
	private static Logger logger = Logger.getLogger(UIPanelViewLayout.class.getName());
		
	public static final String UNIT_PX		= "px";
	public static final int LAYOUT_BORDER	= 0;
	
	public static final String RGB_BTN_SEL 	= "rgb(246, 230, 139)";
	public static final String RGB_BTN_BG	= "#F1F1F1";
	public static final String IMG_NONE		= "none";	

	private ViewLayoutMgr viewLayoutMgr;
	
	private UIPanelView[] uiPanelViews;
	
	private DockLayoutPanel basePanel;
	
	private ComplexPanel upperMainPanel;

	/**
	 * @param viewId
	 * @return
	 */
	private UIPanelView createView(int viewId)
	{
		logger.log(Level.FINE, "getView Begin");
		
		UIPanelView v = new UIPanelView(this, this.uiNameCard, viewId);
		v.setViewId(viewId);
		v.getPanel().addStyleName("project-gwt-panel-viewlayout-inner-view");
		
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
	public ComplexPanel getMainPanel(UINameCard uiNameCard) 
	{ 
		logger.log(Level.FINE, "getMainPanel Begin...");
		
		this.uiNameCard = new UINameCard(uiNameCard);
		this.uiNameCard.appendUIPanel(this);
		
		this.viewLayoutMgr = new ViewLayoutMgr(this, this.uiNameCard);

		basePanel = new DockLayoutPanel(Unit.PX);
		basePanel.addStyleName("project-gwt-panel-viewlayout-main");
		
		logger.log(Level.FINE, "getMainPanel End.");
				
		return basePanel; 
	}
	

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

			borderVisible = true;
			
		} else if ( ViewLayoutMode.Panel == viewLayoutMode ) {
			
			logger.log(Level.FINE, "setLayout ViewLayoutMode.Panel");

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
			uiPanelViews[0].setViewAction(viewLayoutAction);
			uiPanelViews[0].setBorderVisible(borderVisible);
			uiPanelViews[1].setViewType(viewLayoutMode);
			uiPanelViews[1].setViewAction(viewLayoutAction);
			uiPanelViews[1].setBorderVisible(borderVisible);
			
			VerticalPanel vp = new VerticalPanel();
			vp.addStyleName("project-gwt-panel-viewlayout-outer-view-vp");
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
			uiPanelViews[0].setViewAction(viewLayoutAction);
			uiPanelViews[0].setBorderVisible(borderVisible);
			uiPanelViews[1].setViewType(viewLayoutMode);
			uiPanelViews[1].setViewAction(viewLayoutAction);
			uiPanelViews[1].setBorderVisible(borderVisible);
			
			HorizontalPanel hp = new HorizontalPanel();
			hp.addStyleName("project-gwt-panel-viewlayout-outer-view-hp");
			
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

	@Override
	public void setViewIdActivate(int viewId) 
	{
		logger.log(Level.FINE, "setViewIdActivate Begin");
		
		this.viewLayoutMgr.setViewIdActivate(viewId);
		
		onViewIdActivateEvent(viewId);
		
		logger.log(Level.FINE, "setViewIdActivate End");
	}

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

	@Override
	public void setActivateView(int viewIdActivate) {
		onViewIdActivateEvent(viewIdActivate);
	}
	
}
