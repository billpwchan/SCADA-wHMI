package com.thalesgroup.scadagen.whmi.uipanel.uipanelviewlayout.client.view;

import java.util.HashMap;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uitask.uitask.client.UITask_i;
import com.thalesgroup.scadagen.whmi.uitask.uitasklaunch.client.UITaskLaunch;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.WrapperScsSituationViewPanel;
import com.thalesgroup.scadagen.wrapper.wrapper.client.WrapperScsSituationViewPanelEvent;

public class UIPanelViewSchematic extends UIWidget_i implements UIPanelViewProvide_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIPanelViewSchematic.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private final String UIPathUIPanelViewLayout	= ":UIGws:UIPanelScreen:UIScreenMMI:UIPanelViewLayout";

	public static final String UNIT_PX = "px";
	
	@Override
	public void init() {
		final String function = "init";
		
		logger.begin(className, function);
		
		HorizontalPanel hp = new HorizontalPanel();
		hp.setWidth("100%");
		hp.setHeight("100%");
		hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

		rootPanel = new DockLayoutPanel(Unit.PX);
		rootPanel.add(hp);
		
		logger.end(className, function);
	}

	private WrapperScsSituationViewPanel wrapperScsSituationViewPanel = null;
	@Override
	public void setTaskProvide(UITask_i taskProvide) {
		final String function = "setTaskProvide";
		
		logger.begin(className, function);
		if ( null != taskProvide ) {
			if ( taskProvide instanceof UITaskLaunch ) {
				UITaskLaunch taskLaunch = (UITaskLaunch)taskProvide;

				String header = taskLaunch.getHeader();
				String uiPanel = taskLaunch.getUiPanel();
				
				logger.info(className, function, "header[{}] uiPanel[{}]", header, uiPanel);
				
				rootPanel.clear();

				wrapperScsSituationViewPanel = new WrapperScsSituationViewPanel(uiPanel);
				wrapperScsSituationViewPanel.setSize("100%", "100%");
				wrapperScsSituationViewPanel.setWrapperScsSituationViewPanelEvent(new WrapperScsSituationViewPanelEvent() {
					@Override
					public void triggerSymbolWidget(HashMap<String, String> options) {
						
						final String function = "triggerSymbolWidget";
						
						logger.begin(className, function);
						
						String configurationId = wrapperScsSituationViewPanel.getConfigurationId();
						
						logger.debug(className, function, "configurationId[{}]", configurationId);
						
						UITaskLaunch taskLaunch = new UITaskLaunch();
						taskLaunch.setUiPanel("ViewSchematicSymbolSelected");
						taskLaunch.setTaskUiScreen(uiNameCard.getUiScreen());
						taskLaunch.setUiPath(UIPathUIPanelViewLayout);
						
						taskLaunch.setOptions(options);
						taskLaunch.setOption("configurationId", configurationId);
						
						uiNameCard.getUiEventBus().fireEvent(new UIEvent(taskLaunch));
						
						logger.end(className, function);
					}
				});

				rootPanel.add(wrapperScsSituationViewPanel.getMainPanel());
			}
		} else {
			logger.info(className, function, "is not TaskLaunch");
		}
		
		logger.end(className, function);
	}

//	private UIViewEvent uiViewEvent = null;
	@Override
	public void setUIViewEvent(UIViewEvent uiViewEvent) {
//		if ( null != uiViewEvent ) {
//			this.uiViewEvent = uiViewEvent;
//		}
	}
}
