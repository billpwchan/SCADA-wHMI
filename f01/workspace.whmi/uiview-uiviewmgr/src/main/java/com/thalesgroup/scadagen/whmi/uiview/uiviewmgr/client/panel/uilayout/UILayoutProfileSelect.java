package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uilayout;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionariesCache;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uilayout.UILayoutProfileSelect_i.PropertiesName;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIEventActionBus;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIEventActionProcessorMgr;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionProcessor_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIRealize_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.UIActionEventType;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetCtrl_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UILayoutGeneric;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.OpmMgr;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.common.SetCurrentProfileCallback_i;

public class UILayoutProfileSelect extends UIWidget_i {
	
	private final String className = this.getClass().getSimpleName();
	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
	
	private final String strUIWidgetGeneric		= "UIWidgetGeneric";
	
	private final String strUIWidgetProfileSelectInfo	= "UIWidgetProfileSelectInfo";
	private final String strUIWidgetProfileSelectButton	= "UIWidgetProfileSelectButton";
	
	private UIWidget_i uiWidgetGenericInfo		= null;
	private UIWidget_i uiWidgetGenericButton	= null;
	
	private String opmApi						= null;
	private final String strHeader				= "header";
	
	private final String STR_PROFILE			= "profile";

	private final String STR_SELECT				= "select";
	private final String STR_SELECTED			= "selected";
	private final String STR_INVALID			= "invalid";
	private final String STR_BYPASS				= "bypass";
	private final String STR_LOGOUT				= "logout";
	
	private void select() {
		final String f = "select";
		logger.begin(f);
		
		logger.debug(f, "opmApi[{}]", opmApi);
		final UIOpm_i opm = OpmMgr.getInstance().getOpm(opmApi);
		final Widget profiles = uiWidgetGenericInfo.getWidget(STR_PROFILE);
		if(null!=profiles) {
			if (profiles instanceof ListBox) {
				ListBox listBox = (ListBox)profiles;

				int index = listBox.getSelectedIndex();
				String profile = listBox.getSelectedItemText();
				
				logger.debug(f, "index[{}] profile[{}]", index, profile);
				
				opm.setCurrentProfile(profile, new SetCurrentProfileCallback_i() {
					
					@Override
					public void callback(final String profile) {
						logger.debug(f, "callback profile[{}]", profile);
						
						uiEventActionProcessor_i.executeActionSet(STR_SELECTED);
					}
				});
			}
		}

		logger.end(f);
	}
	
	private void logout() {
		String f = "logout";
		logger.begin(f);
		
		uiEventActionProcessor_i.executeActionSet(STR_LOGOUT);
		
		logger.end(f);
	}
	
	// External
	private SimpleEventBus eventBus = null;

	private UILayoutGeneric uiLayoutGeneric = null;
	
	private UIEventActionProcessor_i uiEventActionProcessor_i = null;

	private UIWidgetCtrl_i uiWidgetCtrl_i = new UIWidgetCtrl_i() {
		
		@Override
		public void onUIEvent(UIEvent uiEvent) {
			String f = "onUIEvent";
			logger.beginEnd(f);
		}
		
		@Override
		public void onClick(ClickEvent event) {
			String f = "onClick";
			logger.begin(f);
			
			logger.debug(f, "event[{}]", event);
			
			final Widget widget = (Widget) event.getSource();
			
			logger.debug(f, "widget[{}]", widget);
			
			final String element = uiWidgetGenericButton.getWidgetElement(widget);
			
			logger.debug(f, "element[{}]", element);
			
			if ( null != element ) {
				
				if ( element.equals(STR_SELECT) ) {
					select();
				} else if ( element.equals(STR_LOGOUT) ) {
					logout();
				}
				
			} else {
				logger.warn(f, "element IS NULL");
			}
			
			logger.end(f);
		}
		
		@Override
		public void onActionReceived(UIEventAction uiEventAction) {
			final String f = "onActionReceived";
			logger.beginEnd(f);
		}
	};
	
	@Override
	public void init() {
		final String f = "init";
		
		logger.begin(f);
		
		final String strEventBusName = getStringParameter(UIRealize_i.ParameterName.SimpleEventBus.toString());
		if ( null != strEventBusName ) this.eventBus = UIEventActionBus.getInstance().getEventBus(strEventBusName);
		logger.debug(f, "strEventBusName[{}]", strEventBusName);
		
		final DictionariesCache dictionariesCache = DictionariesCache.getInstance(strUIWidgetGeneric);
		if ( null != dictionariesCache ) {
			opmApi			= dictionariesCache.getStringValue(optsXMLFile, PropertiesName.OpmApi.toString(), strHeader);
		}
		
		uiLayoutGeneric = new UILayoutGeneric();
		uiLayoutGeneric.setUINameCard(this.uiNameCard);
		uiLayoutGeneric.setDictionaryFolder(dictionaryFolder);
		uiLayoutGeneric.setViewXMLFile(viewXMLFile);
		uiLayoutGeneric.setOptsXMLFile(optsXMLFile);
		uiLayoutGeneric.init();
		
		rootPanel = uiLayoutGeneric.getMainPanel();
		
		uiWidgetGenericInfo		= uiLayoutGeneric.getUIWidget(strUIWidgetProfileSelectInfo);
		uiWidgetGenericButton	= uiLayoutGeneric.getUIWidget(strUIWidgetProfileSelectButton);

		UIEventActionProcessorMgr uiEventActionProcessorMgr = UIEventActionProcessorMgr.getInstance();
		uiEventActionProcessor_i = uiEventActionProcessorMgr.getUIEventActionProcessor("UIEventActionProcessor");

		uiEventActionProcessor_i.setUINameCard(uiNameCard);
		uiEventActionProcessor_i.setPrefix(className);
		uiEventActionProcessor_i.setElement(element);
		uiEventActionProcessor_i.setDictionariesCacheName(strUIWidgetGeneric);
		uiEventActionProcessor_i.setEventBus(eventBus);
		uiEventActionProcessor_i.setOptsXMLFile(optsXMLFile);
//		uiEventActionProcessor_i.setUIGeneric((UIGeneric) uiWidgetGenericInfo);
		uiEventActionProcessor_i.setActionSetTagName(UIActionEventType.actionset.toString());
		uiEventActionProcessor_i.setActionTagName(UIActionEventType.action.toString());
		uiEventActionProcessor_i.init();
		
		uiWidgetGenericButton.setCtrlHandler(uiWidgetCtrl_i);

		uiEventActionProcessor_i.executeActionSetInit();
		
		logger.debug(f, "opmApi[{}]", opmApi);
		final UIOpm_i opm = OpmMgr.getInstance().getOpm(opmApi);
		String [] items = opm.getCurrentProfiles();		
		
		if(null != items && items.length > 1) {
			final Widget profiles = uiWidgetGenericInfo.getWidget(STR_PROFILE);
			if(null!=profiles) {
				if (profiles instanceof ListBox) {
					ListBox listBox = (ListBox)profiles;
					listBox.clear();

					for (String s: items) {
						listBox.insertItem(s, s, -1);
					}
				}
			}
		}
		else if(null != items && items.length < 1) {
			logger.debug(f, "STR_INVALID[{}]", STR_INVALID);
			uiEventActionProcessor_i.executeActionSet(STR_INVALID);
		}
		else {
			logger.debug(f, "STR_BYPASS[{}]", STR_BYPASS);
			uiEventActionProcessor_i.executeActionSet(STR_BYPASS);
		}

		logger.end(f);
	}

}
