package com.thalesgroup.scadagen.wrapper.widgetcontroller.client;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.ResettableEventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.layout.view.IWidgetController;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.UIEntryPointFactory;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.common.InitProcess_i;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.common.InitReady_i;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.common.Init_i;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.factory.FASLayoutWidgetFactory_i.FASWidgetArgs;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.scadagen.LoaderFactory;

public class UIWidgetEntryPoint extends ResizeComposite implements IWidgetController {
	
	private EventBus EVENT_BUS = null;
	private ResettableEventBus RESETABLE_EVENT_BUS  = null;
	
	private final static String CLASSNAME_ = UIWidgetEntryPoint.class.getSimpleName();
	private final static UILogger_i LOGGER_ = UILoggerFactory.getInstance().getUILogger(UIWidgetEntryPoint.class.getName());
	
	private UINameCard uiNameCard = null;
	
	private String uiCtrl = null;
	private String uiView = null;
	private String uiOpts = null;
	private String uiElem = null;
	private String uiDict = null;
	
	private UIWidget_i uiWidget_i = null;
	
	private SimplePanel simplePanel = null;
	
	public UIWidgetEntryPoint(final Map<String, Object> params) {
		final String f = "UIWidgetEntryPoint";
		LOGGER_.begin(f);
		
		realize(params);
		
		LOGGER_.end(f);
	}
	
	public UIWidgetEntryPoint(final String uiCtrl, final String uiView, final String uiOpts, final String uiElem, final String uiDict) {
		final String f = "UIWidgetEntryPoint";
		LOGGER_.begin(f);

		final Map<String, Object> params = new HashMap<String, Object>();
		params.put(FASWidgetArgs.uiCtrl.toString(), uiCtrl);
		params.put(FASWidgetArgs.uiView.toString(), uiView);
		params.put(FASWidgetArgs.uiOpts.toString(), uiOpts);
		params.put(FASWidgetArgs.uiElem.toString(), uiElem);
		params.put(FASWidgetArgs.uiDict.toString(), uiDict);
		
		realize(params);
		
		LOGGER_.end(f);
	}
	
	private void realize(final Map<String, Object> params) {
		final String f = "realize";
		LOGGER_.begin(f);
		
		if(null!=params.get(FASWidgetArgs.uiCtrl.toString())) this.uiCtrl = params.get(FASWidgetArgs.uiCtrl.toString()).toString();
		if(null!=params.get(FASWidgetArgs.uiView.toString())) this.uiView = params.get(FASWidgetArgs.uiView.toString()).toString();
		if(null!=params.get(FASWidgetArgs.uiOpts.toString())) this.uiOpts = params.get(FASWidgetArgs.uiOpts.toString()).toString();
		if(null!=params.get(FASWidgetArgs.uiElem.toString())) this.uiElem = params.get(FASWidgetArgs.uiElem.toString()).toString();
		if(null!=params.get(FASWidgetArgs.uiDict.toString())) this.uiDict = params.get(FASWidgetArgs.uiDict.toString()).toString();
		
		LOGGER_.debug(f, "uiCtrl[{}] uiView[{}] uiOpts[{}] uiOpts[{}] uiDict[{}]"
				, new Object[]{uiCtrl, uiView, uiOpts, uiElem, uiDict});

		this.EVENT_BUS = GWT.create(SimpleEventBus.class);
		this.RESETABLE_EVENT_BUS = new ResettableEventBus(EVENT_BUS);
		
		uiNameCard = new UINameCard(0, "", RESETABLE_EVENT_BUS);

		simplePanel = new SimplePanel();
		simplePanel.addStyleName("project-" + CLASSNAME_ + "-root");
		simplePanel.addStyleName("project-" + this.uiElem + "-root");
		
		initWidget(simplePanel);
		
		buildWidget();
		
		LOGGER_.end(f);
	}

	private void buildWidget() {
		final String function = "buildWidget";
		LOGGER_.begin(function);
		
		LOGGER_.debug(function, "uiCtrl[{}] uiView[{}] uiOpts[{}] uiElem[{}]"
				, new Object[]{uiCtrl, uiView, uiOpts, uiElem});
		
		final Map<String, Object> options = new HashMap<String, Object>();
		
		UIEntryPointFactory factory = UIEntryPointFactory.getInstance();
		uiWidget_i = factory.getUIWidget(uiCtrl, uiView, uiNameCard, uiOpts, uiElem, uiDict, options);
		
		if ( null != uiWidget_i ) {
			
			LOGGER_.debug(function, "initWidget before");
		
			Widget widget = uiWidget_i.getMainPanel();
			
			if ( null == widget ) {
				LOGGER_.warn(function, "initWidget widget IS null");
			}
			
			simplePanel.add(widget);
			
			LOGGER_.debug(function, "initWidget after");
		}
	}
	
    /**
     * {@inheritDoc}
     */
    @Override
    public void terminate() {
    	if ( null != uiWidget_i ) uiWidget_i.terminate();
    	RESETABLE_EVENT_BUS.removeHandlers();
    }
	
    /**
     * {@inheritDoc}
     */
	@Override
	public Widget getLayoutView() {
		return this;
	}

    /**
     * {@inheritDoc}
     */
	@Override
	public SafeUri getIconUri() {
		return null;
	}

    /**
     * {@inheritDoc}
     */
	@Override
	public String getWidgetTitle() {
		return uiElem;
	}
	
	private static Map<String, Init_i> inits = new LinkedHashMap<String, Init_i>();
	public static void setInit(String key, Init_i init) { inits.put(key, init); }
	public static void cleanInit() { inits.clear(); }
	
	private static Map<String, InitReady_i> initReadys = new LinkedHashMap<String, InitReady_i>();
	public static void setInitReady(String key, InitReady_i initReady) { initReadys.put(key, initReady); }
	public static void cleanInitReady() { inits.clear(); }
	
	private static Map<String, Map<String, Object>> parameters = new LinkedHashMap<String, Map<String, Object>>();
	public static void setParams(String key, Map<String, Object> params) { parameters.put(key, params); }
	public static void cleanParams() { inits.clear(); }
	
	public static void inits() {
		final String f = "inits";
		LOGGER_.begin(f);
		for ( String key : inits.keySet() ) {
			Init_i init = inits.get(key);
			if ( null != init ) { 
				LOGGER_.debug(f, "Begin to call key["+key+"] init...");

				try {
					init.init(parameters.get(key), initReadys.get(key));
				} catch (Exception ex) {
					LOGGER_.error(f, "key["+key+"] init Exception:"+ex.toString());
				}
				
				LOGGER_.debug(f, "End to call key["+key+"] init.");
			} else {
				LOGGER_.warn(f, "key["+key+"] init IS NULL");
			}
		}
		LOGGER_.end(f);
	}

	// Default Init Process is FAS
	public static void init(final InitReady_i initReady) {
		final String f = "init";
		LOGGER_.begin(f);

		init(null, LoaderFactory.getInitProcess("SingleLoader"), initReady);
		
		LOGGER_.end(f);
	}
	
	public static void init(final Map<String, Object> params, final InitProcess_i initProcess, final InitReady_i initReady) {
		final String f = "init";
		LOGGER_.begin(f);
		if ( null != initProcess ) {
			try {
				initProcess.process(params, initReady);
			} catch (Exception ex) {
				LOGGER_.error(f, "init Exception:"+ex.toString());
			}
		} else {
			LOGGER_.warn(f, "initProcess IS NULL");
		}
		LOGGER_.end(f);
	}

}
