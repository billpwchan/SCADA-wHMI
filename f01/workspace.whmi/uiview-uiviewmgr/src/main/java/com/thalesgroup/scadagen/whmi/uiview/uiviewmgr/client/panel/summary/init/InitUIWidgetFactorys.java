package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.summary.init;

import java.util.Map;

import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.entrypoint.UILayoutEntryPoint;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uilayout.UILayoutLogin;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uilayout.UILayoutSoc;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UILayoutConfiguration;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetBlackboard;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetBox;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetCSSSelection;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetCSSSwitch;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetConfiguration;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetCtlControl;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetDataGrid;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetDioBtnsControl;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetDpcControl;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetDpcManualOverrideControl;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetDpcScanSuspendControl;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetDpcTagControl;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetFilter;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetMatrixViewer;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetOPMChangePasswordControl;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetPrint;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetSimultaneousLoginControl;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetSocAutoManuControl;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetSocControl;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetSocDelayControl;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetSocFilterControl;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetSocGrcPoint;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetSocTitle;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetViewer;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetViewerPager;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIView_i.WidgetParameterName;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetmgr.client.UIWidgetMgr;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetmgr.client.UIWidgetMgrFactory;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.matrix.ScsMatrixPanel;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.panel.ScsOlsListPanel;

public class InitUIWidgetFactorys {
	
