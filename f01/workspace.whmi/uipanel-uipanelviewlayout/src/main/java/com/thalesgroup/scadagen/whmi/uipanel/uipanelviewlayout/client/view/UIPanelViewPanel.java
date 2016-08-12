package com.thalesgroup.scadagen.whmi.uipanel.uipanelviewlayout.client.view;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.thalesgroup.scadagen.whmi.uitask.uitask.client.UITask_i;
import com.thalesgroup.scadagen.whmi.uitask.uitasklaunch.client.UITaskLaunch;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.UIViewMgr;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;

public class UIPanelViewPanel extends UIWidget_i implements UIPanelViewProvide_i {
	
	private Logger logger = Logger.getLogger(UIPanelViewPanel.class.getName());

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
		equipmenpLabel.setText("Panel: ----");
		hp.add(equipmenpLabel);

		rootPanel = new DockLayoutPanel(Unit.PX);
		rootPanel.add(hp);
		
		logger.log(Level.FINE, "init End");
	}

	@Override
	public void setTaskProvide(UITask_i taskProvide) {
		
		logger.log(Level.FINE, "setTaskProvide Begin");
		
		if ( null != taskProvide) {
		
			if ( taskProvide instanceof UITaskLaunch ) {
				
				UITaskLaunch taskLaunch = (UITaskLaunch)taskProvide;

				logger.log(Level.FINE, "setTaskProvide taskLaunch.getHeader()["+taskLaunch.getUiPanel()+"]");
				
				this.equipmenpLabel.setText("Panel: ---- ");
				
				logger.log(Level.FINE, "setTaskProvide root clear()");
				
				UIViewMgr viewFactoryMgr = UIViewMgr.getInstance();
				
				logger.log(Level.FINE, "setTaskProvide viewFactoryMgr.getPanel["+taskLaunch.getUiPanel()+"]");
				
				UIWidget_i uiWidget_i = viewFactoryMgr.getPanel(taskLaunch.getUiPanel(), uiNameCard);
				
				if ( null != uiWidget_i ) {
					
					logger.log(Level.FINE, "setTaskProvide root.clear");
				
					this.rootPanel.clear();

					logger.log(Level.FINE, "setTaskProvide uiViewProvide.getMainPanel["+uiNameCard.getUiPath()+"]");
				
					ComplexPanel dockLayoutPanel = uiWidget_i.getMainPanel();
					
					logger.log(Level.FINE, "setTaskProvide root.add");
				
					this.rootPanel.add(dockLayoutPanel);					
				
				} else {
					
					logger.log(Level.FINE, "setTaskProvide taskLaunch.getUiPanel()["+taskLaunch.getUiPanel()+"] can't NOT FOUND!");
					
					String header = taskLaunch.getHeader();
					
					logger.log(Level.FINE, "setTaskProvide taskLaunch.getHeader()["+header+"]");
				
					this.equipmenpLabel.setText("Panel: "+header);					
				}
			} else {
				logger.log(Level.FINE, "setTaskProvide taskProvide is not TaskLaunch");
			}
		} else {
			logger.log(Level.FINE, "setTaskProvide taskProvide is null");
		}
		
		logger.log(Level.FINE, "setTaskProvide End");
	}
}
