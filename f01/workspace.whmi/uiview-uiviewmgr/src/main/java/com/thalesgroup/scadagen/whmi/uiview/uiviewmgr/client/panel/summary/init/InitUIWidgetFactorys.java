package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.summary.init;

import java.util.Map;

import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.entrypoint.UILayoutEntryPoint;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uilayout.UILayoutLogin;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uilayout.UILayoutProfileSelect;
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
	private final static String className = InitUIWidgetFactorys.class.getSimpleName();
	private final static UILogger logger = UILoggerFactory.getInstance().getLogger(name);

	public static void init(final Map<String, String> parameters) {
		String function = "init";
		logger.begin(className, function);

		UIWidgetMgr uiWidgetMgr = UIWidgetMgr.getInstance();
		uiWidgetMgr.removeUIWidgetFactory(name);
		uiWidgetMgr.addUIWidgetFactory(name, new UIWidgetMgrFactory() {
			@Override
			public UIWidget_i getUIWidget(final String uiCtrl, final String uiView, final UINameCard uiNameCard, final String uiOpts, final String uiElement
					, final String uiDict
					, final Map<String, Object> options) {
				final String function = "getUIWidget";

				logger.debug(className, function, "uiCtrl[{}] uiView[{}] uiOpts[{}] uiDict[{}]", new Object[]{uiCtrl, uiView, uiOpts, uiDict});

				if (null != uiNameCard) {
					logger.debug(className, function, "uiNameCard UIPath[{}] UIScreen[{}]"
							, uiNameCard.getUiPath(), uiNameCard.getUiScreen());
				} else {
					logger.warn(className, function, "uiNameCard IS NULL");
				}
				logger.debug(className, function, "options IS NULL[{}]", null == options);

				UIWidget_i uiWidget_i = null;

				if ( 
						UILayoutEntryPoint.class.getSimpleName().equals(uiCtrl) ) {

					uiWidget_i = new UILayoutEntryPoint();
				}
				else if ( 
						UIWidgetConfiguration.class.getSimpleName().equals(uiCtrl) ) {

					uiWidget_i = new UIWidgetConfiguration();
				}
				else if (
						UILayoutConfiguration.class.getSimpleName().equals(uiCtrl)) {

					uiWidget_i = new UILayoutConfiguration();
				}
				else if (
						UIWidgetViewer.class.getSimpleName().equals(uiCtrl)) {

					uiWidget_i = new UIWidgetViewer();
				}
				else if (
						UIWidgetViewerPager.class.getSimpleName().equals(uiCtrl)) {

					uiWidget_i = new UIWidgetViewerPager();
				}
				else if (
						UIWidgetMatrixViewer.class.getSimpleName().equals(uiCtrl)) {

					uiWidget_i = new UIWidgetMatrixViewer();
				}
				else if ( 
						UIWidgetCtlControl.class.getSimpleName().equals(uiCtrl) ) {

					uiWidget_i = new UIWidgetCtlControl();
				}
				else if (
						UIWidgetDpcControl.class.getSimpleName().equals(uiCtrl)) {

					uiWidget_i = new UIWidgetDpcControl();
				}
				else if (
						UIWidgetDioBtnsControl.class.getSimpleName().equals(uiCtrl)) {

					uiWidget_i = new UIWidgetDioBtnsControl();
				}
				else if (
						UIWidgetDpcTagControl.class.getSimpleName().equals(uiCtrl)) {

					uiWidget_i = new UIWidgetDpcTagControl();
				}
				else if (
						UIWidgetDpcScanSuspendControl.class.getSimpleName().equals(uiCtrl)) {

					uiWidget_i = new UIWidgetDpcScanSuspendControl();
				}
				else if (
						UIWidgetDpcManualOverrideControl.class.getSimpleName().equals(uiCtrl)) {

					uiWidget_i = new UIWidgetDpcManualOverrideControl();
				}
				else if (
						UIWidgetFilter.class.getSimpleName().equals(uiCtrl)) {

					uiWidget_i = new UIWidgetFilter();
				}
				else if (
						UIWidgetPrint.class.getSimpleName().equals(uiCtrl)) {

					uiWidget_i = new UIWidgetPrint();
				}
				else if (
						ScsOlsListPanel.class.getSimpleName().equals(uiCtrl)) {

					uiWidget_i = new ScsOlsListPanel();
				}
				else if (
						ScsMatrixPanel.class.getSimpleName().equals(uiCtrl)) {

					uiWidget_i = new ScsMatrixPanel();
				}
				else if (
						UIWidgetCSSSelection.class.getSimpleName().equals(uiCtrl)) {

					uiWidget_i = new UIWidgetCSSSelection();
				}
				else if (
						UIWidgetCSSSwitch.class.getSimpleName().equals(uiCtrl)) {

					uiWidget_i = new UIWidgetCSSSwitch();
				}
				else if (
						UIWidgetBlackboard.class.getSimpleName().equals(uiCtrl)) {

					uiWidget_i = new UIWidgetBlackboard();
				}
				else if (
						UIWidgetBox.class.getSimpleName().equals(uiCtrl)) {

					uiWidget_i = new UIWidgetBox();
				}
				else if (
						UIWidgetOPMChangePasswordControl.class.getSimpleName().equals(uiCtrl)) {

					uiWidget_i = new UIWidgetOPMChangePasswordControl();
				}
				else if (
						UILayoutLogin.class.getSimpleName().equals(uiCtrl)) {

					uiWidget_i = new UILayoutLogin();
				}
				else if (
						UILayoutSoc.class.getSimpleName().equals(uiCtrl)) {

					uiWidget_i = new UILayoutSoc();
				}
				else if (
						UIWidgetDataGrid.class.getSimpleName().equals(uiCtrl)) {

					uiWidget_i = new UIWidgetDataGrid();
				}
				else if (
						UIWidgetSocControl.class.getSimpleName().equals(uiCtrl)) {

					uiWidget_i = new UIWidgetSocControl();
				}
				else if (
						UIWidgetSocDelayControl.class.getSimpleName().equals(uiCtrl)) {

					uiWidget_i = new UIWidgetSocDelayControl();
				}
				else if (
						UIWidgetSocAutoManuControl.class.getSimpleName().equals(uiCtrl)) {

					uiWidget_i = new UIWidgetSocAutoManuControl();
				}
				else if (
						UIWidgetSocTitle.class.getSimpleName().equals(uiCtrl)) {

					uiWidget_i = new UIWidgetSocTitle();
				}
				else if (
						UIWidgetSocGrcPoint.class.getSimpleName().equals(uiCtrl)) {

					uiWidget_i = new UIWidgetSocGrcPoint();
				}
				else if (
						UIWidgetSocFilterControl.class.getSimpleName().equals(uiCtrl)) {

					uiWidget_i = new UIWidgetSocFilterControl();
				}
				else if (
						UIWidgetSimultaneousLoginControl.class.getSimpleName().equals(uiCtrl)) {

					uiWidget_i = new UIWidgetSimultaneousLoginControl();
				}
				else if (
						UILayoutProfileSelect.class.getSimpleName().equals(uiCtrl)) {

					uiWidget_i = new UILayoutProfileSelect();
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
