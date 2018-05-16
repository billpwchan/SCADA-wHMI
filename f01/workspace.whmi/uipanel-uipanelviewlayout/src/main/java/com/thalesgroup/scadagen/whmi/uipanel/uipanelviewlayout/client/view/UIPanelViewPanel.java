package com.thalesgroup.scadagen.whmi.uipanel.uipanelviewlayout.client.view;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Panel;
import com.thalesgroup.scadagen.whmi.uitask.uitask.client.UITask_i;
import com.thalesgroup.scadagen.whmi.uitask.uitasklaunch.client.UITaskLaunch;
import com.thalesgroup.scadagen.whmi.uitask.uitasklaunch.client.UITaskLaunch_i.UITaskLaunchAttribute;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetmgr.client.UIWidgetMgr;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetmgr.client.UIWidgetMgrFactory;

public class UIPanelViewPanel extends UIWidget_i implements UIPanelViewProvide_i {

	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());

	InlineLabel equipmenpLabel = null;
	
	@Override
	public void init() {
		final String function = "init";
		
		logger.begin(function);
		
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
		
		logger.end(function);
	}

	private UIWidget_i uiWidget_i = null;
	@Override
	public void setTaskProvide(UITask_i taskProvide) {
		final String function = "setTaskProvide";
		
		logger.begin(function);
		
		if ( null != taskProvide) {
		
			if ( taskProvide instanceof UITaskLaunch ) {
				
				if ( null != uiWidget_i ) {
					logger.debug(function, "uiWidget_i.terminate");
					uiWidget_i.terminate();
					uiWidget_i = null;
					logger.debug(function, "root.clear");
					this.rootPanel.clear();
				}
				
				this.equipmenpLabel.setText("Panel: ---- ");
				
				UITaskLaunch taskLaunch = (UITaskLaunch)taskProvide;
				
				String uiConf = taskLaunch.getUiConf();
				logger.debug(function, "uiConf[{}]", uiConf);
				
				String uiCtrl = null;
				String uiView = null;
				String uiOpts = null;
				String uiElem = null;
				String uiDict = null;
				
				if ( null != uiConf && uiConf.trim().length() > 0 ) {
					Map<String, String> confs = Util.getParameters(uiConf);
					uiCtrl	= confs.get(UITaskLaunchAttribute.UICtrl.toString());
					uiView	= confs.get(UITaskLaunchAttribute.UIView.toString());
					uiOpts	= confs.get(UITaskLaunchAttribute.UIOpts.toString());
					uiElem	= confs.get(UITaskLaunchAttribute.UIElem.toString());
					uiDict	= confs.get(UITaskLaunchAttribute.UIDict.toString());
				} else {
					logger.debug(function, "uiConf IS NULL");
					
					uiCtrl	= taskLaunch.getUiCtrl();
					uiView	= taskLaunch.getUiView();
					uiOpts	= taskLaunch.getUiOpts();
					uiElem	= taskLaunch.getUiElem();
					uiDict	= taskLaunch.getUiDict();
				}
				logger.debug(function, "uiCtrl[{}] uiView[{}] uiOpts[{}] uiElem[{}]", new Object[]{uiCtrl, uiView, uiOpts, uiElem});
				
				Map<String, Object> options = new HashMap<String, Object>();

				UIWidgetMgrFactory factory = UIWidgetMgr.getInstance();
				uiWidget_i = factory.getUIWidget(uiCtrl, uiView, uiNameCard, uiOpts, uiElem, uiDict, options);
				
				if ( null != uiWidget_i ) {
					logger.debug(function, "uiViewProvide.getMainPanel[{}]", uiNameCard.getUiPath());
				
					Panel panel = uiWidget_i.getMainPanel();
					logger.debug(function, "root.add");
				
					this.rootPanel.add(panel);					
				
				} else {
					logger.debug(function, "uiCtrl[{}] can't NOT FOUND!", uiCtrl);
					
					String header = taskLaunch.getHeader();
					logger.debug(function, "taskLaunch.getHeader()[{}]", header);
				
					this.equipmenpLabel.setText("Panel: "+header);					
				}
			} else {
				logger.warn(function, "taskProvide is not TaskLaunch");
			}
		} else {
			logger.warn(function, "taskProvide is null");
		}
		
		logger.end(function);
	}
	@Override
	public void setUIViewEvent(UIViewEvent uiViewEvent) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void terminate() {
		final String function = "terminate";
		logger.begin(function);
		if ( null != uiWidget_i ) uiWidget_i.terminate();
		logger.end(function);
	}
}
