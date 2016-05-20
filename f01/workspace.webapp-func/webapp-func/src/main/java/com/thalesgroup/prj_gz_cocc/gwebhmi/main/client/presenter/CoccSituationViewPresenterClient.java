package com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.presenter;

import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.shared.EventBus;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.ClientLogger;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.event.NavigationActivationEvent;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.situation.presenter.SituationViewPresenterClient;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.situation.view.ISituationView;
import com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.util.ConfigurationConstantUtil;

public class CoccSituationViewPresenterClient extends
		SituationViewPresenterClient {
	
	/** Logger */
    private static final ClientLogger logger_ = ClientLogger.getClientLogger();
    
    private EventBus eventBus_;

	public CoccSituationViewPresenterClient(String configurationId,
			ISituationView situationView) {
		super(configurationId, situationView);
	}

	public CoccSituationViewPresenterClient(String configurationId,
			ISituationView situationView, EventBus eventBus) {
		super(configurationId, situationView, eventBus);
		eventBus_ = eventBus;
	}
	
	@Override
	protected void processRightClick(MouseUpEvent event) {
		//super.processRightClick(event);
		if (event.getNativeButton() == com.google.gwt.dom.client.NativeEvent.BUTTON_RIGHT) {
			logger_.debug("****  ProcessRightClick ****");
			
            eventBus_.fireEventFromSource(
                    new NavigationActivationEvent(ConfigurationConstantUtil.EQUIPMENT_QUERY_PANEL_ID),
                    CoccSituationViewPresenterClient.this);
   		}
	}
}
