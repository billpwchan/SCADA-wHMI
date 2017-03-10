package com.thalesgroup.scadagen.whmi.uipanel.uipanelviewlayout.client.view;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.thalesgroup.scadagen.whmi.uitask.uitask.client.UITask_i;
import com.thalesgroup.scadagen.whmi.uitask.uitasklaunch.client.UITaskLaunch;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;

public class UIPanelViewEmpty extends UIWidget_i implements UIPanelViewProvide_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIPanelViewEmpty.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	public static final String UNIT_PX		= "px";

	public static final String IMAGE_PATH	= "imgs";
	
	InlineLabel equipmenpLabel = null;
	
	private boolean bInit = false;
	private boolean bKill = false;
	
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
		equipmenpLabel.setText("Panel: This is the empty panel");
		hp.add(equipmenpLabel);
		
//		equipmenpLabel.addClickHandler(new ClickHandler() {
//			
//			@Override
//			public void onClick(ClickEvent arg0) {
//				onClickLabel();
//			}
//		});

		rootPanel = new DockLayoutPanel(Unit.PX);
		rootPanel.add(hp);
		
		if ( ! bInit ) logger.info(className, function, "already init");
		
		bInit = true;
		
		logger.end(className, function);
	}
	
	@Override
	public void setTaskProvide(UITask_i taskProvide) {
		final String function = "setTaskProvide";
		
		logger.begin(className, function);
		
		if ( null != taskProvide ) {
			if ( taskProvide instanceof UITaskLaunch ) {
				
				UITaskLaunch taskLaunch = (UITaskLaunch)taskProvide;
				
				if ( null != taskLaunch) {
					logger.info(className, function, "taskLaunch.getHeader()[{}]", taskLaunch.getHeader());
					this.equipmenpLabel.setText("Empty: "+taskLaunch.getHeader()+" can't be found!");
				}
			}			
		} else {
			logger.info(className, function, "taskProvide is not TaskLaunch");
		}

		logger.end(className, function);
	}
	
	@Override
	public void setUIViewEvent(UIViewEvent uiViewEvent) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void terminate() {
		final String function = "terminate";
		logger.begin(className, function);
		
		if ( ! bKill ) logger.info(className, function, "already kill");
		
		bKill = true;
		
		logger.end(className, function);
	}
}
