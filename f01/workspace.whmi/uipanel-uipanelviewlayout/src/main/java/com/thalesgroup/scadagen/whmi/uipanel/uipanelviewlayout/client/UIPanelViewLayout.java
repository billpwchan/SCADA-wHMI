package com.thalesgroup.scadagen.whmi.uipanel.uipanelviewlayout.client;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.thalesgroup.scadagen.whmi.uipanel.uipanelviewlayout.client.view.UIPanelView;
import com.thalesgroup.scadagen.whmi.uipanel.uipanelviewlayout.client.view.UIPanelViewEvent;
import com.thalesgroup.scadagen.whmi.uitask.uitasklaunch.client.UITaskLaunch;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;

public class UIPanelViewLayout extends UIWidget_i implements UIPanelViewEvent, ViewLayoutMgrEvent {

	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());

	private ViewLayoutMgr viewLayoutMgr;
	
	private UIPanelView[] uiPanelViews = null;
	
	private Panel upperMainPanel;
	
	@Override
	public void init() {
		final String function = "init";
		
		logger.begin(function);
		
		this.viewLayoutMgr = new ViewLayoutMgr(this, this.uiNameCard);
		
		logger.debug(function, "this.uiNameCard.getUiScreen()[{}] this.uiNameCard.getUiPath()[{}]", this.uiNameCard.getUiScreen(), this.uiNameCard.getUiPath());
		
		rootPanel = new DockLayoutPanel(Unit.PX);
		rootPanel.addStyleName("project-gwt-panel-viewlayout-main");

		logger.end(function);
	}

	/**
	 * @param viewId
	 * @return
	 */
	private UIPanelView createView(int viewId) {
		final String function = "createView";
		
		logger.begin(function);
		
		UIPanelView v = new UIPanelView(this, this.uiNameCard, viewId);
		v.setViewId(viewId);
		v.getMainPanel().addStyleName("project-gwt-panel-viewlayout-inner-view");
		
		logger.end(function);
		
		return v;
	}
	
	/**
	 * @param size
	 * @return
	 */
	private UIPanelView[] createViews(int size) {
		final String function = "createViews";
		
		logger.begin(function);
		
		UIPanelView[] vs = new UIPanelView[size];
		for(int i=0;i<size;++i) {
			vs[i] = createView(i);
		}
		
		logger.end(function);
		
		return vs;
	}
	
	@Override
	public void setLayout(ViewLayoutMode viewLayoutMode, ViewLayoutAction viewLayoutAction) {
		final String function = "setLayout";
		
		logger.begin(function);
		
		logger.debug(function, "ViewLayoutMode[{}] viewLayoutAction[{}]", viewLayoutMode, viewLayoutAction);
		
//		UIPanelMgr uiPanelFactoryMgr = UIPanelMgr.getInstance();
//		upperMainPanel = uiPanelFactoryMgr.getMainPanel("UIPanelEmpty", uiNameCard);
		
		UIPanelEmpty uiPanelEmpty = new UIPanelEmpty();
		uiPanelEmpty.setUINameCard(uiNameCard);
		uiPanelEmpty.setDictionaryFolder(dictionaryFolder);
		uiPanelEmpty.setViewXMLFile(viewXMLFile);
		uiPanelEmpty.setOptsXMLFile(optsXMLFile);
		uiPanelEmpty.init();
		upperMainPanel = uiPanelEmpty.getMainPanel();
		
		rootPanel.clear();
		rootPanel.add(upperMainPanel);
		
		boolean borderVisible = false;
		
		if ( ViewLayoutMode.Image == viewLayoutMode ) {
			
			logger.debug(function, "ViewLayoutMode.Image");

			borderVisible = true;
			
		} else if ( ViewLayoutMode.Panel == viewLayoutMode ) {
			logger.debug(function, "ViewLayoutMode.Panel");
		}
		
		if ( null != uiPanelViews ) {
			for ( UIPanelView uiPanelView : uiPanelViews ) {
				if ( null != uiPanelView )	uiPanelView.terminate();
			}
		}
		
		switch (viewLayoutAction) 
		{
		case VDoubleLayout:
		{
			logger.debug(function, "ViewLayoutAction.VDoubleLayout");
			
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
			
			Panel v0 = uiPanelViews[0].getMainPanel();
			vp.add(v0);
			
			Panel v1 = uiPanelViews[1].getMainPanel();
			vp.add(v1);
			
			vp.setCellHeight(v0, "50%");
			vp.setCellHeight(v1, "50%");
			
			upperMainPanel.add(vp);
			
			onViewIdActivateEvent(0); 
		}
			break;
		case HDoubleLayout:
		{
			logger.debug(function, "ViewLayoutAction.HDoubleLayout");
			
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
			
			Panel v0 = uiPanelViews[0].getMainPanel();
			hp.add(v0);
			
			Panel v1 = uiPanelViews[1].getMainPanel();
			hp.add(v1);
			
			hp.setCellWidth(v0, "50%");
			hp.setCellWidth(v1, "50%");
			
			upperMainPanel.add(hp);
			
			onViewIdActivateEvent(0); 
		}
			break;
		case SingleLayout:
			logger.debug(function, "ViewLayoutAction.SingleLayout");
		default:
		{
			logger.debug(function, "default ViewLayoutAction.SingleLayout");
			borderVisible = false;
			
			upperMainPanel.clear();
			
			uiPanelViews = createViews(1);
			
			uiPanelViews[0].setViewType(viewLayoutMode);
			uiPanelViews[0].setBorderVisible(borderVisible);
			
			upperMainPanel.add(uiPanelViews[0].getMainPanel());
			
			onViewIdActivateEvent(0); 
		}	
			break;
			
		}
		logger.end(function);
	}

	@Override
	public void setViewIdActivate(int viewId) {
		final String function = "setViewIdActivate";
		
		logger.begin(function);
		
		this.viewLayoutMgr.setViewIdActivate(viewId);
		
		onViewIdActivateEvent(viewId);
		
		logger.end(function);
	}

	@Override
	public void setTaskLaunch(UITaskLaunch taskLaunch, int viewId) {
		final String function = "setTaskLaunch";
		
		logger.begin(function);
		
		if ( null != this.uiPanelViews ) {
			if ( viewId >= 0 && viewId < this.uiPanelViews.length ) {
				UIPanelView uiPanelView = this.uiPanelViews[viewId];
				uiPanelView.terminate();
				uiPanelView.setTaskLaunch(taskLaunch);
			} else {
				logger.debug(function, "viewId is INVALID viewId["+viewId+"] set to this.uiPanelViews.length["+this.uiPanelViews.length+"]");
			}
		} else {
			logger.debug(function, "this.uiPanelViews is null");
		}
		logger.end(function);
	}

	@Override
	public void onViewIdActivateEvent(int viewId) {
		final String function = "onViewIdActivateEvent";
		
		logger.begin(function);
		
		for(UIPanelView uiPanelView: uiPanelViews){
			if ( uiPanelView.getViewId() != viewId ) {
				uiPanelView.setActivate(false);
			} else {
				uiPanelView.setActivate(true);
			}
		}
		logger.end(function);
	}

	@Override
	public void setActivateView(int viewIdActivate) {
		onViewIdActivateEvent(viewIdActivate);
	}

}