	private final static String name = InitUIWidgetFactorys.class.getName();
	private final static String className = UIWidgetUtil.getClassSimpleName(name);
	private final static UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	public static void init(final Map<String, String> parameters) {
		String function = "init";
		logger.begin(className, function);
	
		UIWidgetMgr uiWidgetMgr = UIWidgetMgr.getInstance();
		uiWidgetMgr.removeUIWidgetFactory(name);
		uiWidgetMgr.addUIWidgetFactory(name, new UIWidgetMgrFactory() {
			@Override
			public UIWidget_i getUIWidget(String uiCtrl, String uiView, UINameCard uiNameCard, String uiOpts, String uiElement
					, String uiDict
					, Map<String, Object> options) {
				final String function = "getUIWidget";
	
				logger.info(className, function, "uiCtrl[{}] uiView[{}] uiOpts[{}] uiDict[{}]", new Object[]{uiCtrl, uiView, uiOpts, uiDict});
	
				if (null != uiNameCard) {
					logger.info(className, function, "uiNameCard UIPath[{}] UIScreen[{}]", uiNameCard.getUiPath(),
							uiNameCard.getUiScreen());
				} else {
					logger.warn(className, function, "uiNameCard IS NULL");
				}
				logger.info(className, function, "options IS NULL[{}]", null == options);
	
				UIWidget_i uiWidget_i = null;
	
				if ( UIWidgetUtil.getClassSimpleName(
						UILayoutEntryPoint.class.getName()).equals(uiCtrl) ) {
					
					uiWidget_i = new UILayoutEntryPoint();

				}
				else if (
						UIWidgetUtil.getClassSimpleName(UIWidgetConfiguration.class.getName())
						.equals(uiCtrl)
						) {
	
					uiWidget_i = new UIWidgetConfiguration();
	
				}
				else if (
						UIWidgetUtil.getClassSimpleName(UILayoutConfiguration.class.getName())
						.equals(uiCtrl)
						) {
	
					uiWidget_i = new UILayoutConfiguration();
	
				}
				else if (
						UIWidgetUtil.getClassSimpleName(UIWidgetViewer.class.getName())
						.equals(uiCtrl)
						) {
	
					uiWidget_i = new UIWidgetViewer();
	
				}
				else if (
						UIWidgetUtil.getClassSimpleName(UIWidgetViewerPager.class.getName())
						.equals(uiCtrl)
						) {
	
					uiWidget_i = new UIWidgetViewerPager();
					
				}
				else if (
						UIWidgetUtil.getClassSimpleName(UIWidgetMatrixViewer.class.getName())
						.equals(uiCtrl)
						) {
	
					uiWidget_i = new UIWidgetMatrixViewer();
	
				}
				else if ( 
						UIWidgetUtil.getClassSimpleName(UIWidgetCtlControl.class.getName())
						.equals(uiCtrl) ) {
					
					uiWidget_i = new UIWidgetCtlControl();
						
				}
				else if (
						UIWidgetUtil.getClassSimpleName(UIWidgetDpcControl.class.getName())
						.equals(uiCtrl)
						) {
					
					uiWidget_i = new UIWidgetDpcControl();
	
				}
				else if (
						UIWidgetUtil.getClassSimpleName(UIWidgetDioBtnsControl.class.getName())
						.equals(uiCtrl)
						) {
					
					uiWidget_i = new UIWidgetDioBtnsControl();
	
				}
				else if (
						UIWidgetUtil.getClassSimpleName(UIWidgetDpcTagControl.class.getName())
						.equals(uiCtrl)
						) {
					
					uiWidget_i = new UIWidgetDpcTagControl();
	
				}
				else if (
						UIWidgetUtil.getClassSimpleName(UIWidgetDpcScanSuspendControl.class.getName())
						.equals(uiCtrl)
						) {
					
					uiWidget_i = new UIWidgetDpcScanSuspendControl();
	
				}
				else if (
						UIWidgetUtil.getClassSimpleName(UIWidgetDpcManualOverrideControl.class.getName())
						.equals(uiCtrl)
						) {
					
					uiWidget_i = new UIWidgetDpcManualOverrideControl();
	
				}
				else if (
						UIWidgetUtil.getClassSimpleName(UIWidgetFilter.class.getName())
						.equals(uiCtrl)
						) {
	
					uiWidget_i = new UIWidgetFilter();
	
				}
				else if (
						UIWidgetUtil.getClassSimpleName(UIWidgetPrint.class.getName())
						.equals(uiCtrl)
						) {
	
					uiWidget_i = new UIWidgetPrint();
	
				}
				else if (
						UIWidgetUtil.getClassSimpleName(ScsOlsListPanel.class.getName())
						.equals(uiCtrl)
						) {
					
					uiWidget_i = new ScsOlsListPanel();
					
				}
				else if (
						UIWidgetUtil.getClassSimpleName(ScsMatrixPanel.class.getName())
						.equals(uiCtrl)
						) {
					
					uiWidget_i = new ScsMatrixPanel();
	
				}
				else if (
						UIWidgetUtil.getClassSimpleName(UIWidgetCSSSelection.class.getName())
						.equals(uiCtrl)
						) {
	
					uiWidget_i = new UIWidgetCSSSelection();
	
				}
				else if (
						UIWidgetUtil.getClassSimpleName(UIWidgetCSSSwitch.class.getName())
						.equals(uiCtrl)
						) {
	
					uiWidget_i = new UIWidgetCSSSwitch();
	
				}
				else if (
						UIWidgetUtil.getClassSimpleName(UIWidgetBlackboard.class.getName())
						.equals(uiCtrl)
						) {
	
					uiWidget_i = new UIWidgetBlackboard();
	
				}
				else if (
						UIWidgetUtil.getClassSimpleName(UIWidgetBox.class.getName())
						.equals(uiCtrl)
						) {
	
					uiWidget_i = new UIWidgetBox();
					
				}
				else if (
						UIWidgetUtil.getClassSimpleName(UIWidgetOPMChangePasswordControl.class.getName())
						.equals(uiCtrl)
						) {
	
					uiWidget_i = new UIWidgetOPMChangePasswordControl();
					
					
				}
				else if (
						UIWidgetUtil.getClassSimpleName(UILayoutLogin.class.getName())
						.equals(uiCtrl)
						) {
	
					uiWidget_i = new UILayoutLogin();
	
				}
				else if (
						UIWidgetUtil.getClassSimpleName(UILayoutSoc.class.getName())
						.equals(uiCtrl)
						) {
	
					uiWidget_i = new UILayoutSoc();
	
				}
				else if (
						UIWidgetUtil.getClassSimpleName(UIWidgetDataGrid.class.getName())
						.equals(uiCtrl)
						) {
	
					uiWidget_i = new UIWidgetDataGrid();
	
				}
				else if (
						UIWidgetUtil.getClassSimpleName(UIWidgetSocControl.class.getName())
						.equals(uiCtrl)
						) {
	
					uiWidget_i = new UIWidgetSocControl();
	
				}
				else if (
						UIWidgetUtil.getClassSimpleName(UIWidgetSocDelayControl.class.getName())
						.equals(uiCtrl)
						) {
	
					uiWidget_i = new UIWidgetSocDelayControl();
	
				}
				else if (
						UIWidgetUtil.getClassSimpleName(UIWidgetSocAutoManuControl.class.getName())
						.equals(uiCtrl)
						) {
	
					uiWidget_i = new UIWidgetSocAutoManuControl();
	
				}
				else if (
						UIWidgetUtil.getClassSimpleName(UIWidgetSocTitle.class.getName())
						.equals(uiCtrl)
						) {
	
					uiWidget_i = new UIWidgetSocTitle();
	
				}
				else if (
						UIWidgetUtil.getClassSimpleName(UIWidgetSocGrcPoint.class.getName())
						.equals(uiCtrl)
						) {
	
					uiWidget_i = new UIWidgetSocGrcPoint();
					
				}
				else if (
						UIWidgetUtil.getClassSimpleName(UIWidgetSocFilterControl.class.getName())
						.equals(uiCtrl)
						) {
	
					uiWidget_i = new UIWidgetSocFilterControl();
	
				}
				else if (
						UIWidgetUtil.getClassSimpleName(UIWidgetSimultaneousLoginControl.class.getName())
						.equals(uiCtrl)
						) {
	
					uiWidget_i = new UIWidgetSimultaneousLoginControl();
	
				}
				else {
					logger.warn(className, function, "uiCtrl[{}] type for UIWidget IS UNKNOW", uiCtrl);
				}
				
				if ( null != uiWidget_i ) {
					uiWidget_i.setParameter(WidgetParameterName.SimpleEventBus.toString(), parameters.get(WidgetParameterName.SimpleEventBus.toString()));
					uiWidget_i.setParameter(WidgetParameterName.ScsEnvIds.toString(), parameters.get(WidgetParameterName.ScsEnvIds.toString()));
					uiWidget_i.setUINameCard(uiNameCard);
					uiWidget_i.setElement(uiElement);
					uiWidget_i.setDictionaryFolder(uiDict);
					uiWidget_i.setViewXMLFile(uiView);
					uiWidget_i.setOptsXMLFile(uiOpts);
					uiWidget_i.init();	
				} else {
					logger.warn(className, function, "uiCtrl[{}] uiWidget IS NULL", uiCtrl);
				}
	
				return uiWidget_i;
			}
		});	
	}
}
