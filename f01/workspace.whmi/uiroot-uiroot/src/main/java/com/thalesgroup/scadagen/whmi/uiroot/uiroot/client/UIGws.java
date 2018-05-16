package com.thalesgroup.scadagen.whmi.uiroot.uiroot.client;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.ResettableEventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;
import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionariesCache;
import com.thalesgroup.scadagen.whmi.config.configenv.client.Settings;
import com.thalesgroup.scadagen.whmi.config.configenv.client.UICookiesForward;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiroot.uiroot.client.UIGws_i.ParameterName;
import com.thalesgroup.scadagen.whmi.uiroot.uiroot.client.util.Util;
import com.thalesgroup.scadagen.whmi.uiscreen.uiscreenroot.client.UIScreenRootMgr;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.UIWidgetEntryPoint;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.common.InitReady_i;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.scadagen.PhaseALoader;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.scadagen.PhaseBLoader;

/**
 * @author syau
 *
 */
public class UIGws {

	private final String className = this.getClass().getSimpleName();
	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
	
	private EventBus EVENT_BUS = null;
	private ResettableEventBus RESETABLE_EVENT_BUS  = null;
	
	private final String opts = className+"/"+className+".opts.xml";

	private final String strCssPanel	= "project-"+className+"-panel";
	
	private final String STR_NUM_OF_SCREEN				= "numofscreen";
	
	private Panel root = null;

	protected UINameCard uiNameCard = null;
	public void setUINameCard(final UINameCard uiNameCard) {
		final String function = "setUINameCard";
		if ( null != uiNameCard ) {
			this.uiNameCard = new UINameCard(uiNameCard);
			this.uiNameCard.appendUIPanel(this);
		} else {
			logger.warn(function, "uiNameCard IS NULL");
		}
	}
	
	private Map<String, Object> params = new HashMap<String, Object>();
	public void setParameter(String key, Object value) {
		params.put(key, value);
	}
	public void setParameters(final Map<String, Object> params) {
		this.params.clear();
		for ( String key : params.keySet() ) {
			this.params.put(key, params.get(key));
		}
	}
	
	private String uiDict = null, uiProp = null, uiJson = null, uiCtrl = null, uiView = null, uiOpts = null, uiElem = null;

