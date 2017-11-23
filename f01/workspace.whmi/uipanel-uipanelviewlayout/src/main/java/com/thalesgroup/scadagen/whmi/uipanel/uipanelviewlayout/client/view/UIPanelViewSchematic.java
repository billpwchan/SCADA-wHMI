package com.thalesgroup.scadagen.whmi.uipanel.uipanelviewlayout.client.view;

import java.util.Map;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uitask.uitask.client.UITask_i;
import com.thalesgroup.scadagen.whmi.uitask.uitasklaunch.client.UITaskLaunch;
import com.thalesgroup.scadagen.whmi.uitask.uitasklaunch.client.UITaskLaunch_i.UITaskLaunchAttribute;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.panel.SCADAgenSituationViewPanel;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.panel.SCADAgenSituationViewPanelEvent;

public class UIPanelViewSchematic extends UIWidget_i implements UIPanelViewProvide_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIPanelViewSchematic.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private final String UIPathUIPanelViewLayout	= ":UIGws:UIPanelScreen:UIScreenMMI:UIPanelViewLayout";
	
	private final String STR_SYMBOL_SELECTED		= "ViewSchematicSymbolSelected";
	
	private final String STR_CONFIGURATION_ID		= "configurationId";
	
	private final String STR_SG_S_VP_CONTROL_NAME	= UIWidgetUtil.getClassSimpleName(SCADAgenSituationViewPanel.class.getName());

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

	private SCADAgenSituationViewPanel scadagenSituationViewPanel = null;
	@Override
	public void setTaskProvide(UITask_i taskProvide) {
		final String function = "setTaskProvide";
		
		logger.begin(className, function);
		if ( null != taskProvide ) {
			if ( taskProvide instanceof UITaskLaunch ) {
				UITaskLaunch taskLaunch = (UITaskLaunch)taskProvide;
				
				String uiConf = taskLaunch.getUiConf();
				logger.debug(className, function, "uiConf[{}]", uiConf);
				
				String uiCtrl = null;
				String uiSvId = null;
				
				if ( null != uiConf && uiConf.trim().length() > 0 ) {
					Map<String, String> confs = Util.getParameters(uiConf);
					uiCtrl = confs.get(UITaskLaunchAttribute.UICtrl.toString());
					uiSvId = confs.get(UITaskLaunchAttribute.UISvId.toString());
				} else {
					uiCtrl = taskLaunch.getUiCtrl();
					uiSvId = taskLaunch.getUiSvId();
				}
				
				logger.debug(className, function, "uiCtrl[{}]", uiCtrl);
				
				if ( null != uiCtrl ) {
					
					if ( uiCtrl.equals(STR_SG_S_VP_CONTROL_NAME) ) {
						
						logger.debug(className, function, "uiCtrl[{}] uiSvId[{}]", new Object[]{uiCtrl, uiSvId});
						
						rootPanel.clear();

						scadagenSituationViewPanel = new SCADAgenSituationViewPanel(uiSvId);
						scadagenSituationViewPanel.setSize("100%", "100%");
						scadagenSituationViewPanel.setSCADAgenSituationViewPanelEvent(new SCADAgenSituationViewPanelEvent() {
							@Override
							public void triggerSymbolWidget(Map<String, String> options) {
								final String function = "triggerSymbolWidget";
								logger.begin(className, function);
								
								String configurationId = scadagenSituationViewPanel.getConfigurationId();
								
								logger.debug(className, function, "configurationId[{}]", configurationId);
								
								UITaskLaunch taskLaunch = new UITaskLaunch();
								taskLaunch.setUiCtrl(STR_SYMBOL_SELECTED);
								taskLaunch.setTaskUiScreen(uiNameCard.getUiScreen());
								taskLaunch.setUiPath(UIPathUIPanelViewLayout);
								
								taskLaunch.setOptions(options);
								taskLaunch.setOption(STR_CONFIGURATION_ID, configurationId);
								
								uiNameCard.getUiEventBus().fireEvent(new UIEvent(taskLaunch));
								
								logger.end(className, function);
							}
						});

						rootPanel.add(scadagenSituationViewPanel.getMainPanel());
					} else {
						logger.warn(className, function, "uiCtrl[{}] NOT FOUND!", uiCtrl);
					}
				} else {
					logger.warn(className, function, "uiCtrl IS NULL");
				}
			}
		} else {
			logger.warn(className, function, "is not TaskLaunch");
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
	
	@Override
	public void terminate() {
		final String function = "terminate";
		logger.begin(className, function);
		if ( null != scadagenSituationViewPanel ) scadagenSituationViewPanel.terminate();
		logger.end(className, function);
	}
}
