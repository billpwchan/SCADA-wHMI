package com.thalesgroup.scadagen.wrapper.wrapper.client;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.soundserver.SoundServerPanel;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.panel.MwtEventBuses;

public class WrapperSoundServerPanel {

	public HorizontalPanel getMainPanel() {
		
		String eventBusName = MwtEventBuses.AppUtils_EVENT_BUS;
		
		EventBus eventBus = MwtEventBuses.getInstance().getEventBus(eventBusName);
		
		HorizontalPanel hp = new HorizontalPanel();
		hp.add(new SoundServerPanel(eventBus));
		return hp;
	}
}
