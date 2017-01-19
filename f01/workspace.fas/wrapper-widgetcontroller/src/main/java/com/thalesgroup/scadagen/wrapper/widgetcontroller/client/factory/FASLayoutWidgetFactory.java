package com.thalesgroup.scadagen.wrapper.widgetcontroller.client.factory;

import java.util.Map;

import com.google.gwt.event.shared.EventBus;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.layout.dto.AppLayoutsData;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.layout.dto.WidgetConfig;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.layout.factory.HypWidgetFactory;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.layout.view.IWidgetController;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.UIWidgetEntryPoint;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.factory.FASLayoutWidgetFactory_i.FASWidgetType;

/**
 * Showcase Widget Factory
 */
public class FASLayoutWidgetFactory extends HypWidgetFactory {
	
	/** logger */
	private final String className = UIWidgetUtil.getClassSimpleName(FASLayoutWidgetFactory.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);

	/**
	 * Factory constructor
	 * @param eventBus event bus to share between widgets
	 * @param appLayoutsData Application Layout configuration
	 */
	public FASLayoutWidgetFactory(final EventBus eventBus, final AppLayoutsData appLayoutsData) {
		super(eventBus, appLayoutsData);
	}

	@Override
	public IWidgetController buildWidget(final WidgetConfig widgetConf) {
		final String function = "buildWidget";
		
		logger.begin(className, function);

		final String typeId = widgetConf.getTypeId();
		
		logger.debug(className, function, "try to build[{}] ", typeId);
		
		IWidgetController widget = null;
		
		if ( FASWidgetType.UIWidgetEntryPoint.toString().equals(typeId) ) {
			Map<String, String> args = widgetConf.getArgs();
			
			logger.debug(className, function, "args.size[{}]", args.size());
			for ( String key : args.keySet() ) {
				logger.debug(className, function, "args.get([{}])[{}]", key, args.get(key));
			}
			
			String uiCtrl	= args.get("uiCtrl");
			String uiView	= args.get("uiView");
			
		    String uiOpts	= args.get("uiOpts");
		    String uiElem	= args.get("uiElem");
		    String uiDict	= args.get("uiDict");
		    
		    logger.debug(className, function, "uiCtrl[{}] ", uiCtrl);
		    logger.debug(className, function, "uiView[{}] ", uiView);
		    logger.debug(className, function, "uiOpts[{}] ", uiOpts);
		    logger.debug(className, function, "uiElem[{}] ", uiElem);
		    logger.debug(className, function, "uiDict[{}] ", uiDict);
		    
		    widget = new UIWidgetEntryPoint(uiCtrl, uiView, uiOpts, uiElem, uiDict);
			
		}
		
		if ( null == widget ) {
			logger.warn(className, function, "typeId[{}] widget IS NULL", typeId);
		}
		
		logger.end(className, function);
		
		return widget;
	}


}
