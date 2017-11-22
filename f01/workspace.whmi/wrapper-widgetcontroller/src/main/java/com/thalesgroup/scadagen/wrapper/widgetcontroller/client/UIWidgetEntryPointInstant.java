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
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.UIEntryPointFactory;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.common.InitReady_i;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.init.InitCacheJsonsFile;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.init.InitCacheXMLFile;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.init.InitOpm;

public class UIWidgetEntryPointInstant extends ResizeComposite implements IWidgetController {
	
	private EventBus EVENT_BUS = null;
	private ResettableEventBus RESETABLE_EVENT_BUS  = null;
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIWidgetEntryPointInstant.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private UINameCard uiNameCard = null;
	
	private String uiCtrl = null;
	private String uiView = null;
	private String uiOpts = null;
	private String uiElem = null;
	private String uiDict = null;
	
	private SimplePanel simplePanel = null;
	
	public UIWidgetEntryPointInstant(String uiCtrl, String uiView, String uiOpts, String uiElem, String uiDict) {
		final String function = "UIWidgetEntryPoint";
		logger.begin(className, function);
		
		this.uiCtrl = uiCtrl;
		this.uiView = uiView;
		this.uiOpts = uiOpts;
		this.uiElem = uiElem;
		this.uiDict = uiDict;
		
		logger.debug(className, function, "uiCtrl[{}] uiView[{}] uiOpts[{}] uiOpts[{}] uiDict[{}]", new Object[]{uiCtrl, uiView, uiOpts, uiElem, uiDict});

		this.EVENT_BUS = GWT.create(SimpleEventBus.class);
		this.RESETABLE_EVENT_BUS = new ResettableEventBus(EVENT_BUS);		
		
		uiNameCard = new UINameCard(0, "", RESETABLE_EVENT_BUS);

		simplePanel = new SimplePanel();
		simplePanel.addStyleName("project-" + className + "-root");
		simplePanel.addStyleName("project-" + this.uiElem + "-root");
		
		initWidget(simplePanel);
		
		InitOpm.getInstance().initFactory();
		
		InitCacheJsonsFile.getInstance().initCacheJsonsFile("UIJson", "*.json");
		
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
				
				logger.debug(className, function, "dictionaryCacheEventReady received[{}]", received);
				UIWidgetEntryPointInstant.this.ready("UIWidgetGeneric", received);
			}
		});
		
		logger.end(className, function);
	}
	
    /**
     * {@inheritDoc}
     */
    @Override
    public void terminate() {
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

	private UIWidget_i uiWidget_i = null;
	private boolean isCreated = false;
	private void ready(String folder, int received) {
		final String function = "ready";
		logger.begin(className, function);
		
		logger.debug(className, function, "folder[{}] received[{}]", folder, received);

		if ( ! isCreated ) {

			logger.debug(className, function, "uiCtrl[{}] uiView[{}] uiOpts[{}]", new Object[]{uiCtrl, uiView, uiOpts});
			
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
			
			isCreated = true;
			
		} else {
			logger.warn(className, function, "ready folder[{}] received[{}] isCreated", folder, received);
		}
		
		logger.end(className, function);
	}
}
