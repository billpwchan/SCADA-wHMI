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
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.UIEntryPointFactory;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.common.InitReady_i;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.factory.FASLayoutWidgetFactory_i.FASWidgetArgs;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.init.InitCacheJsonsFile;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.init.InitCacheXMLFile;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.init.opm.InitOpm;

public class UIWidgetEntryPointInstant extends ResizeComposite implements IWidgetController {
	
	private EventBus EVENT_BUS = null;
	private ResettableEventBus RESETABLE_EVENT_BUS  = null;
	
	private final String className_ = this.getClass().getSimpleName();
	private final UILogger_i logger_ = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
	
	private UINameCard uiNameCard = null;
	
	private String uiCtrl = null;
	private String uiView = null;
	private String uiOpts = null;
	private String uiElem = null;
	private String uiDict = null;
	
	private SimplePanel simplePanel = null;
	
	public UIWidgetEntryPointInstant(final Map<String, Object> params) {
		final String f = "UIWidgetEntryPointInstant";
		logger_.begin(f);
		
		realize(params);
		
		logger_.end(f);
	}
	
	public UIWidgetEntryPointInstant(final String uiCtrl, final String uiView, final String uiOpts, final String uiElem, final String uiDict) {
		final String function = "UIWidgetEntryPointInstant";
		logger_.begin(function);
		
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put(FASWidgetArgs.uiCtrl.toString(), uiCtrl);
		params.put(FASWidgetArgs.uiView.toString(), uiView);
		params.put(FASWidgetArgs.uiOpts.toString(), uiOpts);
		params.put(FASWidgetArgs.uiElem.toString(), uiElem);
		params.put(FASWidgetArgs.uiDict.toString(), uiDict);

		realize(params);
		
		logger_.end(function);
	}
	
	private void realize(final Map<String, Object> params) {
		final String f = "realize";
		logger_.begin(f);
		
		if(null!=params.get(FASWidgetArgs.uiCtrl.toString())) this.uiCtrl = params.get(FASWidgetArgs.uiCtrl.toString()).toString();
		if(null!=params.get(FASWidgetArgs.uiView.toString())) this.uiView = params.get(FASWidgetArgs.uiView.toString()).toString();
		if(null!=params.get(FASWidgetArgs.uiOpts.toString())) this.uiOpts = params.get(FASWidgetArgs.uiOpts.toString()).toString();
		if(null!=params.get(FASWidgetArgs.uiElem.toString())) this.uiElem = params.get(FASWidgetArgs.uiElem.toString()).toString();
		if(null!=params.get(FASWidgetArgs.uiDict.toString())) this.uiDict = params.get(FASWidgetArgs.uiDict.toString()).toString();

		logger_.debug(f, " uiCtrl[{}] uiView[{}] uiOpts[{}] uiOpts[{}] uiDict[{}]"
				, new Object[]{uiCtrl, uiView, uiOpts, uiElem, uiDict});

		this.EVENT_BUS = GWT.create(SimpleEventBus.class);
		this.RESETABLE_EVENT_BUS = new ResettableEventBus(EVENT_BUS);		
		
		uiNameCard = new UINameCard(0, "", RESETABLE_EVENT_BUS);

		simplePanel = new SimplePanel();
		simplePanel.addStyleName("project-" + className_ + "-root");
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
				
				logger_.debug(f, " dictionaryCacheEventReady received[{}]", received);
				UIWidgetEntryPointInstant.this.ready("UIWidgetGeneric", received);
			}
		});
		
		logger_.end(f);
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
		logger_.begin(function);
		
		logger_.debug(function, "folder[{}] received[{}]", folder, received);

		if ( ! isCreated ) {

			logger_.debug(function, "uiCtrl[{}] uiView[{}] uiOpts[{}]", new Object[]{uiCtrl, uiView, uiOpts});
			
			Map<String, Object> options = new HashMap<String, Object>();
			
			final UIEntryPointFactory factory = UIEntryPointFactory.getInstance();
			uiWidget_i = factory.getUIWidget(uiCtrl, uiView, uiNameCard, uiOpts, uiElem, uiDict, options);
			
			if ( null != uiWidget_i ) {
				
				logger_.debug(function, "initWidget before");
			
				Widget widget = uiWidget_i.getMainPanel();
				
				if ( null == widget ) {
					logger_.warn(function, "initWidget widget IS null");
				}
				
				simplePanel.add(widget);
				
				logger_.debug(function, "initWidget after");

			}
			
			isCreated = true;
			
		} else {
			logger_.warn(function, "ready folder[{}] received[{}] isCreated", folder, received);
		}
		
		logger_.end(function);
	}
}
