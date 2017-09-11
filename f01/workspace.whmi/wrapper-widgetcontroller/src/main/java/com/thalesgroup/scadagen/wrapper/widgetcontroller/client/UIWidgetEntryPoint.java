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
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.UIEntryPointFactory;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.common.InitProcess_i;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.common.InitReady_i;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.common.Init_i;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.scadagen.LoaderFactory;

public class UIWidgetEntryPoint extends ResizeComposite implements IWidgetController {
	
	private EventBus EVENT_BUS = null;
	private ResettableEventBus RESETABLE_EVENT_BUS  = null;
	
	private static final String className = UIWidgetUtil.getClassSimpleName(UIWidgetEntryPoint.class.getName());
	private static final UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private UINameCard uiNameCard = null;
	
	private String uiCtrl = null;
	private String uiView = null;
	private String uiOpts = null;
	private String uiElem = null;
	private String uiDict = null;
	
	private UIWidget_i uiWidget_i = null;
	
	private SimplePanel simplePanel = null;
	
	public UIWidgetEntryPoint(String uiCtrl, String uiView, String uiOpts, String uiElem, String uiDict) {
		final String function = "UIWidgetEntryPoint";
		logger.begin(className, function);
		
		this.uiCtrl = uiCtrl;
		this.uiView = uiView;
		this.uiOpts = uiOpts;
		this.uiElem = uiElem;
		this.uiDict = uiDict;
		
		logger.debug(className, function, "uiCtrl[{}] uiView[{}] uiOpts[{}] uiOpts[{}] uiDict[{}]"
				, new Object[]{uiCtrl, uiView, uiOpts, uiElem, uiDict});

		this.EVENT_BUS = GWT.create(SimpleEventBus.class);
		this.RESETABLE_EVENT_BUS = new ResettableEventBus(EVENT_BUS);
		
		uiNameCard = new UINameCard(0, "", RESETABLE_EVENT_BUS);

		simplePanel = new SimplePanel();
		simplePanel.addStyleName("project-" + className + "-root");
		simplePanel.addStyleName("project-" + this.uiElem + "-root");
		
		initWidget(simplePanel);
		
		buildWidget();
		
		logger.end(className, function);
	}
	
	private void buildWidget() {
		final String function = "buildWidget";
		logger.begin(className, function);
		
		logger.debug(className, function, "uiCtrl[{}] uiView[{}] uiOpts[{}] uiElem[{}]"
				, new Object[]{uiCtrl, uiView, uiOpts, uiElem});
		
		Map<String, Object> options = new HashMap<String, Object>();
		
		UIEntryPointFactory factory = UIEntryPointFactory.getInstance();
		uiWidget_i = factory.getUIWidget(uiCtrl, uiView, uiNameCard, uiOpts, uiElem, uiDict, options);
		
		if ( null != uiWidget_i ) {
			
			logger.debug(className, function, "initWidget before");
		
			Widget widget = uiWidget_i.getMainPanel();
			
			if ( null == widget ) {
				logger.warn(className, function, "initWidget widget IS null");
			}
			
			simplePanel.add(widget);
			
			logger.debug(className, function, "initWidget after");

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
		final String function = "inits";
		logger.begin(className, function);
		for ( String key : inits.keySet() ) {
			Init_i init = inits.get(key);
			if ( null != init ) { 
				logger.debug(className, function, "Begin to call key["+key+"] init...");

				try {
					init.init(parameters.get(key), initReadys.get(key));
				} catch (Exception ex) {
					logger.error(className, function, "key["+key+"] init Exception:"+ex.toString());
				}
				
				logger.debug(className, function, "End to call key["+key+"] init.");
			} else {
				logger.warn(className, function, "key["+key+"] init IS NULL");
			}
		}
		logger.end(className, function);
	}

	// Default Init Process is FAS
	public static void init(final InitReady_i initReady) {
		final String function = "init";
		logger.begin(className, function);

		Map<String, Object> params = null;

		init(params, LoaderFactory.getInitProcess("SingleLoader"), initReady);
		
		logger.end(className, function);
	}
	
	public static void init(final Map<String, Object> params, final InitProcess_i initProcess, final InitReady_i initReady) {
		final String function = "init";
		logger.begin(className, function);
		if ( null != initProcess ) {
			try {
				initProcess.process(params, initReady);
			} catch (Exception ex) {
				logger.error(className, function, "init Exception:"+ex.toString());
			}
		} else {
			logger.warn(className, function, "initProcess IS NULL");
		}
		logger.end(className, function);
	}

}
