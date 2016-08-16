package com.thalesgroup.scadagen.whmi.uipanel.uipanelviewlayout.client.view;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uitask.uitask.client.UITask_i;
import com.thalesgroup.scadagen.whmi.uitask.uitasklaunch.client.UITaskLaunch;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.WrapperScsSituationViewPanel;
import com.thalesgroup.scadagen.wrapper.wrapper.client.WrapperScsSituationViewPanelEvent;

public class UIPanelViewSchematic extends UIWidget_i implements UIPanelViewProvide_i {
	
	private Logger logger = Logger.getLogger(UIPanelViewSchematic.class.getName());
	
	private final String UIPathUIScreenMMI 	= ":UIGws:UIPanelScreen:UIScreenMMI";

	public static final String UNIT_PX = "px";
	
	@Override
	public void init() {
		
		logger.log(Level.FINE, "init Begin");
		
		HorizontalPanel hp = new HorizontalPanel();
		hp.setWidth("100%");
		hp.setHeight("100%");
		hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

		rootPanel = new DockLayoutPanel(Unit.PX);
		rootPanel.add(hp);
		
		logger.log(Level.FINE, "init End");
	}

	@Override
	public void setTaskProvide(UITask_i taskProvide) {
		
		logger.log(Level.FINE, "setTaskProvide Begin");
		if ( null != taskProvide ) {
			if ( taskProvide instanceof UITaskLaunch ) {
				UITaskLaunch taskLaunch = (UITaskLaunch)taskProvide;

				String header = taskLaunch.getHeader();
				String uiPanel = taskLaunch.getUiPanel();
				
				logger.log(Level.SEVERE, "setTaskProvide header["+header+"] uiPanel["+uiPanel+"]");
				
				rootPanel.clear();

				WrapperScsSituationViewPanel wrapperScsSituationViewPanel = new WrapperScsSituationViewPanel(uiPanel);
				wrapperScsSituationViewPanel.setSize("100%", "100%");
				wrapperScsSituationViewPanel.setWrapperScsSituationViewPanelEvent(new WrapperScsSituationViewPanelEvent() {
					@Override
					public void triggerSymbolWidget(String hv_id, int mouseX, int mouseY) {
						showInspectorPanel(hv_id, mouseX, mouseY);
					}
				});

				rootPanel.add(wrapperScsSituationViewPanel.getMainPanel());
			}
		} else {
			logger.log(Level.FINE, "setTaskProvide taskProvide is not TaskLaunch");
		}
		
		logger.log(Level.FINE, "setTaskProvide End");
	}

	private void showInspectorPanel (String hv_id, int mouseX, int mouseY) {
		UITaskLaunch taskLaunch = new UITaskLaunch();
		taskLaunch.setUiPanel("UIPanelInspector");
		taskLaunch.setTaskUiScreen(this.uiNameCard.getUiScreen());
		taskLaunch.setUiPath(UIPathUIScreenMMI);
		taskLaunch.setOption(new Object[]{hv_id, mouseX, mouseY});
		this.uiNameCard.getUiEventBus().fireEvent(new UIEvent(taskLaunch));
	}

}
