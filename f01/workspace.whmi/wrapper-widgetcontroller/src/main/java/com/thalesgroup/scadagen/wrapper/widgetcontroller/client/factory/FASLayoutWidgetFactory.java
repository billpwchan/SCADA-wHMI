package com.thalesgroup.scadagen.wrapper.widgetcontroller.client.factory;

import java.util.Map;

import com.google.gwt.event.shared.EventBus;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.layout.dto.AppLayoutsData;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.layout.dto.WidgetConfig;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.layout.factory.HypWidgetFactory;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.layout.view.IWidgetController;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.UIWidgetEntryPoint;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.factory.FASLayoutWidgetFactory_i.FASWidgetType;

/**
 * Showcase Widget Factory
 */
public class FASLayoutWidgetFactory extends HypWidgetFactory {
	
	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());

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
		
		logger.begin(function);

		final String typeId = widgetConf.getTypeId();
		
		logger.debug(function, "try to build[{}] ", typeId);
		
		IWidgetController widget = null;
		
		if ( FASWidgetType.UIWidgetEntryPoint.toString().equals(typeId) ) {
			Map<String, String> args = widgetConf.getArgs();
			
			logger.debug(function, "args.size[{}]", args.size());
			for ( String key : args.keySet() ) {
				logger.debug(function, "args.get([{}])[{}]", key, args.get(key));
			}
			
			String uiCtrl	= args.get("uiCtrl");
			String uiView	= args.get("uiView");
			
		    String uiOpts	= args.get("uiOpts");
		    String uiElem	= args.get("uiElem");
		    String uiDict	= args.get("uiDict");
		    
		    logger.debug(function, "uiCtrl[{}] ", uiCtrl);
		    logger.debug(function, "uiView[{}] ", uiView);
		    logger.debug(function, "uiOpts[{}] ", uiOpts);
		    logger.debug(function, "uiElem[{}] ", uiElem);
		    logger.debug(function, "uiDict[{}] ", uiDict);
		    
		    widget = new UIWidgetEntryPoint(uiCtrl, uiView, uiOpts, uiElem, uiDict);
			
		}
		
		if ( null == widget ) {
			logger.warn(function, "typeId[{}] widget IS NULL", typeId);
		}
		
		logger.end(function);
		
		return widget;
	}


}
