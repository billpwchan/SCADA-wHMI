package com.thalesgroup.scadagen.wrapper.widgetcontroller.client;

import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.ResettableEventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.layout.view.IWidgetController;
import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionariesCache;
import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionariesCacheEvent;
import com.thalesgroup.scadagen.whmi.config.configenv.shared.DictionaryCacheInterface.ConfigurationType;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.UIViewMgr;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.OpmMgr;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpmFactory;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpmSCADAgen;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i;

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
        initCacheXMLFile("UIWidgetGeneric", "*.xml", initReady);
        initCacheJsonsFile("UIJson", "*.json");
        initOpmFactory();
        initOpm();
        logger.end(className, function);
	}
	public static void initOpmFactory() {
		final String function = "initOpmFactory";
		logger.begin(className, function);
		
		OpmMgr opmMgr = OpmMgr.getInstance();
		opmMgr.addUIOpmFactory(className, new UIOpmFactory() {
			
			@Override
			public UIOpm_i getOpm(String key) {
				UIOpm_i uiOpm_i = null;
				if ( null != key ) {
					
					String UIOpmSCADAgenClassName = UIWidgetUtil.getClassSimpleName(UIOpmSCADAgen.class.getName());
					
					if ( key.equalsIgnoreCase(UIOpmSCADAgenClassName) ) {
						uiOpm_i = UIOpmSCADAgen.getInstance();
					}
				}
				return uiOpm_i;
			}
		});
		
		logger.end(className, function);
	}
	public static void initOpm() {
		final String function = "initOpm";
		logger.begin(className, function);
		String opmkey = "UIOpmSCADAgen";
		logger.debug(className, function, "Try to init opm[{}]", opmkey);
		OpmMgr opmMgr = OpmMgr.getInstance();
		UIOpm_i uiOpm_i = opmMgr.getOpm(opmkey);
		uiOpm_i.init();
		logger.end(className, function);
	}
	public interface InitReady_i {
		void ready(int received);
	}
	public static void initCacheJsonsFile(final String folder, final String extention) {
		initCacheJsonsFile(folder, extention, null);
	}
	public static void initCacheJsonsFile(final String folder, final String extention, final InitReady_i initReady) {
		final String function = "initCacheJsonsFile";
		logger.begin(className, function);
		logger.debug(className, function, "folder[{}] extention[{}]", folder, extention);

		String mode = ConfigurationType.JsonFile.toString();
		String module = null;
		DictionariesCache dictionariesCache = DictionariesCache.getInstance(folder);
		dictionariesCache.add(folder, extention, null);
		dictionariesCache.init(mode, module, new DictionariesCacheEvent() {
			@Override
			public void dictionariesCacheEventReady(int received) {
				logger.debug(className, function, "dictionaryCacheEventReady received[{}]", received);
				if ( null != initReady ) initReady.ready(received);
			}
		});

		logger.end(className, function);
	}
	public static void initCacheXMLFile(final String folder, final String extention) {
		initCacheXMLFile(folder, extention, null);
	}
	public static void initCacheXMLFile(final String folder, final String extention, final InitReady_i initReady) {
		final String function = "initCacheXMLFile";
		logger.begin(className, function);
		logger.debug(className, function, "folder[{}] extention[{}]", folder, extention);
		
		final String header			= "header";
		final String option			= "option";
		final String action			= "action";
		final String actionset		= "actionset";
		final String [] tags = {header, option, action, actionset};
		String mode = ConfigurationType.XMLFile.toString();
		String module = null;
		DictionariesCache dictionariesCache = DictionariesCache.getInstance(folder);
		for(String tag : tags ) {
			dictionariesCache.add(folder, extention, tag);
		}
		dictionariesCache.init(mode, module, new DictionariesCacheEvent() {
			@Override
			public void dictionariesCacheEventReady(int received) {
				logger.debug(className, function, "dictionaryCacheEventReady received[{}]", received);
				if ( null != initReady ) initReady.ready(received);
			}
		});
		
		logger.end(className, function);
	}

}
