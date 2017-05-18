package com.thalesgroup.scadagen.wrapper.widgetcontroller.client;

import java.util.HashMap;
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
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.UIViewMgr;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.common.InitProcess_i;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.common.InitReady_i;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.init.InitCacheJsonsFile;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.init.InitCachePropertiesFile;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.init.InitCacheXMLFile;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.init.InitDatabase;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.init.InitOpm;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.init.InitTranslation;

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
		
		logger.info(className, function, "uiCtrl[{}] uiView[{}] uiOpts[{}] uiOpts[{}] uiDict[{}]", new Object[]{uiCtrl, uiView, uiOpts, uiElem, uiDict});

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
		
		logger.info(className, function, "uiCtrl[{}] uiView[{}] uiOpts[{}]", new Object[]{uiCtrl, uiView, uiOpts});
		
		HashMap<String, Object> options = new HashMap<String, Object>();
		
		UIViewMgr viewFactoryMgr = UIViewMgr.getInstance();
		
		uiWidget_i = viewFactoryMgr.getUIWidget(uiCtrl, uiView, uiNameCard, uiOpts, uiElem, uiDict, options);
		
		if ( null != uiWidget_i ) {
			
			logger.info(className, function, "initWidget before");
		
			Widget widget = uiWidget_i.getMainPanel();
			
			if ( null == widget ) {
				logger.warn(className, function, "initWidget widget IS null");
			}
			
			simplePanel.add(widget);
			
			logger.info(className, function, "initWidget after");

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

	public static void init(final InitReady_i initReady) {
		final String function = "init";
		logger.begin(className, function);
		init(null, initReady);
		 logger.end(className, function);
	}
	
	public static void init(final Map<String, Object> params, final InitReady_i initReady) {
		final String function = "init";
		logger.begin(className, function);
		
//		String projectKey = "FAS";
//		
//		if ( null != params ) {
//			Object obj = params.get("ProjectKey");
//			if ( obj instanceof String ) {
//				projectKey = (String)obj;
//			}
//		}
		
		InitProcess_i initProcess = new InitProcess_i() {
			
			@Override
			public void process(final Map<String, Object> params, final InitReady_i initReady) {
				final String function = "process";
				logger.begin(className, function);
					
				// Loading the UIJson Data Dictionary
				InitCacheJsonsFile.getInstance().initCacheJsonsFile("UIJson", "*.json");
			    
			    // Loading the UIInspector Data Dictionary
			    InitCachePropertiesFile.getInstance().initCachePropertiesFile("UIInspectorPanel", "*.properties");
			    
			    // Loading the XML Data Dictionary
			    InitCacheXMLFile.getInstance().initCacheXMLFile("UIWidgetGeneric", "*.xml", new InitReady_i() {
					
					@Override
					public void ready(final Map<String, Object> params) {
						
						int received = 0;
						if ( null != params) {
							Object obj = params.get("received");
							if ( null != obj && obj instanceof Integer ) {
								received = (Integer)obj;
							}
						}
						
						logger.debug(className, function, " UIWidgetEntryPoint.init ready received["+received+"]");
						
						// Loading SCADAgen OPM Factory
						InitOpm.getInstance().initOpmFactory();
				        
				        // Init the SCADAgen OPM API
				        InitOpm.getInstance().initOpm("UIOpmSCADAgen");
				        
						// Init for the Database Singleton Usage		        
				        InitDatabase.getInstance().initDatabaseReadingSingletonKey("DatabaseMultiReadingProxySingleton");
				        InitDatabase.getInstance().initDatabaseSubscribeSingleton("DatabaseGroupPollingDiffSingleton", 500);
				        InitDatabase.getInstance().initDatabaseWritingSingleton("DatabaseWritingSingleton");
						
						InitTranslation.getInstance().initTranslation("&\\w+", "g");

						if ( null != initReady ) initReady.ready(params);
					}
				});
			    
			    logger.end(className, function);
			}
		};
		
		init(params, initProcess, initReady);
		
	    logger.end(className, function);
	}
	
	public static void init(final Map<String, Object> params, final InitProcess_i initProcess, final InitReady_i initReady) {
		final String function = "init";
		logger.begin(className, function);
		if ( null != initProcess ) {
			initProcess.process(params, initReady);
		} else {
			logger.warn(className, function, "initProcess IS NULL");
		}
		logger.end(className, function);
	}

}
