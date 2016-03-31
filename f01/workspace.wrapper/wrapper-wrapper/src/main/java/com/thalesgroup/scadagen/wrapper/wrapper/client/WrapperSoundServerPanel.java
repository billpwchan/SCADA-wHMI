package com.thalesgroup.scadagen.wrapper.wrapper.client;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.soundserver.SoundServerPanel;
import com.thalesgroup.scadagen.wrapper.wrapper.scadasoft.gwebhmi.main.client.AppUtils;

public class WrapperSoundServerPanel {

	public HorizontalPanel getMainPanel() {
		HorizontalPanel hp = new HorizontalPanel();
		hp.add(new SoundServerPanel(AppUtils.EVENT_BUS));
		return hp;
	}
}
