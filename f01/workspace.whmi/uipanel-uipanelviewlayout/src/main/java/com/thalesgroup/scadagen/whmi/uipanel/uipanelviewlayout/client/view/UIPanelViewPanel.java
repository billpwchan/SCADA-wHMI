package com.thalesgroup.scadagen.whmi.uipanel.uipanelviewlayout.client.view;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Panel;
import com.thalesgroup.scadagen.whmi.uitask.uitask.client.UITask_i;
import com.thalesgroup.scadagen.whmi.uitask.uitasklaunch.client.UITaskLaunch;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.UIViewMgr;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;

public class UIPanelViewPanel extends UIWidget_i implements UIPanelViewProvide_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIPanelViewPanel.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);

	InlineLabel equipmenpLabel = null;
	
	@Override
	public void init() {
		final String function = "init";
		
		logger.begin(className, function);
		
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
		
		logger.end(className, function);
	}

	@Override
	public void setTaskProvide(UITask_i taskProvide) {
		final String function = "setTaskProvide";
		
		logger.begin(className, function);
		
		if ( null != taskProvide) {
		
			if ( taskProvide instanceof UITaskLaunch ) {
				
				UITaskLaunch taskLaunch = (UITaskLaunch)taskProvide;

				logger.info(className, function, "taskLaunch.getHeader()[{}]", taskLaunch.getUiPanel());
				
				this.equipmenpLabel.setText("Panel: ---- ");
				
				logger.info(className, function, "root clear()");
				
				UIViewMgr viewFactoryMgr = UIViewMgr.getInstance();
				
				logger.info(className, function, "viewFactoryMgr.getPanel[{}]", taskLaunch.getUiPanel());
				
				UIWidget_i uiWidget_i = viewFactoryMgr.getPanel(taskLaunch.getUiPanel(), uiNameCard);
				
				if ( null != uiWidget_i ) {
					
					logger.info(className, function, "root.clear");
				
					this.rootPanel.clear();

					logger.info(className, function, "uiViewProvide.getMainPanel[{}]", uiNameCard.getUiPath());
				
					Panel panel = uiWidget_i.getMainPanel();
					
					logger.info(className, function, "root.add");
				
					this.rootPanel.add(panel);					
				
				} else {
					
					logger.info(className, function, "taskLaunch.getUiPanel()[{}] can't NOT FOUND!", taskLaunch.getUiPanel());
					
					String header = taskLaunch.getHeader();
					
					logger.info(className, function, "taskLaunch.getHeader()[{}]", header);
				
					this.equipmenpLabel.setText("Panel: "+header);					
				}
			} else {
				logger.info(className, function, "taskProvide is not TaskLaunch");
			}
		} else {
			logger.info(className, function, "taskProvide is null");
		}
		
		logger.end(className, function);
	}
}