	public void init() {
		final String function = "init";
		logger.begin(function);	

		Util util = new Util();
		uiDict = util.getStringParameter(params, UIGws_i.Parameters.uiDict.toString());
		uiProp = util.getStringParameter(params, UIGws_i.Parameters.uiProp.toString());
		uiJson = util.getStringParameter(params, UIGws_i.Parameters.uiJson.toString());
		uiCtrl = util.getStringParameter(params, UIGws_i.Parameters.uiCtrl.toString());
		uiView = util.getStringParameter(params, UIGws_i.Parameters.uiView.toString());
		uiOpts = util.getStringParameter(params, UIGws_i.Parameters.uiOpts.toString());
		uiElem = util.getStringParameter(params, UIGws_i.Parameters.uiElem.toString());
		
		this.EVENT_BUS = GWT.create(SimpleEventBus.class);
		this.RESETABLE_EVENT_BUS = new ResettableEventBus(EVENT_BUS);
		
		logger.debug(function, "uiDict[{}]", uiDict);
		logger.debug(function, "uiProp[{}]", uiProp);
		logger.debug(function, "uiJson[{}]", uiJson);
		
		final PhaseALoader phaseALoader = PhaseALoader.getInstance();
		
		phaseALoader.setParameter(PhaseALoader.strUIDict, uiDict);
		phaseALoader.setParameter(PhaseALoader.strUIProp, uiProp);
		phaseALoader.setParameter(PhaseALoader.strUIJson, uiJson);
		
		UIWidgetEntryPoint.init(null, phaseALoader.getLoader(), new InitReady_i() {
			
			@Override
			public void ready(Map<String, Object> params) {

				final PhaseBLoader phaseBLoader = PhaseBLoader.getInstance();
			
				DictionariesCache dictionariesCache = DictionariesCache.getInstance(uiDict);
				if ( null != dictionariesCache ) {
					
					final String strHeader = "header";
					
					String strUIOpmSCADAgenValue = null;

					String strDatabaseReadingSingletonValue = null;
					String strDatabaseSubscribeSingletonValue = null;
					String strDatabaseSubscribePeriodMillisValue = null;
					String strDatabaseWritingSingletonValue = null;
					String strDatabaseGetFullPathSingletonValue = null;
					
					strUIOpmSCADAgenValue					= dictionariesCache.getStringValue(opts, ParameterName.UIOpmSCADAgenKey.toString(), strHeader);
					
					strDatabaseReadingSingletonValue		= dictionariesCache.getStringValue(opts, ParameterName.DatabaseReadingSingletonKey.toString(), strHeader);
					strDatabaseSubscribeSingletonValue		= dictionariesCache.getStringValue(opts, ParameterName.DatabaseSubscribeSingletonKey.toString(), strHeader);
					strDatabaseSubscribePeriodMillisValue	= dictionariesCache.getStringValue(opts, ParameterName.DatabaseSubscribeSingletonPeriodMillisKey.toString(), strHeader);
					strDatabaseWritingSingletonValue		= dictionariesCache.getStringValue(opts, ParameterName.DatabaseWritingSingletonKey.toString(), strHeader);
					strDatabaseGetFullPathSingletonValue	= dictionariesCache.getStringValue(opts, ParameterName.DatabaseGetFullPathSingletonKey.toString(), strHeader);
							
					logger.debug(function, "strUIOpmSCADAgenValue[{}]", strDatabaseReadingSingletonValue);
					
					logger.debug(function, "strDatabaseReadingSingletonValue[{}]", strDatabaseReadingSingletonValue);
					logger.debug(function, "strDatabaseSubscribeSingletonValue[{}]", strDatabaseSubscribeSingletonValue);
					logger.debug(function, "strDatabaseSubscribePeriodMillisValue[{}]", strDatabaseSubscribePeriodMillisValue);
					logger.debug(function, "strDatabaseWritingSingletonValue[{}]", strDatabaseWritingSingletonValue);
					logger.debug(function, "strDatabaseGetFullPathSingletonValue[{}]", strDatabaseGetFullPathSingletonValue);
				
					phaseBLoader.setParameter(PhaseBLoader.strUIOpmSCADAgenKey, strUIOpmSCADAgenValue);
					
					phaseBLoader.setParameter(PhaseBLoader.strDatabaseReadingSingletonKey, strDatabaseReadingSingletonValue);
					phaseBLoader.setParameter(PhaseBLoader.strDatabaseSubscribeSingletonKey, strDatabaseSubscribeSingletonValue);
					phaseBLoader.setParameter(PhaseBLoader.strDatabaseSubscribeSingletonPeriodMillisKey, strDatabaseSubscribePeriodMillisValue);
					phaseBLoader.setParameter(PhaseBLoader.strDatabaseWritingSingletonKey, strDatabaseWritingSingletonValue);
					phaseBLoader.setParameter(PhaseBLoader.strDatabaseGetFullPathSingletonKey, strDatabaseGetFullPathSingletonValue);
				}
				
				UIWidgetEntryPoint.init(params, phaseBLoader.getLoader(), new InitReady_i() {
					
					@Override
					public void ready(final Map<String, Object> params) {
						
						loadUIWidget(uiCtrl, uiView, uiNameCard, uiOpts, uiElem, uiDict, options);

					}
				});
			}
		});

		setUINameCard(new UINameCard(0, "", RESETABLE_EVENT_BUS));

		// Store Number Of Screens to Cookies
		UICookiesForward.forwardInt(STR_NUM_OF_SCREEN, 1);

		this.root = new FlowPanel();
		this.root.addStyleName(strCssPanel);
		this.root.add(new UIGwsCache().getMainPanel(this.uiNameCard));

		Settings.getInstance().dumpSetting(Settings.getInstance().getMaps());

		logger.end(function);
	}
	
	public Panel getMainPanel() {
		final String function = "getMainPanel";
		logger.beginEnd(function);
		return root;
	}

	private Map<String, Object> options = null;
	public void setOptions(final Map<String, Object> options) {
		this.options = options;
	}

	private boolean isCreated = false;
	private void loadUIWidget(final String uiCtrl, String uiView, final UINameCard uiNameCard, final String uiOpts, final String uiElem, final String uiDict, final Map<String, Object> options) {
		final String function = "loadUIWidget";
		logger.begin(function);

		if ( ! isCreated ) {
			
			final UIScreenRootMgr uiPanelFactoryMgr = UIScreenRootMgr.getInstance();
			final UIWidget_i uiWidget_i = 
					uiPanelFactoryMgr.getUIWidget(uiCtrl, uiView, uiNameCard, uiOpts, uiElem, uiDict, options);
			
			if ( null != uiWidget_i ) {
				final Panel panel = uiWidget_i.getMainPanel();
				
				this.root.clear();
				this.root.add(panel);
			} else {
				logger.warn(function, "uiWidget_i IS NULL");
			}

			isCreated = true;
			
		} else {
			logger.warn(function, "ready isCreated");
		}
		
		logger.end(function);
	}
}
